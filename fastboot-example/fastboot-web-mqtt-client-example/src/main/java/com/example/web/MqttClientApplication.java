//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.web;

import io.github.stylesmile.annotation.*;
import io.github.stylesmile.app.App;
import io.github.stylesmile.ioc.Bean;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.tool.PropertyUtil;
import net.dreamlu.iot.mqtt.core.client.MqttClient;

import java.nio.charset.StandardCharsets;

/**
 * 启动时，只需扫描 controller service 和配置，这样可以提高启动速度
 */
@Fastboot()
@Controller
public class MqttClientApplication {

    @AutoWired
    private static MqttClient mqttClient;



    @Value("fastboot.name")
    String name;

    public static void main(String[] args) {
        App.start(MqttClientApplication.class, args);
        mqttClient.subQos1("/test/1", (context, topic, message, payload) -> {
            System.out.println(topic + '\t' + new String(payload, StandardCharsets.UTF_8));
        });
        // 消息订阅，同类方法 subxxx
    }

    @RequestMapping("/")
    public String hello() {
        return "hello fastboot";
    }

    @RequestMapping("/1")
    public String hello1() {
        mqttClient.publish("/test/1", "test".getBytes(StandardCharsets.UTF_8));
        return PropertyUtil.props.getProperty("fast.name");
    }

}
