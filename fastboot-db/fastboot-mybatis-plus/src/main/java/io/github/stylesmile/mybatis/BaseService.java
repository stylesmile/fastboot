package io.github.stylesmile.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BaseService<M extends BaseMapper<T>, T> {
    public boolean insert(T t) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).insert(t) > 0;
            session.commit();
        }
        return b;
    }

    public boolean deleteById(Serializable id) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).deleteById(id) > 0;
            session.commit();
        }
        return b;
    }

    public boolean deleteById(T t) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).deleteById(t) > 0;
            session.commit();
        }
        return b;
    }

    public boolean deleteByMap(Map<String, Object> columnMap) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).deleteByMap(columnMap) > 0;
            session.commit();
        }
        return b;
    }


    public boolean delete(Wrapper wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).delete(wrapper) > 0;
            session.commit();
        }
        return b;
    }

    public boolean deleteBatchIds(Collection<?> idList) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).deleteBatchIds(idList) > 0;
            session.commit();
        }
        return b;
    }

    public boolean updateById(T t) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).updateById(t) > 0;
            session.commit();
        }
        return b;
    }

    public boolean update(T t, Wrapper wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).update(t, wrapper) > 0;
            session.commit();
        }
        return b;
    }

    public boolean update(Wrapper wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean b = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            b = getMapper(session).update(wrapper) > 0;
            session.commit();
        }
        return b;
    }

    public T selectById(Serializable id) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        T t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = (T) getMapper(session).selectById(id);
            session.commit();
        }
        return t;
    }

    public List<T> selectBatchIds(Collection<? extends Serializable> idList) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        List<T> t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectBatchIds(idList);
        }
        return t;
    }

    public void selectBatchIds(Collection<? extends Serializable> idList, ResultHandler<T> resultHandler) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        List<T> t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            getMapper(session).selectBatchIds(idList, resultHandler);
        }
    }

    public T selectById(Wrapper wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        T t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = (T) getMapper(session).selectById(wrapper);
            session.commit();
        }
        return t;
    }

    public List<T> selectByMap(Map<String, Object> columnMap) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        List<T> t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectByMap(columnMap);
            session.commit();
        }
        return t;
    }

    public void selectByMap(Map<String, Object> columnMap, ResultHandler<T> resultHandler) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            getMapper(session).selectByMap(columnMap, resultHandler);
            session.commit();
        }
    }

    public T selectOne(Wrapper<T> queryWrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        T t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectOne(queryWrapper);
            session.commit();
        }
        return t;
    }

    public T selectOne(Wrapper<T> queryWrapper, boolean throwEx) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        T t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectOne(queryWrapper, throwEx);
            session.commit();
        }
        return t;
    }

    public boolean exists(Wrapper<T> queryWrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        boolean t = false;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).exists(queryWrapper);
            session.commit();
        }
        return t;
    }

    public long selectCount(Wrapper wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        long t = 0;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectCount(wrapper);
        }
        return t;
    }

    public List<T> selectList(Wrapper<T> wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        List<T> t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectList(wrapper);
        }
        return t;
    }

    public void selectList(Wrapper wrapper, ResultHandler<T> resultHandler) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            getMapper(session).selectList(wrapper, resultHandler);
        }
    }

    public List<T> selectList(IPage<T> page, Wrapper wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        List<T> t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectList(page, wrapper);
        }
        return t;
    }

    public void selectList(IPage<T> page, Wrapper<T> wrapper, ResultHandler<T> resultHandler) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            getMapper(session).selectList(page, wrapper, resultHandler);
        }
    }

    public List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        List<Map<String, Object>> t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectMaps(wrapper);
        }
        return t;
    }

    public void selectMaps(Wrapper<T> wrapper, ResultHandler<Map<String, Object>> resultHandler) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            getMapper(session).selectMaps(wrapper, resultHandler);
        }
    }

    public List<Map<String, Object>> selectMaps(IPage<? extends Map<String, Object>> page, Wrapper<T> wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        List<Map<String, Object>> t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectMaps(page, wrapper);
        }
        return t;
    }

    public void selectMaps(IPage<? extends Map<String, Object>> page, Wrapper<T> wrapper, ResultHandler<Map<String, Object>> resultHandler) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            getMapper(session).selectMaps(page, wrapper, resultHandler);
        }
    }

    public <E> List<E> selectObjs(Wrapper<T> wrapper) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        List<E> t = null;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            t = getMapper(session).selectObjs(wrapper);
        }
        return t;
    }

    void selectObjs(Wrapper<T> wrapper, ResultHandler<Map<String, Object>> resultHandler) {
        SqlSessionFactory sqlSessionFactory = FastbootMybatisPlusUtil.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            getMapper(session).selectObjs(wrapper, resultHandler);
        }
    }

    <P extends IPage<T>> P selectPage(P page, Wrapper<T> queryWrapper) {
        page.setRecords(this.selectList(page, queryWrapper));
        return page;
    }

    <P extends IPage<Map<String, Object>>> P selectPageMap(P page, Wrapper<T> wrapper) {
        page.setRecords(this.selectMaps(page, wrapper));
        return page;
    }

    public M getMapper(SqlSession session) {
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        System.out.println(type.getClass());
        System.out.println(type.getTypeName());
        System.out.println(type.toString());
        Class tClass = null;
        try {
            tClass = Class.forName(type.getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (M) session.getMapper(tClass);
    }

}
