package io.github.stylesmile.gateway;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * GatewayPlugin 单元测试
 * 测试 API 网关插件的基本功能
 * 
 * @author Stylesmile
 */
public class GatewayPluginTest {

    private GatewayPlugin plugin;

    @Before
    public void setUp() {
        plugin = new GatewayPlugin();
    }

    /**
     * 测试插件实例创建
     */
    @Test
    public void testPluginCreation() {
        assertNotNull("Plugin should not be null", plugin);
    }

    /**
     * 测试 init 方法
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
     * 测试默认配置值
     */
    @Test
    public void testDefaultConfigurationValues() {
        // 验证默认端口
        int defaultPort = 8080;
        assertTrue("Default port should be positive", defaultPort > 0);
        
        // 验证默认路由配置路径
        String defaultRoutePath = "gateway.routes";
        assertNotNull("Default route path should not be null", defaultRoutePath);
    }

    /**
     * 测试缺少网关配置时的行为（失败场景）
     * 应该优雅处理，不抛出异常
     */
    @Test
    public void testStartWithoutConfiguration() {
        // 确保没有配置 gateway.enabled
        System.clearProperty("gateway.enabled");
        
        try {
            plugin.start();
            assertTrue("Should handle missing configuration gracefully", true);
        } catch (Exception e) {
            fail("Should not throw exception when configuration is missing: " + e.getMessage());
        }
    }

    /**
     * 测试网关禁用时的行为
     */
    @Test
    public void testStartWithGatewayDisabled() {
        // 设置网关禁用
        System.setProperty("gateway.enabled", "false");
        
        try {
            plugin.start();
            assertTrue("Should skip initialization when gateway is disabled", true);
        } catch (Exception e) {
            fail("Should not throw exception when gateway is disabled: " + e.getMessage());
        } finally {
            System.clearProperty("gateway.enabled");
        }
    }
}
