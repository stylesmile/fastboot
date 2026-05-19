package io.github.stylesmile.test.mock;

import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.handle.MappingHandler;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.ioc.BeanKey;
import io.github.stylesmile.parameter.ParameterWrap;
import io.github.stylesmile.request.RequestMethod;
import io.github.stylesmile.server.Headers;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * MockMvc for fastboot, similar to Spring's MockMvc.
 * Allows simulating HTTP requests against the fastboot application
 * without starting a real HTTP server.
 *
 * <p>Usage:</p>
 * <pre>
 * MockMvc mockMvc = new MockMvc();
 * MockMvcResult result = mockMvc.perform(MockRequest.get("/hello"));
 * assertEquals(200, result.getStatus());
 * assertEquals("hello world", result.getBody());
 * </pre>
 */
public class MockMvc {

    /**
     * Perform a mock request and return the result.
     *
     * @param mockRequest the mock request
     * @return the mock result
     */
    public MockMvcResult perform(MockRequest mockRequest) {
        MockResponse mockResponse = new MockResponse();

        try {
            // Find the mapping handler for the request URI
            MappingHandler handler = HandlerManager.getMappingHandler(mockRequest.getUri());
            if (handler == null) {
                mockResponse.status(404);
                mockResponse.body("Not Found: " + mockRequest.getUri());
                return new MockMvcResult(mockRequest, mockResponse);
            }

            // Check method match
            if (!methodMatches(handler, mockRequest.getMethod())) {
                mockResponse.status(405);
                mockResponse.body("Method Not Allowed: " + mockRequest.getMethod());
                return new MockMvcResult(mockRequest, mockResponse);
            }

            // Build real Request and Response objects from mocks
            Request request = buildRealRequest(mockRequest);
            Response response = buildRealResponse(mockResponse);

            // Execute the handler
            handler.handle(request, response);

            // Extract response data back to mockResponse
            extractResponseData(response, mockResponse);

        } catch (Exception e) {
            mockResponse.status(500);
            String errorMsg = "Internal Server Error: " + e.getMessage();
            mockResponse.body(errorMsg);
            // Print stack trace for debugging
            System.err.println("MockMvc Error: " + errorMsg);
            e.printStackTrace();
            return new MockMvcResult(mockRequest, mockResponse, e);
        }

        return new MockMvcResult(mockRequest, mockResponse);
    }

    /**
     * Perform a GET request.
     */
    public MockMvcResult get(String uri) {
        return perform(MockRequest.get(uri));
    }

    /**
     * Perform a GET request with query parameters.
     * Example: mockMvc.get("/api/users", "page", "1", "size", "10")
     */
    public MockMvcResult get(String uri, String... queryParams) {
        return perform(MockRequest.get(uri, queryParams));
    }

    /**
     * Perform a POST request.
     */
    public MockMvcResult post(String uri) {
        return perform(MockRequest.post(uri));
    }

    /**
     * Perform a PUT request.
     */
    public MockMvcResult put(String uri) {
        return perform(MockRequest.put(uri));
    }

    /**
     * Perform a DELETE request.
     */
    public MockMvcResult delete(String uri) {
        return perform(MockRequest.delete(uri));
    }

    private boolean methodMatches(MappingHandler handler, String requestMethod) {
        RequestMethod handlerMethod = handler.getRequestMethod();
        if (handlerMethod == null) {
            return true; // no method restriction
        }
        return handlerMethod.name().equalsIgnoreCase(requestMethod);
    }

    private Request buildRealRequest(MockRequest mockRequest) throws IOException, URISyntaxException {
        // Build raw HTTP request bytes
        String queryString = mockRequest.buildQueryString();
        String fullUri = mockRequest.getUri();
        if (!queryString.isEmpty()) {
            fullUri += "?" + queryString;
        }

        String requestLine = mockRequest.getMethod() + " " + fullUri + " HTTP/1.1\r\n";
        StringBuilder sb = new StringBuilder(requestLine);

        // Add Host header
        if (mockRequest.getHeaders().get("Host") == null) {
            sb.append("Host: localhost\r\n");
        }

        // Add Content-Type
        if (mockRequest.getHeaders().get("Content-Type") == null
                && mockRequest.getBody().length > 0) {
            sb.append("Content-Type: application/x-www-form-urlencoded\r\n");
        }

        // Add Content-Length
        if (mockRequest.getBody().length > 0) {
            sb.append("Content-Length: ").append(mockRequest.getBody().length).append("\r\n");
        }

        // Add custom headers
        for (io.github.stylesmile.server.Header header : mockRequest.getHeaders()) {
            sb.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
        }

        sb.append("\r\n");

        byte[] headerBytes = sb.toString().getBytes("UTF-8");
        byte[] bodyBytes = mockRequest.getBody();

        byte[] fullRequest = new byte[headerBytes.length + bodyBytes.length];
        System.arraycopy(headerBytes, 0, fullRequest, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, fullRequest, headerBytes.length, bodyBytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(fullRequest);

        // Use a null socket - we need to extend Request to avoid socket dependency
        try {
            Request request = new TestRequest(inputStream);
            // Add mock request params as attributes
            for (Map.Entry<String, String> entry : mockRequest.getParams().entrySet()) {
                request.getParams().put(entry.getKey(), entry.getValue());
            }
            // Add mock headers
            if (mockRequest.getHeaders() != null) {
                for (io.github.stylesmile.server.Header header : mockRequest.getHeaders()) {
                    request.getHeaders().replace(header.getName(), header.getValue());
                }
            }
            return request;
        } catch (Exception e) {
            throw new IOException("Failed to build mock request", e);
        }
    }

    private Response buildRealResponse(MockResponse mockResponse) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Response response = new Response(outputStream);
        // Store the outputStream in mockResponse for later extraction
        mockResponse.setOutputStream(outputStream);
        return response;
    }

    private void extractResponseData(Response response, MockResponse mockResponse) throws IOException {
        // Extract headers
        io.github.stylesmile.server.Headers headers = response.getHeaders();
        for (io.github.stylesmile.server.Header header : headers) {
            mockResponse.header(header.getName(), header.getValue());
        }
        
        // The body should already be in the outputStream stored in mockResponse
        // getBodyAsString will read from it automatically
    }

    /**
     * Test-friendly Request subclass that doesn't require a real Socket.
     */
    private static class TestRequest extends Request {
        public TestRequest(InputStream in) throws IOException {
            super(in, new Socket() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(new byte[0]);
                }

                @Override
                public OutputStream getOutputStream() throws IOException {
                    return new ByteArrayOutputStream();
                }

                @Override
                public boolean isConnected() {
                    return true;
                }

                @Override
                public boolean isClosed() {
                    return false;
                }

                @Override
                public void close() throws IOException {
                    // Do nothing
                }
            });
        }
    }
}
