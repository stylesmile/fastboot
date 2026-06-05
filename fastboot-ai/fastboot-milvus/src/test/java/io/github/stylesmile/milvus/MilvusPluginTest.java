package io.github.stylesmile.milvus;

import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MilvusPlugin.
 */
public class MilvusPluginTest {

    @Test
    public void testImplementsPlugin() {
        MilvusPlugin plugin = new MilvusPlugin();

        assertTrue(plugin instanceof Plugin);
    }

    @Test
    public void testStartAndEndDoNotThrow() {
        MilvusPlugin plugin = new MilvusPlugin();

        plugin.start();
        plugin.end();
    }
}
