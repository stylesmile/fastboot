package io.github.stylesmile.ioc;

import io.github.stylesmile.annotation.AutoWired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean容器
 *
 * @author Stylesmile
 */
public class BeanContainer {

    public static Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    public static Map<BeanKey, Object> instancesHasName = new ConcurrentHashMap<>();

    /**
     * 添加一个Bean
     *
     * @param cls 类
     * @return Object
     */
    public static void setInstance(Class cls, Object o) {
        instances.put(cls, o);
    }

    public static void setInstancesHasName(BeanKey beanKey, Object o) {
        instancesHasName.put(beanKey, o);
    }

    public static Object getInstancesHasName(BeanKey beanKey) {
        return instancesHasName.get(beanKey);
    }

    /**
     * 获取一个Bean
     *
     * @param cls 类
     * @return Object
     */
    public static <T> T getSingleInstance(Class<T> cls) {
        try {
            //判断是否走单例
            if (!cls.isAnnotationPresent(AutoWired.class)) {
                return getInstance(cls);
            }
            Object obj = instances.get(cls);
            if (null != obj) {
                return (T) obj;
            }
            //使用类锁锁代码块
            synchronized (cls) {
                if (null == instances.get(cls)) {
                    obj = getInstance(cls);
                    instances.put(cls, obj);
                }

            }
            return (T) obj;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static <T> T getInstance(Class<T> cls) {

        try {
            T obj = null;
            obj = (T) instances.get(cls);
            if (obj == null) {
                obj = cls.newInstance();
            }
            //返回本类申明的字段包括非public,不包括父类
//            Field[] declaredFields = cls.getDeclaredFields();
//            for (Field f : declaredFields) {
//                //判断字段是否包含指定注解类型
//                if (f.isAnnotationPresent(AutoWired.class) || f.isAnnotationPresent(Resource.class)) {
//                    //判断字段是否为私有
//                    if (!f.isAccessible()) {
//                        f.setAccessible(true);
//                    }
//                    //再次递归调用赋值
//                    f.set(obj, getInstance(f.getType()));
//                }
//            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
