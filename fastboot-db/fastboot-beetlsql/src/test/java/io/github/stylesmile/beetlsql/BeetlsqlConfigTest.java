package io.github.stylesmile.beetlsql;

import org.junit.Test;

/**
 * Unit tests for BeetlsqlConfig.
 */
public class BeetlsqlConfigTest {

    @Test(expected = Exception.class)
    public void testGetSQLManagerWithoutProperties() {
        // Without setting db properties, HikariCP will fail
        BeetlsqlConfig.getSQLManager();
    }
}