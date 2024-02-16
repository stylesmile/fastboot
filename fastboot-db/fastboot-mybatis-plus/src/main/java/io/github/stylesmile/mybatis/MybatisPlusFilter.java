package io.github.stylesmile.mybatis;

import io.github.stylesmile.annotation.Service;
import io.github.stylesmile.filter.Filter;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;

@Service
public class MybatisPlusFilter implements Filter {

    @Override
    public boolean preHandle(Request request, Response response) {
        return true;
    }

    @Override
    public boolean afterCompletion(Request request, Response response) {
        MybatisPlusPlugin.getSession().commit();
        return true;
    }
}
