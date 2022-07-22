//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.app.App;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.tool.PropertyUtil;

@Controller
public class Application {
    public Application() {
    }

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
}
