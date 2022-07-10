package com.example;


import io.github.stylesmile.annotation.Service;

import javax.annotation.Resource;

@Service
public class SalaryService {
    @Resource
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