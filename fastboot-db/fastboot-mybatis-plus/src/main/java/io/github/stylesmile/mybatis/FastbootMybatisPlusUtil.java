package io.github.stylesmile.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

public class FastbootMybatisPlusUtil {
    public static SqlSessionFactory getSqlSessionFactory() {
        return MybatisPlusPlugin.getSqlSessionFactory();
    }

}
