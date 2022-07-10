package io.github.stylesmile;

import io.github.stylesmile.annotation.Bean;

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