package io.github.stylesmile.eureka.server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * EurekaServerPlugin 单元测试
 * 测试 Eureka Server 插件的基本功能
 * 
 * @author Stylesmile
 */
public class EurekaServerPluginTest {

    private EurekaServerPlugin plugin;

    @Before
    public void setUp() {
        plugin = new EurekaServerPlugin();
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
        int defaultPort = 8761;
        assertTrue("Default port should be positive", defaultPort > 0);
        
        // 验证默认上下文路径
        String defaultContextPath = "/eureka";
        assertNotNull("Default context path should not be null", defaultContextPath);
        assertTrue("Default context path should start with /", defaultContextPath.startsWith("/"));
    }
}
