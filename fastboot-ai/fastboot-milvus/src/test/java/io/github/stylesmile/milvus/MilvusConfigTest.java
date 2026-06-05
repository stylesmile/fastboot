package io.github.stylesmile.milvus;

import io.github.stylesmile.tool.PropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Unit tests for MilvusConfig.
 */
public class MilvusConfigTest {

    private Properties oldProps;

    @Before
    public void setUp() {
        oldProps = PropertyUtil.props;
        PropertyUtil.props = new Properties();
    }

    @After
    public void tearDown() {
        PropertyUtil.props = oldProps;
    }

    @Test
    public void testDefaultConnectionConfig() {
        MilvusConfig config = MilvusConfig.load();

        assertEquals("localhost", config.getHost());
        assertEquals(19530, config.getPort());
        assertEquals("default", config.getDatabaseName());
        assertEquals("", config.getUsername());
        assertEquals("", config.getPassword());
    }

    @Test
    public void testCustomConnectionConfig() {
        PropertyUtil.props.setProperty("milvus.host", "192.168.1.10");
        PropertyUtil.props.setProperty("milvus.port", "19531");
        PropertyUtil.props.setProperty("milvus.database", "fastboot_ai");
        PropertyUtil.props.setProperty("milvus.username", "root");
        PropertyUtil.props.setProperty("milvus.password", "milvus");

        MilvusConfig config = MilvusConfig.load();

        assertEquals("192.168.1.10", config.getHost());
        assertEquals(19531, config.getPort());
        assertEquals("fastboot_ai", config.getDatabaseName());
        assertEquals("root", config.getUsername());
        assertEquals("milvus", config.getPassword());
    }

    @Test
    public void testInvalidPortUsesDefaultPort() {
        PropertyUtil.props.setProperty("milvus.port", "not-a-number");

        MilvusConfig config = MilvusConfig.load();

        assertEquals(19530, config.getPort());
    }
}
