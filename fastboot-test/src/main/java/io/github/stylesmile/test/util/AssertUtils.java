package io.github.stylesmile.test.util;

import io.github.stylesmile.test.mock.MockMvcResult;
import io.github.stylesmile.test.mock.MockResponse;

import static org.junit.Assert.*;

/**
 * Assertion utilities for fastboot test results.
 */
public final class AssertUtils {

    private AssertUtils() {
    }

    /**
     * Assert that the response status is the expected value.
     */
    public static void assertStatus(MockMvcResult result, int expected) {
        assertNotNull("Result should not be null", result);
        assertEquals("Response status", expected, result.getStatus());
    }

    /**
     * Assert that the response status is 200 (OK).
     */
    public static void assertOk(MockMvcResult result) {
        assertStatus(result, 200);
    }

    /**
     * Assert that the response status is 404 (Not Found).
     */
    public static void assertNotFound(MockMvcResult result) {
        assertStatus(result, 404);
    }

    /**
     * Assert that the response body contains the expected string.
     */
    public static void assertBodyContains(MockMvcResult result, String expected) {
        assertNotNull("Result should not be null", result);
        assertNotNull("Response body should not be null", result.getBody());
        assertTrue("Response body should contain '" + expected + "'",
                result.getBody().contains(expected));
    }

    /**
     * Assert that the response body equals the expected string.
     */
    public static void assertBodyEquals(String expected, MockMvcResult result) {
        assertNotNull("Result should not be null", result);
        assertEquals("Response body", expected, result.getBody());
    }

    /**
     * Assert that the response is a JSON response.
     */
    public static void assertJsonResponse(MockMvcResult result) {
        assertNotNull("Result should not be null", result);
        assertTrue("Response should be JSON",
                result.getResponse().isJsonResponse());
    }

    /**
     * Assert that the response is an HTML response.
     */
    public static void assertHtmlResponse(MockMvcResult result) {
        assertNotNull("Result should not be null", result);
        assertTrue("Response should be HTML",
                result.getResponse().isHtmlResponse());
    }

    /**
     * Assert that the response has no errors.
     */
    public static void assertNoError(MockMvcResult result) {
        assertNotNull("Result should not be null", result);
        assertFalse("Response should have no error", result.hasError());
    }

    /**
     * Assert that an object is not null.
     */
    public static void assertNotNull(Object obj) {
        org.junit.Assert.assertNotNull(obj);
    }

    /**
     * Assert that an object is not null with a message.
     */
    public static void assertNotNull(String message, Object obj) {
        org.junit.Assert.assertNotNull(message, obj);
    }
}
