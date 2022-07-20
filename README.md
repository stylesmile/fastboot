# java_write_frame

#### 介绍

一个极速java web 框架

#### 说明

0.1秒就可以启动项目

####  

####快速开始

#####maven依赖
```maven
        <parent>
            <groupId>io.github.stylesmile</groupId>
            <artifactId>fastboot-parent</artifactId>
            <version>0.1.7-M2</version>
        </parent>
```
```maven
        <dependency>
            <groupId>io.github.stylesmile</groupId>
            <artifactId>fastboot-core</artifactId>
        </dependency>
```
#####如果你管理依赖用的gradle
参考下面的配置
```gradle
plugins {
    id 'java'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
}

group 'org.example'
version '1.0-SNAPSHOT'
repositories {
    maven {url "https://s01.oss.sonatype.org/content/repositories/releases/"}
    maven {url "https://repo2.maven.org/maven2/"}
    mavenCentral()
}
jar {
    //详细信息参考 https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Jar.html
    archivesBaseName = 'fastboot-demo'//基本的文件名
    archiveVersion = '0.0.1' //版本
    manifest { //配置jar文件的manifest
        attributes(
                "Manifest-Version": 1.0,
                'Main-Class': 'com.example.Application' //指定main方法所在的文件
        )
    }
    //打包依赖包
    from {
        (configurations.runtimeClasspath).collect {
            it.isDirectory() ? it : zipTree(it)
        }
    }
}
dependencies {
    implementation 'io.github.stylesmile:fastboot-core'
}
dependencyManagement {
    imports {
        mavenBom 'io.github.stylesmile:fastboot-parent:0.1.7-M2'
    }
}
tasks.named('test') {
    useJUnitPlatform()
}

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
####发布最新版后，其他各个镜像仓库可能没有马上更新，可以用maven中央仓库原仓库下载
```maven
        <repository>
            <id>maven1</id>
            <url>https://s01.oss.sonatype.org/content/repositories/releases/</url>
        </repository>
```

