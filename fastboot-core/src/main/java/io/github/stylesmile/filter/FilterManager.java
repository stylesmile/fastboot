package io.github.stylesmile.filter;


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

    public static void excutePreHandle(Request request, Response response) {
        for (Filter plugAdapter : obj) {
            plugAdapter.preHandle(request, response);
        }
    }

    /**
     * 执行所有预处理
     *
     * @param request  请求
     * @param response 响应
     */

    public static void excuteAfterCompletion(Request request, Response response) {
        for (Filter plugAdapter : obj) {
            plugAdapter.afterCompletion(request, response);
        }
    }


}
