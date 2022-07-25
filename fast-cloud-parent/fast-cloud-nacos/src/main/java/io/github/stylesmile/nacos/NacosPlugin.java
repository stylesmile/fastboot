package io.github.stylesmile.nacos;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.PropertyUtil;

import java.util.Properties;

/**
 * 参考文献 https://nacos.io/en-us/docs/quick-start.html
 * https://help.aliyun.com/document_detail/94562.html
 *
 * @author Stylesmile
 */
public class NacosPlugin implements Plugin {


    @Override
    public void start() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, PropertyUtil.getProperty("nacos.serverAddr"));
        properties.put(PropertyKeyConst.USERNAME, PropertyUtil.getProperty("nacos.username"));
        properties.put(PropertyKeyConst.PASSWORD, PropertyUtil.getProperty("nacos.password"));
        try {
            ConfigService configService = NacosFactory.createConfigService(properties);
            BeanContainer.setInstance(ConfigService.class, configService);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void init() {

    }

    @Override
    public void end() {

    }

}
