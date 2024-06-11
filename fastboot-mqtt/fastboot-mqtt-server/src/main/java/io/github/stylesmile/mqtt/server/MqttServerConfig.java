package io.github.stylesmile.mqtt.server;

import io.github.stylesmile.annotation.Service;
import io.github.stylesmile.ioc.Bean;
import io.github.stylesmile.ioc.Value;
import net.dreamlu.iot.mqtt.core.server.MqttServer;
import net.dreamlu.iot.mqtt.core.server.dispatcher.IMqttMessageDispatcher;
import net.dreamlu.iot.mqtt.core.server.event.IMqttConnectStatusListener;
import net.dreamlu.iot.mqtt.core.server.model.Message;
import org.tio.core.ChannelContext;

@Service
public class MqttServerConfig {
    @Value("mqtt.server.serverIp")
    private String ip ="0.0.0.0" +
            "" +
            "" +
            "" +
            "";

    @Value("mqtt.server.port")
    private String port = "1884";

    @Value("mqtt.server.username")
    private String username = "admin";

    @Value("mqtt.server.password")
    private String password = "123456";

    @Value("mqtt.server.clientId")
    private String clientId;

    @Value("mqtt.server.readBufferSize")
    private String readBufferSize = "512";

    @Value("mqtt.server.heartbeatTimeout")
    private String heartbeatTimeout = "120000";

    @Value("mqtt.server.maxBytesInMessage")
    private String maxBytesInMessage = "1024000";


    @Bean("MqttServer")
    public MqttServer getMqttServer() {
        // 注意：为了能接受更多链接（降低内存），请添加 jvm 参数 -Xss129k
        MqttServer mqttServer = MqttServer.create()
                // 服务端 ip 默认为空，0.0.0.0，建议不要设置
                .ip(ip)
                // 默认：1883
                .port(Integer.parseInt(port))
                // 默认为： 8092（mqtt 默认最大消息大小），为了降低内存可以减小小此参数，如果消息过大 t-io 会尝试解析多次（建议根据实际业务情况而定）
                .readBufferSize(Integer.parseInt(readBufferSize))
                // 最大包体长度，如果包体过大需要设置此参数，默认为： 8092
                .maxBytesInMessage(Integer.parseInt(maxBytesInMessage))
                // 自定义认证
//                .authHandler((clientId, userName, password) -> true)
                .usernamePassword(username, password)
                // 消息监听
//                .messageListener((context, clientId, message) -> {
//                    logger.info("clientId:{} message:{} payload:{}", clientId, message, new String(message.getPayload(), StandardCharsets.UTF_8));
//                })
                // 心跳超时时间，单位 毫秒 默认：120s 120_1000L
                .heartbeatTimeout(Long.valueOf(heartbeatTimeout))
                // ssl 配置
//                .useSsl("", "", "")
//                .useSsl("", "")
                // 自定义客户端上下线监听
                .connectStatusListener(new IMqttConnectStatusListener() {
                    @Override
                    public void online(ChannelContext context, String clientId, String username) {
                    }

                    @Override
                    public void offline(ChannelContext context, String clientId, String username, String reason) {

                    }
                })
                // 自定义消息转发，可用 mq 广播实现集群化处理
                .messageDispatcher(new IMqttMessageDispatcher() {
                    @Override
                    public boolean send(Message message) {
                        return false;
                    }

                    @Override
                    public boolean send(String clientId, Message message) {
                        return false;
                    }
                })
                .debug() // 开启 debug 信息日志
                .start();

        // 发送给某个客户端
//        mqttServer.publish("clientId", "/test/123", "mica最牛皮".getBytes(StandardCharsets.UTF_8));

        // 发送给所有在线监听这个 topic 的客户端
//        mqttServer.publishAll("/test/123", "mica最牛皮".getBytes(StandardCharsets.UTF_8));

        // 停止服务
//        mqttServer.stop();
        return mqttServer;
    }

}
