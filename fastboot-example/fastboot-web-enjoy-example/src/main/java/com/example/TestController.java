package com.example;

import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.request.ServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {
//    @RequestMapping("/test")
//    public String test(HttpServletRequest request,
//                HttpServletResponse response, Model model) {
//        User user = new User();
//        user.setName("张三");
//        user.setAge(20);
//        model.addAttribute("user", user);
//        return "test";
//    }
    @AutoWired
    Engine engine;

    @RequestMapping("/")
    public void test1(ServletResponse response) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "My Blog");
        data.put("age", 123);
        User user = new User();
        user.setName("张三");
        user.setAge(20);

        Kv kv = Kv.by("name", "zhangsan");
        String result = engine.getTemplate("view/hello.html").renderToString(kv);
//        return result;
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("<html><body><h1>Hello, World!</h1></body></html>");
    }
}