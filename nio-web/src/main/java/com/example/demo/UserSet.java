package com.example.demo;

import java.io.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
 
public class UserSet {
    private volatile static UserSet instance;
    public static String FILE_NAME = "user.txt";
    public static String SPNARATOR = "/%abc%/";
    private Set<User> userSet = new CopyOnWriteArraySet<>();
 
    private UserSet(){}
 
    public static UserSet getInstance() {
        if(instance == null){
            synchronized (UserSet.class){
                if(instance == null){
                    instance = new UserSet();
                }
            }
        }
        return instance;
    }
 
    // 将文件内容反序列化到userSet中
    void read(){
        File file = new File(FILE_NAME);
        if(!file.exists()) {
            return;
        }
        try {
            Reader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null){
                String[] split = line.split(SPNARATOR);
                if(split.length >= 2){
                    User user = new User();
                    user.username = split[0];
                    user.password = split[1];
                    userSet.add(user);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public User getUser(String username, String password){
        if(contanis(username, password)){
            return new User(username, password);
        }
        return null;
    }
 
    public boolean contanis(String username, String password){
        User user = new User();
        user.username = username;
        user.password = password;
        if(userSet.size() == 0){
            read();
        }
        return userSet.contains(user);
    }
 
    public class User{
        private String username;
        private String password;
 
        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
        public User(){};
 
        @Override
        public boolean equals(Object obj) {
            User user = (User) obj;
            return username.equals(user.username) &&
                    password.equals(user.password);
        }
 
        @Override
        public int hashCode() {
            int h1 = username.hashCode();
            int h2 = password.hashCode();
            // 随便写的，我也不知道冲突率怎样。
            int h = h1 ^ h2;
            return (h) ^ (h >>> 16);
        }
 
        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}