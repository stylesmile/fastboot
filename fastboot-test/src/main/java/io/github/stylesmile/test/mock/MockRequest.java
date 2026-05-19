package io.github.stylesmile.test.mock;

import io.github.stylesmile.server.Headers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Mock HTTP Request for testing, similar to Spring's MockHttpServletRequest.
 * Allows simulating HTTP requests without a real server.
 */
public class MockRequest {

    private String method = "GET";
    private String uri = "/";
    private final Headers headers = new Headers();
    private final Map<String, String> params = new LinkedHashMap<>();
    private final Map<String, Object> attributes = new LinkedHashMap<>();
    private byte[] body = new byte[0];
    private String queryString = "";

    public MockRequest() {
    }

    /**
     * Create a GET request to the given URI.
     */
    public static MockRequest get(String uri) {
        MockRequest request = new MockRequest();
        request.method = "GET";
        request.uri = uri;
        return request;
    }

    /**
     * Create a GET request with query parameters.
     * Example: MockRequest.get("/api/users", "page", "1", "size", "10")
     */
    public static MockRequest get(String uri, String... queryParams) {
        MockRequest request = new MockRequest();
        request.method = "GET";
        request.uri = uri;
        // Parse query parameters (key-value pairs)
        for (int i = 0; i < queryParams.length; i += 2) {
            if (i + 1 < queryParams.length) {
                request.params.put(queryParams[i], queryParams[i + 1]);
            }
        }
        return request;
    }

    /**
     * Create a POST request to the given URI.
     */
    public static MockRequest post(String uri) {
        MockRequest request = new MockRequest();
        request.method = "POST";
        request.uri = uri;
        return request;
    }

    /**
     * Create a PUT request to the given URI.
     */
    public static MockRequest put(String uri) {
        MockRequest request = new MockRequest();
        request.method = "PUT";
        request.uri = uri;
        return request;
    }

    /**
     * Create a DELETE request to the given URI.
     */
    public static MockRequest delete(String uri) {
        MockRequest request = new MockRequest();
        request.method = "DELETE";
        request.uri = uri;
        return request;
    }

    public MockRequest method(String method) {
        this.method = method;
        return this;
    }

    public MockRequest uri(String uri) {
        this.uri = uri;
        return this;
    }

    public MockRequest header(String key, String value) {
        headers.add(key, value);
        return this;
    }

    public MockRequest param(String name, String value) {
        params.put(name, value);
        return this;
    }

    public MockRequest body(byte[] body) {
        this.body = body;
        return this;
    }

    public MockRequest body(String body) {
        this.body = body.getBytes();
        return this;
    }

    public MockRequest contentType(String contentType) {
        headers.replace("Content-Type", contentType);
        return this;
    }

    public MockRequest accept(String accept) {
        headers.replace("Accept", accept);
        return this;
    }

    public MockRequest attribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    // --- Getters ---

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public URI getURI() {
        try {
            String fullUri = uri;
            if (queryString != null && !queryString.isEmpty()) {
                fullUri = uri + "?" + queryString;
            }
            return new URI(fullUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Headers getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public InputStream getBodyStream() {
        return new ByteArrayInputStream(body);
    }

    public byte[] getBody() {
        return body;
    }

    public String getBodyAsString() {
        return new String(body);
    }

    public String getQueryString() {
        return queryString;
    }

    /**
     * Build the query string from params.
     */
    public String buildQueryString() {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            try {
                sb.append(java.net.URLEncoder.encode(entry.getKey(), "UTF-8"))
                  .append("=")
                  .append(java.net.URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (Exception e) {
                // Fallback to unencoded
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return sb.toString();
    }
}
