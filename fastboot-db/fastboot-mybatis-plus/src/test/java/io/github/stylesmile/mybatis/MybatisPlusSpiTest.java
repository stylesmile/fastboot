package io.github.stylesmile.mybatis;

import io.github.stylesmile.plugin.Plugin;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Verify SPI registration for MybatisPlusPlugin.
 */
public class MybatisPlusSpiTest {

    @Test
    public void testSpiFileExists() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/services/io.github.stylesmile.plugin.Plugin");
        assertNotNull("SPI file for Plugin should exist", is);
        String content = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
        assertTrue("SPI file should contain MybatisPlusPlugin",
                content.contains("io.github.stylesmile.mybatis.MybatisPlusPlugin"));
    }
}