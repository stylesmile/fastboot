package io.github.stylesmile.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MybatisPlusUtil.
 */
public class MybatisPlusUtilTest {

    @Test
    public void testGetSqlSessionFactoryReturnsNull() {
        // Before plugin init, SqlSessionFactory should be null
        assertNull(MybatisPlusUtil.getSqlSessionFactory());
    }
}