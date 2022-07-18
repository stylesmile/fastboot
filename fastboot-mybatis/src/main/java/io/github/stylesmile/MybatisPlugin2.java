package io.github.stylesmile;

import cn.hserver.core.ioc.IocUtil;
import cn.hserver.core.ioc.annotation.Autowired;
import cn.hserver.core.server.util.ExceptionUtil;
import cn.hserver.plugin.mybatis.annotation.Mybatis;
import cn.hserver.plugin.mybatis.bean.MybatisConfig;
import cn.hserver.plugin.mybatis.proxy.MybatisProxy;
import io.github.stylesmile.app.App;
import io.github.stylesmile.plugin.Plugin;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * 参考文献 https://blog.csdn.net/qq_42413011/article/details/118640420
 *
 * @author hxm
 */
public class MybatisPlugin2 implements Plugin {

    private static final Logger log = LoggerFactory.getLogger(MybatisPlugin2.class);

    private Map<String, SqlSessionFactory> stringSqlSessionFactoryMap;

    @Override
    public void start() {

    }

    @Override
    public void init() {
        //开始把自己的Sql装备进去
        Set<Class> classes = new HashSet<>();
        Map<String, Object> all = App.classList;
        all.forEach((k, v) -> {
            if (v instanceof List) {
                List v1 = (List) v;
                for (Object o : v1) {
                    //获取当前类的所有字段向上最加一层，有可能是代理类查不到
                    List<Field> objectField = getObjectField(o);
                    for (Field declaredField : objectField) {
                        mybatisScan(declaredField, classes);
                    }
                }
            } else {
                //获取当前类的所有字段向上最加一层，有可能是代理类查不到
                List<Field> objectField = getObjectField(v);
                for (Field declaredField : objectField) {
                    mybatisScan(declaredField, classes);
                }
            }
        });

        try {
            stringSqlSessionFactoryMap = MybatisInit.initMybatis(IocUtil.getBean(MybatisConfig.class), classes);
            if (stringSqlSessionFactoryMap == null) {
                return;
            }
            stringSqlSessionFactoryMap.forEach(IocUtil::addBean);
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
        }
    }


    @Override
    public void end() {
        if (stringSqlSessionFactoryMap == null) {
            return;
        }
        //Bean对象
        Map<String, Object> all = IocUtil.getAll();
        all.forEach((k, v) -> {
            if (v instanceof List) {
                List v1 = (List) v;
                for (Object o : v1) {
                    //获取当前类的所有字段向上最加一层，有可能是代理类查不到
                    List<Field> objectField = getObjectField(o);
                    for (Field declaredField : objectField) {
                        mybatisConfig(declaredField, o);
                    }
                }
            } else {
                //获取当前类的所有字段向上最加一层，有可能是代理类查不到
                List<Field> objectField = getObjectField(v);
                for (Field declaredField : objectField) {
                    mybatisConfig(declaredField, v);
                }
            }
        });
        log.info("mybatis插件执行完成");
    }


    /**
     * Mybatis
     *
     * @param declaredField
     * @param v
     */
    private void mybatisConfig(Field declaredField, Object v) {
        //检查是否有注解@Autowired
        Autowired annotation = declaredField.getAnnotation(Autowired.class);
        if (annotation != null) {
            declaredField.setAccessible(true);
            //检查字段是类型是否被@Beetlsql标注
            Mybatis mybatis = declaredField.getType().getAnnotation(Mybatis.class);
            try {
                if (mybatis != null) {
                    String value = mybatis.value();
                    if (value.trim().length() == 0) {
                        value = SqlSessionFactory.class.getName();
                    }
                    SqlSessionFactory sqlSessionFactory = stringSqlSessionFactoryMap.get(value);
                    if (sqlSessionFactory == null) {
                        log.error("数据源名字：{} 不存在", mybatis.value());
                        return;
                    }
                    Object mapper = MybatisProxy.getInstance().getProxy(declaredField.getType(), sqlSessionFactory);
                    //同类型注入
                    if (declaredField.getType().isAssignableFrom(mapper.getClass())) {
                        declaredField.set(v, mapper);
                        log.info("{}----->{}：装配完成，{}", mapper.getClass().getSimpleName(), v.getClass().getSimpleName(), "Mybatis注入");
                    } else {
                        log.error("{}----->{}：装配错误:类型不匹配", v.getClass().getSimpleName(), v.getClass().getSimpleName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("装配错误");
            }
        }
    }


    private void mybatisScan(Field declaredField, Set<Class> classes) {
        //检查是否有注解@Autowired
        Autowired annotation = declaredField.getAnnotation(Autowired.class);
        if (annotation != null) {
            declaredField.setAccessible(true);
            //检查字段是类型是否被@Beetlsql标注
            Mybatis mybatis = declaredField.getType().getAnnotation(Mybatis.class);
            try {
                if (mybatis != null) {
                    classes.add(declaredField.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("装配错误");
            }
        }
    }

    private List<Field> getObjectField(Object clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> aClass = clazz.getClass();
        while (!aClass.equals(Object.class)) {
            Field[] declaredFields = aClass.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            aClass = aClass.getSuperclass();
        }
        return fields;
    }

}
