//package com.example;
//
//import com.example.mapper.UserMapper;
//import org.apache.ibatis.datasource.pooled.PooledDataSource;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.mapping.Environment;
//import org.apache.ibatis.session.Configuration;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.apache.ibatis.transaction.TransactionFactory;
//import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
//
//import java.io.IOException;
//import java.io.Reader;
//
//
//public class MybatisConfig {
//    public static void main2(String[] args) throws IOException {
//        String resource = "mybatis-config.xml";
//        Reader reader = Resources.getResourceAsReader(resource);
//        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//        SqlSessionFactory factory = builder.build(reader);
//        SqlSession session = factory.openSession();
//        User user = session.selectOne(
//                "com.example.UserMapper.selectById", 1);
//        System.out.println(user);
//    }
//
//    public SqlSessionFactory main(String[] args) {
//        // 数据库连接池信息
//        PooledDataSource dataSource = new PooledDataSource();
//        dataSource.setDriver("com.mysql.jdbc.Driver");
//        dataSource.setUsername("root");
//        dataSource.setPassword("1128");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
////        dataSource.setDefeultAutoCommit(false);
//        // 采用 MyBatis 的 JDBC 事务方式
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        // 创建 Configuration 对象
//        Configuration configuration = new Configuration(environment);
//        // 注册一个 MyBatis 上下文别名
//        configuration.getTypeAliasRegistry().registerAlias("user", User.class);
//        // 加入一个映射器
//        configuration.addMapper(UserMapper.class);
//        //使用 SqlSessionFactoryBuilder 构建 SqlSessionFactory
//        SqlSessionFactory SqlSessionFactory =
//                new SqlSessionFactoryBuilder().build(configuration);
//        return SqlSessionFactory;
//    }
//
//}
