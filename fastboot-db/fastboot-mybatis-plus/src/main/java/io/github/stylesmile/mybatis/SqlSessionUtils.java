package io.github.stylesmile.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class SqlSessionUtils {
    public static SqlSession getSqlSession(SqlSessionFactory sqlSessionFactory) {
        return FastbootMybatisPlusUtil.getSqlSessionFactory().openSession();
    }

    public static void closeSqlSession(SqlSession sqlSession, SqlSessionFactory sqlSessionFactory) {
        sqlSession.close();
    }
}
