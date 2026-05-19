# Fast Cloud Minio 使用教程

FastBoot 框架的 Minio 对象存储插件，提供文件上传、下载和管理功能。

## 功能特性

- 文件上传/下载
- 存储桶管理
- 文件预览
- 权限控制

## 快速开始

### 1. 添加依赖

在项目的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>io.github.stylesmile</groupId>
    <artifactId>fast-cloud-minio</artifactId>
    <version>0.4.0</version>
</dependency>
```

### 2. 配置 Minio

在 `application.properties` 中添加 Minio 配置：

```properties
# Minio 服务器地址（必填）
minio.endpoint=http://localhost:9000

# Access Key（必填）
minio.accessKey=minioadmin

# Secret Key（必填）
minio.secretKey=minioadmin
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

应用启动后，Minio 插件会自动初始化 MinioClient 并注册到 IOC 容器。

### 4. 使用 MinioClient

通过 IOC 容器获取 MinioClient：

```java
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import io.github.stylesmile.ioc.BeanContainer;

import java.util.List;

public class MinioExample {
    
    public List<Bucket> listBuckets() throws Exception {
        // 从 IOC 容器获取 MinioClient
        MinioClient minioClient = BeanContainer.getInstance(MinioClient.class);
        
        // 列出所有存储桶
        return minioClient.listBuckets();
    }
}
```

### 5. 文件上传

```java
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.github.stylesmile.ioc.BeanContainer;
import java.io.FileInputStream;

public void uploadFile() throws Exception {
    MinioClient minioClient = BeanContainer.getInstance(MinioClient.class);
    
    String bucketName = "my-bucket";
    String objectName = "example.txt";
    String filePath = "/path/to/file.txt";
    
    // 确保存储桶存在
    boolean found = minioClient.bucketExists(
        io.minio.BucketExistsArgs.builder().bucket(bucketName).build()
    );
    
    if (!found) {
        minioClient.makeBucket(
            io.minio.MakeBucketArgs.builder().bucket(bucketName).build()
        );
    }
    
    // 上传文件
    minioClient.putObject(
        PutObjectArgs.builder()
            .bucket(bucketName)
            .object(objectName)
            .stream(new FileInputStream(filePath), -1, 10485760) // 10MB part size
            .build()
    );
    
    System.out.println("文件上传成功: " + objectName);
}
```

### 6. 文件下载

```java
import io.minio.GetObjectArgs;
import java.io.InputStream;

public void downloadFile() throws Exception {
    MinioClient minioClient = BeanContainer.getInstance(MinioClient.class);
    
    String bucketName = "my-bucket";
    String objectName = "example.txt";
    
    // 下载文件
    InputStream stream = minioClient.getObject(
        GetObjectArgs.builder()
            .bucket(bucketName)
            .object(objectName)
            .build()
    );
    
    // 处理输入流
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = stream.read(buffer)) != -1) {
        // 处理数据
    }
    
    stream.close();
}
```

### 7. 生成预签名 URL

```java
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;

public String getPresignedUrl() throws Exception {
    MinioClient minioClient = BeanContainer.getInstance(MinioClient.class);
    
    String bucketName = "my-bucket";
    String objectName = "example.txt";
    
    // 生成预签名 URL（有效期 7 天）
    String url = minioClient.getPresignedObjectUrl(
        GetPresignedObjectUrlArgs.builder()
            .method(Method.GET)
            .bucket(bucketName)
            .object(objectName)
            .expiry(7 * 24 * 3600) // 7 days
            .build()
    );
    
    return url;
}
```

## 配置说明

| 配置项 | 说明 | 默认值 | 是否必填 |
|--------|------|--------|----------|
| minio.endpoint | Minio 服务器地址 | 无 | 是 |
| minio.accessKey | Access Key | 无 | 是 |
| minio.secretKey | Secret Key | 无 | 是 |

## 完整示例：文件上传接口

```java
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.annotation.RequestParam;
import io.github.stylesmile.app.App;
import io.github.stylesmile.file.UploadedFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.github.stylesmile.ioc.BeanContainer;

import java.io.ByteArrayInputStream;

@Controller
public class FileUploadController {
    
    @RequestMapping("/upload")
    public String upload(@RequestParam("file") UploadedFile file) {
        try {
            MinioClient minioClient = BeanContainer.getInstance(MinioClient.class);
            
            String bucketName = "uploads";
            String objectName = System.currentTimeMillis() + "_" + file.getName();
            
            // 确保存储桶存在
            boolean found = minioClient.bucketExists(
                io.minio.BucketExistsArgs.builder().bucket(bucketName).build()
            );
            
            if (!found) {
                minioClient.makeBucket(
                    io.minio.MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }
            
            // 上传文件
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(file.getContent()), 
                           file.getContent().length, -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            return "上传成功: " + objectName;
        } catch (Exception e) {
            return "上传失败: " + e.getMessage();
        }
    }
    
    public static void main(String[] args) {
        App.start(FileUploadController.class, args);
    }
}
```

## 注意事项

1. **确保 Minio Server 已启动**：使用前请确保 Minio Server 正在运行
2. **存储桶命名规则**：存储桶名称必须符合 DNS 命名规范（小写字母、数字、连字符）
3. **文件大小限制**：注意 Minio 的单文件大小限制（默认 5TB）
4. **配置缺失处理**：如果配置不完整，插件会跳过初始化并记录警告日志
5. **异常处理**：建议在文件操作时添加完善的异常处理

## 常见问题

### Q: 如何创建私有存储桶？

A: 创建存储桶后设置访问策略：

```java
minioClient.setBucketPolicy(
    SetBucketPolicyArgs.builder()
        .bucket(bucketName)
        .config(policyJson)
        .build()
);
```

### Q: 如何删除文件？

A: 使用 `removeObject` 方法：

```java
minioClient.removeObject(
    RemoveObjectArgs.builder()
        .bucket(bucketName)
        .object(objectName)
        .build()
);
```

### Q: 如何列出存储桶中的所有文件？

A: 使用 `listObjects` 方法：

```java
Iterable<Result<Item>> results = minioClient.listObjects(
    ListObjectsArgs.builder()
        .bucket(bucketName)
        .build()
);
```

## 参考资料

- [Minio 官方文档](https://docs.min.io/)
- [Minio Java SDK](https://docs.min.io/docs/java-client-quickstart-guide.html)

## 许可证

Apache License 2.0
