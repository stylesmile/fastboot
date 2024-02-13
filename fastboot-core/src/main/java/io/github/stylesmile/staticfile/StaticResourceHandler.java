//package io.github.stylesmile.staticfile;
//
//import io.github.stylesmile.filter.Filter;
//import io.github.stylesmile.server.Request;
//import io.github.stylesmile.server.Response;
//import io.github.stylesmile.tool.StringUtil;
//
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Date;
//
///**
// * 静态文件资源处理
// *
// * @author noear
// * @since 1.0
// */
//public class StaticResourceHandler implements Filter {
//    private static final String CACHE_CONTROL = "Cache-Control";
//    private static final String LAST_MODIFIED = "Last-Modified";
//    private static final Date modified_time = new Date();
//
//
//
//    /**
//     * 尝试查找路径的后缀名
//     */
//    private String findByExtName(String path) {
//        int pos = path.lastIndexOf(35); //'#'
//        if (pos > 0) {
//            path = path.substring(0, pos - 1);
//        }
//
//        pos = path.lastIndexOf(46); //'.'
//        pos = Math.max(pos, path.lastIndexOf(47)); //'/'
//        pos = Math.max(pos, path.lastIndexOf(63)); //'?'
//
//        if (pos != -1 && path.charAt(pos) == '.') {
//            return path.substring(pos).toLowerCase();
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean preHandle(Request request, Response response) {
////        if (ctx.getHandled()) {
////            return;
////        }
////
////        if (MethodType.GET.name.equals(ctx.method()) == false) {
////            return;
////        }
//
//        String path = ctx.pathNew();
//
//        //找后缀名
//        String suffix = findByExtName(path);
//        if (StringUtil.isEmpty(suffix)) {
//            return;
//        }
//
//        //找内容类型(先用配置的，再用jdk的)
//        String conentType = StaticMimes.findByExt(suffix);
//
//        if (StringUtil.isEmpty(conentType)) {
//            conentType = Utils.mime(suffix);
//        }
//
//        //说明没有支持的mime
//        if (StringUtil.isEmpty(conentType)) {
//            return;
//        }
//
//        //找资源
//        URL uri = StaticMappings.find(path);
//
//        if (uri != null) {
//            ctx.setHandled(true);
//
//            String modified_since = ctx.header("If-Modified-Since");
//            String modified_now = modified_time.toString();
//
//            if (modified_since != null && StaticConfig.getCacheMaxAge() > 0) {
//                if (modified_since.equals(modified_now)) {
//                    ctx.headerSet(CACHE_CONTROL, "max-age=" + StaticConfig.getCacheMaxAge());//单位秒
//                    ctx.headerSet(LAST_MODIFIED, modified_now);
//                    ctx.status(304);
//                    return;
//                }
//            }
//
//            if (StaticConfig.getCacheMaxAge() > 0) {
//                ctx.headerSet(CACHE_CONTROL, "max-age=" + StaticConfig.getCacheMaxAge());//单位秒
//                ctx.headerSet(LAST_MODIFIED, modified_time.toString());
//            }
//
//
//            //如果支持配置的类型
//            if (GzipProps.hasMime(conentType)) {
//
//                String ae = ctx.headerOrDefault("Accept-Encoding", "");
//
//                if (ae.contains("br")) {
//                    //如果支持 br
//                    URL zipedFile = StaticMappings.find(path + ".br");
//                    if (zipedFile != null) {
//                        try (InputStream stream = zipedFile.openStream()) {
//                            ctx.contentType(conentType);
//                            ctx.headerSet("Vary", "Accept-Encoding");
//                            ctx.headerSet("Content-Encoding", "br");
//                            OutputUtils.global().outputStreamAsRange(ctx, stream, stream.available());
//                        }
//                        return;
//                    }
//                }
//
//                if (ae.contains("gzip")) {
//                    //如果支持 gzip
//                    URL zipedFile = StaticMappings.find(path + ".gz");
//                    if (zipedFile != null) {
//                        try (InputStream stream = zipedFile.openStream()) {
//                            ctx.contentType(conentType);
//                            ctx.headerSet("Vary", "Accept-Encoding");
//                            ctx.headerSet("Content-Encoding", "gzip");
//                            OutputUtils.global().outputStreamAsRange(ctx, stream, stream.available());
//                        }
//                        return;
//                    }
//                }
//            }
//
//            OutputUtils.global().outputFile(ctx, uri, conentType, StaticConfig.getCacheMaxAge() >= 0);
//        }
//    }
//
//    @Override
//    public boolean afterCompletion(Request request, Response response) {
//        return false;
//    }
//}
