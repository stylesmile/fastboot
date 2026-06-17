package io.github.stylesmile.openai;

import io.github.stylesmile.tool.PropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static io.github.stylesmile.test.util.AssertUtils.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for OpenAIConfig.
 */
public class OpenAIConfigTest {

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
    public void testDefaultConfig() {
        OpenAIConfig config = OpenAIConfig.load();

        assertNotNull("OpenAIConfig should not be null", config);
        assertEquals("", config.getApiKey());
        assertEquals(60, config.getTimeoutSeconds());
        assertEquals("gpt-3.5-turbo", config.getDefaultModel());
        assertEquals(1000, config.getMaxTokens());
        assertEquals(0.7, config.getTemperature(), 0.001);
    }

    @Test
    public void testCustomConfig() {
        PropertyUtil.props.setProperty("openai.api.key", "sk-test-key-123");
        PropertyUtil.props.setProperty("openai.timeout.seconds", "30");
        PropertyUtil.props.setProperty("openai.default.model", "gpt-4");
        PropertyUtil.props.setProperty("openai.max.tokens", "2000");
        PropertyUtil.props.setProperty("openai.temperature", "0.5");

        OpenAIConfig config = OpenAIConfig.load();

        assertEquals("sk-test-key-123", config.getApiKey());
        assertEquals(30, config.getTimeoutSeconds());
        assertEquals("gpt-4", config.getDefaultModel());
        assertEquals(2000, config.getMaxTokens());
        assertEquals(0.5, config.getTemperature(), 0.001);
    }

    @Test
    public void testInvalidTimeoutUsesDefault() {
        PropertyUtil.props.setProperty("openai.timeout.seconds", "not-a-number");

        OpenAIConfig config = OpenAIConfig.load();

        assertEquals(60, config.getTimeoutSeconds());
    }

    @Test
    public void testInvalidMaxTokensUsesDefault() {
        PropertyUtil.props.setProperty("openai.max.tokens", "abc");

        OpenAIConfig config = OpenAIConfig.load();

        assertEquals(1000, config.getMaxTokens());
    }

    @Test
    public void testInvalidTemperatureUsesDefault() {
        PropertyUtil.props.setProperty("openai.temperature", "xyz");

        OpenAIConfig config = OpenAIConfig.load();

        assertEquals(0.7, config.getTemperature(), 0.001);
    }

    @Test(expected = RuntimeException.class)
    public void testGetOpenAiServiceThrowsWhenApiKeyEmpty() {
        OpenAIConfig config = OpenAIConfig.load();

        config.getOpenAiService();
    }
}
