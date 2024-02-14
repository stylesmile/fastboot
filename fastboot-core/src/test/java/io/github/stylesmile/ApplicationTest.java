package io.github.stylesmile;


import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.annotation.RequestParam;
import io.github.stylesmile.app.App;
import io.github.stylesmile.file.MultipartFile;
import io.github.stylesmile.tool.IoUtil;
import org.junit.Test;

import java.io.*;

/**
 * 测试入口类
 *
 * @author stylesmile
 */
@Controller
public class ApplicationTest {
    public static void main(String[] args) {
        App.start(ApplicationTest.class, args);
    }

    public void test() {
        App.start(ApplicationTest.class, new String[0]);

    }

    @RequestMapping("/1")
    public String test(MultipartFile multipartFile) throws IOException {
        multipartFile.saveLocal("d://test//" + multipartFile.getFilename());
        return "success~";
    }
    @RequestMapping("/11")
    public String test(String username) throws IOException {
        return username;
    }

}
