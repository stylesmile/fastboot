package io.github.stylesmile.server;


import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.handle.MappingHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class JlHttpContextHandler implements HTTPServer.ContextHandler {

    @Override
    public int serve(HTTPServer.Request request, HTTPServer.Response response) throws IOException {
        // System.out.println(request.getPath())
        try {
            //跳转controller
            goController(request, response);
        } catch (Throwable ex) {
            //context 初始化时，可能会出错
            response.sendHeaders(500);
            throw new RuntimeException();
        }
        return 0;
    }


    /**
     * 跳转controller
     *
     * @throws IOException 异常
     */
    private void goController(HTTPServer.Request request, HTTPServer.Response response) throws IOException {
        //获取所有Controller和内部定义的接口方法列表
        List<MappingHandler> mappingHandlerList = HandlerManager.getMappingHandlerList();
        //找到当前请求Url对应的Controller接口处理方法
        for (MappingHandler mappingHandler : mappingHandlerList) {
            try {
                if (mappingHandler.handle(request, response)) {
                    return;
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
