package io.github.stylesmile.beetlsql;

import com.zaxxer.hikari.HikariDataSource;
import io.github.stylesmile.tool.PropertyUtil;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.core.db.PostgresStyle;
import org.beetl.sql.ext.DebugInterceptor;

import javax.sql.DataSource;


/**
 * @author Stylesmile
 */
public class BeetlsqlConfig {


    /**
     * 获取 mongo工厂
     *
     * @return 工厂
     */
    public static SQLManager getSQLManager() {
        DataSource dataSource = initDataSource();
        ConnectionSource source = ConnectionSourceHelper.getSingle(dataSource);
        SQLManagerBuilder builder = new SQLManagerBuilder(source);
        builder.setNc(new UnderlinedNameConversion());
        builder.setInters(new Interceptor[]{new DebugInterceptor()});
        builder.setDbStyle(new MySqlStyle());
//        builder.setDbStyle(new PostgresStyle());
        SQLManager sqlManager = builder.build();
        return sqlManager;
    }

    /**
     * 初始化数据源
     */
    private static javax.sql.DataSource initDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(PropertyUtil.getProperty("fast.db.url"));
        dataSource.setDriverClassName(PropertyUtil.getProperty("fast.db.driverClassName"));
        dataSource.setUsername(PropertyUtil.getProperty("fast.db.username"));
        dataSource.setPassword(PropertyUtil.getProperty("fast.db.password"));
        dataSource.setIdleTimeout(60000);
        dataSource.setAutoCommit(true);
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(1);
        dataSource.setMaxLifetime(60000 * 10);
        dataSource.setConnectionTestQuery("SELECT 1");
        return dataSource;
    }
}
