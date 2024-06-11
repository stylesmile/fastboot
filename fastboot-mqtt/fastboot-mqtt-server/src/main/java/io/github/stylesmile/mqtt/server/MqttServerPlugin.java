package io.github.stylesmile.mqtt.server;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.FastbootUtil;
import net.dreamlu.iot.mqtt.core.server.MqttServer;

/**
 * @author Stylesmile
 */
public class MqttServerPlugin implements Plugin {


    @Override
    public void start() {
        // 添加到 待扫描的类中
        FastbootUtil.addClass(MqttServerConfig.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void end() {
    }

}
