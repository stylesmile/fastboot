package com.example.demo.test;

import com.example.demo.annotation.Bean;

/**
 * 工资计算类
 */
@Bean
public class SalaryHelper {
    /**
     * 计算
     *
     * @param count 工龄
     */
    public Integer calSalary(Integer count) {
        return count * 5000;
    }
}