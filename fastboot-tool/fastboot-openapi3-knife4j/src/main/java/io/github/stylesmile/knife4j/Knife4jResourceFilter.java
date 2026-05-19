package io.github.stylesmile.knife4j;

import io.github.stylesmile.filter.Filter;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;
import io.github.stylesmile.staticfile.StaticMimes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Knife4j 静态资源过滤器
 * 用于从类路径（包括JAR包）中提供静态资源
 * 
 * @author Stylesmile
 */
public class Knife4jResourceFilter implements Filter {

    private static final String RESOURCE_PREFIX = "META-INF/resources";

    @Override
    public boolean preHandle(Request request, Response response) {
        URI uri = request.getURI();
        String path = uri.getPath();
        
        // 只处理 knife4j 相关的路径
        if (!isKnife4jPath(path)) {
            return true;
        }
        
        // 构建资源路径
        String resourcePath = RESOURCE_PREFIX + path;
        
        // 尝试从类加载器获取资源
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                // 读取资源内容
                byte[] content = readAllBytes(inputStream);
                
                // 根据文件扩展名设置 Content-Type
                String contentType = getContentType(path);
                
                // 设置响应头
                response.setHeader("Content-Type", contentType);
                response.setHeader("Cache-Control", "public, max-age=3600");
                
                // 发送响应头和状态码
                response.sendHeaders(200, content.length, -1,
                    "W/\"" + Integer.toHexString(content.hashCode()) + "\"",
                    contentType, null);
                
                // 写入响应体
                java.io.OutputStream out = response.getBody();
                if (out != null) {
                    out.write(content);
                    out.flush();
                }
                
                System.out.println("Served knife4j resource: " + path + " (" + content.length + " bytes, " + contentType + ")");
                return false; // 请求已处理，不再继续
            }
        } catch (IOException e) {
            System.err.println("Error loading knife4j resource: " + path + ", error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return true; // 继续处理
    }

    @Override
    public boolean afterCompletion(Request request, Response response) {
        return true;
    }
    
    /**
     * 判断是否是 knife4j 相关的路径
     */
    private boolean isKnife4jPath(String path) {
        return path != null && (
            path.equals("/doc.html") ||
            path.startsWith("/webjars/") ||
            path.startsWith("/img/") ||
            path.startsWith("/favicon.ico")
        );
    }
    
    /**
     * 根据文件路径获取 Content-Type
     */
    private String getContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html;charset=utf-8";
        } else if (path.endsWith(".css")) {
            return "text/css;charset=utf-8";
        } else if (path.endsWith(".js")) {
            return "application/javascript;charset=utf-8";
        } else if (path.endsWith(".json")) {
            return "application/json;charset=utf-8";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        } else if (path.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (path.endsWith(".woff")) {
            return "font/woff";
        } else if (path.endsWith(".woff2")) {
            return "font/woff2";
        } else if (path.endsWith(".ttf")) {
            return "font/ttf";
        } else {
            return "application/octet-stream";
        }
    }
    
    /**
     * 读取输入流的所有字节
     */
    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        
        return output.toByteArray();
    }
}
