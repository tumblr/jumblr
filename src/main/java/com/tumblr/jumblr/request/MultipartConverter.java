package com.tumblr.jumblr.request;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.scribe.model.OAuthRequest;

/**
 * Convert a OAuthRequest POST into a multi-part OAuthRequest
 * @author jc
 */
public class MultipartConverter {

    private String boundary;
    private OAuthRequest originalRequest;

    private Integer bodyLength = 0;
    private List<Object> responsePieces;

    public MultipartConverter(OAuthRequest request, Map<String, ?> bodyMap) throws IOException {
        this.originalRequest = request;
        this.boundary = Long.toHexString(System.nanoTime());
        this.computeBody(bodyMap);
    }

    public OAuthRequest getRequest() {
        OAuthRequest request = new OAuthRequest(originalRequest.getVerb(), originalRequest.getUrl());
        request.addHeader("Authorization", originalRequest.getHeaders().get("Authorization"));
        request.addHeader("Content-Type", "multipart/form-data, boundary=" + boundary);
        request.addHeader("Content-length", bodyLength.toString());
        request.addPayload(complexPayload());
        return request;
    }

    private byte[] complexPayload() {
        int used = 0;
        byte[] payload = new byte[bodyLength];
        byte[] local;
        for (Object piece : responsePieces) {
            if (piece instanceof StringBuilder) {
                local = ((StringBuilder) piece).toString().getBytes();
            } else {
                local = (byte[]) piece;
            }
            System.arraycopy(local, 0, payload, used, local.length);
            used += local.length;
        }
        return payload;
    }

    private void addResponsePiece(byte[] arr) {
        responsePieces.add(arr);
        bodyLength += arr.length;
    }

    private void addResponsePiece(StringBuilder builder) {
        responsePieces.add(builder);
        bodyLength += builder.toString().length();
    }

    private void computeBody(Map<String, ?> bodyMap) throws IOException {
        responsePieces = new ArrayList<Object>();

        StringBuilder message = new StringBuilder();
        message.append("Content-Type: multipart/form-data; boundary=").append(boundary).append("\r\n\r\n");
        for (String key : bodyMap.keySet()) {
            Object object = bodyMap.get(key);
            if (object == null) { continue; }
            if (object instanceof File) {
                File f = (File) object;
                String mime = URLConnection.guessContentTypeFromName(f.getName());

                DataInputStream dis = null;
                byte[] result = new byte[(int)f.length()];

                try {
                    dis = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
                    dis.readFully(result);
                } finally {
                    dis.close();
                }

                message.append("--").append(boundary).append("\r\n");
                message.append("Content-Disposition: form-data; name=\"").append(key).append("\"; filename=\"").append(f.getName()).append("\"\r\n");
                message.append("Content-Type: ").append(mime).append("\r\n\r\n");
                this.addResponsePiece(message);
                this.addResponsePiece(result);
                message = new StringBuilder("\r\n");
            } else {
                message.append("--").append(boundary).append("\r\n");
                message.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
                message.append(object.toString()).append("\r\n");
            }
        }

        message.append("--").append(boundary).append("--\r\n");
        this.addResponsePiece(message);
    }

}
