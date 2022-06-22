package com.example.demo;

import java.util.Objects;

//请求类的方法和路径对应@RequestMapping注解里的方法和路径
public class Request {
    //请求方法
    private String requestMethod;
    //请求路径
    private String requestPath;

    public Request(String requestMethod, String requestPath){
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }


    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }


    //方法名和路径都一样就是同一对象
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj instanceof Request){
            Request e = (Request) obj;
            if(e.requestMethod.equalsIgnoreCase(this.requestMethod) && Objects.equals(e.requestPath, this.requestPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return requestPath.hashCode() + requestMethod.hashCode() + 1;
    }
}