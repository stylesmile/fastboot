package com.example.demo.test;

import com.example.demo.annotation.AutoWired;
import com.example.demo.annotation.Controller;
import com.example.demo.annotation.RequestMapping;
import com.example.demo.annotation.RequestParam;
import com.example.demo.tool.FileUploadUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * 工资的控制器
 */
@Controller
public class SalaryController {
    /**
     * 依赖注入
     */
    @AutoWired
    private SalaryService salaryService;

//    /**
//     * 计算
//     *
//     * @param count 数量
//     */
//    @RequestMapping("/test")
//    public Integer getSalary(@RequestParam("count") String count) {
//        System.out.println("salaryService => " + salaryService);
//        System.out.println("获取到的参数 => ,count=" + count);
//        return salaryService.calSalary(Integer.parseInt(count));
//    }
//
//    /**
//     * hello
//     * http://localhost:8080/hello
//     */
//    @RequestMapping("/hello")
//    public Integer hello(@RequestParam("name") String name, @RequestParam("experience") String experience) {
//        return 1;
//    }
//

    /**
     * hello
     * http://localhost:8080/hello2?name=1&pwd=2
     * http://localhost:80/hello2?name=1&pwd=2
     */
//    @RequestMapping("/hello2")
//    public Integer hello2(@RequestParam("name") Integer name, @RequestParam("pwd") String pwd) {
//        return name * 100;
//    }

    @RequestMapping("/file")
    public Integer file(HttpExchange httpExchange, @RequestParam("name") Integer name, @RequestParam("pwd") String pwd) {
        FileUploadUtil.fileUpload(httpExchange,"d://file");
        return name * 100;
    }


}