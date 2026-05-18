package io.github.stylesmile.mybatis;

import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MybatisPlusPlugin.
 */
public class MybatisPlusPluginTest {

    @Test
    public void testImplementsPlugin() {
        MybatisPlusPlugin plugin = new MybatisPlusPlugin();
        assertTrue(plugin instanceof Plugin);
    }

    @Test
    public void testEnd() {
        // end() calls FastbootUtil.addClass() which requires App.classList
        // In standalone unit test, it will throw NullPointerException
        MybatisPlusPlugin plugin = new MybatisPlusPlugin();
        try {
            plugin.end();
            fail("Expected NullPointerException because App.classList is not initialized");
        } catch (NullPointerException e) {
            // Expected - App.classList is null outside of fastboot app context
        }
    }

    @Test
    public void testGetSqlSessionFactoryInitiallyNull() {
        assertNull(MybatisPlusPlugin.getSqlSessionFactory());
    }

    @Test
    public void testStart() {
        // start() requires App.classList to be initialized (fastboot app context)
        // In standalone unit test, it will throw NullPointerException
        MybatisPlusPlugin plugin = new MybatisPlusPlugin();
        try {
            plugin.start();
            fail("Expected NullPointerException because App.classList is not initialized");
        } catch (NullPointerException e) {
            // Expected - App.classList is null outside of fastboot app context
        }
    }
}