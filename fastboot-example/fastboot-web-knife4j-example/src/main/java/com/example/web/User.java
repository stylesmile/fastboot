package com.example.web;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Schema(description = "用户信息")
public class User implements Serializable {
    
    @Schema(description = "用户姓名", example = "张三")
    private String name;
    
    @Schema(description = "用户年龄", example = "25")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
