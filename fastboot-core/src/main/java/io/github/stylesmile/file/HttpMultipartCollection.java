package io.github.stylesmile.file;


import io.github.stylesmile.jlhttpserver.HTTPServer;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.tool.Utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class HttpMultipartCollection implements Iterator<HttpMultipart> {

    protected final MultipartInputStream in;
    protected boolean next;


    public HttpMultipartCollection(Request req) throws IOException {
        Map<String, String> ct = Utils.getHeaderParams(req.getHeaders().get("Content-Type"));
        if (!ct.containsKey("multipart/form-data"))
            throw new IllegalArgumentException("Content-Type is not multipart/form-data");

        String boundary = ct.get("boundary"); // should be US-ASCII
        if (boundary == null)
            throw new IllegalArgumentException("Content-Type is missing boundary");
        in = new MultipartInputStream(req.getBody(), Utils.getBytes(boundary));
    }

    public boolean hasNext() {
        try {
            return next || (next = in.nextPart());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public HttpMultipart next() {
        if (!hasNext())
            throw new NoSuchElementException();
        next = false;
        HttpMultipart p = new HttpMultipart();
        try {
            p.headers = Utils.readHeaders(in);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        Map<String, String> cd = p.headers.getParams("Content-Disposition");
        p.name = cd.get("name");
        p.filename = cd.get("filename");
        p.body = in;
        return p;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
