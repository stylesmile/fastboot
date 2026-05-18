package io.github.stylesmile.test.runner;

import io.github.stylesmile.annotation.Fastboot;
import io.github.stylesmile.app.App;
import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.ioc.BeanFactory;
import io.github.stylesmile.plugin.StartPlugsManager;
import io.github.stylesmile.test.annotation.FastBootTest;
import io.github.stylesmile.test.context.TestApplicationContext;
import io.github.stylesmile.tool.ClassScanner;
import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Custom JUnit Runner for fastboot tests, similar to SpringRunner.
 * Initializes the fastboot application context (beans, handlers, plugins)
 * without starting the actual HTTP server.
 */
public class FastbootTestRunner extends Runner {

    private final BlockJUnit4ClassRunner innerRunner;
    private TestApplicationContext context;

    public FastbootTestRunner(Class<?> testClass) throws InitializationError {
        innerRunner = new BlockJUnit4ClassRunner(testClass);
    }

    @Override
    public Description getDescription() {
        return innerRunner.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        Class<?> testClass = innerRunner.getTestClass().getJavaClass();
        FastBootTest fastBootTest = testClass.getAnnotation(FastBootTest.class);
        if (fastBootTest == null) {
            throw new IllegalStateException("Test class must be annotated with @FastBootTest");
        }

        Class<?> applicationClass = fastBootTest.value();
        context = new TestApplicationContext(applicationClass);
        context.start();

        try {
            innerRunner.run(notifier);
        } finally {
            context.stop();
        }
    }
}
