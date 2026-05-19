# Fast Cloud Nacos 使用教程

FastBoot 框架的 Nacos 配置管理插件，提供动态配置管理服务。

## 功能特性

- 动态配置管理
- 配置实时更新
- 多环境配置支持
- 配置版本管理

## 快速开始

### 1. 添加依赖

在项目的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fast-cloud-nacos</artifactId>
    <version>0.5.1</version>
</dependency>
```

### 2. 配置 Nacos

在 `application.properties` 中添加 Nacos 配置：

```properties
# Nacos 服务器地址（必填）
nacos.serverAddr=127.0.0.1:8848

# 用户名（可选）
nacos.username=nacos

# 密码（可选）
nacos.password=nacos
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

应用启动后，Nacos 插件会自动连接 Nacos Server 并初始化 ConfigService。

### 4. 使用 ConfigService

通过 IOC 容器获取 ConfigService：

```java
import com.alibaba.nacos.api.config.ConfigService;
import io.github.stylesmile.ioc.BeanContainer;

public class ConfigExample {
    
    public String getConfig() throws Exception {
        // 从 IOC 容器获取 ConfigService
        ConfigService configService = BeanContainer.getInstance(ConfigService.class);
        
        // 获取配置
        String dataId = "example.properties";
        String group = "DEFAULT_GROUP";
        long timeoutMs = 5000;
        
        String content = configService.getConfig(dataId, group, timeoutMs);
        return content;
    }
    
    public void publishConfig() throws Exception {
        ConfigService configService = BeanContainer.getInstance(ConfigService.class);
        
        // 发布配置
        String dataId = "example.properties";
        String group = "DEFAULT_GROUP";
        String content = "key=value";
        
        boolean result = configService.publishConfig(dataId, group, content);
        System.out.println("Publish result: " + result);
    }
}
```

### 5. 监听配置变化

```java
import com.alibaba.nacos.api.config.listener.Listener;

public void listenConfig() throws Exception {
    ConfigService configService = BeanContainer.getInstance(ConfigService.class);
    
    String dataId = "example.properties";
    String group = "DEFAULT_GROUP";
    
    configService.addListener(dataId, group, new Listener() {
        @Override
        public Executor getExecutor() {
            return null;
        }
        
        @Override
        public void receiveConfigInfo(String configInfo) {
            System.out.println("配置更新: " + configInfo);
            // 处理配置变化
        }
    });
}
```

## 配置说明

| 配置项 | 说明 | 默认值 | 是否必填 |
|--------|------|--------|----------|
| nacos.serverAddr | Nacos 服务器地址 | 无 | 是 |
| nacos.username | 用户名 | 无 | 否 |
| nacos.password | 密码 | 无 | 否 |

## 完整示例

```java
import com.alibaba.nacos.api.config.ConfigService;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.app.App;
import io.github.stylesmile.ioc.BeanContainer;

@Controller
public class NacosExample {
    
    @RequestMapping("/config")
    public String getConfig() {
        try {
            ConfigService configService = BeanContainer.getInstance(ConfigService.class);
            String content = configService.getConfig("example.properties", "DEFAULT_GROUP", 5000);
            return content;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    public static void main(String[] args) {
        App.start(NacosExample.class, args);
    }
}
```

## 注意事项

1. **确保 Nacos Server 已启动**：使用前请确保 Nacos Server 正在运行
2. **网络连通性**：确保应用可以访问 Nacos Server
3. **配置缺失处理**：如果未配置 `nacos.serverAddr`，插件会跳过初始化并记录警告日志
4. **异常处理**：建议在获取配置时添加异常处理

## 常见问题

### Q: 如何切换不同的 Nacos 环境？

A: 通过修改 `nacos.serverAddr` 配置指向不同的 Nacos Server 地址。

### Q: 配置更新后如何实时生效？

A: 使用 `addListener` 方法监听配置变化，在回调中处理配置更新逻辑。

### Q: 如何获取命名空间下的配置？

A: 在 Properties 中添加 `namespace` 配置：

```java
properties.put(PropertyKeyConst.NAMESPACE, "your-namespace-id");
```

## 参考资料

- [Nacos 官方文档](https://nacos.io/zh-cn/docs/what-is-nacos.html)
- [Nacos Java SDK](https://nacos.io/zh-cn/docs/sdk.html)

## 许可证

Apache License 2.0
