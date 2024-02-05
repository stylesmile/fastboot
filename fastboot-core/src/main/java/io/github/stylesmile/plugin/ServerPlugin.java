package io.github.stylesmile.plugin;

/**
 * @author chenye
 * 2023-0814
 */
public interface ServerPlugin {
    /**
     * 启动方法
     *
     * @param applicationClass app 启动类
     * @param args             参数
     */
    void start(Class applicationClass, String[] args);
}
