package io.github.stylesmile.jlhttpserver;


import io.github.stylesmile.filter.FilterManager;
import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.handle.MappingHandler;
import io.github.stylesmile.server.Context;
import io.github.stylesmile.server.ContextHandler;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class JdkHttpContextHandler implements ContextHandler {


    public JdkHttpContextHandler(Handler handler) {
    }

    public JdkHttpContextHandler() {
    }

    //    @Override
    public int serve(Request request, Response response) throws IOException {
        // System.out.println(request.getPath())
        try {
            //跳转controller
            goController(request, response);
        } catch (Throwable ex) {
            //context 初始化时，可能会出错
            System.err.println("error：" + ex.getCause());
            response.sendHeaders(500);
            ex.printStackTrace();
        }
        return 0;
    }


    /**
     * 跳转controller
     *
     * @throws IOException 异常
     */
    private void goController(Request request, Response response) {
        //获取Controller和内部定义的接口方法列表

        try {
            boolean pre = FilterManager.excutePreHandle(request, response);
            if (!pre) {
                return;
            }
            MappingHandler mappingHandler = HandlerManager.getMappingHandler(request.getPath());
            if (mappingHandler == null) {
                try {
                    response.sendError(404);
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            //找到当前请求Url对应的Controller接口处理方法
            mappingHandler.handle(request, response);
            FilterManager.excuteAfterCompletion(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void handle(Context ctx) throws Throwable {
        //跳转controller
//        goController(ctx., response);
    }
}
