package io.github.stylesmile.mqtt.server;

import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.FastbootUtil;

/**
 * @author Stylesmile
 */
public class MqttServerPlugin implements Plugin {


    @Override
    public void start() {
        FastbootUtil.addClass(MqttServerConfig.class);
    }

    @Override
    public void init() {
    }

    @Override
    public void end() {

    }

}
