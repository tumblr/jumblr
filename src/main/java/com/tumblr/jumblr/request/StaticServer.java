package com.tumblr.jumblr.request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
// The Javadocs for the Sun httpserver stuff can be found at
// http://docs.oracle.com/javase/6/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An HTTP server that returns a static page and offers up the requests that have been sent to it. Note: I modified this
 * from http://stackoverflow.com/a/3732328/1772907
 * @author Jackson
 */
@SuppressWarnings("restriction")
public class StaticServer {

    private final List<URI> requests;
    private final HttpServer server;

    public static final int DEFAULT_PORT = 8000;
    public static final String DEFAULT_PATH = "/"; // Matches all calls

    /**
     * Creates a server to listen on the given port and path that returns the given static HTML response page to every
     * caller.
     * @param responsePage The single HTML page to respond to all requests with.
     * @param listenAddress Address to bind to
     * @param listenPath Path to listen to
     */
    public StaticServer(String responsePage, InetSocketAddress listenAddress, String listenPath) throws IOException {
        this.requests = Collections.synchronizedList(new LinkedList<URI>());
        this.server = HttpServer.create(listenAddress, -1); // -1 specifies the system-default TCP backlog length
        HttpHandler handler = new RequestHandler(this.requests, responsePage);
        server.createContext(listenPath, handler);
        server.start();
    }

    /**
     * Creates a server to listen on the given port and default path that returns the given static HTML response page to
     * every caller.
     * @param responsePage The single HTML page to respond to all requests with.
     * @param listenPort Port to bind to
     */
    public StaticServer(String responsePage, int listenPort) throws IOException {
        this(responsePage, getLegalAddress(listenPort), DEFAULT_PATH);
    }

    private static InetSocketAddress getLegalAddress(int port) throws IOException {
        return new InetSocketAddress(port);
    }

    static class RequestHandler implements HttpHandler {

        private final List<URI> requests;
        private final String response;

        public RequestHandler(StaticServer s, List<URI> requests, String response) {
            this(requests, response);
        }

        public RequestHandler(List<URI> requests, String response) {
            this.requests = requests;
            this.response = response;
        }

        public void handle(HttpExchange t) throws IOException {
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

            requests.add(t.getRequestURI());
        }
    }

    /**
     * Returns whether or there are any more stored requests.
     * @return true if there is at least one more request, false otherwise
     */
    public boolean hasNextRequest() {
        return !requests.isEmpty();
    }

    /**
     * Returns whether or there are any more stored requests.
     * @return false if there is at least one more request, true otherwise
     */
    public boolean isEmpty() {
        return requests.isEmpty();
    }

    /**
     * Removes and returns the next stored request. Returns null if there are no stored requests.
     * @return The next request
     */
    public URI getRequest() {
        if (isEmpty()) { return null; }
        return requests.remove(0);
    }

    /**
     * Removes and returns all stored requests.
     * @return All remaining stored requests
     */
    public List<URI> getAllRequests() {
        List<URI> list = new ArrayList<URI>();
        while (hasNextRequest()) {
            list.add(getRequest());
        }
        return list;
    }

    /**
     * Waits for a maximum of timeout seconds until there is a new request. Warning: this method blocks not only on the
     * network, but on user input.
     * @param timeout the maximum number of seconds that this method will wait before returning even if there is no new
     *            request
     */
    public void waitOnNextRequest(int timeout) {
        long timeoutMillis = timeout * 1000l;
        int cycleTime = 100;
        long startTime = System.currentTimeMillis();
        int size = requests.size();
        while (size == requests.size()) {
            long timeLeft = startTime + timeoutMillis - System.currentTimeMillis();
            if (timeLeft < 0) { return; }

            try {
                Thread.sleep(Math.min(timeLeft + 1, cycleTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes and returns the next request in the list. If the list is empty, waits for the next request. Warning: this
     * method blocks on the network and probably on user input.
     * @param timeout The number of seconds that must pass before this method times out and returns {@code null}.
     * @return The next request
     */
    public URI waitForRequest(int timeout) {
        if (isEmpty()) {
            waitOnNextRequest(timeout);
        }
        return getRequest();
    }

    /**
     * Removes and returns the next stored request. If there are no stored requests, this method waits for the next
     * request. There is a timeout of 10 minutes on this; after that, it will return null. Warning: this method blocks
     * not only on the network, but on user input.
     * @return The next request
     */
    public URI waitForRequest() {
        return waitForRequest(600);
    }

    /**
     * Stops the server. You should call this when you are done, or else the port will stay blocked.
     */
    public void stop() {
        server.stop(1);
    }
}
