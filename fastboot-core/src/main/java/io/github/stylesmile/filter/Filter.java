package io.github.stylesmile.filter;

import io.github.stylesmile.plugin.StartPlugsManager;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;

/**
 * @author Stylesmile
 */
public interface Filter {
    /**
     * 进入接口前执行
     */
    boolean preHandle(Request request, Response response);

    /**
     * 接口完成之后执行
     */
    boolean afterCompletion(Request request, Response response);
}
