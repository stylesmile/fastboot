package io.github.stylesmile.tool;

import io.github.stylesmile.server.Response;

import java.io.IOException;

public class ResultUtil {
    public static void sendJson(Response response, int status, Object o) {
        try {
            response.send(200, JsonGsonUtil.objectToJson(o));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
