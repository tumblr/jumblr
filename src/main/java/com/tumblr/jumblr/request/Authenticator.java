package com.tumblr.jumblr.request;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

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
    public static Token autoAuthenticate(OAuthService service, String verifierParameter, URI callbackUrl) throws IOException {
        return new Authenticator(service, verifierParameter, callbackUrl).autoAuthenticate();
    }
    
    public Authenticator(OAuthService service, String verifierParameter, URI callbackUrl) throws IOException {
        this.service = service;
        this.request = service.getRequestToken();
        this.verifierParameter = verifierParameter;
        String responsePage = readResource("callback_response_page.html");
        this.s = new StaticServer(callbackUrl, responsePage);
    }
    
    public Token autoAuthenticate() {
        if (!this.openBrowser() || !this.handleRequest()) {
            return null;
        }
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

    public boolean openBrowser() {
        try {
            return openBrowser(getAuthorizationUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Opens the browser to the given url
     * @param url url to open the browser to
     * @throws URISyntaxException 
     */
    private static boolean openBrowser(String url) throws URISyntaxException {
        return openBrowser(new URI(url));
    }
    
    private static boolean openBrowser(URI url) {
        if(!Desktop.isDesktopSupported()) {
            return false;
        }
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(url);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean handleRequest() {
        URI requestUrl = s.waitForNextRequest();
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
        ListMultimap<String, String> ret = ArrayListMultimap.create();
        for (NameValuePair param : URLEncodedUtils.parse(url, "UTF-8")) {
            ret.put(param.getName(), param.getValue());
        }
        return ret;
    }

    public Token getAccessToken() {
        return access;
    }
    
    public String getAuthorizationUrl() {
        return service.getAuthorizationUrl(request);
    }    
}
