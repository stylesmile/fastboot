package io.github.stylesmile.mybatis;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Service;

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