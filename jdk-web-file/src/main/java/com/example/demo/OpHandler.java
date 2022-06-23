package com.example.demo;

import java.io.IOException;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

// 支持运维/工程管理App相关的接口
public class OpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
//        FileUploadUtil.fileUpload(he);
//        FileUploadUtil.readRequestBody(he);
        FileUploadUtil.fileUpload(he);
    }
}