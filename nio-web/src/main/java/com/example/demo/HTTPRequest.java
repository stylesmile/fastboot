package com.example.demo;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
 
public class HTTPRequest {
 
    public static final int POST = 0;
    public static final int GET = 1;
 
    private String url;
    private int method;
    private String host;
    private String location;
    private byte[] body;
    private Map<String, String> cookie = new HashMap<>();
 
    public String getUrl() {
        return url;
    }
 
    public void setUrl(String url) {
        this.url = url;
    }
 
    public int getMethod() {
        return method;
    }
 
    public void setMethod(int method) {
        this.method = method;
    }
 
    public String getLocation() {
        return location;
    }
 
    public void setLocation(String location) {
        this.location = location;
    }
 
    public String getHost() {
        return host;
    }
 
    public void setHost(String host) {
        this.host = host;
    }
 
    public Map<String, String> getCookie() {
        return cookie;
    }
 
    public void setCookie(Map<String, String> cookie) {
        this.cookie = cookie;
    }
 
    public byte[] getBody() {
        return body;
    }
 
    public void setBody(byte[] body) {
        this.body = body;
    }
 
    @Override
    public String toString() {
        return "HTTPRequest{" +
                "url='" + url + '\'' +
                ", method=" + method +
                ", host='" + host + '\'' +
                ", cookie=" + cookie +
                '}';
    }
}