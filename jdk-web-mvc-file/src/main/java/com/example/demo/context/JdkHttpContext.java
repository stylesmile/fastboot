package com.example.demo.context;

import com.example.demo.file.UploadedFile;
import com.example.demo.tool.MultipartUtil;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;

public class JdkHttpContext extends Context {
    private HttpExchange _exchange;
    private Map<String, Object> _parameters;
    protected Map<String, List<UploadedFile>> _fileMap;

    private HashMap _headerMap;

    public JdkHttpContext(HttpExchange exchange) {
        _exchange = exchange;
        _parameters = (Map<String, Object>) _exchange.getAttribute("parameters");
        _fileMap = new HashMap<>();
    }

    private boolean _loadMultipartFormData = false;
    private void loadMultipartFormData() throws IOException{
        if (_loadMultipartFormData) {
            return;
        } else {
            _loadMultipartFormData = true;
        }

        //文件上传需要
        if (isMultipartFormData()) {
            MultipartUtil.buildParamsAndFiles(this);
        }
    }

    @Override
    public Object request() {
        return _exchange;
    }

    private String _ip;

    @Override
    public String ip() {
        if (_ip == null) {
            _ip = header("X-Forwarded-For");

            if (_ip == null) {
                _ip = _exchange.getRemoteAddress().getAddress().getHostAddress();
            }
        }

        return _ip;
    }

    @Override
    public String method() {
        return _exchange.getRequestMethod();
    }

    @Override
    public String protocol() {
        return _exchange.getProtocol();
    }

    private URI _uri;

    @Override
    public URI uri() {
        if (_uri == null) {
            _uri = URI.create(url());
        }

        return _uri;
    }

    @Override
    public String path() {
        return uri().getPath();
    }

    private String _url;

    @Override
    public String url() {
        if (_url == null) {
            _url = _exchange.getRequestURI().toString();

            if (_url != null) {
                if (_url.startsWith("/")) {
                    String host = header("Host");

                    if (host == null) {
                        host = header(":authority");
                        String scheme = header(":scheme");

                        if (host == null) {
                            host = "localhost";
                        }

                        if (scheme != null) {
                            _url = "https://" + host + _url;
                        } else {
                            _url = scheme + "://" + host + _url;
                        }

                    } else {
                        _url = "http://" + host + _url;
                    }
                }

                int idx = _url.indexOf("?");
                if (idx > 0) {
                    _url = _url.substring(0, idx);
                }
            }
        }

        return _url;
    }

    @Override
    public long contentLength() {
        try {
            return bodyAsStream().available();
        } catch (Exception ex) {
            EventBus.push(ex);
            return 0;
        }
    }

    @Override
    public String contentType() {
        return header("Content-Type");
    }

    @Override
    public String queryString() {
        return _exchange.getRequestURI().getQuery();
    }

    @Override
    public InputStream bodyAsStream() throws IOException {
        return _exchange.getRequestBody();
    }

    @Override
    public String[] paramValues(String key) {
        List<String> list = paramsMap().get(key);
        if (list == null) {
            return null;
        }

        return list.toArray(new String[list.size()]);
    }

    @Override
    public String param(String key) {
        //要充许为字符串
        //默认不能为null
        return paramMap().get(key);
    }

    @Override
    public String param(String key, String def) {
        try {
            String temp = paramMap().get(key);

            if (Utils.isEmpty(temp)) {
                return def;
            } else {
                return temp;
            }
        } catch (Exception ex) {
            EventBus.push(ex);
            return def;
        }
    }

    private HashMap _paramMap;

    @Override
    public HashMap paramMap() {
        if (_paramMap == null) {
            _paramMap = new NvMap();

            try {
                if(autoMultipart()) {
                    loadMultipartFormData();
                }

                _parameters.forEach((k, v) -> {
                    if (v instanceof List) {
                        _paramMap.put(k, ((List<String>) v).get(0));
                    } else {
                        _paramMap.put(k, (String) v);
                    }
                });
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        return _paramMap;
    }

    private Map<String, List<String>> _paramsMap;

    @Override
    public Map<String, List<String>> paramsMap() {
        if (_paramsMap == null) {
            _paramsMap = new LinkedHashMap<>();

            _parameters.forEach((k, v) -> {
                if (v instanceof List) {
                    _paramsMap.put(k, (List<String>) v);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add((String) v);
                    _paramsMap.put(k, list);
                }
            });
        }

        return _paramsMap;
    }

    @Override
    public List<UploadedFile> files(String key) throws Exception {
        if (isMultipartFormData()) {
            loadMultipartFormData();

            List<UploadedFile> temp = _fileMap.get(key);
            if (temp == null) {
                return new ArrayList<>();
            } else {
                return temp;
            }
        } else {
            return new ArrayList<>();
        }
    }

    private HashMap _cookieMap;

    @Override
    public HashMap headerMap() {
        if (_headerMap == null) {
            _headerMap = new NvMap();

            Headers headers = _exchange.getRequestHeaders();

            if (headers != null) {
                headers.forEach((k, l) -> {
                    if (l.size() > 0) {
                        _headerMap.put(k, l.get(0));
                    }
                });
            }
        }

        return _headerMap;
    }


}
