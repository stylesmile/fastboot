package io.github.stylesmile.milvus;

import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import static io.github.stylesmile.test.util.AssertUtils.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for MilvusPlugin.
 */
public class MilvusPluginTest {

    @Test
    public void testImplementsPlugin() {
        MilvusPlugin plugin = new MilvusPlugin();

        assertNotNull("MilvusPlugin should not be null", plugin);
        assertTrue(plugin instanceof Plugin);
    }

    @Test
    public void testStartAndEndDoNotThrow() {
        MilvusPlugin plugin = new MilvusPlugin();

        plugin.start();
        plugin.end();
    }
}
