//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.web;

import io.github.stylesmile.annotation.*;
import io.github.stylesmile.app.App;
import io.github.stylesmile.file.UploadedFile;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.tool.JsonGsonUtil;
import io.github.stylesmile.tool.PropertyUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 启动时，只需扫描 controller service 和配置，这样可以提高启动速度
 */
@Fastboot(exclude = {User.class})
@Controller
public class Application {

    @Value("fast.name")
    String name;

    public static void main(String[] args) {
        App.start(Application.class, args);
    }

    @RequestMapping("/")
    public String hello() {
        return "hello fastboot";
    }

    @RequestMapping("/1")
    public String hello1() {
        return PropertyUtil.props.getProperty("fast.name");
    }

    @RequestMapping("/2")
    public String hello2() {
        return name;
    }

    @RequestMapping("/3")
    public String hello3(@RequestParam("name") String name) {
        return name;
    }

    @RequestMapping("/4")
    public Map<String, String> hello4() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "1");
        System.out.println(JsonGsonUtil.objectToJson(map));
        return map;
    }

    @RequestMapping("/5")
    public User hello5() {
        User user = new User();
        user.setName("lisi");
        user.setAge(18);
        System.out.println(JsonGsonUtil.objectToJson(user));
        return user;
    }

    @RequestMapping("/6")
    public String test(UploadedFile file, String username) throws IOException {
        file.save("d://test//" + System.currentTimeMillis() + file.getName());
        return "success~" + username;
    }

    @RequestMapping("/7")
    public String test(String password, String username) throws IOException {
        return "username~" + username + "， password: " + password;
    }

    @RequestMapping("/8")
    public String test8(@RequestBody User user) throws IOException {
        return "username~" + user.getName() + "， password: " + user.getName();
    }
    @RequestMapping("/9")
    public String test8(@RequestParam("username1") String username) throws IOException {
        return "username~" + username ;
    }

}
