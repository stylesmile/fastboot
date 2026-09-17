package io.github.stylesmile.flyway;

import com.zaxxer.hikari.HikariDataSource;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import io.github.stylesmile.tool.PropertyUtil;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

/**
 * Flyway 数据库迁移插件
 *
 * @author Stylesmile
 */
public class FlywayPlugin implements Plugin {

    @Override
    public void start() {

    }

    @Override
    public void init() {
        DataSource dataSource = buildDataSource();
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
        flyway.migrate();
        BeanContainer.setInstance(DataSource.class, dataSource);
        BeanContainer.setInstance(Flyway.class, flyway);
    }

    @Override
    public void end() {

    }

    private static DataSource buildDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(PropertyUtil.getProperty("fast.db.url"));
        dataSource.setDriverClassName(PropertyUtil.getProperty("fast.db.driverClassName"));
        dataSource.setUsername(PropertyUtil.getProperty("fast.db.username"));
        dataSource.setPassword(PropertyUtil.getProperty("fast.db.password"));
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(1);
        dataSource.setAutoCommit(true);
        dataSource.setIdleTimeout(60000);
        dataSource.setMaxLifetime(600000);
        return dataSource;
    }
}
