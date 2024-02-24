package io.github.stylesmile.parameter;

import io.github.stylesmile.tool.StringUtil;
import lombok.Getter;

import java.lang.reflect.*;
import java.util.Map;

/**
 * Parameter参数包装类
 */
@Getter
public class ParameterWrap {
    private final Parameter parameter;
    private Class<?> type;
    private Type genericType;
    private String name;

    public ParameterWrap(Parameter parameter) {
        this(parameter, null, null, null);
    }

    public ParameterWrap(Parameter parameter, Method method, Map<String, Type> genericInfo, String name) {
        super();
        this.parameter = parameter;
        this.type = parameter.getType();
        this.genericType = parameter.getParameterizedType();
        if (StringUtil.isNotBlank(name)) {
            this.name = name;
        } else {
            this.name = parameter.getName();
        }

        if (method != null) {

            if (genericInfo != null && genericType instanceof TypeVariable) {
                Type type0 = genericInfo.get(genericType.getTypeName());

                if (type0 instanceof ParameterizedType) {
                    genericType = type0;
                    type0 = ((ParameterizedType) type0).getRawType();
                }

                if (type0 instanceof Class) {
                    type = (Class<?>) type0;
                } else {
                    throw new IllegalStateException("Mapping mehtod generic analysis error: "
                            + method.getDeclaringClass().getName()
                            + "."
                            + method.getName());
                }
            }
        }
    }

}