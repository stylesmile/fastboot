# Fast Cloud Eureka Server

FastBoot 框架的 Eureka 服务注册中心插件。

## 功能特性

- 提供 Eureka 服务注册中心
- 支持多实例集群
- 服务健康检查
- Web 管理界面

## 使用方法

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fast-cloud-eureka-server</artifactId>
    <version>0.5.1</version>
</dependency>
```

### 2. 配置 Eureka Server

在 `application.properties` 中添加：

```properties
# Eureka Server 端口
server.port=8761

# 禁用自我注册（生产环境建议）
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

# 服务 URL
eureka.client.service-url.default-zone=http://localhost:8761/eureka/
```

### 3. 启动应用

```java
@Fastboot
public class EurekaServerApplication {
    public static void main(String[] args) {
        App.start(EurekaServerApplication.class, args);
    }
}
```

访问 `http://localhost:8761` 查看 Eureka 管理界面。

## 许可证

Apache License 2.0
