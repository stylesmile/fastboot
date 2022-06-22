package com.example.demo;

import java.util.Map;

public class Param {
    //请求参数Map
    private Map<String, Object> paramMap;

    public Param(){

    }

    public Param(Map<String, Object> paramMap){
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public boolean isEmpty(){
        return paramMap.isEmpty();
    }

}