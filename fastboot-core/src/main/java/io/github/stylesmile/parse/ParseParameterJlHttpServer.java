package io.github.stylesmile.parse;

import io.github.stylesmile.server.HTTPServer;
import io.github.stylesmile.server.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Stylesmile
 */
public class ParseParameterJlHttpServer {

    /**
     * 解析get参数
     */

    public static void parseGetParameters(Request request, Map<String, Object> parameters) {
        URI requestedUri = request.getURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
    }

    /**
     * 解析post参数
     */
    public static void parsePostParameters(Request request, Map<String, Object> parameters) {

        String ct = request.getHeaders().get("Content-Type");
        if (ct == null) {
            return;
        }
        if (ct.toLowerCase(Locale.US).startsWith("application/x-www-form-urlencoded") == false) {
            return;
        }
        Map<String, String> map = null;
        try {
            map = request.getParams();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        map.forEach((k, v) -> {
            parameters.put(k, v);
        });
    }

    public static void parseQuery(String query, Map<String, Object> parameters) {
        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    try {
                        key = URLDecoder.decode(param[0], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (param.length > 1) {
                    try {
                        value = URLDecoder.decode(param[1], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    value = "";
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);

                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
