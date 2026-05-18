package io.github.stylesmile.test;

import io.github.stylesmile.test.context.TestApplicationContext;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for TestApplicationContext functionality.
 */
public class TestApplicationContextTest {

    @Test
    public void testContextCreation() {
        TestApplicationContext context = new TestApplicationContext(TestApplication.class);
        assertNotNull(context);
        assertFalse(context.isStarted());
    }

    @Test
    public void testContextStart() {
        TestApplicationContext context = new TestApplicationContext(TestApplication.class);
        context.start();
        
        assertTrue(context.isStarted());
    }

    @Test
    public void testContextStop() {
        TestApplicationContext context = new TestApplicationContext(TestApplication.class);
        context.start();
        assertTrue(context.isStarted());
        
        context.stop();
        assertFalse(context.isStarted());
    }

    @Test
    public void testGetBean() {
        TestApplicationContext context = new TestApplicationContext(TestApplication.class);
        context.start();
        
        // Try to get a bean - this should work if the context is properly initialized
        TestApplication app = context.getBean(TestApplication.class);
        assertNotNull(app);
        
        context.stop();
    }

    @Test
    public void testMultipleStartCalls() {
        TestApplicationContext context = new TestApplicationContext(TestApplication.class);
        context.start();
        context.start(); // Second call should be safe
        
        assertTrue(context.isStarted());
        context.stop();
    }
}
