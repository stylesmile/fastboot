package io.github.stylesmile.server;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import static io.github.stylesmile.server.JdkHTTPServer.*;

/**
 * The {@code Response} class encapsulates a single HTTP response.
 */
public class Response implements Closeable {

    protected OutputStream out; // the underlying output stream
    protected OutputStream encodedOut; // chained encoder streams
    protected Headers headers;
    protected boolean discardBody;
    protected int state; // nothing sent, headers sent, or closed
    protected Request req; // request used in determining client capabilities

    /**
     * Constructs a Response whose output is written to the given stream.
     *
     * @param out the stream to which the response is written
     */
    public Response(OutputStream out) {
        this.out = out;
        this.headers = new Headers();
    }

    /**
     * Sets whether this response's body is discarded or sent.
     *
     * @param discardBody specifies whether the body is discarded or not
     */
    public void setDiscardBody(boolean discardBody) {
        this.discardBody = discardBody;
    }

    /**
     * Sets the request which is used in determining the capabilities
     * supported by the client (e.g. compression, encoding, etc.)
     *
     * @param req the request
     */
    public void setClientCapabilities(Request req) {
        this.req = req;
    }

    /**
     * Returns the request headers collection.
     *
     * @return the request headers collection
     */
    public Headers getHeaders() {
        return headers;
    }

    /**
     * set header
     * @param key key
     * @param value value
     */
    public void setHeader(String key, String value) {
        this.headers.add(key, value);
    }
    /**
     * Returns the underlying output stream to which the response is written.
     * Except for special cases, you should use {@link #getBody()} instead.
     *
     * @return the underlying output stream to which the response is written
     */
    public OutputStream getOutputStream() {
        return out;
    }

    /**
     * Returns whether the response headers were already sent.
     *
     * @return whether the response headers were already sent
     */
    public boolean headersSent() {
        return state == 1;
    }

    /**
     * Returns an output stream into which the response body can be written.
     * The stream applies encodings (e.g. compression) according to the sent headers.
     * This method must be called after response headers have been sent
     * that indicate there is a body. Normally, the content should be
     * prepared (not sent) even before the headers are sent, so that any
     * errors during processing can be caught and a proper error response returned -
     * after the headers are sent, it's too late to change the status into an error.
     *
     * @return an output stream into which the response body can be written,
     * or null if the body should not be written (e.g. it is discarded)
     * @throws IOException if an error occurs
     */
    public OutputStream getBody() throws IOException {
        if (encodedOut != null || discardBody)
            return encodedOut; // return the existing stream (or null)
        // set up chain of encoding streams according to headers
        List<String> te = Arrays.asList(splitElements(headers.get("Transfer-Encoding"), true));
        List<String> ce = Arrays.asList(splitElements(headers.get("Content-Encoding"), true));
        encodedOut = new ResponseOutputStream(out); // leaves underlying stream open when closed
        if (te.contains("chunked"))
            encodedOut = new JdkHTTPServer.ChunkedOutputStream(encodedOut);
        if (ce.contains("gzip") || te.contains("gzip"))
            encodedOut = new GZIPOutputStream(encodedOut, 4096);
        else if (ce.contains("deflate") || te.contains("deflate"))
            encodedOut = new DeflaterOutputStream(encodedOut);

        return encodedOut; // return the outer-most stream
    }

    /**
     * Closes this response and flushes all output.
     *
     * @throws IOException if an error occurs
     */
    public void close() throws IOException {
        state = -1; // closed
        if (encodedOut != null)
            encodedOut.close(); // close all chained streams (except the underlying one)
        out.flush(); // always flush underlying stream (even if getBody was never called)
    }

    /**
     * Sends the response headers with the given response status.
     * A Date header is added if it does not already exist.
     * If the response has a body, the Content-Length/Transfer-Encoding
     * and Content-Type headers must be set before sending the headers.
     *
     * @param status the response status
     * @throws IOException if an error occurs or headers were already sent
     * @see #sendHeaders(int, long, long, String, String, long[])
     */
    public void sendHeaders(int status) throws IOException {
        if (headersSent())
            throw new IOException("headers were already sent");
        if (!headers.contains("Date"))
            headers.add("Date", formatDate(System.currentTimeMillis()));
        headers.add("Server", "JLHTTP/2.6");
        out.write(getBytes("HTTP/1.1 ", Integer.toString(status), " ", statuses[status]));
        out.write(CRLF);
        headers.writeTo(out);
        state = 1; // headers sent
    }

    /**
     * Sends the response headers, including the given response status
     * and description, and all response headers. If they do not already
     * exist, the following headers are added as necessary:
     * Content-Range, Content-Type, Transfer-Encoding, Content-Encoding,
     * Content-Length, Last-Modified, ETag, Connection  and Date. Ranges are
     * properly calculated as well, with a 200 status changed to a 206 status.
     *
     * @param status       the response status
     * @param length       the response body length, or zero if there is no body,
     *                     or negative if there is a body but its length is not yet known
     * @param lastModified the last modified date of the response resource,
     *                     or non-positive if unknown. A time in the future will be
     *                     replaced with the current system time.
     * @param etag         the ETag of the response resource, or null if unknown
     *                     (see RFC2616#3.11)
     * @param contentType  the content type of the response resource, or null
     *                     if unknown (in which case "application/octet-stream" will be sent)
     * @param range        the content range that will be sent, or null if the
     *                     entire resource will be sent
     * @throws IOException if an error occurs
     */
    public void sendHeaders(int status, long length, long lastModified,
                            String etag, String contentType, long[] range) throws IOException {
        if (range != null) {
            headers.add("Content-Range", "bytes " + range[0] + "-" +
                    range[1] + "/" + (length >= 0 ? length : "*"));
            length = range[1] - range[0] + 1;
            if (status == 200)
                status = 206;
        }
        String ct = headers.get("Content-Type");
        if (ct == null) {
            ct = contentType != null ? contentType : "application/octet-stream";
            headers.add("Content-Type", ct);
        } else {
            if (contentType != null) { //noear,20181220
                ct = contentType;
                headers.replace("Content-Type", ct);
            }
        }


        if (!headers.contains("Content-Length") && !headers.contains("Transfer-Encoding")) {
            // RFC2616#3.6: transfer encodings are case-insensitive and must not be sent to an HTTP/1.0 client
            boolean modern = req != null && req.getVersion().endsWith("1.1");
            String accepted = req == null ? null : req.getHeaders().get("Accept-Encoding");
            List<String> encodings = Arrays.asList(splitElements(accepted, true));
            String compression = encodings.contains("gzip") ? "gzip" :
                    encodings.contains("deflate") ? "deflate" : null;
            if (compression != null && (length < 0 || length > 300) && isCompressible(ct) && modern) {
                //todo: by noear 20220316; add() -> replace()
                headers.replace("Transfer-Encoding", "chunked"); // compressed data is always unknown length
                headers.replace("Content-Encoding", compression);
            } else if (length < 0 && modern) {
                headers.replace("Transfer-Encoding", "chunked"); // unknown length
            } else if (length >= 0) {
                headers.replace("Content-Length", Long.toString(length)); // known length
            }
        }
        if (!headers.contains("Vary")) // RFC7231#7.1.4: Vary field should include headers
            headers.add("Vary", "Accept-Encoding"); // that are used in selecting representation
        if (lastModified > 0 && !headers.contains("Last-Modified")) // RFC2616#14.29
            headers.add("Last-Modified", formatDate(Math.min(lastModified, System.currentTimeMillis())));
        if (etag != null && !headers.contains("ETag"))
            headers.add("ETag", etag);
        if (req != null && "close".equalsIgnoreCase(req.getHeaders().get("Connection"))
                && !headers.contains("Connection"))
            headers.add("Connection", "close"); // #RFC7230#6.6: should reply to close with close
        sendHeaders(status);
    }

    /**
     * Sends the full response with the given status, and the given string
     * as the body. The text is sent in the UTF-8 charset. If a
     * Content-Type header was not explicitly set, it will be set to
     * text/html, and so the text must contain valid (and properly
     * {@link JdkHTTPServer#escapeHTML escaped}) HTML.
     *
     * @param status the response status
     * @param text   the text body (sent as text/html)
     * @throws IOException if an error occurs
     */
    public void send(int status, String text) throws IOException {
        byte[] content = text.getBytes(StandardCharsets.UTF_8);
        sendHeaders(status, content.length, -1,
                "W/\"" + Integer.toHexString(text.hashCode()) + "\"",
//                "text/html; charset=utf-8", null);
                "application/json; charset=utf-8", null);
        headers.add("Content-Type", "application/json; charset=utf-8");
        OutputStream out = getBody();
        if (out != null)
            out.write(content);
    }

    public void sendHtml(int status, byte[] content) throws IOException {
        sendHeaders(status, content.length, -1,
                "W/\"" + Integer.toHexString(content.hashCode()) + "\"",
                "text/html; charset=utf-8", null);
//                "application/json; charset=utf-8", null);
        headers.add("Content-Type", "application/json; charset=utf-8");
        OutputStream out = getBody();;
        if (out != null){
            out.write(content);
        }
    }
    public void send2(int status, String text) throws IOException {
        byte[] content = text.getBytes(StandardCharsets.UTF_8);
        sendHeaders(status, content.length, -1,
                "W" + Integer.toHexString(text.hashCode()) + "",
//                "text/html; charset=utf-8", null);
                "application/json; charset=utf-8", null);
        OutputStream out = getBody();
        if (out != null)
            out.write(content);
    }
    public void send(int status) throws IOException {
    }

    /**
     * Sends an error response with the given status and detailed message.
     * An HTML body is created containing the status and its description,
     * as well as the message, which is escaped using the
     * {@link JdkHTTPServer#escapeHTML escape} method.
     *
     * @param status the response status
     * @param text   the text body (sent as text/html)
     * @throws IOException if an error occurs
     */
    public void sendError(int status, String text) throws IOException {
        send(status, String.format(
                "<!DOCTYPE html>%n<html>%n<head><title>%d %s</title></head>%n" +
                        "<body><h1>%d %s</h1>%n<p>%s</p>%n</body></html>",
                status, statuses[status], status, statuses[status], escapeHTML(text)));
    }

    /**
     * Sends an error response with the given status and default body.
     *
     * @param status the response status
     * @throws IOException if an error occurs
     */
    public void sendError(int status) throws IOException {
        String text = status < 400 ? ":)" : "sorry it didn't work out :(";
        sendError(status, text);
    }

    /**
     * Sends the response body. This method must be called only after the
     * response headers have been sent (and indicate that there is a body).
     *
     * @param body   a stream containing the response body
     * @param length the full length of the response body, or -1 for the whole stream
     * @param range  the sub-range within the response body that should be
     *               sent, or null if the entire body should be sent
     * @throws IOException if an error occurs
     */
    public void sendBody(InputStream body, long length, long[] range) throws IOException {
        OutputStream out = getBody();
        if (out != null) {
            if (range != null) {
                long offset = range[0];
                length = range[1] - range[0] + 1;
                while (offset > 0) {
                    long skip = body.skip(offset);
                    if (skip == 0)
                        throw new IOException("can't skip to " + range[0]);
                    offset -= skip;
                }
            }
            transfer(body, out, length);
        }
    }

    /**
     * Sends a 301 or 302 response, redirecting the client to the given URL.
     *
     * @param url       the absolute URL to which the client is redirected
     * @param permanent specifies whether a permanent (301) or
     *                  temporary (302) redirect status is sent
     * @throws IOException if an IO error occurs or url is malformed
     */
    public void redirect(String url, boolean permanent) throws IOException {
        try {
            url = new URI(url).toASCIIString();
        } catch (URISyntaxException e) {
            throw new IOException("malformed URL: " + url);
        }
        headers.add("Location", url);
        // some user-agents expect a body, so we send it
        if (permanent)
            sendError(301, "Permanently moved to " + url);
        else
            sendError(302, "Temporarily moved to " + url);
    }
}
