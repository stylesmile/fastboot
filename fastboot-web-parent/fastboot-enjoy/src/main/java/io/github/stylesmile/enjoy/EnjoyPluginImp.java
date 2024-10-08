package io.github.stylesmile.enjoy;

import com.jfinal.template.Engine;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;

/**
 * @author Stylesmile
 */
public class EnjoyPluginImp implements Plugin {
    @Override
    public void start() {

    }

    /**
     * 初始化
     */
    @Override
    public void init() {
        // 创建用于整合 的 ViewResolver 扩展对象
        Engine engine = Engine.use();

        // 热加载配置能对后续配置产生影响，需要放在最前面
        engine.setDevMode(true);

        // 使用 ClassPathSourceFactory 从 class path 与 jar 包中加载模板文件
        engine.setToClassPathSourceFactory();

        // 在使用 ClassPathSourceFactory 时要使用 setBaseTemplatePath
        // 代替 jfr.setPrefix("/view/")
        engine.setBaseTemplatePath("/templates/");

        // 添加模板函数
//        engine.addSharedFunction("/common/_layout.html");
//        engine.addSharedFunction("/common/_paginate.html");

        // 更多配置与前面章节完全一样
        // engine.addDirective(...)
        // engine.addSharedMethod(...);

        BeanContainer.setInstance(Engine.class, engine);
//        BeanContainer.setInstance(JFinalViewResolver.class, jfr);
    }

    @Override
    public void end() {

    }
//    @Override
//    public void start(AopContext context) {
//
//        EnjoyRender render = EnjoyRender.global();
//
//        context.lifecycle(-99, () -> {
//            context.beanForeach((k, v) -> {
//                if (k.startsWith("view:")) { //java view widget
//                    if (Directive.class.isAssignableFrom(v.clz())) {
//                        render.putDirective(k.split(":")[1], (Class<? extends Directive>) v.clz());
//                    }
//                    return;
//                }
//
//                if (k.startsWith("share:")) { //java share object
//                    render.putVariable(k.split(":")[1], v.raw());
//                    return;
//                }
//            });
//        });
//
//        RenderManager.register(render);
//        RenderManager.mapping(".shtm", render);

//        if (ClassUtil.hasClass(() -> AuthUtil.class)) {
//            render.putDirective(AuthConstants.TAG_authPermissions, AuthPermissionsTag.class);
//            render.putDirective(AuthConstants.TAG_authRoles, AuthRolesTag.class);
//        }
//        BeanContainer.setInstance(SQLManager.class, sqlManager);
//
//    }
}
