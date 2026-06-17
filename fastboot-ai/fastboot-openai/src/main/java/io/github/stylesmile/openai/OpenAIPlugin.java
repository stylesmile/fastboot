package io.github.stylesmile.openai;

import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.ioc.BeanContainer;

/**
 * OpenAI插件
 * 用于集成OpenAI到fastboot框架
 */
public class OpenAIPlugin implements Plugin {

    @Override
    public void start() {
    }

    @Override
    public void init() {
        // 注册OpenAI相关组件
        OpenAIConfig config = OpenAIConfig.load();
        OpenAIService service = new OpenAIService(config);
        BeanContainer.setInstance(OpenAIConfig.class, config);
        BeanContainer.setInstance(OpenAIService.class, service);
    }

    @Override
    public void end() {
    }
}
