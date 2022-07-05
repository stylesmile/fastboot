package com.example.demo.context;

import com.example.demo.file.UploadedFile;
import com.example.demo.tool.ContextUtil;
import com.example.demo.tool.IpUtil;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 通用上下文接口（实现：Context + Handler 架构）
 *
 * @author noear
 * @since 1.0
 */
public abstract class Context {
    /**
     * 获取当前线程的上下文
     */
    public static Context current() {
        return ContextUtil.current();
    }

    private Locale locale;

    /**
     * 获取地区
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * 设置地区
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * 是否已处理（用于控制处理链）
     */
    private boolean handled;

    /**
     * 设置处理状态
     */
    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    /**
     * 获取处理状态
     */
    public boolean getHandled() {
        return handled;
    }

    /**
     * 是否已渲染（用于控制渲染链）
     */
    private boolean rendered;

    /**
     * 设置渲染状态
     */
    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    /**
     * 获取渲染状态
     */
    public boolean getRendered() {
        return rendered;
    }

    /**
     * 获取请求对象
     */
    public abstract Object request();

    /**
     * 获取远程IP
     */
    public abstract String ip();

    private String realIp;

    /**
     * 获取客户端真实IP
     */
    public String realIp() {
        if (realIp == null) {
            realIp = IpUtil.getIp(this);
        }

        return realIp;
    }

    /**
     * ::默认不自动处理；仅在取文件时解析
     */
    private boolean allowMultipart = false;

    /**
     * 是否自动解析分段内容
     */
    public boolean autoMultipart() {
        return allowMultipart;
    }

    /**
     * 设置是否自动解析分段内容
     */
    public void autoMultipart(boolean auto) {
        this.allowMultipart = auto;
    }

    /**
     * 是否为分段内容
     */
    public boolean isMultipart() {
        String temp = contentType();
        if (temp == null) {
            return false;
        } else {
            return temp.toLowerCase().contains("multipart/");
        }
    }

    /**
     * 是否为分段表单数据
     */
    public boolean isMultipartFormData() {
        String temp = contentType();
        if (temp == null) {
            return false;
        } else {
            return temp.toLowerCase().contains("multipart/form-data");
        }
    }

    /**
     * 获取请求方法
     */
    public abstract String method();

    /**
     * 获取请求协议
     */
    public abstract String protocol();

    private String protocolAsUpper;

    /**
     * 获取请求协议并大写
     */
    public String protocolAsUpper() {
        if (protocolAsUpper == null) {
            protocolAsUpper = protocol().toUpperCase();
        }

        return protocolAsUpper;
    }

    /**
     * 获取请求的URI
     */
    public abstract URI uri();

    /**
     * 获取请求的URI路径
     */
    public abstract String path();

    /**
     * 设置新路径
     */
    public void pathNew(String pathNew) {
        this.pathNew = pathNew;
    }

    private String pathNew;

    /**
     * 获取新路径，不存在则返回原路径
     */
    public String pathNew() {
        if (pathNew == null) {
            return path();
        } else {
            return pathNew;
        }
    }

    /**
     * 获取请求的URI路径变量,根据路径表达式
     */
//    public NvMap pathMap(String expr) {
//        return PathUtil.pathVarMap(path(), expr);
//    }

    private String pathAsUpper;

    /**
     * 获取请求的URI路径并大写
     */
    public String pathAsUpper() {
        if (pathAsUpper == null) {
            pathAsUpper = path().toUpperCase();
        }

        return pathAsUpper;
    }

    /**
     * 获取请求的UA
     */
    public String userAgent() {
        return header("User-Agent");
    }

    /**
     * 获取请求的URL字符串
     */
    public abstract String url();

    /**
     * 获取内容长度
     */
    public abstract long contentLength();

    /**
     * 获取内容类型
     */
    public abstract String contentType();

    /**
     * 获取查询字符串
     */
    public abstract String queryString();

    private String accept;

    /**
     * 获取 Accept 头信息
     */
    public String accept() {
        if (accept == null) {
            accept = header("Accept", "");
        }

        return accept;
    }


    private String body;

    /**
     * 获取body内容
     */
    public String body() throws IOException {
        return body(null);
    }

    /**
     * 获取body内容
     */
    public String body(String charset) throws IOException {
        if (body == null) {
            try (InputStream ins = bodyAsStream()) {
//                body = Utils.transferToString(ins, charset);
            }
        }

        return body;
    }

    private String bodyNew;

    /**
     * 获取新的body
     */
    public String bodyNew() throws IOException {
        if (bodyNew == null) {
            return body();
        } else {
            return bodyNew;
        }
    }

    /**
     * 设置新的body
     */
    public void bodyNew(String bodyNew) {
        this.bodyNew = bodyNew;
    }

    /**
     * 获取body内容为byte[]
     */
    public byte[] bodyAsBytes() throws IOException {
        try (InputStream ins = bodyAsStream()) {
            if (ins == null) {
                return null;
            }
            ByteArrayOutputStream outs = new ByteArrayOutputStream(); //这个不需要关闭
            int len = 0;
            byte[] buf = new byte[512]; //0.5k
            while ((len = ins.read(buf)) != -1) {
                outs.write(buf, 0, len);
            }

            return outs.toByteArray();
        }
    }

    /**
     * 获取body内容为Stream
     */
    public abstract InputStream bodyAsStream() throws IOException;


    /**
     * 获取参数并转为Bean
     */
    public <T> T paramAsBean(Class<T> type) {
        //不如参数注入的强；不支持 body 转换;
//        return ClassWrap.get(type).newBy(this::param, this);
        return null;
    }

    /**
     * 获取所有参数并转为map
     */
    public abstract Map paramMap();

    /**
     * 设置参数
     */
    public void paramSet(String name, String val) {
        paramMap().put(name, val);
        paramsAdd(name, val);
    }

    /**
     * 获取所有参数并转为Map
     */
    public abstract Map<String, List<String>> paramsMap();

    /**
     * 添加参数
     */
    public void paramsAdd(String name, String val) {
        if (paramsMap() != null) {
            List<String> ary = paramsMap().get(name);
            if (ary == null) {
                ary = new ArrayList<>();
                paramsMap().put(name, ary);
            }
            ary.add(val);
        }
    }

    /**
     * 获取上传文件
     *
     * @param name 文件名
     */
    public abstract List<UploadedFile> files(String name) throws Exception;

    /**
     * 获取 header
     *
     * @param name header名
     */
    public String header(String name) {
        return headerMap().get(name);
    }

    /**
     * 获取 header
     *
     * @param name header名
     * @param def  默认值
     */
    public String header(String name, String def) {
        return headerMap().getOrDefault(name, def);
    }

}
