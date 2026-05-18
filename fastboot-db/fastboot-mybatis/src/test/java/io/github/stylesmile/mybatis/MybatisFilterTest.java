package io.github.stylesmile.mybatis;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Service;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MybatisFilter.
 */
public class MybatisFilterTest {

    @Test
    public void testClassHasServiceAnnotation() {
        assertTrue("MybatisFilter should be annotated with @Service",
                MybatisFilter.class.isAnnotationPresent(Service.class));
    }
}