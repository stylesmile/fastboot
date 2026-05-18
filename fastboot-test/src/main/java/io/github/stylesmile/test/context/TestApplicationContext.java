package io.github.stylesmile.test.context;

import io.github.stylesmile.annotation.Fastboot;
import io.github.stylesmile.app.App;
import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.ioc.BeanFactory;
import io.github.stylesmile.plugin.StartPlugsManager;
import io.github.stylesmile.tool.ClassScanner;
import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;

import java.io.IOException;
import java.util.List;

/**
 * Test application context that initializes fastboot beans and handlers
 * without starting the actual HTTP server.
 * Similar to Spring's TestApplicationContext.
 */
public class TestApplicationContext {

    private final Class<?> applicationClass;
    private final StartPlugsManager beanPlugsManager = new StartPlugsManager();
    private volatile boolean started = false;

    public TestApplicationContext(Class<?> applicationClass) {
        this.applicationClass = applicationClass;
    }

    /**
     * Start the test context: scan classes, init beans, resolve handlers.
     * Does NOT start the HTTP server.
     */
    public void start() {
        if (started) {
            return;
        }

        String scanPackage = null;
        Class<?>[] includeClass = null;
        Class<?>[] excludeClass = null;

        if (applicationClass.isAnnotationPresent(Fastboot.class)) {
            Fastboot annotation = applicationClass.getAnnotation(Fastboot.class);
            scanPackage = annotation.scanPackage();
            includeClass = annotation.include();
            excludeClass = annotation.exclude();
        }

        PropertyUtil.loadProps(applicationClass, "application.properties", new String[0]);

        try {
            if (StringUtil.isEmpty(scanPackage)) {
                scanPackage = applicationClass.getPackage().getName();
            }

            List<Class<?>> classList = ClassScanner.scanClasses(scanPackage);

            if (includeClass != null && includeClass.length != 0) {
                for (Class<?> aClass : includeClass) {
                    classList.add(aClass);
                }
            }
            if (excludeClass != null && excludeClass.length != 0) {
                for (Class<?> aClass : excludeClass) {
                    classList.remove(aClass);
                }
            }

            App.classList = classList;

            beanPlugsManager.start();
            beanPlugsManager.init();
            BeanFactory.initBean(classList);
            HandlerManager.resolveMappingHandler(classList);
            beanPlugsManager.end();

        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to start test context", e);
        }

        started = true;
    }

    /**
     * Stop the test context and clean up resources.
     */
    public void stop() {
        started = false;
    }

    /**
     * Check if the context is started.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Get a bean from the container.
     */
    public <T> T getBean(Class<T> cls) {
        return BeanContainer.getSingleInstance(cls);
    }
}
