//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example;

import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.app.App;
import io.github.stylesmile.server.Response;
import io.github.stylesmile.web.HtmlView;

import java.io.IOException;

@Controller
public class FastbootEnjoyApplication {
    @AutoWired
    Engine engine;
    public static void main(String[] args) {
        App.start(FastbootEnjoyApplication.class, args);
    }
    @RequestMapping("/1")
    public HtmlView api(Response response) throws IOException {

        engine.setDevMode(true);
        engine.setToClassPathSourceFactory();
        Kv kv = Kv.by("key", 123);
        Template template = engine.getTemplate("/index.html");
        // 字节流模式输出到 OutputStream
        String str = template.renderToString(kv);
        return new HtmlView(str);
    }
    @RequestMapping("/2")
    public HtmlView api2(Response response) throws IOException {

        engine.setDevMode(true);
        engine.setToClassPathSourceFactory();
        Kv kv = Kv.create();
        kv.put("name", "zhangsan");
        kv.put("age", 18);
        Template template = engine.getTemplate("/hello.html");
        // 字节流模式输出到 OutputStream
        String str = template.renderToString(kv);
        return new HtmlView(str);
    }
    @RequestMapping("/3")
    public HtmlView api4(Response response) throws IOException {

        engine.setDevMode(true);
        engine.setToClassPathSourceFactory();
        Kv kv = Kv.by("key", 123);
        Template template = engine.getTemplate("/test/index.html");
        // 字节流模式输出到 OutputStream
        String str = template.renderToString(kv);
        return new HtmlView(str);
    }
}
