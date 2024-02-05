package io.github.stylesmile.plugin;

/**
 * @author Stylesmile
 */
public interface Plugin {
    /**
     * 开始初始化
     */
    void start();

    /**
     * 初始化
     */
    void init();

    /**
     * 完成
     */
    void end();
}
