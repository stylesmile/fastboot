package com.example.demo.app;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class MyFilter extends Filter {
    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        String url = httpExchange.getRequestURI().toString();
        System.out.println(url);
    }

    @Override
    public String description() {
        return null;
    }
}
