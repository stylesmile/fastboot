# Introduction

a Extremely fast Java web framework, with a startup speed 20 times faster than commonly used frameworks, one-fifth memory usage, less than 0.1 seconds startup, and a minimum memory usage of 20-30m

# Project Vision

The best cloud native solution in the Java field.

# Personal Vision

* What is needed is a platform, on which one can constantly cultivate oneself, after all, learning things and being exposed to new technological points every day
* Has a sense of direction, is not confused, does not waste time, and has a feasible learning plan
* Continuously accumulate and improve learning efficiency through work and study experience
* It can be summarized and reflected on, and the process can be continuously polished to create a platform, a product, or a masterpiece
* I hope to improve my process skills in writing this project

## Support Description

In order to better open source and support, the following technical support is provided to assist enterprises in quickly realizing cloud based bio platform and platform integration

* Construction and Implementation Guidelines for Enterprise DevOps Technology Platform
* Enterprise automation and continuous integration system support
* Planning and design guidance for the architecture of enterprise cloud original biochemistry in Taiwan and Taiwan
* Enterprise Process Technology Q&A and Technical Guidance
* If you need technical support or group communication, please follow the official account and WeChat communication:

<img src="./doc/image/index/java_zhilu_gongzonghao.png">

### 云原生概念

#### [云原生是什么](doc/云原生/云原生是什么.md)

#### [如何云原生](doc/云原生/如何云原生.md)

### devOps架构

基础 DevOps 技术体
<img src="./doc/image/index/fastboot-devOps.png">

### 整体架构支撑

整体架构支撑是为了整体平台的流程，从管理、开发、测试、运维、生产几条线，
实现整体平台的落地和管理
<img src="./doc/image/index/tech_design.png">

#### [快速开始详细文档](doc/1.fastboot-start.md)

#### [快速开始 k8s部署fastboot](doc/云原生/Kubernetes/k8s部署简单fastboot.md)

#### [快速开始 docker部署fastboot](doc/应用开发手册/docker/docker部署简单应用.md)

#### [快速开始 graalvm本地镜像](doc/应用开发手册/graalvm/graalvm部署打包本地镜像.md)

#### [快速开始 mybatis](doc/db/1.fastboot-mybatis.md)

#### [快速开始 redis](doc/db/2.fastboot-redis.md)

#### [快速开始 mongodb](doc/db/3.fastboot-mongodb.md)

#### [快速开始 beetlsql](doc/db/4.fastboot-beetlsql.md)
#### [快速开始 模板引擎 enjoy](fastboot-example%2Ffastboot-enjoy-example%2Fpom.xml)

应用开发手册：[访问](doc/应用开发手册/应用开发手册.md)

##### 快速开始 [example](fastboot-example/fastboot-web-example)

### 开发规范

#### [1.java编码的基本规范](doc/应用开发手册/开发规范/1.java编码的基本规范.md)

#### [2.mysql规范.md](doc/应用开发手册/开发规范/2.mysql规范.md)

#### [3.redis规范.md](doc/应用开发手册/开发规范/3.redis规范.md)

```maven
<parent>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fastboot-parent</artifactId>
    <version>2.7.8</version>
</parent>
```

```maven
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fastboot-web</artifactId>
</dependency>
```

##### 如果你管理依赖用的gradle[参考gradle配置](doc/1.fastboot-start-gradle.md)

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

#### 发布最新版后，其他各个镜像仓库可能过好几天才同步镜像，可以用maven官方仓库原仓库下载

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

占用20m内存，启动时间仅需01秒（服务器是1核1g的虚拟机）
<img src="./doc/image/index/fastboot-memory.png">
<img src="./doc/image/index/fastboot-start-time.png">

#### 开发遇到问题
[问题解决](doc%2F%CE%CA%CC%E2%BD%E2%BE%F6.md)
