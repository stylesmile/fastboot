package io.github.stylesmile.jlhttpserver;

import io.github.stylesmile.server.JdkHTTPServer;
import io.github.stylesmile.tool.StringUtil;
import java.util.concurrent.Executor;

/**
 * Jl Http Server（允许被复用）
 * @author noear
 * @since 2.2
 */
public class JlHttpServer implements ServerLifecycle {

    private JdkHTTPServer server = null;
    private Handler handler;
    private Executor executor;
    private boolean enableSsl = true;
    private boolean isSecure;
    public boolean isSecure() {
        return isSecure;
    }
    public void JlHttpContextHandler(Handler handler) {
        this.handler = handler;
    }

//    private ServerSslProps sslProps;
//    protected boolean supportSsl() {
//        if (sslProps == null) {
//            sslProps = ServerSslProps.of(ServerConstants.SIGNAL_HTTP);
//        }
//
//        return sslProps.isEnable() && sslProps.getSslKeyStore() != null;
//    }

    public void enableSsl(boolean enable) {
        this.enableSsl = enable;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void start(String host, int port) throws Throwable {
        server = new JdkHTTPServer();

//        if (enableSsl && supportSsl()) {
//            // enable SSL if configured
//            server.setServerSocketFactory(SslContextFactory.create(sslProps).getServerSocketFactory());
//            isSecure = true;
//        }

//        JdkHTTPServer.VirtualHost virtualHost = server.getVirtualHost(null);
//
//        virtualHost.setDirectoryIndex(null);
//        virtualHost.addContext("/", new JlHttpContextHandler(handler), "*");
//
//        server.setExecutor(executor);
//        server.setPort(port);
//        if (StringUtil.isNotEmpty(host)) {
//            server.setHost(host);
//        }
//        server.start();
    }

    @Override
    public void stop() throws Throwable {
        if (server != null) {
            server.stop();
            server = null;
        }
    }
}
