package io.github.stylesmile.mongodb;

import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MongodbPlugin.
 */
public class MongodbPluginTest {

    @Test
    public void testStartDoesNotThrow() {
        MongodbPlugin plugin = new MongodbPlugin();
        plugin.start();
    }

    @Test
    public void testEndDoesNotThrow() {
        MongodbPlugin plugin = new MongodbPlugin();
        plugin.end();
    }

    @Test
    public void testImplementsPlugin() {
        MongodbPlugin plugin = new MongodbPlugin();
        assertTrue(plugin instanceof Plugin);
    }
}