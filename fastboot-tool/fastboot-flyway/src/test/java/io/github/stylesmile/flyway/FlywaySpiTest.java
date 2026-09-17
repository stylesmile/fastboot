package io.github.stylesmile.flyway;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Verify SPI registration for FlywayPlugin.
 */
public class FlywaySpiTest {

    @Test
    public void testSpiFileExists() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/services/io.github.stylesmile.plugin.Plugin");
        assertNotNull("SPI file for Plugin should exist", is);
        String content = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
        assertTrue("SPI file should contain FlywayPlugin",
                content.contains("io.github.stylesmile.flyway.FlywayPlugin"));
    }
}
