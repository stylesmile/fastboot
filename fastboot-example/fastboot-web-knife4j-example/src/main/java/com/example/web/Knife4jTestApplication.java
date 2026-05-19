package com.example.web;

import io.github.stylesmile.annotation.*;
import io.github.stylesmile.app.App;
import io.github.stylesmile.file.UploadedFile;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.tool.JsonGsonUtil;
import io.github.stylesmile.tool.PropertyUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Knife4j 示例应用
 * 启动时，只需扫描 controller service 和配置，这样可以提高启动速度
 */
@Fastboot(exclude = {User.class})
@Controller
@Tag(name = "示例接口", description = "FastBoot Knife4j 示例接口集合")
public class Knife4jTestApplication {

    @Value("fastboot.name")
    String name;

    public static void main(String[] args) {
        for (int a = 0; a < args.length; a++) {
            System.out.println(args[a]);
        }
        App.start(Knife4jTestApplication.class, args);
    }

    @Operation(summary = "首页接口", description = "返回欢迎信息")
    @RequestMapping("/")
    public String hello() {
        return "hello fastboot";
    }

    @Operation(summary = "获取配置属性", description = "从 application.properties 读取 fast.name 配置")
    @RequestMapping("/1")
    public String hello1() {
        return PropertyUtil.props.getProperty("fast.name");
    }

    @Operation(summary = "获取注入配置值", description = "通过 @Value 注解注入 fastboot.name 配置")
    @RequestMapping("/2")
    public String hello2() {
        return name;
    }

    @Operation(summary = "测试请求参数", description = "接收 URL 参数并返回")
    @RequestMapping("/3")
    public String hello3(@Parameter(description = "用户名称", required = true) @RequestParam("name") String name) {
        return name;
    }

    @Operation(summary = "返回 JSON Map", description = "返回一个包含键值对的 JSON 对象")
    @RequestMapping("/4")
    public Map<String, String> hello4() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "1");
        System.out.println(JsonGsonUtil.objectToJson(map));
        return map;
    }

    @Operation(summary = "返回 User 对象", description = "返回一个 User 对象的 JSON 表示")
    @RequestMapping("/5")
    public User hello5() {
        User user = new User();
        user.setName("lisi");
        user.setAge(18);
        System.out.println(JsonGsonUtil.objectToJson(user));
        return user;
    }

    @Operation(summary = "文件上传", description = "上传文件并保存到指定目录")
    @RequestMapping("/6")
    public String test(UploadedFile file, String username) throws IOException {
        file.save("d://test//" + System.currentTimeMillis() + file.getName());
        return "success~" + username;
    }

    @Operation(summary = "测试表单参数", description = "接收用户名和密码参数")
    @RequestMapping("/7")
    public String test(String password, String username) throws IOException {
        return "username~" + username + "， password: " + password;
    }

    @Operation(summary = "测试 JSON Body", description = "接收 JSON 格式的请求体")
    @RequestMapping("/8")
    public String test8(@RequestBody User user) throws IOException {
        return "username~" + user.getName() + "， password: " + user.getName();
    }
    
    @Operation(summary = "测试指定参数名", description = "使用 @RequestParam 指定参数名")
    @RequestMapping("/9")
    public String test8(@Parameter(description = "用户名", required = true) @RequestParam("username1") String username) throws IOException {
        return "username~" + username ;
    }

}
