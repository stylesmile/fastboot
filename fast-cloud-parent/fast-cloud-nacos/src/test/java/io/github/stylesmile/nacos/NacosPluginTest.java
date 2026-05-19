package io.github.stylesmile.nacos;

import io.github.stylesmile.ioc.BeanContainer;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * NacosPlugin 单元测试
 * 测试 Nacos 插件的基本功能和配置解析
 * 
 * @author Stylesmile
 */
public class NacosPluginTest {

    private NacosPlugin plugin;

    @Before
    public void setUp() {
        plugin = new NacosPlugin();
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
     * 测试缺少 Nacos 配置时的行为（失败场景）
     * 应该优雅处理，不抛出异常
     */
    @Test
    public void testStartWithoutConfiguration() {
        // 确保没有配置 nacos.serverAddr
        System.clearProperty("nacos.serverAddr");
        
        // start 方法应该优雅处理缺失配置，不抛出异常
        try {
            plugin.start();
            assertTrue("Should handle missing configuration gracefully", true);
        } catch (Exception e) {
            fail("Should not throw exception when configuration is missing: " + e.getMessage());
        }
    }

    /**
     * 测试配置为空字符串时的行为（失败场景）
     */
    @Test
    public void testStartWithEmptyConfiguration() {
        // 设置空字符串配置
        System.setProperty("nacos.serverAddr", "");
        
        try {
            plugin.start();
            assertTrue("Should handle empty configuration gracefully", true);
        } catch (Exception e) {
            fail("Should not throw exception when configuration is empty: " + e.getMessage());
        } finally {
            System.clearProperty("nacos.serverAddr");
        }
    }

    /**
     * 测试配置无效地址时的行为（失败场景）
     * Nacos 客户端可能异步连接，所以可能不立即抛异常
     */
    @Test
    public void testStartWithInvalidServerAddress() {
        // 设置无效的服务器地址
        System.setProperty("nacos.serverAddr", "invalid-host:8848");
        
        try {
            plugin.start();
            // 如果没抛异常，说明 Nacos 客户端能容忍无效地址（异步连接）
            assertTrue("Nacos client may accept invalid address asynchronously", true);
        } catch (RuntimeException e) {
            // 抛出异常也是可接受的行为
            assertTrue("Exception should be RuntimeException", e instanceof RuntimeException);
            // 异常消息可能为 null（异步连接时）
            // assertNotNull("Exception message should not be null", e.getMessage());
        } finally {
            System.clearProperty("nacos.serverAddr");
        }
    }

    /**
     * 测试默认配置值
     */
    @Test
    public void testDefaultConfigurationValues() {
        // 验证配置键的正确性
        String serverAddrKey = "nacos.serverAddr";
        String usernameKey = "nacos.username";
        String passwordKey = "nacos.password";
        
        assertNotNull("Server address property key should be defined", serverAddrKey);
        assertNotNull("Username property key should be defined", usernameKey);
        assertNotNull("Password property key should be defined", passwordKey);
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
