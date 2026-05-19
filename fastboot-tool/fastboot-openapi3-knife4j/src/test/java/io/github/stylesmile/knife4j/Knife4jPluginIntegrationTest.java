package io.github.stylesmile.knife4j;

import io.github.stylesmile.ApplicationKnife4jTest;
import io.github.stylesmile.test.annotation.FastBootTest;
import io.github.stylesmile.test.runner.FastbootTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Knife4j 插件集成测试
 * 使用 fastboot-test 模块进行应用上下文级别的测试
 */
@RunWith(FastbootTestRunner.class)
@FastBootTest(value = ApplicationKnife4jTest.class, webEnvironment = false)
public class Knife4jPluginIntegrationTest {

    /**
     * 测试应用上下文是否正常启动
     */
    @Test
    public void testApplicationContextStarted() {
        // 如果测试能运行到这里，说明应用上下文已经成功启动
        assertTrue("Application context should be started", true);
    }

    /**
     * 测试 Knife4jPluginImp 是否可以被实例化
     */
    @Test
    public void testKnife4jPluginCanBeInstantiated() {
        Knife4jPluginImp plugin = new Knife4jPluginImp();
        assertNotNull("Plugin instance should not be null", plugin);
    }

    /**
     * 测试插件的 init 方法
     */
    @Test
    public void testPluginInit() {
        Knife4jPluginImp plugin = new Knife4jPluginImp();
        // init 方法应该不抛出异常
        plugin.init();
    }

    /**
     * 测试插件的 end 方法
     */
    @Test
    public void testPluginEnd() {
        Knife4jPluginImp plugin = new Knife4jPluginImp();
        // end 方法应该不抛出异常
        plugin.end();
    }
}
