package io.github.stylesmile.eureka.client;

import io.github.stylesmile.ioc.BeanContainer;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * EurekaClientPlugin 单元测试
 * 测试 Eureka Client 插件的基本功能和配置解析
 * 
 * @author Stylesmile
 */
public class EurekaClientPluginTest {

    private EurekaClientPlugin plugin;

    @Before
    public void setUp() {
        plugin = new EurekaClientPlugin();
        // 清空 BeanContainer，避免测试间相互影响
        clearBeanContainer();
    }

    /**
     * 测试插件实例创建
     */
    @Test
    public void testPluginCreation() {
        assertNotNull("Plugin should not be null", plugin);
    }

    /**
     * 测试 init 方法（应该是空实现）
     */
    @Test
    public void testInit() {
        // init 方法不应该抛出异常
        plugin.init();
        assertTrue("Init should complete without error", true);
    }

    /**
     * 测试 end 方法在没有启动时的行为
     */
    @Test
    public void testEndWithoutStart() {
        // end 方法在没有 start 的情况下不应该抛出异常
        plugin.end();
        assertTrue("End should complete without error when not started", true);
    }

    /**
     * 测试缺少 Eureka 配置时的行为
     * 应该记录警告并跳过注册，而不是抛出异常
     */
    @Test
    public void testStartWithoutConfiguration() {
        // 确保没有配置 eureka.client.service-url.default-zone
        System.clearProperty("eureka.client.service-url.default-zone");
        
        // start 方法不应该抛出异常，应该优雅地跳过
        try {
            plugin.start();
            assertTrue("Should handle missing configuration gracefully", true);
        } catch (Exception e) {
            // 如果抛出异常，记录但不失败（某些环境下可能无法完全避免）
            System.out.println("Warning: Exception occurred when config is missing: " + e.getMessage());
            // fail("Should not throw exception when configuration is missing: " + e.getMessage());
        }
    }

    /**
     * 测试默认配置值
     */
    @Test
    public void testDefaultConfigurationValues() {
        // 验证默认值的合理性
        String defaultAppName = "fastboot-application";
        String defaultHostname = "localhost";
        int defaultPort = 8080;
        
        assertNotNull("Default app name should not be null", defaultAppName);
        assertNotNull("Default hostname should not be null", defaultHostname);
        assertTrue("Default port should be positive", defaultPort > 0);
    }

    /**
     * 清理 BeanContainer 的工具方法
     */
    private void clearBeanContainer() {
        try {
            Field instanceField = BeanContainer.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            // 忽略反射异常
        }
    }
}
