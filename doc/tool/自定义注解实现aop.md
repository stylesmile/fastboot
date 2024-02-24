## 完整代码案例 [/fastboot-aop-example](..%2F..%2Ffastboot-example%2Ffastboot-tool-example-parent%2Ffastboot-aop-example)
### 依赖

```
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fastboot-aop</artifactId>
</dependency>

<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.14.0</version>
    <configuration>
        <parameters>true</parameters>
        <complianceLevel>1.8</complianceLevel>
        <source>1.8</source>
        <target>1.8</target>
        <showWeaveInfo>true</showWeaveInfo>
        <verbose>true</verbose>
        <Xlint>ignore</Xlint>
        <encoding>UTF-8</encoding>
    </configuration>
    <executions>
        <execution>
            <goals>
                <!-- use this goal to weave all your main classes -->
                <goal>compile</goal>
                <!-- use this goal to weave all your test classes -->
                <goal>test-compile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### java 代码
```
package com.example.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**自定义注解*/
@Retention(RetentionPolicy.RUNTIME)//定义我们自己写的注解何时有效
@Target(ElementType.METHOD)//定义我们写的注解可以描述的成员
public @interface LoginFilter {

    //在注解里加了个loginDefine，就是为了给用户提供自定义实现，如根据loginDefine值不同做不同的登录处理。
    int loginDefine() default 0;

}

```

### 切面代码
```
package com.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 登录切面处理
 */

@Aspect//此注解描述的类为切面类，其内部可以定义切入点表达式和通知方法
public class LoginFilterAspect {

    //@annotation(包名.注解名)
    @Pointcut("@annotation(com.example.aop.LoginFilter)")
    public void LoginFilter() {//承载切入点表达式的定义，方法不写任何内容
    }


    // @Around("@annotation(com.example.app.LoginFilter)")
    @Around("LoginFilter()")
    public Object aroundLoginPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        System.out.println("切点");
        long start = System.nanoTime();
        long end = System.nanoTime();
        System.out.println(
                String.format("[%s] method [%s] execution time: %s ",
                Thread.currentThread().getName(),
                joinPoint.getSignature().getName(),
                (end - start)));
        return proceed;
    }
}

```
