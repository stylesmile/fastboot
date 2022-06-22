package com.example.demo.test;

import com.example.demo.annotation.AutoWired;
import com.example.demo.annotation.Service;

@Service
public class SalaryService {
    @AutoWired
    private SalaryHelper salaryHelper;

    /**
     * 计算工资
     *
     * @param experience 工龄
     */
    public Integer calSalary(Integer experience) {
        return salaryHelper.calSalary(experience);
    }
}