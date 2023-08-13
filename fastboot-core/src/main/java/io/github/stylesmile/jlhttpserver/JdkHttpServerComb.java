package io.github.stylesmile.jlhttpserver;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 通过组合支持多端口模式
 *
 * @author noear
 * @since 2.2
 */
public class JdkHttpServerComb implements HttpServerConfigure, ServerLifecycle {
    private Executor executor;
    private Handler handler;
    private boolean enableSsl = true;
    private Set<Integer> addHttpPorts = new LinkedHashSet<>();
    private List<JdkHttpServer> servers = new ArrayList<>();


    @Override
    public void enableSsl(boolean enable) {
        this.enableSsl = enable;
    }

    /**
     * 添加 HttpPort（当 ssl 时，可再开个 http 端口）
     */
    @Override
    public void addHttpPort(int port) {
        addHttpPorts.add(port);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public boolean isSecure() {
        if (servers.size() > 0) {
            return servers.get(0).isSecure();
        } else {
            return false;
        }
    }

    @Override
    public void start(String host, int port) throws Throwable {
        {
            JdkHttpServer s1 = new JdkHttpServer();
            s1.setExecutor(executor);
            s1.setHandler(handler);
            s1.enableSsl(enableSsl);
            s1.start(host, port);

            servers.add(s1);
        }

        for (Integer portAdd : addHttpPorts) {
            JdkHttpServer s2 = new JdkHttpServer();
            s2.setExecutor(executor);
            s2.setHandler(handler);
            s2.enableSsl(false); //只支持http
            s2.start(host, portAdd);

            servers.add(s2);
        }
    }

    @Override
    public void stop() throws Throwable {
        for (ServerLifecycle s : servers) {
            s.stop();
        }
    }
}
