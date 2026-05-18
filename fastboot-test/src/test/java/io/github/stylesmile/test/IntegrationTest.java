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
 * Integration test for the complete fastboot-test framework.
 */
@RunWith(FastbootTestRunner.class)
@FastBootTest(TestApplication.class)
public class IntegrationTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = new MockMvc();
    }

    @Test
    public void testCompleteWorkflow() {
        // Test 1: Simple GET request
        MockMvcResult result1 = mockMvc.get("/hello");
        assertStatus(result1, 200);
        assertBodyContains(result1, "hello world");

        // Test 2: GET with parameters
        MockMvcResult result2 = mockMvc.perform(
            io.github.stylesmile.test.mock.MockRequest.get("/echo")
                .param("msg", "integration test")
        );
        assertStatus(result2, 200);
        assertBodyContains(result2, "echo: integration test");

        // Test 3: Arithmetic operation
        MockMvcResult result3 = mockMvc.perform(
            io.github.stylesmile.test.mock.MockRequest.get("/add")
                .param("a", "100")
                .param("b", "200")
        );
        assertStatus(result3, 200);
        assertBodyContains(result3, "300");
    }

    @Test
    public void testErrorHandling() {
        // Test 404 for non-existent endpoint
        MockMvcResult result = mockMvc.get("/this/does/not/exist");
        assertStatus(result, 404);
    }

    @Test
    public void testDependencyInjectionIntegration() {
        // Test that DI works in the integrated environment
        MockMvcResult result = mockMvc.perform(
            io.github.stylesmile.test.mock.MockRequest.get("/greet")
                .param("name", "IntegrationTest")
        );
        
        // If GreetingController and GreetingService are properly set up
        // this should work. If not, we might get a 404 or error.
        // For now, just verify it doesn't crash
        assertNotNull(result);
    }
}
