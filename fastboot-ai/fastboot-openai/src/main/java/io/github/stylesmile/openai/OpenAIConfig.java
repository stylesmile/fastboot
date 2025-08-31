package io.github.stylesmile.openai;

import com.theokanning.openai.service.OpenAiService;
import io.github.stylesmile.ioc.annotation.Component;
import io.github.stylesmile.config.Config;
import okhttp3.OkHttpClient;

import java.time.Duration;

/**
 * OpenAI配置类
 * 提供OpenAI API连接和基本配置
 */
@Component
public class OpenAIConfig {
    
    private OpenAiService openAiService;
    
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
        String apiKey = Config.get("openai.api.key", "");
        if (apiKey.isEmpty()) {
            throw new RuntimeException("OpenAI API key is not configured. Please set 'openai.api.key' in your configuration.");
        }
        
        // 配置超时时间
        int timeoutSeconds = Config.getInt("openai.timeout.seconds", 60);
        
        // 创建自定义的OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .readTimeout(Duration.ofSeconds(timeoutSeconds))
                .writeTimeout(Duration.ofSeconds(timeoutSeconds))
                .build();
        
        // 创建OpenAI服务实例
        openAiService = new OpenAiService(apiKey, Duration.ofSeconds(timeoutSeconds));
    }
    
    /**
     * 获取API密钥
     * @return API密钥
     */
    public String getApiKey() {
        return Config.get("openai.api.key", "");
    }
    
    /**
     * 获取默认模型
     * @return 默认模型名称
     */
    public String getDefaultModel() {
        return Config.get("openai.default.model", "gpt-3.5-turbo");
    }
    
    /**
     * 获取最大令牌数
     * @return 最大令牌数
     */
    public int getMaxTokens() {
        return Config.getInt("openai.max.tokens", 1000);
    }
    
    /**
     * 获取温度参数
     * @return 温度参数
     */
    public double getTemperature() {
        return Config.getDouble("openai.temperature", 0.7);
    }
}