package io.github.stylesmile.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FastBoot测试注解，类似于Spring Boot的@SpringBootTest。
 * <p>
 * 该注解用于标记测试类，触发FastbootTestRunner初始化FastBoot应用上下文，
 * 使得可以在不启动实际HTTP服务器的情况下进行单元测试。
 * </p>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * @RunWith(FastbootTestRunner.class)
 * @FastBootTest(Application.class)
 * public class MyControllerTest {
 *     @Autowired
 *     private MyService myService;
 *     
 *     @Test
 *     public void testSomething() {
 *         // 测试逻辑
 *     }
 * }
 * }</pre>
 *
 * @author stylesmile
 * @since 2.10.3
 * @see io.github.stylesmile.test.runner.FastbootTestRunner
 * @see io.github.stylesmile.test.context.TestApplicationContext
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FastBootTest {
    /**
     * 指定要测试的应用程序类（传递给App.start的类）。
     * <p>
     * 该类应该包含@Fastboot注解，用于配置扫描包等信息。
     * </p>
     *
     * @return 应用程序类
     */
    Class<?> value();

    /**
     * 是否启动HTTP服务器环境。
     * <p>
     * 默认为false，表示只初始化应用上下文而不启动服务器，
     * 这样可以加快测试速度并减少资源占用。
     * </p>
     *
     * @return true表示启动Web环境，false表示仅初始化上下文
     */
    boolean webEnvironment() default false;

    /**
     * HTTP服务器的端口号，仅在webEnvironment为true时生效。
     * <p>
     * 默认值为0，表示使用随机可用端口。
     * </p>
     *
     * @return 端口号
     */
    int port() default 0;
}
