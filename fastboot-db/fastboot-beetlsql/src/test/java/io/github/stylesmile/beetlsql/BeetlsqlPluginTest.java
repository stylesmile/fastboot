package io.github.stylesmile.beetlsql;

import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for BeetlsqlPlugin.
 */
public class BeetlsqlPluginTest {

    @Test
    public void testStartDoesNotThrow() {
        BeetlsqlPlugin plugin = new BeetlsqlPlugin();
        plugin.start();
    }

    @Test
    public void testEndDoesNotThrow() {
        BeetlsqlPlugin plugin = new BeetlsqlPlugin();
        plugin.end();
    }

    @Test
    public void testImplementsPlugin() {
        BeetlsqlPlugin plugin = new BeetlsqlPlugin();
        assertTrue(plugin instanceof Plugin);
    }
}