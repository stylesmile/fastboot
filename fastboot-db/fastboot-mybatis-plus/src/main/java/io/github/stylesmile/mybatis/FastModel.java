//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.stylesmile.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Service;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public  class FastModel <T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient Log log = LogFactory.getLog(this.getClass());

    @AutoWired
    SqlSessionFactory sqlSessionFactory;
    public FastModel() {
    }

    public boolean insert() {
        SqlSession sqlSession = this.sqlSession();

        boolean var2;
        try {
            var2 = SqlHelper.retBool(sqlSession.insert(this.sqlStatement(SqlMethod.INSERT_ONE), this));
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return var2;
    }

    public boolean insertOrUpdate() {
        return !StringUtils.checkValNull(this.pkVal()) && !Objects.isNull(this.selectById(this.pkVal())) ? this.updateById() : this.insert();
    }

    public boolean deleteById(Serializable id) {
        SqlSession sqlSession = this.sqlSession();

        boolean var3;
        try {
            var3 = SqlHelper.retBool(sqlSession.delete(this.sqlStatement(SqlMethod.DELETE_BY_ID), id));
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return var3;
    }

    public boolean deleteById() {
        Assert.isFalse(StringUtils.checkValNull(this.pkVal()), "deleteById primaryKey is null.", new Object[0]);
        return this.deleteById(this.pkVal());
    }

    public boolean delete(Wrapper<T> queryWrapper) {
        Map<String, Object> map = new HashMap(1);
        map.put("ew", queryWrapper);
        SqlSession sqlSession = this.sqlSession();

        boolean var4;
        try {
            var4 = SqlHelper.retBool(sqlSession.delete(this.sqlStatement(SqlMethod.DELETE), map));
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return var4;
    }

    public boolean updateById() {
        Assert.isFalse(StringUtils.checkValNull(this.pkVal()), "updateById primaryKey is null.", new Object[0]);
        Map<String, Object> map = new HashMap(1);
        map.put("et", this);
        SqlSession sqlSession = this.sqlSession();

        boolean var3;
        try {
            var3 = SqlHelper.retBool(sqlSession.update(this.sqlStatement(SqlMethod.UPDATE_BY_ID), map));
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return var3;
    }

    public boolean update(Wrapper<T> updateWrapper) {
        Map<String, Object> map = new HashMap(2);
        map.put("et", this);
        map.put("ew", updateWrapper);
        SqlSession sqlSession = this.sqlSession();

        boolean var4;
        try {
            var4 = SqlHelper.retBool(sqlSession.update(this.sqlStatement(SqlMethod.UPDATE), map));
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return var4;
    }

    public List<T> selectAll() {
        SqlSession sqlSession = this.sqlSession();

        List var2;
        try {
            var2 = sqlSession.selectList(this.sqlStatement(SqlMethod.SELECT_LIST));
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return var2;
    }

    public T selectById(Serializable id) {
        SqlSession sqlSession = this.sqlSession();

        T var3;
        try {
            var3 = (T) sqlSession.selectOne(this.sqlStatement(SqlMethod.SELECT_BY_ID), id);
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return (T) var3;
    }

    public T selectById() {
        Assert.isFalse(StringUtils.checkValNull(this.pkVal()), "selectById primaryKey is null.", new Object[0]);
        return this.selectById(this.pkVal());
    }

    public List<T> selectList(Wrapper<T> queryWrapper) {
        Map<String, Object> map = new HashMap(1);
        map.put("ew", queryWrapper);
        SqlSession sqlSession = this.sqlSession();

        List var4;
        try {
            var4 = sqlSession.selectList(this.sqlStatement(SqlMethod.SELECT_LIST), map);
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return var4;
    }

    public T selectOne(Wrapper<T> queryWrapper) {
        return (T) SqlHelper.getObject(this.log, this.selectList(queryWrapper));
    }

    public <E extends IPage<T>> E selectPage(E page, Wrapper<T> queryWrapper) {
        Map<String, Object> map = new HashMap(2);
        map.put("ew", queryWrapper);
        map.put("page", page);
        SqlSession sqlSession = this.sqlSession();

        try {
            page.setRecords(sqlSession.selectList(this.sqlStatement(SqlMethod.SELECT_PAGE), map));
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return page;
    }

    public Long selectCount(Wrapper<T> queryWrapper) {
        Map<String, Object> map = new HashMap(1);
        map.put("ew", queryWrapper);
        SqlSession sqlSession = this.sqlSession();

        Long var4;
        try {
            var4 = SqlHelper.retCount(
                    (Long) sqlSession.selectOne(
                            this.sqlStatement(SqlMethod.SELECT_COUNT), map
                    )
            );
        } finally {
            this.closeSqlSession(sqlSession);
        }

        return var4;
    }

    public SqlRunner sql() {
        return new SqlRunner(this.getClass());
    }

    protected SqlSession sqlSession() {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        return sqlSessionFactory.openSession();

    }

    protected String sqlStatement(SqlMethod sqlMethod) {
        return this.sqlStatement(sqlMethod.getMethod());
    }

    protected String sqlStatement(String sqlMethod) {
        return SqlHelper.table(this.getClass()).getSqlStatement(sqlMethod);
    }

    protected Serializable pkVal() {
        return (Serializable) ReflectionKit.getFieldValue(this, TableInfoHelper.getTableInfo(this.getClass()).getKeyProperty());
    }

    protected void closeSqlSession(SqlSession sqlSession) {
        sqlSession.close();
    }
}
