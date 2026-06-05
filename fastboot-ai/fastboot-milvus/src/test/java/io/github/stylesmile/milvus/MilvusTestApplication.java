package io.github.stylesmile.milvus;

import io.github.stylesmile.annotation.Fastboot;

/**
 * Test application for fastboot-test context.
 */
@Fastboot(scanPackage = "io.github.stylesmile.milvus")
public class MilvusTestApplication {

    public static void main(String[] args) {
        io.github.stylesmile.app.App.start(MilvusTestApplication.class, args);
    }
}
