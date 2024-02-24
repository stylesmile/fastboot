package io.github.stylesmile.parameter;

import io.github.stylesmile.tool.StringUtil;
import lombok.Getter;

import java.lang.reflect.Parameter;

/**
 * Parameter参数包装类
 */
@Getter
public class ParameterWrap {
    private final Parameter parameter;
    private String name;

    public ParameterWrap(Parameter parameter) {
        this(parameter, null);
    }

    public ParameterWrap(Parameter parameter, String name) {
        super();
        this.parameter = parameter;
        if (StringUtil.isNotBlank(name)) {
            this.name = name;
        } else {
            this.name = parameter.getName();
        }
    }

}