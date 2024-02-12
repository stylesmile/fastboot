package io.github.stylesmile.openapi2.openapi;

import io.swagger.models.Swagger;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.handle.Context;
import org.noear.solon.docs.DocDocket;
import org.noear.solon.docs.models.ApiGroupResource;
import org.noear.solon.docs.util.BasicAuthUtil;
import org.noear.solon.docs.util.JsonUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Open Api v2 工具类
 *
 * @author noear
 * @since 2.4
 */
public class OpenApi2Utils {
    /**
     * 获取接口分组资源
     */
    public static String getApiGroupResourceJson() throws IOException {
        return getApiGroupResourceJson("/swagger/v2");
    }

    /**
     * 获取接口分组资源
     */
    public static String getApiGroupResourceJson(String resourceUri) throws IOException {
        List<BeanWrap> list = Solon.context().getWrapsOfType(DocDocket.class);

        List<ApiGroupResource> resourceList = list.stream().filter(bw -> Utils.isNotEmpty(bw.name()))
                .map(bw -> {
                    String group = bw.name();
                    String groupName = ((DocDocket) bw.raw()).groupName();
                    String url = resourceUri + "?group=" + group;

                    return new ApiGroupResource(groupName, "2.0", url);
                })
                .collect(Collectors.toList());

        return JsonUtil.toJson(resourceList);
    }

    /**
     * 获取接口
     */
    public static String getApiJson(Context ctx, String group) throws IOException {
        DocDocket docket = Solon.context().getBean(group);

        if (docket == null) {
            return null;
        }

        if (!BasicAuthUtil.basicAuth(ctx, docket)) {
            BasicAuthUtil.response401(ctx);
            return null;
        }

        if (docket.globalResponseCodes().containsKey(200) == false) {
            docket.globalResponseCodes().put(200, "");
        }

        Swagger swagger = new OpenApi2Builder(docket).build();
        return JsonUtil.toJson(swagger);
    }
}
