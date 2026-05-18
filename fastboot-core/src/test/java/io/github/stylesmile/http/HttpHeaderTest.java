package io.github.stylesmile.http;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for HttpHeader class.
 */
public class HttpHeaderTest {

    @Test
    public void testConstructor() {
        HttpHeader header = new HttpHeader("Content-Type", "application/json");
        assertNotNull(header);
        assertEquals("Content-Type", header.getName());
        assertEquals("application/json", header.getValue());
    }

    @Test
    public void testGetName() {
        HttpHeader header = new HttpHeader("Accept", "text/html");
        assertEquals("Accept", header.getName());
    }

    @Test
    public void testGetValue() {
        HttpHeader header = new HttpHeader("Authorization", "Bearer token123");
        assertEquals("Bearer token123", header.getValue());
    }

    /**
     * Test: Creating HttpHeader with null name should throw NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testWithNullName() {
        new HttpHeader(null, "value");
    }

    /**
     * Test: Creating HttpHeader with null value should throw NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void testWithNullValue() {
        new HttpHeader("name", null);
    }

    /**
     * Test: Creating HttpHeader with empty name should throw IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyName() {
        new HttpHeader("", "value");
    }

    /**
     * Test: Creating HttpHeader with empty value is allowed (RFC2616#14.23).
     */
    @Test
    public void testWithEmptyValue() {
        HttpHeader header = new HttpHeader("Content-Type", "");
        assertEquals("Content-Type", header.getName());
        assertEquals("", header.getValue());
    }

    @Test
    public void testDifferentHeaders() {
        HttpHeader header1 = new HttpHeader("Content-Type", "application/json");
        HttpHeader header2 = new HttpHeader("Content-Type", "text/html");
        HttpHeader header3 = new HttpHeader("Accept", "application/json");

        assertEquals(header1.getName(), header2.getName());
        assertNotEquals(header1.getValue(), header2.getValue());
        assertNotEquals(header1.getName(), header3.getName());
    }
}
