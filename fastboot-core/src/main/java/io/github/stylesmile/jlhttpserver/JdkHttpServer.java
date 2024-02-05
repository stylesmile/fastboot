package io.github.stylesmile.jlhttpserver;

import com.sun.net.httpserver.HttpServer;
import io.github.stylesmile.tool.StringUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * Jdk Http Server（允许被复用）
 *
 * @author noear
 * @since 2.2
 */
public class JdkHttpServer implements ServerLifecycle {
    private HttpServer server = null;
    private Executor executor;
    private Handler handler;
    private boolean enableSsl = true;
    private boolean isSecure;

    public boolean isSecure() {
        return isSecure;
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
//        if (enableSsl && supportSsl()) {
//            // enable SSL if configured
//            if (Utils.isNotEmpty(host)) {
//                server = HttpsServer.create(new InetSocketAddress(host, port), 0);
//            } else {
//                server = HttpsServer.create(new InetSocketAddress(port), 0);
//            }
//
//            addSslConfig((HttpsServer) server);
//            isSecure = true;
//        } else {
        if (StringUtil.isNotEmpty(host)) {
            server = HttpServer.create(new InetSocketAddress(host, port), 0);
        } else {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        }
//        }

//        HttpContext httpContext = server.createContext("/", new JdkHttpContextHandler(handler));
//        httpContext.getFilters().add(new ParameterFilter());
//
//        server.setExecutor(executor);
//        server.start();
    }

    @Override
    public void stop() throws Throwable {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }

//    private void addSslConfig(HttpsServer httpsServer) throws IOException {
//        SSLContext sslContext = SslContextFactory.create(sslProps);
//
//        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
//            public void configure(HttpsParameters params) {
//                try {
//                    // Initialise the SSL context
//                    SSLContext c = SSLContext.getDefault();
//                    SSLEngine engine = c.createSSLEngine();
//                    params.setNeedClientAuth(false);
//                    params.setCipherSuites(engine.getEnabledCipherSuites());
//                    params.setProtocols(engine.getEnabledProtocols());
//
//                    // Get the default parameters
//                    SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
//                    params.setSSLParameters(defaultSSLParameters);
//                } catch (Throwable e) {
//                    //"Failed to create HTTPS port"
//                    EventBus.publishTry(e);
//                }
//            }
//        });
//    }
}
