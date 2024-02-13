package io.github.stylesmile.staticfile;

import io.github.stylesmile.filter.Filter;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;
import io.github.stylesmile.tool.StringUtil;

import java.io.*;
import java.net.URI;

public class StaticFileFilter implements Filter {

    @Override
    public boolean preHandle(Request request, Response response) {
        URI uri = request.getURI();
        if ("/favicon.ico".equals(uri.toString())) {
            return true;
        }
        String path = StaticFilePluginImp.get(uri.getPath());
        if (StringUtil.isNotEmpty(path)) {
            File file = new File(path);
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                response.sendHtml(200,buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean afterCompletion(Request request, Response response) {
        return true;
    }

}
