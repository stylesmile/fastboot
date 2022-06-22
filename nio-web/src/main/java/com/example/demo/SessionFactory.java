package com.example.demo;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
 
public class SessionFactory {
    volatile static private SessionFactory instance;
    private Map<String, Map<String,Object>> sessions = new ConcurrentHashMap<>();
    // 用来存放session的过期时间，session不给续约一说，一旦过期，就只能更换
    private Map<String, Long> expires = new ConcurrentHashMap<>();
 
    private SessionFactory(){}
 
    public static SessionFactory getInstance(){
        if (instance == null){
            synchronized (SessionFactory.class){
                if(instance == null){
                    instance = new SessionFactory();
                }
            }
        }
        return instance;
    }
 
    /**
     * 敲黑板，这不算传统的工厂模式直接返回对象，而是普通的get方法
     * @param id
     * @return
     */
    public Map<String, Object> getSession(String id){
        return sessions.get(id);
    }
 
    /**
     * 敲黑板，返回的是session_id，这才是传统工厂模式返回的对象
     * @return
     */
    public String createSession(){
        String id = UUID.randomUUID().toString().replace("-", "");
        sessions.put(id, new ConcurrentHashMap<>());
//        long now = new Date().getTime();
        long now = System.currentTimeMillis();
        // 简单写死，设定为3天后过期
        long expireTime  = now + TimeUnit.DAYS.toMillis(3);
        expires.put(id, expireTime);
        return id;
    }
 
    /**
     * 如果返回值为true代表过期
     * @param id
     * @return
     */
    public boolean checkExpire(String id){
//        long now = new Date().getTime();
        long now = System.currentTimeMillis();
        long expireTime = expires.get(id);
        return now > expireTime;
    }
 
 
    /**
     * 如果session_id过期，返回新的session_id,并将过期的处理掉
     * @return
     */
    public String getVaildSessionId(String id){
        // 如果sessionid不存在，返回一个有效的sessionid
        if(id == null || sessions.get(id) == null) {
            return createSession();
        }else {
            // 如果sessionid过期，清除并返回一个有效的sessionid
            if (checkExpire(id)) {
                return flush(id);
            }
            return id;
        }
    }
 
 
    public String flush(String id){
        Map<String, Object> oldSession = sessions.get(id);
        sessions.remove(id);
        expires.remove(id);
        String newId = createSession();
        sessions.put(newId, oldSession);
        return newId;
    }
 
}