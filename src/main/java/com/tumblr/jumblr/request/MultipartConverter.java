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
    private List<byte[]> responsePieces;

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
            local = (byte[]) piece;
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
    	byte[] bytes = builder.toString().getBytes();
        responsePieces.add(bytes);
        bodyLength += bytes.length;
    }

    private void computeBody(Map<String, ?> bodyMap) throws IOException {
        responsePieces = new ArrayList<byte[]>();

        StringBuilder message = new StringBuilder();
        message.append("Content-Type: multipart/form-data; boundary=").append(boundary).append("\r\n\r\n");
        for (Map.Entry<String, ?> entry : bodyMap.entrySet()) {
        	String key = entry.getKey();
            Object object = entry.getValue();
            if (object == null) { continue; }
            if (object instanceof File) {
                File f = (File) object;
                String mime = URLConnection.guessContentTypeFromName(f.getName());

                byte[] result = new byte[(int)f.length()];
                
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
                try {
                    dis.readFully(result);
                } finally {
                    if (dis != null) {
                        dis.close();
                    }
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
