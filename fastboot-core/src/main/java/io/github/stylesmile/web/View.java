//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.stylesmile.web;

import io.github.stylesmile.request.ServletRequest;
import io.github.stylesmile.request.ServletResponse;

import java.util.Map;

public interface View {
    String RESPONSE_STATUS_ATTRIBUTE = View.class.getName() + ".responseStatus";
    String PATH_VARIABLES = View.class.getName() + ".pathVariables";
    String SELECTED_CONTENT_TYPE = View.class.getName() + ".selectedContentType";

    
    default String getContentType() {
        return null;
    }

    void render( Map<String, ?> model,
                ServletRequest request, ServletResponse response) throws Exception;
}
