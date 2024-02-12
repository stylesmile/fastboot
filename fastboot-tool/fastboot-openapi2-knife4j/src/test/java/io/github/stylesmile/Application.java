package io.github.stylesmile;


import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.app.App;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;
import io.github.stylesmile.web.ModelAndView;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 测试入口类
 *
 * @author stylesmile
 */
@Controller
public class Application {

    public static void main(String[] args) {
        App.start(Application.class, args);
    }

    @RequestMapping("/3")
    public ModelAndView api3(Request request, Response response) throws IOException {
        String filePath = "C:\\Users\\32398\\Desktop\\openapi2\\doc.html";
        String strHtml = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<title></title>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\thello world!~\n" +
                "\t</body>\n" +
                "</html>";
        // 直接输出到 String 变量
//        String str = template.renderToString();
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(strHtml.getBytes(StandardCharsets.UTF_8));
        return new ModelAndView();
    }
}
