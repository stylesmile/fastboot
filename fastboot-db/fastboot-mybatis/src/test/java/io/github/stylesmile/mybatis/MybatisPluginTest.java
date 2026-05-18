package io.github.stylesmile.mybatis;

import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MybatisPlugin.
 */
public class MybatisPluginTest {

    @Test
    public void testEndDoesNotThrow() {
        MybatisPlugin plugin = new MybatisPlugin();
        plugin.end();
    }

    @Test
    public void testImplementsPlugin() {
        MybatisPlugin plugin = new MybatisPlugin();
        assertTrue(plugin instanceof Plugin);
    }

    @Test
    public void testGetSessionInitiallyNull() {
        assertNull(MybatisPlugin.getSession());
    }

    @Test
    public void testStart() {
        // start() requires App.classList to be initialized (fastboot app context)
        // In standalone unit test, it will throw NullPointerException
        // This test documents that behavior
        MybatisPlugin plugin = new MybatisPlugin();
        try {
            plugin.start();
            fail("Expected NullPointerException because App.classList is not initialized");
        } catch (NullPointerException e) {
            // Expected - App.classList is null outside of fastboot app context
            assertTrue(e.getMessage() == null || e.getMessage().contains("classList"));
        }
    }
}