package com.example.demo;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
 
public class Controller {
 
    public static String HOST = "http://localhost:8082";
    public static String STATIC_DIR = "templates";
    public Logger logger = Logger.getLogger(Controller.class.getName());
    // 注意这里的session没有封装到request里
    private Map<String, Object> session;
    private String sessionid;
    // 客户端的channel
    private SocketChannel client;
    private HTTPResponse response;
    // 该属性标志url请求的是否是静态资源
    private boolean isStatic;
    private String url;
 
 
    public void control(HTTPRequest request, SocketChannel client) throws IOException {
        // 初始化，用来设置类属性
        init(request,client);
 
        // 首页路径处理
        if(url.equals("/")){
             response = getFileResponse("/index.html");
        }
        else if(url.endsWith("/index")) {
            response = getFileResponse(url + ".html");
        }
        // 登录页处理，注册功能懒得写，下次一定
        else if (url.endsWith("/login")){
            if(request.getMethod() == HTTPRequest.POST){
                Map<String, String> params = parsePostParam(request);
                // 如果登录成功
                if (login(params)) {
                    response = new HTTPResponse().setStatus(302).setBody(new byte[2]);
                    response.addHeaders("Location", HOST + "/user/index.html");
//                    response = getFileResponse("/user/index.html");
                }else{
                    // 懒得改前端，就多个页面吧
                    response = getFileResponse("/indexerror.html");
                }
            }else if(request.getMethod() == HTTPRequest.GET){
                response = getFileResponse("/index.html");
//                response = new HTTPResponse().setStatus(302).setBody(new byte[2]);
//                response.addHeaders("Location", HOST + "/index.html");
            }
        }
        // 个人页处理
        else if(url.startsWith("/user/")) {
            if (!isStatic && session.get("_user") == null) {
                response = getFileResponse(url);
                // 如果是html文件，且存在（这么写的原因是isStatic判断并不完全准确）
                if(url.endsWith(".html") && response.getStatus() != 404){
                    response.setStatus(302).setBody(new byte[2]);
                    response.addHeaders("Location", HOST + "/index");
                }
            } else {
                response = getFileResponse(url);
                response.setStatus(200);
            }
        }
        // 直接获取文件页面的处理，如静态资源与html
        else{
            response = getFileResponse(url);
        }
 
 
        // 设置set_cookie
        if(!isStatic) {
            Map<String, String> setCookie = response.getCookie();
            setCookie.put("session_id", sessionid);
        }
 
        sendData(response);
    }
 
    private void init(HTTPRequest request, SocketChannel client){
        this.client = client;
        // url处理
        url = request.getUrl();
        logger.info(Thread.currentThread().toString() + "\t访问路径为：" +  url);
        if(url.contains("?")){
            // 不匹配参数，打扰了
            String[] split = url.split("[?]");
            url = split[0];
        }
        // 看看url是不是要获取的是静态文件，是的话放行
        isStatic = isStaticResource(url);
 
        // 获取session
        Map<String, String> cookie = request.getCookie();
        sessionid = cookie.get("session_id");
 
        if(!isStatic){
            // 验证并获取一个有效的sessionid
            sessionid = SessionFactory.getInstance().getVaildSessionId(sessionid);
            session = SessionFactory.getInstance().getSession(sessionid);
        }
    }
 
    /**
     * 判断url是否要获取静态资源
     * @param url
     * @return
     */
    private boolean isStaticResource(String url){
        if(url.endsWith(".css") || url.endsWith(".js")
                || url.endsWith(".png") || url.endsWith(".jpg")
                || url.endsWith(".jpeg") || url.endsWith(".woff2")
                || url.endsWith(".woff") || url.endsWith(".ttf")){
            return true;
        }
        return false;
    }
 
    /**
     * 根据传入的文件路径获取对应的文件，需要注意path第一字符为/
     * @param path
     * @return
     * @throws IOException
     */
    private HTTPResponse getFileResponse(String path) throws IOException {
        // 读取对应的html文件作为body，path自带/
        File tempfile = new File(STATIC_DIR + path);
        File file;
        FileInputStream fileInputStream = null;
        byte[] body;
        try {
            // 将文件读取到body数组中，作为response的body
            file = tempfile.getCanonicalFile();
            fileInputStream = new FileInputStream(file);
            body = fileInputStream.readAllBytes();
        }catch (FileNotFoundException e){
            // 文件不存在，直接404警告
            HTTPResponse httpResponse = new HTTPResponse().setStatus(404);
//            sendData(httpResponse);
            return httpResponse;
        }
        finally {
            if(fileInputStream != null) {
                fileInputStream.close();
            }
        }
 
        // 设置response的各种参数
        HTTPResponse httpResponse = new HTTPResponse().setBody(body).setStatus(200);
        // 对静态资源设置不同的contentType
        if(path.endsWith(".css")) {
            httpResponse.setContentType("text/css");
        }else if(path.endsWith(".js")){
            httpResponse.setContentType("application/javascript");
        }else if(path.endsWith(".png")){
            httpResponse.setContentType("image/png");
        }else if(path.endsWith(".jpg")){
            httpResponse.setContentType("image/jpg");
        }else if(path.endsWith(".jpeg")){
            httpResponse.setContentType("image/jpeg");
        }else if(path.endsWith(".woff2") || path.endsWith(".woff") || path.endsWith(".ttf")){
            httpResponse.setContentType("");
        }
 
        return httpResponse;
    }
 
    /**
     * 用于发送response给客户端
     * @param httpResponse
     * @throws IOException
     */
    private void sendData(HTTPResponse httpResponse) throws IOException {
        byte[] response = httpResponse.toBytes();
        ByteBuffer send = ByteBuffer.allocate(response.length);
        send.limit(response.length);
        send.put(response);
        send.position(0);
        client.write(send);
    }
 
    private Map<String, String> parsePostParam(HTTPRequest request){
        HashMap<String, String> result = new HashMap<>();
        String params = new String(request.getBody());
        String[] split = params.split("&");
        if(split.length >= 2){
            // 遍历所有参数
            for (int i = 0; i < split.length; i++) {
                String[] param = split[i].split("=");
                // 把参数的key与value写入map
                if(param.length >= 2){
                    result.put(param[0], param[1]);
                }
            }
        }
        return result;
    }
 
    /**
     * 验证登录用户，并放到session中。这里使用文件读写来实现账号保存
     * @param params
     * @return
     */
    public boolean login(Map<String, String> params){
        String username = params.get("username").trim();
        String password = params.get("password").trim();
        UserSet.User user = UserSet.getInstance().getUser(username, password);
        if(user != null){
            session.put("_user", user);
            return true;
        }
        return false;
 
    }
 
}