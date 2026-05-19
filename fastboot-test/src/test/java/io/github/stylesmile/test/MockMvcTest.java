package io.github.stylesmile.test;

import io.github.stylesmile.test.mock.MockMvc;
import io.github.stylesmile.test.mock.MockMvcResult;
import io.github.stylesmile.test.mock.MockRequest;
import org.junit.Before;
import org.junit.Test;

import static io.github.stylesmile.test.util.AssertUtils.*;
import static org.junit.Assert.assertEquals;

/**
 * Test for MockMvc functionality.
 */
public class MockMvcTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = new MockMvc();
    }

    @Test
    public void testMockMvcCreation() {
        assertNotNull(mockMvc);
    }

    @Test
    public void testPerformMethod() {
        MockRequest request = MockRequest.get("/test");
        MockMvcResult result = mockMvc.perform(request);
        
        assertNotNull(result);
    }

    @Test
    public void testConvenienceMethods() {
        // Test convenience methods don't throw exceptions
        MockMvcResult getResult = mockMvc.get("/test");
        MockMvcResult postResult = mockMvc.post("/test");
        MockMvcResult putResult = mockMvc.put("/test");
        MockMvcResult deleteResult = mockMvc.delete("/test");
        
        assertNotNull(getResult);
        assertNotNull(postResult);
        assertNotNull(putResult);
        assertNotNull(deleteResult);
    }

    @Test
    public void testGetWithQueryParameters() {
        // Test GET with query parameters
        MockMvcResult result = mockMvc.get("/test", "page", "1", "size", "10");
        
        assertNotNull(result);
        assertNotNull(result.getRequest());
        // Verify query parameters are set
        assertEquals("1", result.getRequest().getParams().get("page"));
        assertEquals("10", result.getRequest().getParams().get("size"));
    }

    @Test
    public void testMockMvcResultContainsRequestAndResponse() {
        MockRequest request = MockRequest.get("/test");
        MockMvcResult result = mockMvc.perform(request);
        
        assertNotNull(result.getRequest());
        assertNotNull(result.getResponse());
    }
}
