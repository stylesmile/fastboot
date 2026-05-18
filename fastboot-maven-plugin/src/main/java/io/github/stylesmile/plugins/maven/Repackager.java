package io.github.stylesmile.plugins.maven;


import io.github.stylesmile.annotation.Fastboot;
import io.github.stylesmile.plugins.maven.tools.tool.*;
import javassist.ClassPool;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static io.github.stylesmile.plugins.maven.tools.Constant.*;


/**
 * JAR 重新打包器。
 * <p>
 * 该类负责将普通的 JAR 文件重新打包为可执行的 Fat JAR，包括：
 * <ul>
 *     <li>自动检测并配置启动类（通过 @Fastboot 注解）</li>
 *     <li>嵌入所有依赖库到 JAR 文件中</li>
 *     <li>生成正确的 MANIFEST.MF 文件</li>
 *     <li>重命名应用类以避免与依赖冲突</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>{@code
 * File sourceJar = new File("myapp.jar");
 * Repackager repackager = new Repackager(sourceJar, logger);
 * repackager.repackage(new File("myapp-exec.jar"), libraries);
 * }</pre>
 *
 * @author hxm
 * @since 2.10.0
 */
public class Repackager {

    /**
     * ZIP 文件头标识字节（PK\x03\x04）。
     */
    private static final byte[] ZIP_FILE_HEADER = new byte[]{'P', 'K', 3, 4};

    /**
     * 源 JAR 文件。
     */
    private final File source;

    /**
     * JAR 布局策略，决定类和库的存放位置。
     */
    private Layout layout;
    
    /**
     * Maven 日志记录器。
     */
    private Log logger;

    /**
     * 创建 Repackager 实例。
     *
     * @param source 源 JAR 文件，必须存在且为文件类型
     * @param logger Maven 日志记录器
     * @throws IllegalArgumentException 如果源文件为空或不存在
     */
    public Repackager(File source, Log logger) {
        this.logger = logger;
        if (source == null) {
            throw new IllegalArgumentException("Source file must be provided");
        }
        if (!source.exists() || !source.isFile()) {
            throw new IllegalArgumentException("Source must refer to an existing file, "
                    + "got " + source.getAbsolutePath());
        }
        this.source = source.getAbsoluteFile();
    }

    /**
     * 重新打包 JAR 文件，使其可以通过 'java -jar' 命令直接运行。
     * <p>
     * 该方法会：
     * <ol>
     *     <li>验证目标文件和依赖库参数</li>
     *     <li>检查是否已经重新打包过</li>
     *     <li>备份原始文件（如果需要）</li>
     *     <li>执行实际的重新打包操作</li>
     * </ol>
     *
     * @param destination 目标文件（可以与源文件相同）
     * @param libraries   运行所需的依赖库列表
     * @throws IOException 如果重新打包失败
     */
    public void repackage(File destination, Libraries libraries) throws IOException {
        if (destination == null || destination.isDirectory()) {
            throw new IllegalArgumentException("Invalid destination");
        }
        if (libraries == null) {
            throw new IllegalArgumentException("Libraries must not be null");
        }
        if (this.layout == null) {
            this.layout = getLayoutFactory().getLayout(this.source);
        }
        if (alreadyRepackaged()) {
            return;
        }
        destination = destination.getAbsoluteFile();
        File workingSource = this.source;
        if (this.source.equals(destination)) {
            workingSource = getBackupFile();
            workingSource.delete();
            renameFile(this.source, workingSource);
        }
        destination.delete();
        JarFile jarFileSource = new JarFile(workingSource);
        try {
            repackage(jarFileSource, destination, libraries);
        } finally {
            jarFileSource.close();
        }
    }

    /**
     * 获取布局工厂实例。
     * <p>
     * 默认使用 {@link DefaultLayoutFactory}，可以根据需要扩展自定义布局策略。
     *
     * @return 布局工厂实例
     */
    private LayoutFactory getLayoutFactory() {
        return new DefaultLayoutFactory();
    }

    /**
     * 返回用于备份原始源文件的文件对象。
     * <p>
     * 备份文件名为原文件名 + ".original" 后缀，位于同一目录下。
     *
     * @return 备份文件的 File 对象
     */
    public final File getBackupFile() {
        return new File(this.source.getParentFile(), this.source.getName() + ".original");
    }

    /**
     * 检查源文件是否已经被重新打包过。
     * <p>
     * 通过检查 MANIFEST.MF 中是否存在特定的标记属性来判断。
     *
     * @return 如果已经重新打包过则返回 true
     * @throws IOException 如果读取 JAR 文件失败
     */
    private boolean alreadyRepackaged() throws IOException {
        JarFile jarFile = new JarFile(this.source);
        try {
            Manifest manifest = jarFile.getManifest();
            return (manifest != null && manifest.getMainAttributes()
                    .getValue(JAR_TOOL_VALUE) != null);
        } finally {
            jarFile.close();
        }
    }

    private void repackage(JarFile sourceJar, File destination, Libraries libraries) throws IOException {
        JarWriter writer = new JarWriter(destination);
        try {
            final List<Library> unpackLibraries = new ArrayList<>();
            final List<Library> standardLibraries = new ArrayList<>();
            libraries.doWithLibraries(library -> {
                File file = library.getFile();
                if (isZip(file)) {
                    if (library.isUnpackRequired()) {
                        unpackLibraries.add(library);
                    } else {
                        standardLibraries.add(library);
                    }
                }
            });
            repackage(sourceJar, writer, unpackLibraries, standardLibraries);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                // Ignore
            }
        }
    }

    private void repackage(JarFile sourceJar, JarWriter writer,
                           final List<Library> unpackLibraries, final List<Library> standardLibraries)
            throws IOException {
        writer.writeManifest(buildManifest(sourceJar));
        Set<String> seen = new HashSet<>();
        writeNestedLibraries(unpackLibraries, seen, writer);
        if (this.layout instanceof RepackagingLayout) {
            writer.writeEntries(sourceJar, new RenamingEntryTransformer(
                    ((RepackagingLayout) this.layout).getRepackagedClassesLocation()));
        } else {
            writer.writeEntries(sourceJar);
        }
        writeNestedLibraries(standardLibraries, seen, writer);
    }

    private void writeNestedLibraries(List<Library> libraries, Set<String> alreadySeen,
                                      JarWriter writer) throws IOException {
        for (Library library : libraries) {
            String destination = Repackager.this.layout
                    .getLibraryDestination(library.getName(), library.getScope());
            if (destination != null) {
                if (!alreadySeen.add(destination + library.getName())) {
                    throw new IllegalStateException(
                            "Duplicate library " + library.getName());
                }
                writer.writeNestedLibrary(destination, library);
            }
        }
    }

    private boolean isZip(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                return isZip(fileInputStream);
            } finally {
                fileInputStream.close();
            }
        } catch (IOException ex) {
            return false;
        }
    }

    private boolean isZip(InputStream inputStream) throws IOException {
        for (int i = 0; i < ZIP_FILE_HEADER.length; i++) {
            if (inputStream.read() != ZIP_FILE_HEADER[i]) {
                return false;
            }
        }
        return true;
    }

    private Manifest buildManifest(JarFile source) throws IOException {
        Manifest manifest = source.getManifest();
        if (manifest == null) {
            manifest = new Manifest();
        }
        manifest = new Manifest(manifest);
        manifest.getMainAttributes().putValue("Manifest-Version", "1.0");
//        manifest.getMainAttributes().putValue(JAR_TOOL, JAR_TOOL_VALUE);
        manifest.getMainAttributes().putValue(Main_Class, getStartClass());
//        manifest.getMainAttributes().putValue(START_CLASS, getStartClass());
//        manifest.getMainAttributes().putValue("Main-Class", MAIN_CLASS);
        return manifest;
    }


    /**
     * 扫描 JAR 文件中的所有类，查找带有 @Fastboot 注解的启动类。
     * <p>
     * 该方法会：
     * <ol>
     *     <li>加载备份的 JAR 文件</li>
     *     <li>遍历所有 .class 文件</li>
     *     <li>检查每个类是否有 @Fastboot 注解</li>
     *     <li>返回第一个找到的启动类全限定名</li>
     * </ol>
     *
     * @return 启动类的全限定名
     * @throws IOException 如果找不到启动类或读取 JAR 失败
     * @throws IllegalStateException 如果没有找到带 @Fastboot 注解的类
     */
    private String getStartClass() throws IOException {
        ClassPool pool = ClassPool.getDefault();
        File f = getBackupFile();
        URL url1 = f.toURI().toURL();
        URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());
        JarFile jar = new JarFile(f);
        Enumeration<JarEntry> enumFiles = jar.entries();
        while (enumFiles.hasMoreElements()) {
            JarEntry entry = enumFiles.nextElement();
            final String classFullName = entry.getName();
            if (classFullName.endsWith(".class")) {
                try {
                    String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
                    Class<?> myclass = myClassLoader.loadClass(className);
                    System.out.println("classFullName0: " + classFullName);
                    System.out.println("className0: " + className);
                    Fastboot fastboot = myclass.getAnnotation(Fastboot.class);
                    if (fastboot != null) {
                        logger.info("启动类：" + myclass);
                        return myclass.getName();
                    }
//                    if (fastboot.toString().equals("@io.github.stylesmile.annotation.Fastboot()")) {
//                        logger.info("启动类：" + myclass);
//                        return myclass.getName();
//                    }
//                    for (Method method : myclass.getMethods()) {
//                        System.out.println("classFullName2: " + classFullName);
//                        System.out.println("method: " + method.getName());
//                        if (method.getName().equals("main") && method.getParameterTypes().length == 1) {
//                            System.out.println("method2: " + method.getName());
//                            if (method.getParameterTypes()[0].equals(String[].class) && Modifier.isStatic(method.getModifiers())) {
//                                System.out.println("classFullName: " + classFullName);
//                                System.out.println("method3: " + method.getName());
//                                if (method.getReturnType().getName().equals("void")) {
//                                    System.out.println("classFullName: " + classFullName);
//                                    System.out.println("method4: " + method.getName());
////                                    CtClass ctClass = pool.makeClass(jar.getInputStream(entry));
//                                    for (Object annotation : myclass.getAnnotations()) {
////                                        if (annotation.toString().endsWith("HServerBoot")) {
//                                        System.out.println("annotation : " + annotation.toString());
//                                        if (annotation.toString().equals("@io.github.stylesmile.annotation.Fastboot()")) {
//                                            logger.info("启动类：" + myclass);
//                                            return myclass.getName();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                } catch (Throwable ignored) {
                }
            }
        }
        jar.close();
        myClassLoader.close();
        throw new IllegalStateException("找不到启动类,请使用@Fastboot标记你的启动类");
    }

    private static Class<?> loadClass(String fullClzName) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(fullClzName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void renameFile(File file, File dest) {
        if (!file.renameTo(dest)) {
            throw new IllegalStateException(
                    "Unable to rename '" + file + "' to '" + dest + "'");
        }
    }

    private void deleteFile(File file) {
        if (!file.delete()) {
            throw new IllegalStateException("Unable to delete '" + file + "'");
        }
    }

    /**
     * An {@code EntryTransformer} that renames entries by applying a prefix.
     */
    private static final class RenamingEntryTransformer implements JarWriter.EntryTransformer {

        private final String namePrefix;

        private RenamingEntryTransformer(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public JarEntry transform(JarEntry entry) {
            if (entry.getName().equals("META-INF/INDEX.LIST")) {
                return null;
            }
            if ((entry.getName().startsWith("META-INF/")
                    && !entry.getName().equals("META-INF/aop.xml"))
                    || entry.getName().startsWith("BOOT-INF/")) {
                return entry;
            }
            JarEntry renamedEntry = new JarEntry(this.namePrefix + entry.getName());
            renamedEntry.setTime(entry.getTime());
            renamedEntry.setSize(entry.getSize());
            renamedEntry.setMethod(entry.getMethod());
            if (entry.getComment() != null) {
                renamedEntry.setComment(entry.getComment());
            }
            renamedEntry.setCompressedSize(entry.getCompressedSize());
            renamedEntry.setCrc(entry.getCrc());
            setCreationTimeIfPossible(entry, renamedEntry);
            if (entry.getExtra() != null) {
                renamedEntry.setExtra(entry.getExtra());
            }
            setLastAccessTimeIfPossible(entry, renamedEntry);
            setLastModifiedTimeIfPossible(entry, renamedEntry);
            return renamedEntry;
        }

        private void setCreationTimeIfPossible(JarEntry source, JarEntry target) {
            try {
                if (source.getCreationTime() != null) {
                    target.setCreationTime(source.getCreationTime());
                }
            } catch (NoSuchMethodError ex) {
                // Not running on Java 8. Continue.
            }
        }

        private void setLastAccessTimeIfPossible(JarEntry source, JarEntry target) {
            try {
                if (source.getLastAccessTime() != null) {
                    target.setLastAccessTime(source.getLastAccessTime());
                }
            } catch (NoSuchMethodError ex) {
                // Not running on Java 8. Continue.
            }
        }

        private void setLastModifiedTimeIfPossible(JarEntry source, JarEntry target) {
            try {
                if (source.getLastModifiedTime() != null) {
                    target.setLastModifiedTime(source.getLastModifiedTime());
                }
            } catch (NoSuchMethodError ex) {
                // Not running on Java 8. Continue.
            }
        }


    }

}
