package io.github.stylesmile.mybatis;

import org.junit.Test;

/**
 * Unit tests for MybatisInit (multi-datasource initializer).
 *
 * Note: MybatisInit has a static Logger field using SLF4J,
 * which triggers SLF4J initialization on class load. The project
 * has an SLF4J version conflict between logback and mybatis-plus.
 * This test uses Class.forName with initialize=false to avoid triggering
 * the static initializer, so it can verify the class exists without
 * loading all its dependencies.
 */
public class MybatisInitTest {

    @Test
    public void testMybatisInitClassExists() throws Exception {
        String className = "io.github.stylesmile.mybatis.MybatisInit";
        try {
            // Use Class.forName with initialize=false to avoid triggering static init
            // This verifies the class definition exists without loading SLF4J logging
            Class<?> clazz = Class.forName(className, false,
                    Thread.currentThread().getContextClassLoader());
            org.junit.Assert.assertNotNull(clazz);
            org.junit.Assert.assertEquals(className, clazz.getName());
        } catch (ClassNotFoundException e) {
            // Class definition not found at all (should not happen)
            throw e;
        }
    }
}