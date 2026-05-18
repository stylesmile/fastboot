package io.github.stylesmile.test;

import io.github.stylesmile.test.mock.MockMvc;
import io.github.stylesmile.test.mock.MockMvcResult;
import io.github.stylesmile.test.mock.MockRequest;
import org.junit.Before;
import org.junit.Test;

import static io.github.stylesmile.test.util.AssertUtils.*;

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
    public void testMockMvcResultContainsRequestAndResponse() {
        MockRequest request = MockRequest.get("/test");
        MockMvcResult result = mockMvc.perform(request);
        
        assertNotNull(result.getRequest());
        assertNotNull(result.getResponse());
    }
}
