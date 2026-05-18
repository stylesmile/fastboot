package io.github.stylesmile.mybatis;


import io.github.stylesmile.annotation.Fastboot;
import io.github.stylesmile.app.App;

/**
 * 测试入口类
 *
 * @author stylesmile
 */
@Fastboot
public class Application {
    public static void main(String[] args) {
        App.start(Application.class, args);
    }
}
