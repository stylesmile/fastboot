package com.jiyan.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import io.github.stylesmile.annotation.Service;
import io.github.stylesmile.ioc.Bean;

import java.util.Properties;

@Service
public class CaptchaService {
    public DefaultKaptcha getDefaultKaptcha() {
        Properties props = new Properties();
        props.setProperty("kaptcha.border", "no");
        props.setProperty("kaptcha.border.color", "105,179,90");
        props.setProperty("kaptcha.textproducer.font.color", "blue");
        props.setProperty("kaptcha.image.width", "125");
        props.setProperty("kaptcha.image.height", "45");
        props.setProperty("kaptcha.textproducer.font.size", "45");
        props.setProperty("kaptcha.session.key", "code");
        props.setProperty("kaptcha.textproducer.char.length", "4");
        props.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        props.setProperty("kaptcha.noise.color", "black");
        props.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(props);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
