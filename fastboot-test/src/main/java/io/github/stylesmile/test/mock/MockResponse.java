package io.github.stylesmile.test.mock;

import io.github.stylesmile.server.Headers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Mock HTTP Response for testing, similar to Spring's MockHttpServletResponse.
 * Captures response data without a real server.
 */
public class MockResponse {

    private int status = 200;
    private final Headers headers = new Headers();
    private final ByteArrayOutputStream bodyStream = new ByteArrayOutputStream();
    private String bodyAsString = "";
    private ByteArrayOutputStream outputStream; // Store the real output stream

    public MockResponse() {
    }

    // --- Status ---

    public MockResponse status(int status) {
        this.status = status;
        return this;
    }

    public int getStatus() {
        return status;
    }

    // --- Headers ---

    public MockResponse header(String key, String value) {
        headers.add(key, value);
        return this;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getContentType() {
        return headers.get("Content-Type");
    }

    // --- Body ---

    public MockResponse body(String body) {
        this.bodyAsString = body;
        try {
            bodyStream.write(body.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MockResponse body(byte[] body) {
        try {
            bodyStream.write(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public OutputStream getBodyStream() {
        return bodyStream;
    }

    public String getBodyAsString() {
        // If we have a real output stream, read from it first
        if (outputStream != null && outputStream.size() > 0) {
            return outputStream.toString();
        }
        if (bodyAsString.isEmpty() && bodyStream.size() > 0) {
            return bodyStream.toString();
        }
        return bodyAsString;
    }

    /**
     * Set the output stream for later body extraction.
     */
    public void setOutputStream(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public byte[] getBodyAsBytes() {
        return bodyStream.toByteArray();
    }

    /**
     * Check if the response is a JSON response.
     */
    public boolean isJsonResponse() {
        String ct = getContentType();
        return ct != null && ct.contains("application/json");
    }

    /**
     * Check if the response is an HTML response.
     */
    public boolean isHtmlResponse() {
        String ct = getContentType();
        return ct != null && ct.contains("text/html");
    }

    /**
     * Check if the response status indicates success (2xx).
     */
    public boolean isSuccess() {
        return status >= 200 && status < 300;
    }

    /**
     * Check if the response status indicates a client error (4xx).
     */
    public boolean isClientError() {
        return status >= 400 && status < 500;
    }

    /**
     * Check if the response status indicates a server error (5xx).
     */
    public boolean isServerError() {
        return status >= 500 && status < 600;
    }

    @Override
    public String toString() {
        return "MockResponse{" +
                "status=" + status +
                ", headers=" + headers +
                ", body='" + getBodyAsString() + '\'' +
                '}';
    }
}
