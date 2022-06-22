package com.example.demo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

public final class RequestParamHandler {

    //根据请求返回参数Param
    public static Param createParam(HttpServletRequest request) throws IOException {
        HashMap<String, Object> paramMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        if(!parameterNames.hasMoreElements()){
            return null;
        }

        while (parameterNames.hasMoreElements()){
            String fieldName = parameterNames.nextElement();
            String fieldValue = request.getParameter(fieldName);
            paramMap.put(fieldName, fieldValue);
        }

        return new Param(paramMap);

    }


}
