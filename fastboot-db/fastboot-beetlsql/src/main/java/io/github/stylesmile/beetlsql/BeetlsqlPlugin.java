package io.github.stylesmile.beetlsql;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import org.beetl.sql.core.SQLManager;

/**
 * 参考文献 https://gitee.com/xiandafu/beetlsql/
 *
 * @author Stylesmile
 */
public class BeetlsqlPlugin implements Plugin {

    @Override
    public void start() {

    }

    @Override
    public void init() {
        SQLManager sqlManager = BeetlsqlConfig.getSQLManager();
        BeanContainer.setInstance(SQLManager.class, sqlManager);
    }

    @Override
    public void end() {

    }
}
