package io.github.stylesmile.tool;

import io.github.stylesmile.server.Headers;

public class HeaderUtil {
    /**
     * 是否为 form-data
     *
     * @param headers head
     * @return boolean
     */
    public static boolean isMultipartFormData(Headers headers) {
        String contentType = headers.get("Content-Type");
        if (StringUtil.isEmpty(contentType)) {
            return false;
        }
        return contentType.toLowerCase().contains(("multipart/form-data"));
    }

    /**
     * 是否为 application/json
     *
     * @param headers header
     * @return boolean
     */
    public static boolean isApplicationJsonData(Headers headers) {
        String contentType = headers.get("Content-Type");
        if (StringUtil.isEmpty(contentType)) {
            return false;
        }
        return contentType.toLowerCase().contains(("application/json"));
    }
}
