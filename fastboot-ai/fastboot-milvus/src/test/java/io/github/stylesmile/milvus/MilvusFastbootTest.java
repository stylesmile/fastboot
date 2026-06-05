package io.github.stylesmile.milvus;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.test.annotation.FastBootTest;
import io.github.stylesmile.test.runner.FastbootTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.stylesmile.test.util.AssertUtils.assertNotNull;

/**
 * Tests Milvus plugin registration with fastboot-test.
 */
@RunWith(FastbootTestRunner.class)
@FastBootTest(value = MilvusTestApplication.class, webEnvironment = false)
public class MilvusFastbootTest {

    @Test
    public void testMilvusBeansRegisteredInFastbootContext() {
        assertNotNull("MilvusConfig should be registered", BeanContainer.getInstance(MilvusConfig.class));
        assertNotNull("MilvusTemplate should be registered", BeanContainer.getInstance(MilvusTemplate.class));
        assertNotNull("MilvusOperations should be registered", BeanContainer.getInstance(MilvusOperations.class));
    }
}
