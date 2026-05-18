package io.github.stylesmile.mybatis;

import io.github.stylesmile.mybatis.bean.MybatisConfig;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Unit tests for MybatisInit (multi-datasource initializer).
 */
public class MybatisInitTest {

    @Test
    public void testMybatisInitCanBeInstantiated() {
        MybatisInit init = new MybatisInit();
        assertNotNull(init);
    }
}