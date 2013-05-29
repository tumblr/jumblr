package com.tumblr.jumblr.request;

import com.sun.net.httpserver.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

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
     * Creates a server to listen on the given port and path.
     * 
     * @param listenPort port to bind to
     * @param listenPath path to listen to (default "/callback")
     * @param responsePageLocation (default "callback_response_page.html")
     * @throws IOException 
     */
    public CallbackServer(int listenPort, String listenPath, String responsePageLocation) throws IOException {
        this.responsePage = readResource(responsePageLocation);
        
        this.server = HttpServer.create(new InetSocketAddress(listenPort), 0);
        server.createContext(listenPath, new RequestHandler(this, responsePage));
        server.setExecutor(null); // creates a default executor
        server.start();
        
        this.requests = Collections.synchronizedList(new LinkedList<URI>());
    }

    /**
     * Creates a server to listen on the given port and path.
     * 
     * @param listenPort port to bind to
     * @param listenPath path to listen to (default "/callback")
     * @throws IOException 
     */
    public CallbackServer(int listenPort, String listenPath) throws IOException {
        this(listenPort, listenPath, "callback_response_page.html");
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
    static ListMultimap<String, String> getUrlParameters(URI url) {
        ListMultimap<String, String> ret = ArrayListMultimap.create();
        for (NameValuePair param : URLEncodedUtils.parse(url, "UTF-8")) {
            ret.put(param.getName(), param.getValue());
        }
        return ret;
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
