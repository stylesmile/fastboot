package com.example.demo.test;

import com.example.demo.annotation.Controller;
import com.example.demo.annotation.RequestMapping;
import com.example.demo.file.UploadedFile;

@Controller
public class FileController {
    /**
     * 计算
     *
     * @param uploadedFile 文件
     */
    @RequestMapping("/file")
    public Integer getSalary(UploadedFile uploadedFile) {
        System.out.println("name: " + uploadedFile.name);
        System.out.println("size: " + uploadedFile.contentSize);
        return 1;
    }
}
