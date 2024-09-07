package com.example.web;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Service;
import io.github.stylesmile.ioc.Bean;
import net.dreamlu.iot.mqtt.core.client.MqttClient;

import java.nio.charset.StandardCharsets;

@Service
public class MqttService {
    @AutoWired
    public MqttClient mqttClient;

    @Bean("publish")
    public String publish()
    {
//        mqttClient.subQos0("/test/1", (context, topic, message, payload) -> {
//            System.out.println(topic + '\t' + new String(payload, StandardCharsets.UTF_8));
//        });
        return "publish";
    }
}
