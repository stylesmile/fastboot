package io.github.stylesmile.zookeeper;

import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {
        BaseZookeeper zookeeper = new BaseZookeeper();
        zookeeper.connectZookeeper("127.0.0.1:2181");
        List<String> children = zookeeper.getChildren("/");
        System.out.println(children);
    } 
}