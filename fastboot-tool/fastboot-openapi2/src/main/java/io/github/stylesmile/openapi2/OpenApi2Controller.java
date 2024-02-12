//package io.github.stylesmile.openapi2;
//
//import java.io.IOException;
//
//import org.noear.solon.annotation.Mapping;
//import org.noear.solon.annotation.Produces;
//import org.noear.solon.core.handle.*;
//import org.noear.solon.docs.openapi2.OpenApi2Utils;
//
///**
// * Swagger api Controller
// *
// * @author noear
// * @since 2.3
// */
//public class OpenApi2Controller {
//    /**
//     * swagger 获取分组信息
//     */
//    @Mapping("swagger-resources")
//    public String resources() throws IOException {
//        return OpenApi2Utils.getApiGroupResourceJson("swagger/v2");
//    }
//
//    /**
//     * swagger 获取分组接口数据
//     */
//    @Mapping("swagger/v2")
//    public String api(Context ctx, String group) throws IOException {
//        return OpenApi2Utils.getApiJson(ctx, group);
//    }
//}