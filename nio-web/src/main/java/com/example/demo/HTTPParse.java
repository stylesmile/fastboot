package com.example.demo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
 
public class HTTPParse {
    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final char HT = '\t';
    private static final char SP = ' ';
    private static final char CO = ':';
 
    private StringBuilder sb = new StringBuilder();
    private HTTPRequest HTTPRequest = new HTTPRequest();
 
    enum State { STATUS_LINE,
        HEADER,
        HEADER_FOUND_CR,
        HEADER_FOUND_LF,
        HEADER_FOUND_CR_LF,
        HEADER_FOUND_CR_LF_CR,
        FINISHED }
 
    private HTTPParse.State state = HTTPParse.State.STATUS_LINE;
 
    public HTTPRequest parse(ByteBuffer input) throws IOException {
        assert input != null;
//        if(input.hasRemaining()){
//            System.out.println(new String(input.array()));
//        }
        while (input.hasRemaining() && state != State.FINISHED) {
            switch (state) {
                // 请求行
                case STATUS_LINE:
                    readResumeStatusLine(input);
                    break;
                case HEADER:
                    readResumeHeader(input);
                    break;
                case HEADER_FOUND_CR:
                case HEADER_FOUND_LF:
                    resumeOrLF(input);
                    break;
                case HEADER_FOUND_CR_LF:
                    resumeOrSecondCR(input);
                    break;
                case HEADER_FOUND_CR_LF_CR:
                    resumeOrEndHeaders(input);
                    break;
                default:
                    throw new InternalError(
                            "Unexpected state: " + String.valueOf(state));
            }
        }
        return HTTPRequest;
    }
 
    private void resumeOrLF(ByteBuffer input) throws ProtocolException {
        // 回车符后换行符
        char c = (char)input.get();
        if(c != LF && c != CR) {
            throw new ProtocolException("这协议不对劲");
        }
        state = State.HEADER_FOUND_CR_LF;
    }
 
 
    private void readResumeHeader(ByteBuffer input) throws ProtocolException {
        String name = null;
        String content;
        boolean first = true;
        while (input.hasRemaining()) {
            char c = (char)input.get();
 
            if (c == CR) {
                break;
            } else if (c == HT) {
                c = SP;
            }
            // 遇到冒号(只允许第一次)就获取header的名字
            else if(c == CO) {
                // 第二次匹配到冒号就当成正常的字符
                if(first) {
                    first = false;
                    name = sb.toString();
                    sb = new StringBuilder();
                    continue;
                }
            }
            else if(c == SP) {
                continue;
            }
 
            sb.append(c);
        }
        content = sb.toString();
        sb = new StringBuilder();
        parseHeaders(name, content);
        state = State.HEADER_FOUND_CR;
    }
 
    private void resumeOrEndHeaders(ByteBuffer input) throws ProtocolException {
        // 先把最后的/n去掉
        input.get();
        int length = input.limit() - input.position();
        byte[] body = new byte[length];
        int index = 0;
        // 剩下的数据当成body
        while (input.hasRemaining()){
            body[index++] = input.get();
        }
        HTTPRequest.setBody(body);
        state = State.FINISHED;
    }
 
    /**
     *  到这里代表已经一行结束，接着检测下行开头
     * @param input
     * @throws ProtocolException
     */
    private void resumeOrSecondCR(ByteBuffer input) throws ProtocolException {
        char c = (char)input.get();
        if(c == CR) {
            state = State.HEADER_FOUND_CR_LF_CR;
        } else if (c == SP || c == HT) {
            state = State.HEADER;
        }else{
            // 若是普通字符，放回去，打扰了
            sb.append(c);
            state = State.HEADER;
        }
    }
 
    private void readResumeStatusLine(ByteBuffer input) throws IOException {
        // 获取method
        char c;
        while (input.hasRemaining() && (c =(char)input.get()) != SP) {
            sb.append(c);
        }
        parseMethod(sb.toString());
        sb = new StringBuilder();
        // 获取url
        while (input.hasRemaining() && (c =(char)input.get()) != SP) {
            sb.append(c);
        }
        parseURL(sb.toString());
        sb = new StringBuilder();
 
        // 获取协议版本。。。算了
        while (input.hasRemaining() && (c =(char)input.get()) != CR);
        // 遇到换行符下一个必须是LF
        if((char)input.get() != LF) {
            throw new ProtocolException("这协议不对劲");
        }
        // 清空
        sb = new StringBuilder();
        // 下面读取的就是http头部信息
        state = State.HEADER;
    }
 
 
 
    private void parseURL(String url) throws MalformedURLException {
        HTTPRequest.setUrl(url);
    }
 
    /**
     * 解析方法
     * @param method
     * @throws ProtocolException
     */
    public void parseMethod(String method) throws ProtocolException {
        if(method.equals("GET")){
            HTTPRequest.setMethod(com.example.demo.HTTPRequest.GET);
        }
        else if(method.equals("POST")){
            HTTPRequest.setMethod(com.example.demo.HTTPRequest.POST);
        }else{
            throw new ProtocolException("请求方法不是GET或者POST，再你妈的见");
        }
    }
 
    private Map<String, String> parseCookie(String cookieStr){
        Map<String, String> cookie = new HashMap<>();
        String[] cookeis = cookieStr.split(";");
        // 遍历所有cookie
        for (int i = 0; i < cookeis.length; i++) {
            String[] cookieItem = cookeis[i].split("=");
            // 冒号前半部分作为key，后半部分作为value
            if(cookieItem.length >= 2) {
                cookie.put(cookieItem[0], cookieItem[1]);
            }
        }
        return cookie;
    }
 
    public void parseHeaders(String name, String content){
        assert name != null;
        if(name.equals("Host")){
            HTTPRequest.setHost(content);
        }else if(name.equals("Cookie")){
            HTTPRequest.setCookie(parseCookie(content));
        }else{
            // 其他请求头就不处理了，再见
        }
    }
 
    class ProtocolException extends IOException{
        public ProtocolException(String message) {
            super(message);
        }
    }
}