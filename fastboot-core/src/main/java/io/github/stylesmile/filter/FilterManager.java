package io.github.stylesmile.filter;


import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;

import java.util.HashSet;
import java.util.Set;

/**
 * 拦截器管理器
 *
 * @author Stylesmile
 */
public class FilterManager {
    private final static Set<Filter> obj = new HashSet<>();

    /**
     * 初始化拦截器
     *
     * @param aClass 类名称
     * @throws IllegalAccessException 异常
     * @throws InstantiationException 异常
     */
    public static void addFilter(Class aClass) throws IllegalAccessException, InstantiationException {
        obj.add((Filter) aClass.newInstance());
    }

    /**
     * 执行所有预处理
     *
     * @param request  请求
     * @param response 响应
     */

    public static boolean excutePreHandle(Request request, Response response) {
        for (Filter filter : obj) {
            boolean b = BeanContainer.getSingleInstance(filter.getClass()).preHandle(request, response);
            if (!b) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行所有预处理
     *
     * @param request  请求
     * @param response 响应
     */

    public static void excuteAfterCompletion(Request request, Response response) {
        for (Filter filter : obj) {
            boolean b = BeanContainer.getSingleInstance(filter.getClass()).afterCompletion(request, response);
            if (!b) {
                break;
            }
        }
    }


}
