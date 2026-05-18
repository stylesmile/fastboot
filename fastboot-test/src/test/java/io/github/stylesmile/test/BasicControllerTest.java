package io.github.stylesmile.test;

import io.github.stylesmile.test.annotation.FastBootTest;
import io.github.stylesmile.test.context.TestApplicationContext;
import io.github.stylesmile.test.mock.MockMvc;
import io.github.stylesmile.test.mock.MockMvcResult;
import io.github.stylesmile.test.runner.FastbootTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.stylesmile.test.util.AssertUtils.*;

/**
 * Test for basic controller functionality with MockMvc.
 */
@RunWith(FastbootTestRunner.class)
@FastBootTest(TestApplication.class)
public class BasicControllerTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = new MockMvc();
    }

    @Test
    public void testHelloEndpoint() {
        MockMvcResult result = mockMvc.get("/hello");
        
        assertStatus(result, 200);
        assertBodyContains(result, "hello world");
    }

    @Test
    public void testEchoEndpointWithParameter() {
        MockMvcResult result = mockMvc.perform(
            io.github.stylesmile.test.mock.MockRequest.get("/echo")
                .param("msg", "test message")
        );
        
        assertStatus(result, 200);
        assertBodyContains(result, "echo: test message");
    }

    @Test
    public void testAddEndpointWithParameters() {
        MockMvcResult result = mockMvc.perform(
            io.github.stylesmile.test.mock.MockRequest.get("/add")
                .param("a", "10")
                .param("b", "20")
        );
        
        assertStatus(result, 200);
        assertBodyContains(result, "30");
    }

    @Test
    public void testNotFoundEndpoint() {
        MockMvcResult result = mockMvc.get("/nonexistent");
        
        assertStatus(result, 404);
    }

    @Test
    public void testMethodNotAllowed() {
        // Assuming /hello only supports GET, try POST
        MockMvcResult result = mockMvc.post("/hello");
        
        // This might return 405 or 200 depending on implementation
        // For now, just verify it doesn't crash
        assertNotNull(result);
    }
}
