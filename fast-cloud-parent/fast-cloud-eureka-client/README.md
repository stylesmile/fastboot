# Fast Cloud Eureka Client

FastBoot 框架的 Eureka 客户端插件，用于服务注册与发现。

## 功能特性

- 自动注册到 Eureka Server
- 服务发现
- 健康检查
- 优雅停机时自动注销服务

## 使用方法

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fast-cloud-eureka-client</artifactId>
    <version>0.5.1</version>
</dependency>
```

### 2. 配置 Eureka Client

在 `application.properties` 中添加：

```properties
# Eureka Server 地址（必填）
eureka.client.service-url.default-zone=http://localhost:8761/eureka/

# 应用名称
eureka.instance.appname=my-application

# 实例 ID（可选，默认为应用名称）
eureka.instance.instance-id=my-application-1

# 主机名（可选，默认为 localhost）
eureka.instance.hostname=localhost

# 服务端口（可选，默认为 8080）
server.port=8080
```

### 3. 使用服务发现

```java
@Fastboot
@Controller
public class MyApplication {
    
    @AutoWired
    private DiscoveryClient discoveryClient;
    
    @RequestMapping("/services")
    public List<String> getServices() {
        // 获取所有服务
        return discoveryClient.getServices();
    }
    
    public static void main(String[] args) {
        App.start(MyApplication.class, args);
    }
}
```

## 配置说明

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| eureka.client.service-url.default-zone | Eureka Server 地址 | 必填 |
| eureka.instance.appname | 应用名称 | fastboot-application |
| eureka.instance.instance-id | 实例 ID | 应用名称 |
| eureka.instance.hostname | 主机名 | localhost |
| server.port | 服务端口 | 8080 |

## 注意事项

1. 确保 Eureka Server 已经启动并运行
2. 如果未配置 `eureka.client.service-url.default-zone`，插件会跳过注册
3. 应用关闭时会自动从 Eureka Server 注销

## 许可证

Apache License 2.0
