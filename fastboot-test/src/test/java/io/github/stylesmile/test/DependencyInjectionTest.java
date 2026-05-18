package io.github.stylesmile.test;

import io.github.stylesmile.test.annotation.FastBootTest;
import io.github.stylesmile.test.mock.MockMvc;
import io.github.stylesmile.test.mock.MockMvcResult;
import io.github.stylesmile.test.runner.FastbootTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.stylesmile.test.util.AssertUtils.*;

/**
 * Test for dependency injection functionality.
 */
@RunWith(FastbootTestRunner.class)
@FastBootTest(TestApplication.class)
public class DependencyInjectionTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = new MockMvc();
    }

    @Test
    public void testServiceInjectionInController() {
        // Test that the GreetingService is properly injected into GreetingController
        MockMvcResult result = mockMvc.perform(
            io.github.stylesmile.test.mock.MockRequest.get("/greet")
                .param("name", "World")
        );
        
        assertStatus(result, 200);
        assertBodyContains(result, "Hello, World!");
    }

    @Test
    public void testServiceMethodCall() {
        MockMvcResult result = mockMvc.perform(
            io.github.stylesmile.test.mock.MockRequest.get("/farewell")
                .param("name", "John")
        );
        
        assertStatus(result, 200);
        assertBodyContains(result, "Goodbye, John!");
    }
}
