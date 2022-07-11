# java_write_frame

#### 介绍

一个极速java web 框架

#### 说明

0.1秒就可以启动项目

####  

快速开始

```maven
        <parent>
            <groupId>io.github.stylesmile</groupId>
            <artifactId>fastboot-parent</artifactId>
            <version>0.1.6</version>
        </parent>
```
```maven
        <dependency>
            <groupId>io.github.stylesmile</groupId>
            <artifactId>fastboot-core</artifactId>
        </dependency>
```
```java

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.app.App;

@Controller
public class Application {
    public static void main(String[] args) {
        App.start(Application.class, args);
    }

    @RequestMapping("/")
    public String hello() {
        return "hello fastboot";
    }
}
```

