package com.tumblr.jumblr.request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
// The Javadocs for the Sun httpserver stuff can be found at
// http://docs.oracle.com/javase/6/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An HTTP server that returns a static page and offers up the requests that have been sent to it.
 * 
 * Note: I modified this from http://stackoverflow.com/a/3732328/1772907
 * 
 * @author Jackson
 */
@SuppressWarnings("restriction")
public class StaticServer {

    private final List<URI> requests;
    private final HttpServer server;
    
    public static final int DEFAULT_PORT = 8000;

    /**
     * Creates a server to listen on the given port and path that returns the given static HTML response page to every
     * caller.
     * @param listenPort port to bind to
     * @param listenPath path to listen to
     * @throws IOException 
     */
    public StaticServer(int listenPort, String listenPath, String responsePage) throws IOException {
        InetSocketAddress address;
        try {
            address = new InetSocketAddress(listenPort);
        } catch (IllegalArgumentException e) {
            address = new InetSocketAddress(DEFAULT_PORT);
        }
        this.server = HttpServer.create(address, -1); // -1 specifies with system-default backlog
        server.createContext(listenPath, new RequestHandler(this, responsePage));
        server.setExecutor(null); // creates a default executor
        server.start();
        
        this.requests = Collections.synchronizedList(new LinkedList<URI>());
    }
    
    /**
     * Creates a server to listen for requests made to the given url, but with any parameters. Returns the given static
     * HTML response page to every caller.
     * @param url
     * @param responsePage
     * @throws IOException
     */
    public StaticServer(URI url, String responsePage) throws IOException {
        this(url.getPort(), url.getPath(), responsePage);
    }

    static class RequestHandler implements HttpHandler {

        private final StaticServer s;
        private String response;

        public RequestHandler(StaticServer s, String response) {
            this.s = s;
            this.response = response;
        }

        public void handle(HttpExchange t) throws IOException {
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
    
    /**
     * Removes and returns the next request in the list. Returns null if the list is empty.
     * @return The next request.
     */
    public URI getRequest() {
        if (requests.isEmpty()) {
            return null;
        }
        return requests.remove(0);
    }
    
    /**
     * Removes and returns the next request in the list. If the list is empty, waits for the next request.
     * Warning: this method blocks not only on the network, but on user input.
     * @return The next request.
     */
    public URI waitForNextRequest() {
        URI request = null;
        while ((request = getRequest()) == null) {
            try {
                Thread.sleep(100); // 100 milliseconds, or 0.1 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return request;
    }
    
    /**
     * Stops the server. You should call this when you are done, or the port will stay blocked.
     */
    public void stop() {
        server.stop(1);
    }
}
