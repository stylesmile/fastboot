package io.github.stylesmile.plugins.maven;


import java.io.*;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author hxm
 */
public class CopyLoader {
    static String path;
    static String name;
    static String tempName;
    static String fastbootName = "fastboot.jar";

    public static void start(File file) throws Exception {
        CopyLoader.name = file.getName();
        CopyLoader.tempName = UUID.randomUUID().toString() + ".jar";
        CopyLoader.fastbootName = UUID.randomUUID().toString() + ".jar";
        CopyLoader.path = (file.getAbsolutePath().replace(name, ""));
        getHServerJar();
        setLoader();
        organizeFiles();
    }


    private static void organizeFiles() {
        new File(path + name).delete();
        new File(path + fastbootName).delete();
        new File(path + tempName).renameTo(new File(path + name));

    }

    private static void setLoader() throws IOException {
        JarFile jarfile = new JarFile(path + fastbootName);
        JarFile targetJarfile = new JarFile(path + name);
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(path + tempName));
        //写loader 文件
        Enumeration<JarEntry> entries = jarfile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (!jarEntry.isDirectory() && jarEntry.getName().contains("top/hserver/core/loader/") && jarEntry.getName().endsWith(".class")) {
                jarOutputStream.putNextEntry(jarEntry);
                byte[] bytes = toByteArray(jarfile.getInputStream(jarEntry));
                jarOutputStream.write(bytes);
            }
        }
        //写原文件
        Enumeration<JarEntry> targetEntries = targetJarfile.entries();
        while (targetEntries.hasMoreElements()) {
            JarEntry entry = targetEntries.nextElement();
            InputStream entryInputStream = targetJarfile
                    .getInputStream(entry);
            jarOutputStream.putNextEntry(entry);
            jarOutputStream.write(toByteArray(entryInputStream));
        }
        jarOutputStream.flush();
        jarOutputStream.close();
        targetJarfile.close();
        jarfile.close();
    }

    private static void getHServerJar() throws Exception {
        JarFile jarfile = new JarFile(path + name);
        Enumeration<JarEntry> entries = jarfile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.getName().endsWith(".jar") || jarEntry.getName().startsWith("lib/fastboot")) {
                InputStream inputStream = jarfile.getInputStream(jarEntry);
                writeToLocal(path + fastbootName, inputStream);
                break;
            }
        }
        jarfile.close();

    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    private static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }

}
