package io.github.stylesmile.openai;

import com.theokanning.openai.service.OpenAiService;
import io.github.stylesmile.tool.PropertyUtil;

import java.time.Duration;

/**
 * OpenAI配置类
 * 提供OpenAI API连接和基本配置
 */
public class OpenAIConfig {

    private static final int DEFAULT_TIMEOUT_SECONDS = 60;
    private static final String DEFAULT_MODEL = "gpt-3.5-turbo";
    private static final int DEFAULT_MAX_TOKENS = 1000;
    private static final double DEFAULT_TEMPERATURE = 0.7;

    private final String apiKey;
    private final int timeoutSeconds;
    private final String defaultModel;
    private final int maxTokens;
    private final double temperature;
    private OpenAiService openAiService;

    public OpenAIConfig(String apiKey, int timeoutSeconds, String defaultModel, int maxTokens, double temperature) {
        this.apiKey = apiKey;
        this.timeoutSeconds = timeoutSeconds;
        this.defaultModel = defaultModel;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
    }

    /**
     * 从配置文件加载OpenAI配置
     * @return OpenAIConfig
     */
    public static OpenAIConfig load() {
        String apiKey = property("openai.api.key", "");
        int timeoutSeconds = intProperty("openai.timeout.seconds", DEFAULT_TIMEOUT_SECONDS);
        String defaultModel = property("openai.default.model", DEFAULT_MODEL);
        int maxTokens = intProperty("openai.max.tokens", DEFAULT_MAX_TOKENS);
        double temperature = doubleProperty("openai.temperature", DEFAULT_TEMPERATURE);
        return new OpenAIConfig(apiKey, timeoutSeconds, defaultModel, maxTokens, temperature);
    }

    /**
     * 获取OpenAI服务实例
     * @return OpenAiService
     */
    public OpenAiService getOpenAiService() {
        if (openAiService == null) {
            initOpenAiService();
        }
        return openAiService;
    }

    /**
     * 初始化OpenAI服务
     */
    private void initOpenAiService() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("OpenAI API key is not configured. Please set 'openai.api.key' in your configuration.");
        }
        openAiService = new OpenAiService(apiKey, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * 获取API密钥
     * @return API密钥
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * 获取默认模型
     * @return 默认模型名称
     */
    public String getDefaultModel() {
        return defaultModel;
    }

    /**
     * 获取最大令牌数
     * @return 最大令牌数
     */
    public int getMaxTokens() {
        return maxTokens;
    }

    /**
     * 获取温度参数
     * @return 温度参数
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * 获取超时时间
     * @return 超时秒数
     */
    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    private static String property(String key, String defaultValue) {
        String value = PropertyUtil.getProperty(key, defaultValue);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    private static int intProperty(String key, int defaultValue) {
        String value = PropertyUtil.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static double doubleProperty(String key, double defaultValue) {
        String value = PropertyUtil.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
