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
@Fastboot()
@Controller
public class  MqttServerApplication {


    @Value("fastboot.name")
    String name;

    public static void main(String[] args) {
        App.start(MqttServerApplication.class, args);
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



}
