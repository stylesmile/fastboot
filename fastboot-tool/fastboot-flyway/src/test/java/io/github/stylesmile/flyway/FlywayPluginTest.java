package io.github.stylesmile.flyway;

import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for FlywayPlugin.
 */
public class FlywayPluginTest {

    @Test
    public void testImplementsPlugin() {
        FlywayPlugin plugin = new FlywayPlugin();
        assertTrue(plugin instanceof Plugin);
    }

    @Test
    public void testStartDoesNotThrow() {
        FlywayPlugin plugin = new FlywayPlugin();
        plugin.start();
    }

    @Test
    public void testEndDoesNotThrow() {
        FlywayPlugin plugin = new FlywayPlugin();
        plugin.end();
    }
}
