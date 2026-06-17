package io.github.stylesmile.openai;

import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.PropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static io.github.stylesmile.test.util.AssertUtils.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for OpenAIPlugin.
 */
public class OpenAIPluginTest {

    private Properties oldProps;

    @Before
    public void setUp() {
        oldProps = PropertyUtil.props;
        PropertyUtil.props = new Properties();
    }

    @After
    public void tearDown() {
        PropertyUtil.props = oldProps;
    }

    @Test
    public void testImplementsPlugin() {
        OpenAIPlugin plugin = new OpenAIPlugin();

        assertNotNull("OpenAIPlugin should not be null", plugin);
        assertTrue(plugin instanceof Plugin);
    }

    @Test
    public void testStartAndEndDoNotThrow() {
        OpenAIPlugin plugin = new OpenAIPlugin();

        plugin.start();
        plugin.end();
    }
}
