package com.tumblr.jumblr.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import com.tumblr.jumblr.request.MultipartConverter;
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

    private OAuthService service;
    private Token token;

    private JumblrClient client;

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

    public ResponseWrapper get(String path, Map<String, ?> map) {
        OAuthRequest request = this.constructGet(path, map);
        sign(request);
        return clear(request.send());
    }

    private OAuthRequest constructGet(String path, Map<String, ?> queryParams) {
        String url = "http://api.tumblr.com/v2" + path;
        OAuthRequest request = new OAuthRequest(Verb.GET, url);
        if (queryParams != null) {
            for (String key : queryParams.keySet()) {
                request.addQuerystringParameter(key, queryParams.get(key).toString());
            }
        }
        return request;
    }

    private OAuthRequest constructPost(String path, Map<String, ?> bodyMap) {
        String url = "http://api.tumblr.com/v2" + path;
        OAuthRequest request = new OAuthRequest(Verb.POST, url);

        for (String key : bodyMap.keySet()) {
            if (bodyMap.get(key) == null) { continue; }
            if (bodyMap.get(key) instanceof File) { continue; }
            request.addBodyParameter(key, bodyMap.get(key).toString());
        }
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

    private ResponseWrapper clear(Response response) {
        if (response.getCode() == 200 || response.getCode() == 201) {
            String json = response.getBody();
            try {
                Gson gson = new GsonBuilder().
                        registerTypeAdapter(JsonElement.class, new JsonElementDeserializer()).
                        create();
                ResponseWrapper wrapper = gson.fromJson(json, ResponseWrapper.class);
                wrapper.setClient(client);
                return wrapper;
            } catch (JsonSyntaxException ex) {
                return null;
            }
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


}