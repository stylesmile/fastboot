package io.github.stylesmile.mqtt.client;

import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.FastbootUtil;

/**
 * @author Stylesmile
 */
public class MqttClientPlugin implements Plugin {


    @Override
    public void start() {
        FastbootUtil.addClass(MqttClientConfig.class);
    }

    @Override
    public void init() {
    }

    @Override
    public void end() {

    }

}
