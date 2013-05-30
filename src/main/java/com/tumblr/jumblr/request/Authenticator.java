package com.tumblr.jumblr.request;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Authenticators simplify the OAuth process. If you are okay with using the system default browser, you can call
 * autoAuthenticate() to get your access token. Otherwise, call getAuthorizationUrl() to get the authorization URL. Send
 * your user to this. When they either accept or deny the request, they will be redirected to the callback URL that you
 * set up in the {@link org.scribe.builder.ServiceBuilder}. This should match either the {@code callbackUrl} parameter
 * of the main constructor; if it does, Authenticator will have created a server to handle the request. You can use
 * either the default response page, which uses Javascript to immediately close itself, or your own page. When they get
 * redirected to this page, the server stores the request; at this point, any {@link #handleRequest} calls should return
 * within 100ms and any new calls should return immediately. If {@code handleRequest} returned true (i.e., the user
 * accepted your access request), call {@link #getAccessToken} to get your new access token.
 * 
 * @author Jackson
 */
public class Authenticator {

    private final OAuthService service;
    private final Token request;
    private final String verifierParameter;
    private final StaticServer s;
    private Token access;

    /**
     * Authenticates the current user. Note that this operation opens a browser window and blocks on user input.
     * @param service The OAuthService to authenticate
     * @param verifierParameter The name of the parameter that will have the OAuth verifier
     * @param callbackUrl The url given to the Scribe ServiceBuilder as the callback URL.
     * @return The newly created access token
     * @throws IOException
     */
    public static Token autoAuthenticate(OAuthService service, String verifierParameter, URI callbackUrl)
            throws IOException {
        return new Authenticator(service, verifierParameter, callbackUrl).autoAuthenticate();
    }

    /**
     * Creates an Authenticator.
     * @param service The OAuthService to authenticate
     * @param verifierParameter The name of the parameter that will have the OAuth verifier
     * @param responsePage The HTML page that should be shown to the user after they authenticate with your app. Note
     *            that by the point they see this page, the handleRequest() call will have returned.
     * @param port The url given to the Scribe ServiceBuilder as the callback URL.
     * @throws IOException
     */
    public Authenticator(OAuthService service, String verifierParameter, String responsePage, int port)
            throws IOException {
        this.service = service;
        this.request = service.getRequestToken();
        this.verifierParameter = verifierParameter;
        this.s = new StaticServer(responsePage, port);
    }

    /**
     * Creates an Authenticator.
     * @param service The OAuthService to authenticate
     * @param verifierParameter The name of the parameter that will have the OAuth verifier
     * @param responsePage The HTML page that should be shown to the user after they authenticate with your app. Note
     *            that by the point they see this page, the handleRequest() call will have returned.
     * @param callbackUrl The url given to the Scribe ServiceBuilder as the callback URL.
     * @throws IOException
     */
    public Authenticator(OAuthService service, String verifierParameter, String responsePage, URI callbackUrl)
            throws IOException {
        this(service, verifierParameter, responsePage, callbackUrl.getPort());
    }
    
    /**
     * Creates an Authenticator with the default response page, which uses Javascript to immediately and automatically
     * close itself.
     * @param service The OAuthService to authenticate
     * @param verifierParameter The name of the parameter that will have the OAuth verifier
     * @param callbackUrl The url given to the Scribe ServiceBuilder as the callback URL.
     * @throws IOException
     */
    public Authenticator(OAuthService service, String verifierParameter, URI callbackUrl) throws IOException {
        this(service, verifierParameter, readResource("close_page.html"), callbackUrl);
    }

    /**
     * Authenticates the current user. Note that this operation opens a browser window and blocks on user input.
     * @return The newly created access token
     */
    public Token autoAuthenticate() {
        if (!this.openBrowser()) { return null; }
        if (!this.handleRequest()) { return null; }
        return this.getAccessToken();
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
     * Attempt to open the system default browser to the authorization URL.
     * @return true on success, false on failure
     */
    public boolean openBrowser() {
        return openBrowser(getAuthorizationUrl());
    }

    /**
     * Opens the browser to the given url
     * @param url url to open the browser to
     */
    private static boolean openBrowser(URI url) {
        if (!Desktop.isDesktopSupported()) { return false; }
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(url);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean handleRequest() {
        URI requestUrl = s.waitForRequest();
        s.stop();
        List<String> possibleVerifiers = getUrlParameters(requestUrl).get(verifierParameter);
        if (possibleVerifiers.size() == 0) { // The user denied the request.
            return false;
        }
        String verifier = possibleVerifiers.get(0);
        access = service.getAccessToken(request, new Verifier(verifier));
        return true;
    }

    /**
     * Get all the parameters from a given URI.
     * @param url the url to get the parameters from
     * @return all the parameters and values
     */
    private static ListMultimap<String, String> getUrlParameters(URI url) {
        assert url != null;
        ListMultimap<String, String> ret = ArrayListMultimap.create();
        for (NameValuePair param : URLEncodedUtils.parse(url, "UTF-8")) {
            ret.put(param.getName(), param.getValue());
        }
        return ret;
    }

    public Token getAccessToken() {
        return access;
    }

    public URI getAuthorizationUrl() {
        try {
            return new URI(service.getAuthorizationUrl(request));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
