package io.github.stylesmile.staticfile;

import io.github.stylesmile.filter.Filter;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;
import io.github.stylesmile.tool.StringUtil;

import java.net.URI;

public class StaticFileFilter implements Filter {

    @Override
    public boolean preHandle(Request request, Response response) {
        URI uri = request.getURI();
        if ("/favicon.ico".equals(uri.toString())) {
            return true;
        }
        System.out.println(uri);
        String path = StaticFilePluginImp.get(uri.getPath());
        if (StringUtil.isNotEmpty(path)) {
            System.out.printf(path);
        }
        ;
        String contentType = request.getHeaders().get("Content-Type");
        System.out.println(contentType);

        return false;
    }

    @Override
    public boolean afterCompletion(Request request, Response response) {
        return true;
    }

}
