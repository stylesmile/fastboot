package io.github.stylesmile.tool;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Bean;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean工厂
 */
public class BeanFactory {
    /**
     * Bean容器
     */
    private static final Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    /**
     * 获取一个Bean
     * @param cls 类
     * @return Object
     */
    public static Object getBean(Class<?> cls) {
        return classToBean.get(cls);
    }

    /**
     * 初始化Bean的方法
     * @param classList  所有类列表
     * @throws InstantiationException 异常
     * @throws IllegalAccessException 异常
     */
    public static void initBean(List<Class<?>> classList) throws InstantiationException, IllegalAccessException {
        ArrayList<Class<?>> toCreate = new ArrayList<>(classList);
        while (toCreate.size() != 0) {
            int remainSize = toCreate.size();
            for (int i = 0; i < toCreate.size(); i++) {
                //创建完，就要移除掉
                if (finishCreate(toCreate.get(i))) {
                    toCreate.remove(i);
                }
            }
            //陷入循环依赖的死循环，抛出异常
            if (toCreate.size() == remainSize) {
                throw new RuntimeException("cycle dependency!");
            }
        }
    }

    /**
     * 初始化Bean
     */
    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        boolean hasBeanAnno = cls.isAnnotationPresent(Bean.class);
        boolean hasControllerAnno = cls.isAnnotationPresent(Controller.class);
        boolean hasServiceSAnno = cls.isAnnotationPresent(Service.class);
        //忽略，没有使用Bean注解和不是Controller、Service的类
        if (!hasBeanAnno && !hasControllerAnno && !hasServiceSAnno) {
            return true;
        }
        //创建Bean，处理对象中的属性，查看是否需要依赖注入
        Object bean = cls.newInstance();
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoWired.class)) {
                //获取属性的类型
                Class<?> fieldType = field.getType();
                //从工厂里面获取，获取不到，先返回
                Object reliantBean = BeanFactory.getBean(fieldType);
                if (reliantBean == null) {
                    return false;
                }
                //从工厂获取到了，设置属性字段可接触
                field.setAccessible(true);
                //反射将对象设置到属性上
                field.set(bean, reliantBean);
            }
        }
        //缓存实例到工厂中
        classToBean.put(cls, bean);
        return true;
    }
}