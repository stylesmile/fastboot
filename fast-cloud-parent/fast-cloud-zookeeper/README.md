# Fast Cloud Zookeeper 使用教程

FastBoot 框架的 Zookeeper 分布式协调插件，提供服务发现、配置管理和分布式锁等功能。

## 功能特性

- 服务注册与发现
- 分布式配置管理
- 分布式锁
- 领导者选举

## 快速开始

### 1. 添加依赖

在项目的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fast-cloud-zookeeper</artifactId>
    <version>0.4.0</version>
</dependency>
```

### 2. 配置 Zookeeper

在 `application.properties` 中添加 Zookeeper 配置：

```properties
# Zookeeper 服务器地址
zookeeper.server=127.0.0.1:2181

# 会话超时时间（毫秒）
zookeeper.sessionTimeout=5000

# 连接超时时间（毫秒）
zookeeper.connectionTimeout=3000
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

### 4. 使用 Zookeeper

参考 `Demo.java` 中的示例代码：

```java
import io.github.stylesmile.zookeeper.BaseZookeeper;

public class ZookeeperExample extends BaseZookeeper {
    
    public void createNode() throws Exception {
        // 创建持久节点
        String path = "/myapp/config";
        String data = "config_value";
        
        createPersistent(path, data);
    }
    
    public void createEphemeralNode() throws Exception {
        // 创建临时节点
        String path = "/myapp/service";
        String data = "service_info";
        
        createEphemeral(path, data);
    }
    
    public String getData() throws Exception {
        // 获取节点数据
        String path = "/myapp/config";
        return getData(path);
    }
    
    public void setData() throws Exception {
        // 更新节点数据
        String path = "/myapp/config";
        String data = "new_config_value";
        
        setData(path, data);
    }
    
    public void deleteNode() throws Exception {
        // 删除节点
        String path = "/myapp/config";
        delete(path);
    }
}
```

## 核心 API

### 创建节点

```java
// 创建持久节点
createPersistent(String path, String data)

// 创建临时节点
createEphemeral(String path, String data)

// 创建持久顺序节点
createPersistentSequential(String path, String data)

// 创建临时顺序节点
createEphemeralSequential(String path, String data)
```

### 读取数据

```java
// 获取节点数据
getData(String path)

// 检查节点是否存在
exists(String path)

// 获取子节点列表
getChildren(String path)
```

### 更新数据

```java
// 更新节点数据
setData(String path, String data)
```

### 删除节点

```java
// 删除节点
delete(String path)

// 递归删除节点及其子节点
deleteRecursive(String path)
```

### 监听器

```java
// 添加数据变化监听
addDataListener(String path, DataListener listener)

// 添加子节点变化监听
addChildListener(String path, ChildListener listener)
```

## 应用场景

### 1. 服务注册与发现

```java
public class ServiceRegistry {
    
    public void registerService(String serviceName, String serviceAddress) throws Exception {
        String path = "/services/" + serviceName + "/" + serviceAddress;
        createEphemeral(path, serviceAddress);
    }
    
    public List<String> discoverServices(String serviceName) throws Exception {
        String path = "/services/" + serviceName;
        return getChildren(path);
    }
}
```

### 2. 分布式锁

```java
public class DistributedLock {
    
    private String lockPath = "/locks/mylock";
    
    public boolean tryLock() throws Exception {
        try {
            createEphemeral(lockPath, "locked");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void unlock() throws Exception {
        delete(lockPath);
    }
}
```

### 3. 配置管理

```java
public class ConfigManager {
    
    public void setConfig(String key, String value) throws Exception {
        String path = "/config/" + key;
        if (exists(path)) {
            setData(path, value);
        } else {
            createPersistent(path, value);
        }
    }
    
    public String getConfig(String key) throws Exception {
        String path = "/config/" + key;
        return getData(path);
    }
}
```

## 注意事项

1. **确保 Zookeeper Server 已启动**：使用前请确保 Zookeeper Server 正在运行
2. **会话管理**：注意会话超时时间，避免频繁重连
3. **节点路径规范**：节点路径必须以 `/` 开头
4. **数据大小限制**：单个节点的数据大小不能超过 1MB
5. **临时节点特性**：客户端断开连接后，临时节点会自动删除

## 常见问题

### Q: 如何处理连接断开？

A: BaseZookeeper 内部已实现重连机制，无需手动处理。

### Q: 如何监控 Zookeeper 连接状态？

A: 可以添加 ConnectionStateListener：

```java
addConnectionStateListener(new ConnectionStateListener() {
    @Override
    public void stateChanged(ZooKeeper zooKeeper, KeeperState state) {
        System.out.println("Connection state: " + state);
    }
});
```

### Q: 如何实现分布式锁？

A: 使用临时顺序节点实现公平锁，或使用临时节点实现非公平锁。

## 参考资料

- [Zookeeper 官方文档](https://zookeeper.apache.org/doc/current/index.html)
- [Zookeeper Java API](https://zookeeper.apache.org/doc/current/api/index.html)

## 许可证

Apache License 2.0
