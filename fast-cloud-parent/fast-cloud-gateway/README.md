# Fast Cloud Gateway 使用教程

FastBoot 框架的 API 网关插件，提供路由、限流、认证等网关功能。

## 功能特性

- 动态路由配置
- 请求过滤与拦截
- 负载均衡
- 限流熔断
- 统一认证授权
- 日志记录与监控

## 快速开始

### 1. 添加依赖

在项目的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fast-cloud-gateway</artifactId>
    <version>0.4.0</version>
</dependency>
```

### 2. 配置网关

在 `application.properties` 中添加网关配置：

```properties
# 是否启用网关（默认启用）
gateway.enabled=true

# 网关端口
gateway.port=8080

# 路由配置（可选）
gateway.routes=/api/** -> http://localhost:8081,/admin/** -> http://localhost:8082
```

### 3. 启动应用

```java
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.app.App;

@Controller
public class Application {
    public static void main(String[] args) {
        App.start(Application.class, args);
    }
}
```

应用启动后，网关插件会自动初始化并监听配置的端口。

### 4. 基本使用

网关会自动处理以下功能：

#### 路由转发

```properties
# 将所有 /api/** 的请求转发到后端服务
gateway.routes=/api/** -> http://backend-service:8081
```

#### 禁用网关

如果不需要网关功能，可以禁用：

```properties
gateway.enabled=false
```

## 配置说明

| 配置项 | 说明 | 默认值 | 是否必填 |
|--------|------|--------|----------|
| gateway.enabled | 是否启用网关 | true | 否 |
| gateway.port | 网关监听端口 | 8080 | 否 |
| gateway.routes | 路由规则配置 | 无 | 否 |

## 高级功能

### 1. 自定义过滤器

```java
import io.github.stylesmile.gateway.filter.GatewayFilter;
import io.github.stylesmile.http.Request;
import io.github.stylesmile.http.Response;

public class AuthFilter implements GatewayFilter {
    
    @Override
    public void filter(Request request, Response response) {
        // 检查认证信息
        String token = request.getHeader("Authorization");
        
        if (token == null || !isValidToken(token)) {
            response.setStatus(401);
            response.setBody("Unauthorized");
            return;
        }
        
        // 继续处理
        nextFilter(request, response);
    }
    
    private boolean isValidToken(String token) {
        // 验证 token 逻辑
        return true;
    }
}
```

### 2. 限流配置

```properties
# 每秒最大请求数
gateway.rate-limit=100

# 限流策略：IP、用户、全局
gateway.rate-limit-strategy=ip
```

### 3. 负载均衡

```properties
# 负载均衡策略：round-robin、random、weighted
gateway.load-balance.strategy=round-robin

# 后端服务列表
gateway.services.user-service=http://service1:8081,http://service2:8082
```

### 4. 健康检查

```properties
# 健康检查路径
gateway.health-check.path=/health

# 健康检查间隔（秒）
gateway.health-check.interval=30
```

## 完整示例

### 示例 1：简单的 API 网关

```java
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.app.App;

@Controller
public class GatewayExample {
    
    @RequestMapping("/status")
    public String getStatus() {
        return "Gateway is running";
    }
    
    public static void main(String[] args) {
        App.start(GatewayExample.class, args);
    }
}
```

配置文件：

```properties
gateway.enabled=true
gateway.port=8080
gateway.routes=/api/** -> http://localhost:8081
```

### 示例 2：带认证的网关

```java
import io.github.stylesmile.gateway.filter.GatewayFilter;

public class JwtAuthFilter implements GatewayFilter {
    
    @Override
    public void filter(Request request, Response response) {
        String path = request.getPath();
        
        // 公开路径不需要认证
        if (path.startsWith("/public/")) {
            nextFilter(request, response);
            return;
        }
        
        // 检查 JWT Token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setBody("{\"error\": \"Missing or invalid token\"}");
            return;
        }
        
        // 验证 Token
        String jwt = token.substring(7);
        if (!validateJwt(jwt)) {
            response.setStatus(403);
            response.setBody("{\"error\": \"Invalid token\"}");
            return;
        }
        
        // 继续处理
        nextFilter(request, response);
    }
    
    private boolean validateJwt(String jwt) {
        // JWT 验证逻辑
        return true;
    }
}
```

## 注意事项

1. **端口冲突**：确保网关端口不被其他服务占用
2. **路由顺序**：路由规则按配置顺序匹配，建议将具体路径放在前面
3. **性能优化**：合理使用缓存和连接池提升性能
4. **异常处理**：建议在过滤器中捕获异常并返回友好的错误信息
5. **日志记录**：开启详细日志便于问题排查

## 常见问题

### Q: 如何配置多个路由规则？

A: 使用逗号分隔多个路由规则：

```properties
gateway.routes=/api/user/** -> http://user-service:8081,/api/order/** -> http://order-service:8082
```

### Q: 如何实现灰度发布？

A: 通过权重配置实现：

```properties
gateway.services.user-service=http://v1:8081[weight=90],http://v2:8082[weight=10]
```

### Q: 如何集成服务发现？

A: 结合 Eureka 或 Nacos 使用：

```properties
# 使用 Eureka 服务发现
gateway.discovery.enabled=true
gateway.discovery.type=eureka

# 或使用 Nacos
gateway.discovery.type=nacos
```

### Q: 如何处理跨域请求？

A: 添加 CORS 过滤器：

```java
public class CorsFilter implements GatewayFilter {
    @Override
    public void filter(Request request, Response response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        nextFilter(request, response);
    }
}
```

## 参考资料

- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [API Gateway Pattern](https://microservices.io/patterns/apigateway.html)

## 许可证

Apache License 2.0
