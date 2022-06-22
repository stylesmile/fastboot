package com.example.demo;

import java.lang.reflect.Method;

//封装controller对象和Method方法；
public class Handler {
    //Controller类
    private Class<?> controllerClass;
    //Controller方法
    private Method controllerMethod;

    public Handler(Class<?> controllerClass, Method controllerMethod){
        this.controllerClass = controllerClass;
        this.controllerMethod = controllerMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }

}