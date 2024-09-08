package io.github.stylesmile.mqtt.client;

import io.github.stylesmile.annotation.Service;
import io.github.stylesmile.ioc.Bean;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.tool.StringUtil;
import net.dreamlu.iot.mqtt.codec.MqttVersion;
import net.dreamlu.iot.mqtt.core.client.IMqttClientConnectListener;
import net.dreamlu.iot.mqtt.core.client.MqttClient;
import org.tio.core.ChannelContext;

import java.util.UUID;

@Service
public class MqttClientConfig {
    @Value("mqtt.client.ip")
    private String ip = "127.0.0.1";

    @Value("mqtt.client.port")
    private String port = "1883";

    @Value("mqtt.client.username")
    private String username = "admin";

    @Value("mqtt.client.password")
    private String password = "123456";

    @Value("mqtt.client.clientId")
    private String clientId;

    @Value("mqtt.client.readBufferSize")
    private String readBufferSize = "5120";

    @Value("mqtt.client.maxBytesInMessage")
    private String maxBytesInMessage = "10240";

    @Value("mqtt.client.keepAliveSecs")
    private String keepAliveSecs = "120";

    @Value("mqtt.client.timeout")
    private String timeout = "10";

    @Value("mqtt.client.reconnect")
    private String reconnect = "true";

    @Value("mqtt.client.reInterval")
    private String reInterval = "5000";

    @Value("mqtt.client.willMessage.topic")
    private String topic;

    @Value("mqtt.client.willMessage.message")
    private String message;


    @Bean("MqttClient")
    public MqttClient getMqttClient() {
        if (StringUtil.isBlank(clientId)) {
            clientId = UUID.randomUUID().toString();
        }
        if (StringUtil.isBlank(message)) {
            message = clientId + "，down";
        }
        // 初始化 mqtt 客户端
        MqttClient client = MqttClient.create()
                .ip(ip)                                               // mqtt 服务端 ip 地址
                .port(Integer.parseInt(port))                          // 默认：1883
                .username(username)                                   // 账号
                .password(password)                                   // 密码
                .version(MqttVersion.MQTT_5)                          // 默认：3_1_1
                .clientId(clientId)                                   // 非常重要务必手动设置，一般设备 sn 号，默认：MICA-MQTT- 前缀和 36进制的纳秒数
                .readBufferSize(Integer.parseInt(readBufferSize))      // 消息一起解析的长度，默认：为 8092 （mqtt 消息最大长度）
                .maxBytesInMessage(Integer.parseInt(maxBytesInMessage))// 最大包体长度,如果包体过大需要设置此参数，默认为： 10M (10*1024*1024)
                .keepAliveSecs(Integer.parseInt(keepAliveSecs))        // 默认：60s
                .timeout(Integer.parseInt(timeout))                    // 超时时间，t-io 配置，可为 null，为 null 时，t-io 默认为 5
                .reconnect(Boolean.parseBoolean(reconnect))                                 // 是否重连，默认：true
                .reInterval(Integer.parseInt(reInterval))              // 重连重试时间，reconnect 为 true 时有效，t-io 默认为：5000
                .willMessage(builder -> {
//                    builder.topic("/test/offline").messageText("down");    // 遗嘱消息
                    if (StringUtil.isNotBlank(topic)) {
                        builder.topic(topic).messageText(message);    // 遗嘱消息
                    }
                })
                .connectListener(new IMqttClientConnectListener() {
                    @Override
                    public void onConnected(ChannelContext context, boolean isReconnect) {
                        System.out.println("链接服务器成功...");
                    }

                    @Override
                    public void onDisconnect(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {
                        System.out.println("与链接服务器断开连接...");
                    }
                })
//                .properties()                   // mqtt5 properties
                .connectSync();                 // 同步连接，也可以使用 connect()，可以避免 broker 没启动照成启动卡住。

        // 消息订阅，同类方法 subxxx
//        client.subQos0("/test/#", (context, topic, message, payload) -> {
//            logger.info(topic + '\t' + new String(payload, StandardCharsets.UTF_8));
//        });
//        // 取消订阅
//        client.unSubscribe("/test/#");
//
//        // 发送消息
//        client.publish("/test/client", "mica最牛皮".
//
//                getBytes(StandardCharsets.UTF_8));

        // 断开连接
//        client.disconnect();
        // 重连
//        client.reconnect();
        // 停止
//        client.stop();
        return client;
    }

}
