package io.github.stylesmile.knife4j;

import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.handle.MappingHandler;
import io.github.stylesmile.knife4j.domain.ApiGroupResource;
import io.github.stylesmile.knife4j.domain.DocDocket;
import io.github.stylesmile.tool.FastbootUtil;
import io.github.stylesmile.tool.JsonGsonUtil;
import io.github.stylesmile.tool.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Open Api v2 工具类
 *
 * @author noear
 * @since 2.4
 */
public class OpenApiUtils {
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
        Map<String, MappingHandler> mappingHandlerList  =  HandlerManager.getAllMappingHandler();;

        List<ApiGroupResource> resourceList = new ArrayList<>();
        for(String key : mappingHandlerList.keySet()){
            if(StringUtil.isNotEmpty(key)){
//                String group = bw.name();
//                String groupName = ((DocDocket) bw.raw()).groupName();
//                String url = resourceUri + "?group=" + group;
            }
        }
        return JsonGsonUtil.objectToJson(resourceList);
    }

    /**
     * 获取接口
     */
    public static String getApiJson(String group) throws IOException {
        DocDocket docket = FastbootUtil.getBean(DocDocket.class);
//        Swagger swagger = new OpenApi2Builder(docket).build();
//        return JsonGsonUtil.BeanToJson(swagger);
        return null;
    }
}
