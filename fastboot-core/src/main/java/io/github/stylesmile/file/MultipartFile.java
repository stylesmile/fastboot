package io.github.stylesmile.file;


import io.github.stylesmile.http.HttpHeaderCollection;
import io.github.stylesmile.tool.IoUtil;
import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
public class MultipartFile {
    public String extension;
    public String filename;
    public String contentType;
    public HttpHeaderCollection headers;
    /**     * 输入流
     */
    public InputStream body;
    public Long size;

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
        IoUtil.transferTo(this.body, fileOutputStream);
    }
}
