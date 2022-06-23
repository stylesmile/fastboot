package com.example.demo.test;

import com.example.demo.annotation.AutoWired;
import com.example.demo.annotation.Controller;
import com.example.demo.annotation.RequestMapping;
import com.example.demo.annotation.RequestParam;

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

    /**
     * 查询工资
     *
     * @param name       员工名称
     * @param experience 工龄
     *                   http://localhost:8080/getSalary?experience=5
     */
    @RequestMapping("/getSalary")
    public Integer getSalary(@RequestParam("name") String name, @RequestParam("experience") String experience) {
        System.out.println("salaryService => " + salaryService);
        System.out.println("获取到的参数 => name=" + name + ",experience=" + experience);
        return salaryService.calSalary(Integer.parseInt(experience));
    }

    @RequestMapping("/hello")
    public Integer getSalary2() {
        return 1;
    }
}