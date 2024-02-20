package io.github.stylesmile.websocket;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.app.App;

@Controller
public class StaticFileApp {
    public static void main(String[] args) {
        App.start(StaticFileApp.class, args);
    }

    @RequestMapping("/1")
    public String hello() {
        return "hello fastboot";
    }
}
