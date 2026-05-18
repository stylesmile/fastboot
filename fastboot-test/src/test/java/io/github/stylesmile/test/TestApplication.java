package io.github.stylesmile.test;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.Fastboot;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.annotation.RequestParam;
import io.github.stylesmile.annotation.Service;

/**
 * Test application class for unit testing.
 */
@Fastboot
@Controller
public class TestApplication {

    public static void main(String[] args) {
        io.github.stylesmile.app.App.start(TestApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @RequestMapping("/echo")
    public String echo(@RequestParam("msg") String msg) {
        return "echo: " + msg;
    }

    @RequestMapping("/add")
    public Integer add(@RequestParam("a") Integer a, @RequestParam("b") Integer b) {
        return a + b;
    }
}
