package com.tumblr.jumblr.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.responses.JsonElementDeserializer;
import com.tumblr.jumblr.responses.ResponseWrapper;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TumblrApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Where requests are made from
 * @author jc
 */
public class RequestBuilder {

    private Token token;
    private OAuthService service;
    private String hostname = "api.tumblr.com";
    private String xauthEndpoint = "https://www.tumblr.com/oauth/access_token";
    private String version = "0.0.11";
    private final JumblrClient client;

    public RequestBuilder(JumblrClient client) {
        this.client = client;
    }

    public String getRedirectUrl(String path) {
        OAuthRequest request = this.constructGet(path, null);
        sign(request);
        boolean presetVal = HttpURLConnection.getFollowRedirects();
        HttpURLConnection.setFollowRedirects(false);
        Response response = request.send();
        HttpURLConnection.setFollowRedirects(presetVal);
        if (response.getCode() == 301) {
            return response.getHeader("Location");
        } else {
            throw new JumblrException(response);
        }
    }

    public ResponseWrapper postMultipart(String path, Map<String, ?> bodyMap) throws IOException {
        OAuthRequest request = this.constructPost(path, bodyMap);
        sign(request);
        OAuthRequest newRequest = RequestBuilder.convertToMultipart(request, bodyMap);
        return clear(newRequest.send());
    }

    public ResponseWrapper post(String path, Map<String, ?> bodyMap) {
        OAuthRequest request = this.constructPost(path, bodyMap);
        sign(request);
        return clear(request.send());
    }

    /**
     * Posts an XAuth request. A new method is needed because the response from
     * the server is not a standard Tumblr JSON response.
     * @param email the user's login email.
     * @param password the user's password.
     * @return the login token.
     */
    public Token postXAuth(final String email, final String password) {
        OAuthRequest request = constructXAuthPost(email, password);
        setToken("", ""); // Empty token is required for Scribe to execute XAuth.
        sign(request);
        return clearXAuth(request.send());
    }

    // Construct an XAuth request
    private OAuthRequest constructXAuthPost(String email, String password) {
        OAuthRequest request = new OAuthRequest(Verb.POST, xauthEndpoint);
        request.addBodyParameter("x_auth_username", email);
        request.addBodyParameter("x_auth_password", password);
        request.addBodyParameter("x_auth_mode", "client_auth");
        return request;
    }

    public ResponseWrapper get(String path, Map<String, ?> map) {
        OAuthRequest request = this.constructGet(path, map);
        sign(request);
        return clear(request.send());
    }

    public OAuthRequest constructGet(String path, Map<String, ?> queryParams) {
        String url = "https://" + hostname + "/v2" + path;
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        if (queryParams != null) {
            for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
                request.addQuerystringParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        request.addHeader("User-Agent", "jumblr/" + this.version);

        return request;
    }

    private OAuthRequest constructPost(String path, Map<String, ?> bodyMap) {
        String url = "https://" + hostname + "/v2" + path;
        OAuthRequest request = new OAuthRequest(Verb.POST, url);

        for (Map.Entry<String, ?> entry : bodyMap.entrySet()) {
        	String key = entry.getKey();
        	Object value = entry.getValue();
        	if (value == null || value instanceof File) { continue; }
            request.addBodyParameter(key,value.toString());
        }
        request.addHeader("User-Agent", "jumblr/" + this.version);

        return request;
    }

    public void setConsumer(String consumerKey, String consumerSecret) {
        service = new ServiceBuilder().
        provider(TumblrApi.class).
        apiKey(consumerKey).apiSecret(consumerSecret).
        build();
    }

    public void setToken(String token, String tokenSecret) {
        this.token = new Token(token, tokenSecret);
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    /* package-visible for testing */ ResponseWrapper clear(Response response) {
        if (response.getCode() == 200 || response.getCode() == 201) {
            String json = response.getBody();
            try {
                Gson gson = new GsonBuilder().
                        registerTypeAdapter(JsonElement.class, new JsonElementDeserializer()).
                        create();
                ResponseWrapper wrapper = gson.fromJson(json, ResponseWrapper.class);
                if (wrapper == null) {
                    throw new JumblrException(response);
                }
                wrapper.setClient(client);
                return wrapper;
            } catch (JsonSyntaxException ex) {
                throw new JumblrException(response);
            }
        } else {
            throw new JumblrException(response);
        }
    }

    private Token parseXAuthResponse(final Response response) {
        String responseStr = response.getBody();
        if (responseStr != null) {
            // Response is received in the format "oauth_token=value&oauth_token_secret=value".
            String extractedToken = null, extractedSecret = null;
            final String[] values = responseStr.split("&");
            for (String value : values) {
                final String[] kvp = value.split("=");
                if (kvp != null && kvp.length == 2) {
                    if (kvp[0].equals("oauth_token")) {
                        extractedToken = kvp[1];
                    } else if (kvp[0].equals("oauth_token_secret")) {
                        extractedSecret = kvp[1];
                    }
                }
            }
            if (extractedToken != null && extractedSecret != null) {
                return new Token(extractedToken, extractedSecret);
            }
        }
        // No good
        throw new JumblrException(response);
    }

    /* package-visible for testing */ Token clearXAuth(Response response) {
        if (response.getCode() == 200 || response.getCode() == 201) {
            return parseXAuthResponse(response);
        } else {
            throw new JumblrException(response);
        }
    }

    private void sign(OAuthRequest request) {
        if (token != null) {
            service.signRequest(token, request);
        }
    }

    public static OAuthRequest convertToMultipart(OAuthRequest request, Map<String, ?> bodyMap) throws IOException {
        return new MultipartConverter(request, bodyMap).getRequest();
    }

    public String getHostname() {
        return hostname;
    }

    /**
     * Set hostname without protocol
     * @param host such as "api.tumblr.com"
     */
    public void setHostname(String host) {
        this.hostname = host;
    }

}
