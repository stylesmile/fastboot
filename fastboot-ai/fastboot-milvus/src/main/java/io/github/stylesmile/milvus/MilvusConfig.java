package io.github.stylesmile.milvus;

import io.github.stylesmile.tool.PropertyUtil;
import io.milvus.param.ConnectParam;

/**
 * Milvus 配置类
 *
 * @author Stylesmile
 */
public class MilvusConfig {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 19530;
    private static final String DEFAULT_DATABASE = "default";

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String databaseName;

    public MilvusConfig(String host, int port, String username, String password, String databaseName) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
    }

    public static MilvusConfig load() {
        String host = property("milvus.host", DEFAULT_HOST);
        int port = intProperty("milvus.port", DEFAULT_PORT);
        String username = property("milvus.username", "");
        String password = property("milvus.password", "");
        String databaseName = property("milvus.database", DEFAULT_DATABASE);
        return new MilvusConfig(host, port, username, password, databaseName);
    }

    public ConnectParam toConnectParam() {
        ConnectParam.Builder builder = ConnectParam.newBuilder()
                .withHost(host)
                .withPort(port);
        if (isNotBlank(username)) {
            builder.withAuthorization(username, password);
        }
        if (isNotBlank(databaseName)) {
            builder.withDatabaseName(databaseName);
        }
        return builder.build();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    private static String property(String key, String defaultValue) {
        String value = PropertyUtil.getProperty(key, defaultValue);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    private static int intProperty(String key, int defaultValue) {
        String value = PropertyUtil.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
