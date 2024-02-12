package io.github.stylesmile.openapi2;


import io.github.stylesmile.plugin.Plugin;

/**
 * @author Stylesmile
 */
public class OpenapiKnife4jPlugin implements Plugin {


    @Override
    public void start() {
        BeanWrap beanWrap = context.beanMake(OpenApiExtensionResolver.class);
        OpenApiExtensionResolver openApiExtensionResolver = beanWrap.raw();

        if (openApiExtensionResolver.getSetting().isEnable()) {
            String uiPath = "/";
            if (openApiExtensionResolver.getSetting().isProduction()) {
                if (Solon.cfg().isFilesMode() == false) {
                    //生产环境，只有 files 模式才能用（即开发模式）
                    StaticMappings.add(uiPath, new ClassPathStaticRepository("META-INF/resources"));
                }
            } else {
                //非生产环境，一直可用
                StaticMappings.add(uiPath, new ClassPathStaticRepository("META-INF/resources"));
            }

            Solon.app().add(uiPath, OpenApi2Controller.class);
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void end() {

    }

}
