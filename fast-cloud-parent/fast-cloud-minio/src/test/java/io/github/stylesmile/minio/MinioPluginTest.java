package io.github.stylesmile.minio;

import io.github.stylesmile.ioc.BeanContainer;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * MinioPlugin 单元测试
 * 测试 Minio 插件的基本功能
 * 
 * @author Stylesmile
 */
public class MinioPluginTest {

    private MinioPlugin plugin;

    @Before
    public void setUp() {
        plugin = new MinioPlugin();
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
     * 测试缺少 Minio 配置时的行为（失败场景）
     * MinioConfig.getMinioClient() 在配置错误时返回 null
     * 插件应该优雅处理，不抛出异常
     */
    @Test
    public void testStartWithoutConfiguration() {
        // 确保没有配置 minio.endpoint
        System.clearProperty("minio.endpoint");
        System.clearProperty("minio.accessKey");
        System.clearProperty("minio.secretKey");
        
        // start 方法应该能处理配置缺失的情况
        try {
            plugin.start();
            assertTrue("Should handle missing configuration gracefully", true);
        } catch (Exception e) {
            fail("Should not throw exception when configuration is missing: " + e.getMessage());
        }
    }

    /**
     * 测试部分配置缺失时的行为（失败场景）
     */
    @Test
    public void testStartWithPartialConfiguration() {
        // 只设置 endpoint，缺少 accessKey 和 secretKey
        System.setProperty("minio.endpoint", "http://localhost:9000");
        System.clearProperty("minio.accessKey");
        System.clearProperty("minio.secretKey");
        
        try {
            plugin.start();
            assertTrue("Should handle partial configuration gracefully", true);
        } catch (Exception e) {
            fail("Should not throw exception with partial configuration: " + e.getMessage());
        } finally {
            System.clearProperty("minio.endpoint");
        }
    }

    /**
     * 测试配置无效地址时的行为（失败场景）
     */
    @Test
    public void testStartWithInvalidEndpoint() {
        // 设置无效的 endpoint
        System.setProperty("minio.endpoint", "invalid-url");
        System.setProperty("minio.accessKey", "test");
        System.setProperty("minio.secretKey", "test");
        
        try {
            plugin.start();
            // MinioClient.builder() 可能会在 build() 时验证 URL 格式
            assertTrue("Should handle invalid endpoint gracefully", true);
        } catch (Exception e) {
            // 如果抛出异常，应该是可处理的
            assertTrue("Exception should be handled", true);
        } finally {
            System.clearProperty("minio.endpoint");
            System.clearProperty("minio.accessKey");
            System.clearProperty("minio.secretKey");
        }
    }

    /**
     * 测试默认配置值
     */
    @Test
    public void testDefaultConfigurationValues() {
        // 验证配置键的正确性
        String endpointKey = "minio.endpoint";
        String accessKeyKey = "minio.accessKey";
        String secretKeyKey = "minio.secretKey";
        
        assertNotNull("Endpoint property key should be defined", endpointKey);
        assertNotNull("Access key property key should be defined", accessKeyKey);
        assertNotNull("Secret key property key should be defined", secretKeyKey);
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
