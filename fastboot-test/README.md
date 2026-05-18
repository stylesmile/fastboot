# fastboot-test

fastboot-test 是一个类似于 Spring Boot Test 的单元测试框架，专为 fastboot 框架设计。它提供了一套完整的测试工具，允许开发者在不启动实际 HTTP 服务器的情况下对 fastboot 应用进行单元测试。

## 特性

- **快速测试**: 无需启动HTTP服务器，测试执行速度快
- **Mock MVC**: 提供完整的Mock MVC框架，模拟HTTP请求和响应
- **依赖注入支持**: 完整支持fastboot的IOC容器和依赖注入
- **JUnit 4集成**: 基于标准JUnit 4，易于上手
- **丰富的断言工具**: 提供便捷的断言方法简化测试验证

## 快速开始

### 1. 添加依赖

在您的项目pom.xml中添加fastboot-test依赖：

```xml
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fastboot-test</artifactId>
    <version>${fastboot.version}</version>
    <scope>test</scope>
</dependency>
```

### 2. 创建测试类

```java
import io.github.stylesmile.test.annotation.FastBootTest;
import io.github.stylesmile.test.mock.MockMvc;
import io.github.stylesmile.test.mock.MockMvcResult;
import io.github.stylesmile.test.runner.FastbootTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.stylesmile.test.util.AssertUtils.*;

@RunWith(FastbootTestRunner.class)
@FastBootTest(YourApplication.class)
public class YourControllerTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = new MockMvc();
    }

    @Test
    public void testHelloEndpoint() {
        MockMvcResult result = mockMvc.get("/hello");
        
        assertStatus(result, 200);
        assertBodyContains(result, "hello world");
    }
}
```

### 3. 运行测试

```bash
mvn test
```

## 使用指南

### 基本HTTP请求测试

#### GET请求

```java
@Test
public void testGetRequest() {
    MockMvcResult result = mockMvc.get("/api/users");
    
    assertStatus(result, 200);
    assertBodyContains(result, "users");
}
```

#### 带参数的GET请求

```java
@Test
public void testGetWithParams() {
    MockMvcResult result = mockMvc.perform(
        MockRequest.get("/api/users")
            .param("id", "123")
            .param("name", "john")
    );
    
    assertStatus(result, 200);
}
```

#### POST请求

```java
@Test
public void testPostRequest() {
    MockMvcResult result = mockMvc.post("/api/users");
    
    assertStatus(result, 201);
}
```

### 依赖注入测试

```java
@RunWith(FastbootTestRunner.class)
@FastBootTest(Application.class)
public class ServiceInjectionTest {

    @Autowired
    private UserService userService;

    @Test
    public void testServiceMethod() {
        String result = userService.getUserName(123);
        assertEquals("John", result);
    }
}
```

### 控制器与Service集成测试

```java
@RunWith(FastbootTestRunner.class)
@FastBootTest(Application.class)
public class ControllerIntegrationTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = new MockMvc();
    }

    @Test
    public void testControllerWithService() {
        MockMvcResult result = mockMvc.perform(
            MockRequest.get("/greet")
                .param("name", "World")
        );
        
        assertStatus(result, 200);
        assertBodyContains(result, "Hello, World!");
    }
}
```

## API参考

### @FastBootTest注解

```java
@FastBootTest(
    value = Application.class,      // 必需：应用程序类
    webEnvironment = false,          // 可选：是否启动Web环境（默认false）
    port = 0                         // 可选：端口号（默认0，随机端口）
)
```

### MockMvc类

#### 主要方法

- `perform(MockRequest request)`: 执行模拟请求
- `get(String uri)`: 快捷GET请求
- `post(String uri)`: 快捷POST请求
- `put(String uri)`: 快捷PUT请求
- `delete(String uri)`: 快捷DELETE请求

### MockRequest类

#### 构建请求

```java
MockRequest.get("/api/users")
    .param("id", "123")
    .header("Authorization", "Bearer token")
    .contentType("application/json")
    .body("{\"name\":\"John\"}");
```

#### 可用方法

- `param(String name, String value)`: 添加查询参数
- `header(String key, String value)`: 添加请求头
- `body(String body)`: 设置请求体
- `body(byte[] body)`: 设置请求体（字节数组）
- `contentType(String contentType)`: 设置Content-Type
- `accept(String accept)`: 设置Accept头

### MockMvcResult类

#### 获取结果

```java
MockMvcResult result = mockMvc.get("/api/users");

int status = result.getStatus();           // 获取状态码
String body = result.getBody();            // 获取响应体
MockResponse response = result.getResponse(); // 获取完整响应
Throwable error = result.getError();       // 获取异常（如果有）
boolean hasError = result.hasError();      // 是否有异常
```

### AssertUtils工具类

#### 常用断言

```java
// 状态码断言
assertStatus(result, 200);
assertOk(result);
assertNotFound(result);

// 响应体断言
assertBodyContains(result, "expected text");
assertBodyEquals(result, "exact match");

// 其他断言
assertJsonResponse(result);
assertHtmlResponse(result);
assertNoError(result);
assertNotNull(object);
```

## 高级用法

### 自定义测试配置

创建测试专用的配置文件 `src/test/resources/application.properties`:

```properties
# 测试环境配置
db.url=jdbc:h2:mem:testdb
db.username=sa
db.password=
```

### 测试生命周期管理

```java
@RunWith(FastbootTestRunner.class)
@FastBootTest(Application.class)
public class LifecycleTest {

    @BeforeClass
    public static void beforeAll() {
        // 所有测试执行前的初始化
    }

    @Before
    public void setUp() {
        // 每个测试执行前的初始化
        mockMvc = new MockMvc();
    }

    @After
    public void tearDown() {
        // 每个测试执行后的清理
    }

    @AfterClass
    public static void afterAll() {
        // 所有测试执行后的清理
    }
}
```

### 异常处理测试

```java
@Test
public void testExceptionHandling() {
    MockMvcResult result = mockMvc.get("/api/error");
    
    // 验证返回500错误
    assertStatus(result, 500);
    
    // 或者验证有异常
    assertTrue(result.hasError());
}
```

## 最佳实践

### 1. 测试命名规范

```java
// 格式: test<MethodName>_<Scenario>_<ExpectedResult>
@Test
public void testGetUser_WithValidId_ReturnsUser() { }

@Test
public void testGetUser_WithInvalidId_ReturnsNotFound() { }
```

### 2. AAA模式 (Arrange-Act-Assert)

```java
@Test
public void testAddNumbers() {
    // Arrange - 准备
    MockRequest request = MockRequest.get("/add")
        .param("a", "10")
        .param("b", "20");
    
    // Act - 执行
    MockMvcResult result = mockMvc.perform(request);
    
    // Assert - 验证
    assertStatus(result, 200);
    assertBodyContains(result, "30");
}
```

### 3. 保持测试独立

每个测试应该是独立的，不依赖其他测试的执行结果。

### 4. 使用@Before避免重复代码

```java
@Before
public void setUp() {
    mockMvc = new MockMvc();
    // 其他通用初始化
}
```

## 常见问题

### Q: 为什么我的测试找不到Handler？

A: 确保：
1. 使用了`@RunWith(FastbootTestRunner.class)`
2. 使用了`@FastBootTest(YourApplication.class)`
3. 你的Controller类有正确的注解（`@Controller`, `@RequestMapping`等）

### Q: 如何测试需要认证的路由？

A: 使用MockRequest添加认证头：

```java
MockMvcResult result = mockMvc.perform(
    MockRequest.get("/api/protected")
        .header("Authorization", "Bearer your-token")
);
```

### Q: 测试速度慢怎么办？

A: 
1. 确保`webEnvironment = false`（默认值）
2. 避免在测试中启动外部服务
3. 使用Mock对象替代真实的服务调用

## 示例项目

查看 `fastboot-example` 目录下的完整示例：

- `BasicControllerTest.java`: 基础控制器测试
- `DependencyInjectionTest.java`: 依赖注入测试
- `IntegrationTest.java`: 集成测试

## 贡献

欢迎提交Issue和Pull Request来帮助改进fastboot-test！

## 许可证

本项目采用 Apache License 2.0 许可证。
