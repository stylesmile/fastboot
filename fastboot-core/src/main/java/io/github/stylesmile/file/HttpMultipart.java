package io.github.stylesmile.file;

import io.github.stylesmile.http.HttpHeaderCollection;

import java.io.IOException;
import java.io.InputStream;

public class HttpMultipart {
    public String name;
    public String filename;
    public HttpHeaderCollection headers;
    public InputStream body;

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public HttpHeaderCollection getHeaders() {
        return headers;
    }

    public InputStream getBody() {
        return body;
    }
}
