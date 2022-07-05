package com.example.demo.file;


import com.example.demo.tool.FileTransferUtil;

import java.io.*;

/**
 * 下载的文件模型
 *
 * @author noear
 * @since 1.5
 */
public class DownloadedFile {
    /**
     * 内容类型（有些地方会动态构建，所以不能只读）
     */
    public String contentType;
    /**
     * 内容流
     */
    public InputStream content;
    /**
     * 文件名（带扩展名，例：demo.jpg）
     */
    public String name;

    public DownloadedFile(){

    }

    /**
     * 基于下载输出的构建函数
     *
     * @param contentType 内容类型
     * @param content 内容流
     * @param name 文件名
     * */
    public DownloadedFile(String contentType, InputStream content, String name) {
        this.contentType = contentType;
        this.content = content;
        this.name = name;
    }

    public DownloadedFile(String contentType, byte[] content, String name) {
        this.contentType = contentType;
        this.content = new ByteArrayInputStream(content);
        this.name = name;
    }

    /**
     * 将内容流迁移到..
     *
     * @param file 文件
     * */
    public void transferTo(File file) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            FileTransferUtil.transferTo(content, stream);
        }
    }

    /**
     * 将内容流迁移到..
     *
     * @param stream 输出流
     * */
    public void transferTo(OutputStream stream) throws IOException {
        FileTransferUtil.transferTo(content, stream);
    }
}
