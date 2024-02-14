package io.github.stylesmile.file;


import io.github.stylesmile.tool.IoUtil;

import java.io.*;

/**
 *
 */
public class DownloadedFile {
    /**
     * 内容类型（有些地方会动态构建，所以不能只读）
     */
    private String contentType;
    /**
     * 内容大小
     */
    private long contentSize;
    /**
     * 内容流
     */
    private InputStream content;
    /**
     * 文件名（带扩展名，例：demo.jpg）
     */
    private String name;
    /**
     * 是否附件（即下载模式）
     */
    private boolean attachment = true;

    /**
     * 是否附件输出
     */
    public boolean isAttachment() {
        return attachment;
    }

    /**
     * 作为附件输出
     */
    public DownloadedFile asAttachment(boolean attachment) {
        this.attachment = attachment;
        return this;
    }

    /**
     * 内容类型
     */
    public String getContentType() {
        return contentType;
    }


    /**
     * 内容大小
     */
    public long getContentSize() {
        if (contentSize > 0) {
            return contentSize;
        } else {
            try {
                return content.available();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 内容流
     */
    public InputStream getContent() {
        return content;
    }

    /**
     * 文件名（带扩展名，例：demo.jpg）
     */
    public String getName() {
        return name;
    }

    public DownloadedFile() {

    }

    /**
     * 构造函数
     *
     * @param contentType 内容类型
     * @param contentSize 内容大小
     * @param content     内容流
     * @param name        文件名
     */
    public DownloadedFile(String contentType, long contentSize, InputStream content, String name) {
        this.contentType = contentType;
        this.contentSize = contentSize;
        this.content = content;
        this.name = name;
    }

    /**
     * 构造函数
     *
     * @param contentType 内容类型
     * @param content     内容流
     * @param name        文件名
     */
    public DownloadedFile(String contentType, InputStream content, String name) {
        this(contentType, 0, content, name);
    }

    /**
     * 构造函数
     *
     * @param contentType 内容类型
     * @param content     内容流
     * @param name        文件名
     */
    public DownloadedFile(String contentType, byte[] content, String name) {
        this.contentType = contentType;
        this.contentSize = content.length;
        this.content = new ByteArrayInputStream(content);
        this.name = name;
    }

    /**
     * 构造函数
     *
     * @param file 文件
     * @throws FileNotFoundException
     */
    public DownloadedFile(File file) throws FileNotFoundException {
        this(file, file.getName());
    }

    /**
     * 构造函数
     *
     * @param file 文件
     * @param name 名字
     * @throws FileNotFoundException
     * @since 2.5
     */
    public DownloadedFile(File file, String name) throws FileNotFoundException {
        this(file, name, "application/octet-stream;");
    }

    public DownloadedFile(File file, String name, String contentType) throws FileNotFoundException {
        this.contentType = contentType;
        this.contentSize = file.length();
        this.content = new FileInputStream(file);
        this.name = name;
    }

    /**
     * 将内容流迁移到目标文件
     *
     * @param file 目标文件
     */
    public void transferTo(File file) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            IoUtil.transferTo(content, stream);
        }
    }

    /**
     * 将内容流迁移到目标输出流
     *
     * @param stream 目标输出流
     */
    public void transferTo(OutputStream stream) throws IOException {
        IoUtil.transferTo(content, stream);
    }
}
