package io.github.stylesmile.knife4j;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Knife4jPluginImp 单元测试
 */
public class Knife4jPluginImpTest {

    /**
     * 测试 FILE_MAPPING 初始化
     */
    @Test
    public void testFileMappingInitialization() {
        // 验证 FILE_MAPPING 不为 null
        assertNotNull("FILE_MAPPING should not be null", Knife4jPluginImp.FILE_MAPPING);
    }

    /**
     * 测试 get 方法处理路径转换
     */
    @Test
    public void testGetMethodPathConversion() {
        // 添加一个测试映射
        Knife4jPluginImp.FILE_MAPPING.put("test\\path.txt", "test/path.txt");
        
        // 测试路径转换
        String result = Knife4jPluginImp.get("test/path.txt");
        assertEquals("test/path.txt", result);
    }

    /**
     * 测试 DEFAULT_STATIC_LOCATION 常量
     */
    @Test
    public void testDefaultStaticLocation() {
        assertEquals("static/", Knife4jPluginImp.DEFAULT_STATIC_LOCATION);
    }

    /**
     * 测试插件实例化
     */
    @Test
    public void testPluginInstantiation() {
        Knife4jPluginImp plugin = new Knife4jPluginImp();
        assertNotNull("Plugin instance should not be null", plugin);
    }

    /**
     * 测试 init 方法
     */
    @Test
    public void testInitMethod() {
        Knife4jPluginImp plugin = new Knife4jPluginImp();
        // init 方法应该不抛出异常
        plugin.init();
    }

    /**
     * 测试 end 方法
     */
    @Test
    public void testEndMethod() {
        Knife4jPluginImp plugin = new Knife4jPluginImp();
        // end 方法应该不抛出异常
        plugin.end();
    }
}
