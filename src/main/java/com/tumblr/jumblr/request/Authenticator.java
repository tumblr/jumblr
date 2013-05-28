package com.tumblr.jumblr.request;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;


public class Authenticator {
    private final OAuthService service;
    private final Token request;
    private final String verifierParameter;
    private final URI callbackUrl;
    private CallbackServer s;
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
        
        Authenticator a = new Authenticator(service, verifierParameter, callbackUrl);
        a.startServer();
        a.openBrowser();
        a.handleRequest();
        return a.getAccessToken();
    }
    
    public Authenticator(OAuthService service, String verifierParameter, URI callbackUrl) throws IOException {
        this.service = service;
        this.request = service.getRequestToken();
        this.verifierParameter = verifierParameter;
        this.callbackUrl = callbackUrl;
    }
    
    public void startServer() throws IOException {
        this.s = new CallbackServer(callbackUrl);
        
    }
    
    public void openBrowser() {
        openBrowser(getAuthorizationUrl());
    }
    
    public void handleRequest() {
        URI requestUrl = s.waitForNextRequest();
        s.stop();
        List<String> possibleVerifiers = Authenticator.getUrlParameters(requestUrl).get(verifierParameter);
        if (possibleVerifiers.size() != 1) {
            throw new RuntimeException(String.format("There were %d parameters with the given name," +
                        " when exactly one was expected.", possibleVerifiers.size()));
        }
        String verifier = possibleVerifiers.get(0);
        access = service.getAccessToken(request, new Verifier(verifier));
    }

    public Token getAccessToken() {
        return access;
    }
    
    public String getAuthorizationUrl() {
        return service.getAuthorizationUrl(request);
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
    static void openBrowser(String url) {
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
