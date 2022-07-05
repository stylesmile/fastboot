package com.example.demo.test;

import com.example.demo.annotation.AutoWired;
import com.example.demo.annotation.Service;

@Service
public class SalaryService {
    @AutoWired
    private SalaryHelper salaryHelper;

    /**
     * 计算
     *
     * @param count 数字
     */
    public Integer calSalary(Integer count) {
        return salaryHelper.calSalary(count);
    }
}