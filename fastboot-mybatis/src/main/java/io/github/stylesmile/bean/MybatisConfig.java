package io.github.stylesmile.bean;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stylesmile
 */
public class MybatisConfig {

    private Map<String, DataSource> dataSources;

    private String mapperLocations = "mapper";

    private Interceptor[] plugins;

    private Boolean mapUnderscoreToCamelCase = true;

    private IdentifierGenerator identifierGenerator;

    public Boolean getMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(Boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public Interceptor[] getPlugins() {
        return plugins;
    }

    public void setPlugins(Interceptor[] plugins) {
        this.plugins = plugins;
    }

    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }

    public void addDataSource(DataSource dataSource) {
        if (dataSources == null) {
            dataSources = new HashMap<>();
        }
        dataSources.put(SqlSessionFactory.class.getName(), dataSource);
    }

    public void addDataSource(String dataSourceName, DataSource dataSource) {
        if (dataSources == null) {
            dataSources = new HashMap<>();
        }
        dataSources.put(dataSourceName, dataSource);
    }

    public IdentifierGenerator getIdentifierGenerator() {
        return identifierGenerator;
    }

    public void setIdentifierGenerator(IdentifierGenerator identifierGenerator) {
        this.identifierGenerator = identifierGenerator;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }
}
