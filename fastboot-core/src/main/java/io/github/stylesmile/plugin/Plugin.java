package io.github.stylesmile.plugin;

import java.io.IOException;

public interface Plugin {
    /**
     * 开始初始化
     */
    void start() throws IOException;

    /**
     * 初始化
     */
    void init();

    /**
     * 完成
     */
    void end();
}
