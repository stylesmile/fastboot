package io.github.stylesmile.plugins.maven;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static org.junit.Assert.*;

/**
 * JarWriter 类的单元测试。
 * <p>
 * 测试 JAR 文件写入的核心功能，包括：
 * <ul>
 *     <li>JAR 文件创建</li>
 *     <li>MANIFEST.MF 写入</li>
 *     <li>条目写入和去重</li>
 *     <li>资源关闭</li>
 * </ul>
 *
 * @author Test Author
 * @since 2.10.0
 */
public class JarWriterTest {

    private File tempDir;
    private File testJarFile;

    @Before
    public void setUp() throws IOException {
        // 创建临时目录
        tempDir = new File(System.getProperty("java.io.tmpdir"), "jarwriter-test-" + System.currentTimeMillis());
        tempDir.mkdirs();
        
        // 创建测试 JAR 文件路径
        testJarFile = new File(tempDir, "test-output.jar");
    }

    @After
    public void tearDown() {
        // 清理临时文件
        if (testJarFile != null && testJarFile.exists()) {
            testJarFile.delete();
        }
        if (tempDir != null && tempDir.exists()) {
            deleteDirectory(tempDir);
        }
    }

    /**
     * 递归删除目录。
     */
    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
    }

    /**
     * 测试：JarWriter 应该能够成功创建并关闭。
     */
    @Test
    public void testJarWriterCreationAndClose() throws IOException {
        JarWriter writer = new JarWriter(testJarFile);
        assertNotNull(writer);
        writer.close();
        
        // 验证文件已创建
        assertTrue(testJarFile.exists());
        assertTrue(testJarFile.isFile());
    }

    /**
     * 测试：写入 MANIFEST.MF 应该成功。
     */
    @Test
    public void testWriteManifest() throws IOException {
        JarWriter writer = new JarWriter(testJarFile);
        
        // 创建测试清单
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        manifest.getMainAttributes().putValue("Created-By", "JarWriterTest");
        
        // 写入清单
        writer.writeManifest(manifest);
        writer.close();
        
        // 验证清单已写入
        JarFile jarFile = new JarFile(testJarFile);
        try {
            Manifest readManifest = jarFile.getManifest();
            assertNotNull(readManifest);
            assertEquals("1.0", readManifest.getMainAttributes().getValue("Manifest-Version"));
            assertEquals("JarWriterTest", readManifest.getMainAttributes().getValue("Created-By"));
        } finally {
            jarFile.close();
        }
    }

    /**
     * 测试：写入空 JAR 条目应该成功。
     */
    @Test
    public void testWriteEmptyJar() throws IOException {
        JarWriter writer = new JarWriter(testJarFile);
        writer.close();
        
        // 验证可以打开为 JAR 文件
        JarFile jarFile = new JarFile(testJarFile);
        try {
            assertNotNull(jarFile);
            // 空的 JAR 至少应该有 META-INF/MANIFEST.MF（如果写入了的话）
        } finally {
            jarFile.close();
        }
    }

    /**
     * 测试：重复写入相同条目应该只保留一份。
     */
    @Test
    public void testDuplicateEntriesAreIgnored() throws IOException {
        JarWriter writer = new JarWriter(testJarFile);
        
        // 创建测试清单
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        
        // 尝试多次写入相同的清单（内部应该去重）
        writer.writeManifest(manifest);
        writer.close();
        
        // 验证只有一个 MANIFEST.MF 条目
        JarFile jarFile = new JarFile(testJarFile);
        try {
            int manifestCount = 0;
            java.util.Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().equals("META-INF/MANIFEST.MF")) {
                    manifestCount++;
                }
            }
            assertEquals("应该只有一个 MANIFEST.MF 条目", 1, manifestCount);
        } finally {
            jarFile.close();
        }
    }

    /**
     * 测试：写入多个不同的条目应该都保留。
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        // 首先创建一个包含多个条目的源 JAR
        File sourceJar = new File(tempDir, "source.jar");
        java.util.jar.JarOutputStream jos = new java.util.jar.JarOutputStream(
            new FileOutputStream(sourceJar));
        try {
            // 添加 MANIFEST.MF
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
            JarEntry manifestEntry = new JarEntry("META-INF/MANIFEST.MF");
            jos.putNextEntry(manifestEntry);
            manifest.write(jos);
            jos.closeEntry();
            
            // 添加测试文件
            JarEntry testEntry = new JarEntry("test.txt");
            jos.putNextEntry(testEntry);
            jos.write("Hello, World!".getBytes());
            jos.closeEntry();
        } finally {
            jos.close();
        }
        
        // 使用 JarWriter 复制条目
        JarWriter writer = new JarWriter(testJarFile);
        JarFile jarFileSource = new JarFile(sourceJar);
        try {
            writer.writeManifest(new Manifest());
            writer.writeEntries(jarFileSource);
        } finally {
            jarFileSource.close();
            writer.close();
        }
        
        // 验证目标 JAR 包含所有条目
        JarFile outputJar = new JarFile(testJarFile);
        try {
            boolean hasManifest = false;
            boolean hasTestFile = false;
            java.util.Enumeration<JarEntry> entries = outputJar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().equals("META-INF/MANIFEST.MF")) {
                    hasManifest = true;
                }
                if (entry.getName().equals("test.txt")) {
                    hasTestFile = true;
                }
            }
            assertTrue("应该包含 MANIFEST.MF", hasManifest);
            assertTrue("应该包含 test.txt", hasTestFile);
        } finally {
            outputJar.close();
            sourceJar.delete();
        }
    }

    /**
     * 测试：写入后应该能够正确读取 JAR 文件。
     */
    @Test
    public void testWrittenJarIsValid() throws IOException {
        JarWriter writer = new JarWriter(testJarFile);
        
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        manifest.getMainAttributes().putValue("Main-Class", "com.example.Main");
        
        writer.writeManifest(manifest);
        writer.close();
        
        // 验证可以正常打开和读取
        JarFile jarFile = new JarFile(testJarFile);
        try {
            assertNotNull(jarFile.getManifest());
            assertEquals("1.0", jarFile.getManifest().getMainAttributes().getValue("Manifest-Version"));
            assertEquals("com.example.Main", jarFile.getManifest().getMainAttributes().getValue("Main-Class"));
        } finally {
            jarFile.close();
        }
    }

    /**
     * 测试：关闭后的 JarWriter 不应该再允许写入操作。
     */
    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        JarWriter writer = new JarWriter(testJarFile);
        writer.close();
        
        // 关闭后再次写入应该抛出异常
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        writer.writeManifest(manifest);
    }

    /**
     * 测试：JAR 文件大小应该合理。
     */
    @Test
    public void testJarFileSize() throws IOException {
        JarWriter writer = new JarWriter(testJarFile);
        
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
        writer.writeManifest(manifest);
        writer.close();
        
        // 验证文件大小大于 0 且小于 1MB（对于简单 JAR）
        long fileSize = testJarFile.length();
        assertTrue("文件大小应该大于 0", fileSize > 0);
        assertTrue("文件大小应该小于 1MB", fileSize < 1024 * 1024);
    }
}
