package com.example;

import io.github.stylesmile.nacos.BaseZookeeper;
import org.junit.Test;

import java.util.List;

public class Tests {
    @Test
    public void test() {
        BaseZookeeper zookeeper = new BaseZookeeper();
        try {
            zookeeper.connectZookeeper("192.168.0.1:2181");
            List<String> children = zookeeper.getChildren("/");
            System.out.println(children);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
