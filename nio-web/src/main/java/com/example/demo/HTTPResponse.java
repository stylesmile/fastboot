package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.*;
 
public class HTTPResponse {
    private String line;
//    private String headers;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;
    private String end;
    private Map<String, String> cookie = new HashMap<>();
 
    int status;
 
    HTTPResponse(){
        this.line = "HTTP/1.1 200 OK\r\n";
        headers.put("Server", "Nginx");
        headers.put("Content-Type", "text/html; charset=UTF-8");
        headers.put("Connection", "keep-alive");
        headers.put("Date", getDate());
 
        this.end = "\r\n\r\n";
    }
 
    /**
     * 获取符合http协议的Date字段的时间格式，时区是GMT
     * @return
     */
    private String getDate(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM yyyy HH:MM:ss z", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String now = simpleDateFormat.format(date);
        return now;
    }
 
    public Map<String, String> getCookie() {
        return cookie;
    }
 
    public void setCookie(Map<String, String> cookie) {
        this.cookie = cookie;
    }
 
    public HTTPResponse setContentType(String contentType){
        headers.put("Content-Type", contentType);
        return this;
    }
 
 
    public HTTPResponse setStatus(int status){
        if(status == 200){
            this.status = 200;
            this.line = "HTTP/1.1 200 OK\r\n";
        }else if(status == 302){
            this.status = 302;
            this.line = "HTTP/1.1 302 Found\r\n";
        }
        else{
            this.status = 404;
            this.line = "HTTP/1.1 404 Not Found\r\n";
            String html = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset=\"utf-8\">" +
                    "<title></title>" +
                    "</head>" +
                    "<body>" +
                    "<h1> 404！该页面不存在！</h1>" +
                    "</body>" +
                    "</html>";
            this.body = html.getBytes();
        }
        return this;
    }
 
    public int getStatus() {
        return status;
    }
 
    public void addHeaders(String key, String value) {
        this.headers.put(key, value);
    }
 
    public HTTPResponse setBody(byte[] body) {
        this.body = body;
        return this;
    }
 
    private String getHeadersStr(){
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) ->{
            sb.append(key).append(": ").append(value).append("\r\n");
        });
        if(status == 200) {
            cookie.forEach((key, value) -> {
                sb.append("Set-Cookie: ");
                sb.append(key).append("=").append(value).append(";").append("\r\n");
            });
        }
        sb.append("\r\n");
        return sb.toString();
    }
 
    public byte[] toBytes(){
 
        byte[] lineBytes = line.getBytes();
        byte[] headersBytes = getHeadersStr().getBytes();
        byte[] endBytes = end.getBytes();
 
        // 将几个字节数组相加
        byte[] result = new byte[lineBytes.length + headersBytes.length + body.length + endBytes.length];
        System.arraycopy(lineBytes, 0, result, 0, lineBytes.length);
        System.arraycopy(headersBytes, 0, result, lineBytes.length, headersBytes.length);
        System.arraycopy(body, 0, result, lineBytes.length + headersBytes.length, body.length);
        int length = lineBytes.length + headersBytes.length + body.length;
        System.arraycopy(endBytes, 0, result, length, endBytes.length);
        return result;
    }
}