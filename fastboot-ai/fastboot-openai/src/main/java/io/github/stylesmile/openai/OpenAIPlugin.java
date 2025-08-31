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
        // 注册OpenAI相关组件
        BeanContainer.registerBean(OpenAIConfig.class);
        BeanContainer.registerBean(OpenAIService.class);
        
        System.out.println("OpenAI plugin started successfully!");
    }
    
    @Override
    public void stop() {
        System.out.println("OpenAI plugin stopped!");
    }
}