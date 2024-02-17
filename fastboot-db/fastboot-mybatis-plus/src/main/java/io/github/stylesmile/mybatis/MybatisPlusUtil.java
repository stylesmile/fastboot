package io.github.stylesmile.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

public class MybatisPlusUtil {
    public static SqlSessionFactory getSqlSessionFactory() {
        return MybatisPlusPlugin.getSqlSessionFactory();
    }

    public static SqlSession getSqlSession() {
        return getSqlSessionFactory().openSession();
    }

    public static <M extends BaseMapper> M getMapper(SqlSession session, Class<M> mapperClass) {
        return session.getMapper(mapperClass);
    }


    public <T> Object excuteMapper(Class mapperClass, String function, Object... args) {
        Object o = null;
        try (SqlSession session = getSqlSessionFactory().openSession()) {
//            method.invoke(session.getMapper(mapperClass), args);
            Method method = mapperClass.getMethod(function, Object.class);
//            java.lang.reflect.Method method1 = new java.lang.reflect.Method(method, "");
//            java.lang.reflect.Method method1 = function.getClass().getEnclosingMethod();
            o = method.invoke(session.getMapper(mapperClass), args);
            session.commit();
            System.out.println(o);
//            o = method.apply(args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return o;
    }

    public static <T> Object excuteMapperStatic(Class mapperClass, Function method, List<T> list, Object... args) {
        Object o = null;
        try (SqlSession session = getSqlSessionFactory().openSession()) {
//            method.invoke(session.getMapper(mapperClass), args);
            o = method.apply(args);
        }
        return o;
    }

    public Object test() {
        System.out.println();
        return new Object();
    }

}
