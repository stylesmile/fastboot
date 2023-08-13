package io.github.stylesmile.jlhttpserver;

import io.github.stylesmile.server.JlHttpContextHandler;

public class JlHttpServerStart {
    public static JdkHttpServerComb _server;

    public static   void start(int port, JlHttpContextHandler jlHttpContextHandler) {
        //初始化属性

        final String _host = null;

        long time_start = System.currentTimeMillis();


        _server = new JdkHttpServerComb();
        try {
            _server.start(_host, port);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }


        long time_end = System.currentTimeMillis();

        String serverUrl = (_server.isSecure() ? "https" : "http") + "://localhost:" + port;
    }
}
