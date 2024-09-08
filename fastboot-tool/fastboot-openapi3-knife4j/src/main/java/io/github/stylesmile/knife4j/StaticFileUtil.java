package io.github.stylesmile.knife4j;

import io.github.stylesmile.filter.FilterManager;
import io.github.stylesmile.staticfile.ResourceUtil;
import io.github.stylesmile.staticfile.StaticFileFilter;
import io.github.stylesmile.staticfile.StaticFilePluginImp;

import java.net.URL;
import java.util.List;

public class StaticFileUtil {
    public static final String DEFAULT_STATIC_LOCATION = "static/";
    public void init() {
        // 扫描静态文件
//        StaticFilePluginImp.findFile(DEFAULT_STATIC_LOCATION);
//        List<URL> urlList1 = ResourceUtil.getAllResourceFiles("img/");
//        List<URL> urlList2 = ResourceUtil.getAllResourceFiles("webjars/");
//        List<URL> urlList3 = ResourceUtil.getAllResourceFiles("doc.html");
//        urlList1.addAll(urlList2);
//        urlList1.addAll(urlList3);
//        if (urlList1.size() == 0) {
//            return;
//        }
//        findFile(urlList1.get(0).toString().substring(6));
//        FilterManager.addFilter(StaticFileFilter.class);
    }
}
