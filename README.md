#### 介绍

一个极速java web 框架

#### 说明

0.1秒就可以启动项目

####  

#### [快速开始详细文档](./doc/1.fastboot-start.md)
#### [快速开始 mybatis](./doc/db/1.fastboot-mybatis.md)
#### [快速开始 redis](./doc/db/2.fastboot-redis.md)
#### [快速开始 mongodb](./doc/db/3.fastboot-mongodb.md)
#### [快速开始 beetlsql](./doc/db/4.fastboot-beetlsql.md)

#####maven依赖[example](fastboot-example/fastboot-web-example)
```maven
    <parent>
		<groupId>io.github.stylesmile</groupId>
		<artifactId>fastboot-parent</artifactId>
		<version>0.7.1</version>
	<parent>
```
```maven
        <dependency>
            <groupId>io.github.stylesmile</groupId>
            <artifactId>fastboot-core</artifactId>
        </dependency>
```
#####如果你管理依赖用的gradle[参考gradle配置](doc/1.fastboot-start-gradle.md)

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
####发布最新版后，其他各个镜像仓库可能过好几天才同步镜像，可以用maven官方仓库原仓库下载
```maven
        <repository>
            <id>maven1</id>
            <url>https://repo1.maven.org/maven2/</url>
        </repository>        
        <repository>
            <id>maven1</id>
            <url>https://s01.oss.sonatype.org/content/repositories/releases/</url>
        </repository>
```

