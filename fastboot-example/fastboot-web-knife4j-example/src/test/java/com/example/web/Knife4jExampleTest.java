package com.example.web;

import io.github.stylesmile.test.annotation.FastBootTest;
import io.github.stylesmile.test.mock.MockMvc;
import io.github.stylesmile.test.mock.MockMvcResult;
import io.github.stylesmile.test.runner.FastbootTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Knife4j 示例应用集成测试
 */
@RunWith(FastbootTestRunner.class)
@FastBootTest(value = Knife4jTestApplication.class, webEnvironment = false)
public class Knife4jExampleTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = new MockMvc();
    }

    /**
     * 测试首页接口
     */
    @Test
    public void testHelloEndpoint() throws Exception {
        MockMvcResult result = mockMvc.get("/");
        
        assertNotNull("Response should not be null", result);
        assertEquals("Status code should be 200", 200, result.getStatus());
        assertTrue("Response body should contain 'hello fastboot'", 
                   result.getBody().contains("hello fastboot"));
    }

    /**
     * 测试参数传递接口
     */
    @Test
    public void testRequestParamEndpoint() throws Exception {
        // 使用带 query 参数的 GET 请求
        MockMvcResult result = mockMvc.get("/3", "name", "zhangsan");
        
        assertNotNull("Response should not be null", result);
        assertEquals("Status code should be 200", 200, result.getStatus());
        assertTrue("Response body should contain 'zhangsan'", 
                   result.getBody().contains("zhangsan"));
    }

    /**
     * 测试 JSON 响应接口
     */
    @Test
    public void testJsonResponseEndpoint() throws Exception {
        MockMvcResult result = mockMvc.get("/4");
        
        assertNotNull("Response should not be null", result);
        assertEquals("Status code should be 200", 200, result.getStatus());
        assertTrue("Response should be JSON format", 
                   result.getBody().contains("\"1\":\"1\""));
    }

    /**
     * 测试 User 对象返回接口
     */
    @Test
    public void testUserObjectEndpoint() throws Exception {
        MockMvcResult result = mockMvc.get("/5");
        
        assertNotNull("Response should not be null", result);
        assertEquals("Status code should be 200", 200, result.getStatus());
        assertTrue("Response should contain user name", 
                   result.getBody().contains("lisi"));
        assertTrue("Response should contain user age", 
                   result.getBody().contains("18"));
    }

    /**
     * 测试配置值注入
     */
    @Test
    public void testConfigValueInjection() throws Exception {
        MockMvcResult result = mockMvc.get("/2");
        
        assertNotNull("Response should not be null", result);
        assertEquals("Status code should be 200", 200, result.getStatus());
        // 验证配置值已被注入（具体值取决于 application.properties）
        assertNotNull("Config value should be injected", result.getBody());
    }
}
