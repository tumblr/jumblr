package com.tumblr.jumblr.request;

import java.awt.Desktop;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import com.sun.net.httpserver.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * An HTTP server.
 * 
 * NOTE: I modified this from http://stackoverflow.com/a/3732328/1772907
 * 
 * @author Jackson
 */
@SuppressWarnings("restriction")
public class CallbackServer {

    private URI request;
    private HttpServer server;

    /**
     * Authenticates the current user. Note that this operation opens a browser window and blocks on user input.
     * @param service the OAuthService to authenticate
     * @param verifierParameter the name of the parameter that will have the OAuth verifier 
     * @param listenPort the port to listen on
     * @return the newly created access token
     * @throws IOException
     */
    public static Token authenticate(OAuthService service, String verifierParameter, int listenPort) 
        throws IOException {
        
        Token request = service.getRequestToken();
        
        CallbackServer s = new CallbackServer(listenPort);
        openBrowser(service.getAuthorizationUrl(request));
        s.waitForQuery();
        String verifier = s.getResponseParameter(verifierParameter);
        Token access = service.getAccessToken(request, new Verifier(verifier));

        return access;
    }

    /**
     * Sole constructor.
     * 
     * @param listenPort port to bind to (default 80)
     */
    public CallbackServer(int listenPort) {
        request = null;

        try {
            server = HttpServer.create(new InetSocketAddress(listenPort), 0);
            server.createContext("/callback", new MyHandler(this));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class MyHandler implements HttpHandler {

        private final CallbackServer s;

        public MyHandler(CallbackServer s) {
            this.s = s;
        }

        public void handle(HttpExchange t) throws IOException {
            String response = readResource("callback_response_page.html");

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            s.setRequest(t.getRequestURI());
        }
    }

    /**
     * Reads the given resource, even in jar packaging.
     * @param name the name of the resource
     * @return the entire text of the resource
     * @throws IOException
     */
    private static String readResource(String name) throws IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(name);
        Reader r = new BufferedReader(new InputStreamReader(is));

        StringBuffer sb = new StringBuffer();
        while (r.ready()) {
            sb.append((char) r.read());
        }
        r.close();

        return sb.toString();
    }

    /**
     * This is for {@link MyHandler} to set the request uri.
     * @param uri uri of the request
     */
    private void setRequest(URI uri) {
        request = uri;
    }

    /**
     * Wait until a query is received. Note that this blocks on user input.
     */
    public void waitForQuery() {
        while (request == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        server.stop(1);
    }
    
    /**
     * Get the (first) response parameter with the given name.
     * @param parameterName the name of the response parameter
     * @return the value associated with the parameter
     */
    private String getResponseParameter(String parameterName) {
        return getResponseParameters().get(parameterName).get(0);
    }
    
    /**
     * Get all the response parameters as a Multimap.
     * @return all the parameters and values
     */
    private ListMultimap<String, String> getResponseParameters() {
        assert request != null;
        return getUrlParameters(request);
    }

    /**
     * Get all the parameters from a given URI.
     * @param url the url to get the parameters from
     * @return all the parameters and values
     */
    private static ListMultimap<String, String> getUrlParameters(URI url) {
        ListMultimap<String, String> ret = ArrayListMultimap.create();
        for (NameValuePair param : URLEncodedUtils.parse(url, "UTF-8")) {
            ret.put(param.getName(), param.getValue());
        }
        return ret;
    }

    /**
     * Opens the browser to the given url
     * @param url url to open the browser to
     */
    private static void openBrowser(String url) {
        if(!Desktop.isDesktopSupported()) {
            throw new RuntimeException("Can't open browser: desktop not supported");
        }
        Desktop d = Desktop.getDesktop();
        URI uri;
        try {
            uri = new URI(url);
            d.browse(uri);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
