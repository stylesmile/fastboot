package io.github.stylesmile.ioc;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.Service;
import io.github.stylesmile.filter.Filter;
import io.github.stylesmile.filter.FilterManager;
import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Bean工厂
 *
 * @author Stylesmile
 */
public class BeanFactory {
    /**
     * Bean容器
     */
    private static final List<Class<? extends Annotation>> beanClasses = new CopyOnWriteArrayList<Class<? extends Annotation>>() {{
        add(Controller.class);
        add(Service.class);
        add(Bean.class);
    }};
    private static final List<Class<? extends Annotation>> beanClassesBase = new CopyOnWriteArrayList<Class<? extends Annotation>>() {{
        add(Controller.class);
        add(Service.class);
        add(Bean.class);
    }};

    /**
     * 添加注册bea
     *
     * @param cls 注解名称
     */
    public static void addBeanClasses(Class<? extends Annotation> cls) {
        beanClasses.add(cls);
    }


    /**
     * 初始化Bean的方法
     *
     * @param classList 所有类列表
     * @throws InstantiationException 异常
     * @throws IllegalAccessException 异常
     */
    public static void initBean(List<Class<?>> classList) throws InstantiationException, IllegalAccessException {
        ArrayList<Class<?>> toCreate = new ArrayList<>(classList);
//        while (toCreate.size() != 0) {
        int remainSize = toCreate.size();
        putClassToBean(classList);
        for (int i = 0; i < toCreate.size(); i++) {
            //创建完，就要移除掉
            if (finishCreate(toCreate.get(i))) {
                //toCreate.remove(i);
            }
        }
        // todo 陷入循环依赖的死循环，抛出异常
//        if (toCreate.size() == remainSize) {
//            toCreate.clear();
////                throw new RuntimeException("cycle dependency!");
//        }
//        }
    }

    private static void putClassToBean(List<Class<?>> classList) {
        classList.forEach(cls -> {
            boolean hasBean = false;
            for (Class beanClass : beanClasses) {
                boolean b = cls.isAnnotationPresent(beanClass);
                if (b) {
                    hasBean = true;
                    break;
                }
            }
            if (hasBean) {
                Object bean = null;
                bean = BeanContainer.getSingleInstance(cls);
                BeanContainer.setInstance(cls, bean);
            }
        });
    }

    /**
     * 初始化Bean
     */
    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException {
        boolean hasBean = false;
        for (Class beanClass : beanClassesBase) {
            boolean b = cls.isAnnotationPresent(beanClass);
            if (b) {
                hasBean = true;
                break;
            }
        }
        if (hasBean) {
            //创建Bean，处理对象中的属性，查看是否需要依赖注入
            Object bean = null;
            bean = BeanContainer.getSingleInstance(cls);
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Value.class) != null) {
                    field.setAccessible(true);
                    Value value = field.getAnnotation(Value.class);
                    field.set(bean, StringUtil.isNotEmpty(value.value()) ? PropertyUtil.getProperty(value.value()) : null);
                } else if (field.isAnnotationPresent(AutoWired.class)) {
                    //获取属性的类型
                    Class<?> fieldType = field.getType();
                    //从工厂里面获取，获取不到，先返回
                    Object reliantBean = BeanContainer.getSingleInstance(fieldType);
                    if (reliantBean == null) {
                        continue;
                    }
                    //从工厂获取到了，设置属性字段可接触
                    field.setAccessible(true);
                    //反射将对象设置到属性上
                    field.set(bean, reliantBean);
                }
            }
            addFilter(cls);
            //缓存实例到工厂中
            BeanContainer.setInstance(cls, bean);
        }
        return true;
    }
    private static void addFilter(Class<?> cls) {
        if (!cls.isInterface()) {
            Class<?>[] interfaces = cls.getInterfaces();
            int len = interfaces.length;
            if (interfaces.length > 0) {
                Set<Class> interfaceSet = new HashSet<>(Arrays.asList(interfaces));
                interfaceSet.add(Filter.class);
                if (interfaceSet.size() == len) {
                    try {
                        FilterManager.addFilter(cls);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
