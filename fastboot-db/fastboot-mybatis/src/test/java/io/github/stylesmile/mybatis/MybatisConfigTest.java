package io.github.stylesmile.mybatis;

import io.github.stylesmile.mybatis.bean.MybatisConfig;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MybatisConfig bean.
 */
public class MybatisConfigTest {

    @Test
    public void testDefaultValues() {
        MybatisConfig config = new MybatisConfig();
        assertNull("dataSources should be null by default", config.getDataSources());
        assertEquals("mapperLocations should default to 'mapper'", "mapper", config.getMapperLocations());
        assertNull("plugins should be null by default", config.getPlugins());
        assertTrue("mapUnderscoreToCamelCase should default to true", config.getMapUnderscoreToCamelCase());
    }

    @Test
    public void testSetters() {
        MybatisConfig config = new MybatisConfig();
        config.setMapUnderscoreToCamelCase(false);
        assertFalse(config.getMapUnderscoreToCamelCase());
    }
}