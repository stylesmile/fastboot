//package io.github.stylesmile.mybatis;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import org.apache.ibatis.session.SqlSession;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.function.Function;
//
//public class MybatisMapperUtil {
//    public <M extends BaseMapper> List<M> getList(Class<T> clazz) {
//        List<T> tList = null;
//        try (SqlSession sqlSession = FastbootMybatisPlusUtil.getSqlSessionFactory().openSession()) {
//            tList = getMapper(sqlSession,clazz);
//        }
//
//    }
//
//    private List<T> getMapper(SqlSession sqlSession, Class clazz, Function getSqlSessionFactory) {
//    }
//
//    public <M extends BaseMapper> M getMapper(SqlSession session, Class clazz) {
//        return (M) session.getMapper(clazz);
//    }
//
//}
