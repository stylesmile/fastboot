package io.github.stylesmile.knife4j.domain.model;



import java.util.function.Predicate;

/**
 * 接口资源信息
 *
 * */
public class ApiResource {

    private transient Predicate condition;
    private String basePackage;

    public ApiResource() {

    }

    public ApiResource(String basePackage) {
        this.basePackage = basePackage;
    }

    public ApiResource(Predicate condition) {
        this.condition = condition;
    }

    public String basePackage() {
        return basePackage;
    }

    public ApiResource basePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }


}
