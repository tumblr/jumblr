package com.tumblr.jumblr.request;

import com.sun.net.httpserver.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.awt.Desktop;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * An HTTP server that returns a static page.
 * 
 * NOTE: I modified this from http://stackoverflow.com/a/3732328/1772907
 * 
 * @author Jackson
 */
@SuppressWarnings("restriction")
public class CallbackServer {

    private final List<URI> requests;
    private final HttpServer server;
    private final String responsePage;

    /**
     * Authenticates the current user. Note that this operation opens a browser window and blocks on user input.
     * @param service the OAuthService to authenticate
     * @param verifierParameter the name of the parameter that will have the OAuth verifier 
     * @param callbackUrl The url given to the Scribe ServiceBuilder as the callback URL.
     * @return the newly created access token
     * @throws IOException
     */
    public static Token authenticate(OAuthService service, String verifierParameter, URI callbackUrl) 
        throws IOException {
        
        Token request = service.getRequestToken();
        
        CallbackServer s = new CallbackServer(callbackUrl);
        openBrowser(service.getAuthorizationUrl(request));
        URI requestUrl = s.waitForNextRequest();
        s.stop();
        List<String> possibleVerifiers = getUrlParameters(requestUrl).get(verifierParameter);
        if (possibleVerifiers.size() != 1) {
            throw new RuntimeException(String.format("There were %d parameters with the given name," +
            		" when exactly one was expected.", possibleVerifiers.size()));
        }
        String verifier = possibleVerifiers.get(0);
        Token access = service.getAccessToken(request, new Verifier(verifier));

        return access;
    }

    /**
     * Creates a server to listen on the given port and path.
     * 
     * @param listenPort port to bind to
     * @param listenPath path to listen to (default "/callback")
     * @throws IOException 
     */
    public CallbackServer(int listenPort, String listenPath) throws IOException {
        responsePage = readResource("callback_response_page.html");
        
        server = HttpServer.create(new InetSocketAddress(listenPort), 0);
        server.createContext(listenPath, new RequestHandler(this, responsePage));
        server.setExecutor(null); // creates a default executor
        server.start();
        
        requests = Collections.synchronizedList(new LinkedList<URI>());
    }
    
    public CallbackServer(URI url) throws IOException {
        this(url.getPort(), url.getPath());
    }
    
    public CallbackServer(String listenUrl) throws URISyntaxException, IOException {
        this(new URI(listenUrl));
    }

    static class RequestHandler implements HttpHandler {

        private final CallbackServer s;
        private String response;

        public RequestHandler(CallbackServer s, String response) {
            this.s = s;
            this.response = response;
        }

        public void handle(HttpExchange t) throws IOException {
            String response = readResource("callback_response_page.html");

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            s.addRequest(t.getRequestURI());
        }
    }

    private void addRequest(URI uri) {
        
        requests.add(uri);
    }
    
    public URI waitForNextRequest() {
        while (requests.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return requests.get(0);
    }
    
    public void stop() {
        server.stop(1);
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
}
