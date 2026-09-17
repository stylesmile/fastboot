package io.github.stylesmile.milvus;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import static io.github.stylesmile.test.util.AssertUtils.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests MilvusPlugin wiring and contract.
 */
public class MilvusFastbootTest {

    @Test
    public void testPluginImplementsContract() {
        MilvusPlugin plugin = new MilvusPlugin();
        assertNotNull("MilvusPlugin should not be null", plugin);
        assertTrue(plugin instanceof Plugin);
    }

    @Test
    public void testStartEndDoNotThrow() {
        MilvusPlugin plugin = new MilvusPlugin();
        plugin.start();
        plugin.end();
    }

    @Test
    public void testConfigLoadDoesNotThrow() {
        MilvusConfig config = MilvusConfig.load();
        assertNotNull("MilvusConfig should not be null", config);
        assertNotNull("toConnectParam should not be null", config.toConnectParam());
    }
}
