package io.github.stylesmile.test.mock;

/**
 * Result of a MockMvc request, containing the response and request info.
 * Similar to Spring's MvcResult.
 */
public class MockMvcResult {

    private final MockRequest request;
    private final MockResponse response;
    private final Throwable error;

    public MockMvcResult(MockRequest request, MockResponse response, Throwable error) {
        this.request = request;
        this.response = response;
        this.error = error;
    }

    public MockMvcResult(MockRequest request, MockResponse response) {
        this(request, response, null);
    }

    public MockRequest getRequest() {
        return request;
    }

    public MockResponse getResponse() {
        return response;
    }

    public Throwable getError() {
        return error;
    }

    public boolean hasError() {
        return error != null;
    }

    /**
     * Get response body as string.
     */
    public String getBody() {
        return response.getBodyAsString();
    }

    /**
     * Get response status code.
     */
    public int getStatus() {
        return response.getStatus();
    }

    @Override
    public String toString() {
        return "MockMvcResult{" +
                "status=" + response.getStatus() +
                ", body='" + response.getBodyAsString() + '\'' +
                '}';
    }
}
