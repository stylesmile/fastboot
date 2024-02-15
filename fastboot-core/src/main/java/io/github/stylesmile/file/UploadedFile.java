package io.github.stylesmile.file;


import io.github.stylesmile.tool.IoUtil;

import java.io.*;

/**
 * 上传文件模型（例：通过http form-data 上传的文件）
 *
 * @Controller
 * public class HelloController{
 *     @RequestMapping("/update/")
 *     public String update(UploadedFile file){
 *         return "I receive file ：" + file.name;
 *     }
 * }
 * </code></pre>
 *
 * */
public class UploadedFile extends DownloadedFile {

    /**
     * 扩展名（例：zip ,doc,jar）
     */
    private String extension;

    /**
     * 扩展名（例：jpg）
     */
    public String getExtension() {
        return extension;
    }

    public UploadedFile() {
        super();
    }

    /**
     * 基于下载输出的构建函数
     *
     * @param contentType 内容类型
     * @param content     内容流
     * @param name        文件名
     */
    public UploadedFile(String contentType, InputStream content, String name) {
        super(contentType, content, name);
    }

    /**
     * 基于上传输入的构建函数
     *
     * @param contentType 内容类型
     * @param contentSize 内容大小
     * @param content     内容流
     * @param name        文件名
     * @param extension   文件后缀名
     */
    public UploadedFile(String contentType, long contentSize, InputStream content, String name, String extension) {
        super(contentType, contentSize, content, name);
        this.extension = extension;
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() throws IOException {
        return getContentSize() == 0L;
    }
    /**
     * file save
     *
     * @param path     file all path(文件全路径)
     * @throws IOException e
     */
    public void save(String path) throws IOException {
        File file = new File(path);
        // If the parent folder does not exist, create
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        IoUtil.transferTo(this.getContent(), fileOutputStream);
    }
}
