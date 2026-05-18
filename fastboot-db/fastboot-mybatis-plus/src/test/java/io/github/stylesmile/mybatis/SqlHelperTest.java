package io.github.stylesmile.mybatis;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for SqlHelper utility methods.
 */
public class SqlHelperTest {

    @Test
    public void testRetBoolWithNull() {
        assertFalse(SqlHelper.retBool((Integer) null));
        assertFalse(SqlHelper.retBool((Long) null));
    }

    @Test
    public void testRetBoolWithZero() {
        assertFalse(SqlHelper.retBool(0));
        assertFalse(SqlHelper.retBool(0L));
    }

    @Test
    public void testRetBoolWithPositive() {
        assertTrue(SqlHelper.retBool(1));
        assertTrue(SqlHelper.retBool(5));
        assertTrue(SqlHelper.retBool(1L));
        assertTrue(SqlHelper.retBool(100L));
    }

    @Test
    public void testRetCountWithNull() {
        assertEquals(0L, SqlHelper.retCount(null));
    }

    @Test
    public void testRetCountWithValue() {
        assertEquals(5L, SqlHelper.retCount(5L));
        assertEquals(0L, SqlHelper.retCount(0L));
        assertEquals(100L, SqlHelper.retCount(100L));
    }

    @Test
    public void testGetObjectWithEmptyList() {
        Log log = LogFactory.getLog(SqlHelperTest.class);
        assertNull(SqlHelper.getObject(log, new ArrayList<>()));
    }

    @Test
    public void testGetObjectWithSingleElement() {
        Log log = LogFactory.getLog(SqlHelperTest.class);
        List<String> list = Collections.singletonList("test");
        assertEquals("test", SqlHelper.getObject(log, list));
    }

    @Test
    public void testGetObjectWithMultipleElements() {
        Log log = LogFactory.getLog(SqlHelperTest.class);
        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("second");
        // Should return first element and log a warning
        assertEquals("first", SqlHelper.getObject(log, list));
    }

    @Test
    public void testGetObjectWithSupplier() {
        List<String> list = Collections.singletonList("result");
        assertEquals("result", SqlHelper.getObject(() -> LogFactory.getLog(SqlHelperTest.class), list));
    }

    @Test
    public void testGetObjectWithSupplierAndNull() {
        assertNull(SqlHelper.getObject(() -> LogFactory.getLog(SqlHelperTest.class), new ArrayList<>()));
    }

    @Test
    public void testGetSqlStatement() {
        String prefix = TestMapper.class.getName();
        String statement = SqlHelper.getSqlStatement(TestMapper.class, SqlMethod.INSERT_ONE);
        assertEquals(prefix + ".insert", statement);
    }

    @Test
    public void testGetSqlStatementForDelete() {
        String prefix = TestMapper.class.getName();
        String statement = SqlHelper.getSqlStatement(TestMapper.class, SqlMethod.DELETE_BY_ID);
        assertEquals(prefix + ".deleteById", statement);
    }

    @Test
    public void testGetSqlStatementForUpdate() {
        String prefix = TestMapper.class.getName();
        String statement = SqlHelper.getSqlStatement(TestMapper.class, SqlMethod.UPDATE_BY_ID);
        assertEquals(prefix + ".updateById", statement);
    }

    @Test
    public void testGetSqlStatementForSelect() {
        String prefix = TestMapper.class.getName();
        String statement = SqlHelper.getSqlStatement(TestMapper.class, SqlMethod.SELECT_BY_ID);
        assertEquals(prefix + ".selectById", statement);
    }

    @Test
    public void testGetSqlStatementForSelectCount() {
        String prefix = TestMapper.class.getName();
        String statement = SqlHelper.getSqlStatement(TestMapper.class, SqlMethod.SELECT_COUNT);
        assertEquals(prefix + ".selectCount", statement);
    }

    // Helper interface for testing getSqlStatement
    public interface TestMapper {
        int insert(Object entity);
        int deleteById(Object id);
        int updateById(Object entity);
        Object selectById(Object id);
        long selectCount(Object query);
    }
}