package io.github.stylesmile.knife4j;


import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.FastbootUtil;

/**
 * @author Stylesmile
 */
public class OpenapiKnife4jPlugin implements Plugin {

    /**
     * 基于代码构建
     *
     * @return
     */
    @Override
    public void start() {
        FastbootUtil.addClass(OpenApi2Controller.class);
    }

    @Override
    public void init() {
    }

    @Override
    public void end() {

    }

}
