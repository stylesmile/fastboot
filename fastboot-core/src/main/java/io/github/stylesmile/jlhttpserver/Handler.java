package io.github.stylesmile.jlhttpserver;

import io.github.stylesmile.server.Context;

/**
 * 通用处理接口（实现：Context + Handler 架构）
 *
 * @author noear
 * @since 1.0
 */
@FunctionalInterface
public interface Handler {
    void handle(Context ctx) throws Throwable;
}
