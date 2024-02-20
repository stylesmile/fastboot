package io.github.stylesmile.tool;

import io.github.stylesmile.app.App;
import io.github.stylesmile.ioc.BeanContainer;

/**
 * @author Stylesmile
 */
public class FastbootUtil {
    public static void addClass(Class clz) {
        App.addClass(clz);
    }

    public static <T> T getBean(Class<T> clz) {
        return BeanContainer.getInstance(clz);
    }
}
