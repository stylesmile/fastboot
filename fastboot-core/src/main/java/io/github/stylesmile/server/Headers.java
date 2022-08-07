package io.github.stylesmile.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static io.github.stylesmile.server.HTTPServer.CRLF;
import static io.github.stylesmile.server.HTTPServer.parseDate;

/**
 * The {@code Headers} class encapsulates a collection of HTTP headers.
 * <p>
 * Header names are treated case-insensitively, although this class retains
 * their original case. Header insertion order is maintained as well.
 */
public class Headers implements Iterable<Header> {

    // due to the requirements of case-insensitive name comparisons,
    // retaining the original case, and retaining header insertion order,
    // and due to the fact that the number of headers is generally
    // quite small (usually under 12 headers), we use a simple array with
    // linear access times, which proves to be more efficient and
    // straightforward than the alternatives
    protected Header[] headers = new Header[12];
    protected int count;

    /**
     * Returns the number of added headers.
     *
     * @return the number of added headers
     */
    public int size() {
        return count;
    }

    /**
     * Returns the value of the first header with the given name.
     *
     * @param name the header name (case insensitive)
     * @return the header value, or null if none exists
     */
    public String get(String name) {
        for (int i = 0; i < count; i++)
            if (headers[i].getName().equalsIgnoreCase(name))
                return headers[i].getValue();
        return null;
    }

    /**
     * Returns the Date value of the header with the given name.
     *
     * @param name the header name (case insensitive)
     * @return the header value as a Date, or null if none exists
     * or if the value is not in any supported date format
     */
    public Date getDate(String name) {
        try {
            String header = get(name);
            return header == null ? null : parseDate(header);
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    /**
     * Returns whether there exists a header with the given name.
     *
     * @param name the header name (case insensitive)
     * @return whether there exists a header with the given name
     */
    public boolean contains(String name) {
        return get(name) != null;
    }

    /**
     * Adds a header with the given name and value to the end of this
     * collection of headers. Leading and trailing whitespace are trimmed.
     *
     * @param name  the header name (case insensitive)
     * @param value the header value
     */
    public void add(String name, String value) {
        Header header = new Header(name, value); // also validates
        // expand array if necessary
        if (count == headers.length) {
            Header[] expanded = new Header[2 * count];
            System.arraycopy(headers, 0, expanded, 0, count);
            headers = expanded;
        }
        headers[count++] = header; // inlining header would cause a bug!
    }

    /**
     * Adds all given headers to the end of this collection of headers,
     * in their original order.
     *
     * @param headers the headers to add
     */
    public void addAll(Headers headers) {
        for (Header header : headers)
            add(header.getName(), header.getValue());
    }

    /**
     * Adds a header with the given name and value, replacing the first
     * existing header with the same name. If there is no existing header
     * with the same name, it is added as in {@link #add}.
     *
     * @param name  the header name (case insensitive)
     * @param value the header value
     * @return the replaced header, or null if none existed
     */
    public Header replace(String name, String value) {
        for (int i = 0; i < count; i++) {
            if (headers[i].getName().equalsIgnoreCase(name)) {
                Header prev = headers[i];
                headers[i] = new Header(name, value);
                return prev;
            }
        }
        add(name, value);
        return null;
    }

    /**
     * Removes all headers with the given name (if any exist).
     *
     * @param name the header name (case insensitive)
     */
    public void remove(String name) {
        int j = 0;
        for (int i = 0; i < count; i++)
            if (!headers[i].getName().equalsIgnoreCase(name))
                headers[j++] = headers[i];
        while (count > j)
            headers[--count] = null;
    }

    /**
     * Writes the headers to the given stream (including trailing CRLF).
     *
     * @param out the stream to write the headers to
     * @throws IOException if an error occurs
     */
    public void writeTo(OutputStream out) throws IOException {
        for (int i = 0; i < count; i++) {
            out.write(HTTPServer.getBytes(headers[i].getName(), ": ", headers[i].getValue()));
            out.write(CRLF);
        }
        out.write(CRLF); // ends header block
    }

    /**
     * Returns a header's parameters. Parameter order is maintained,
     * and the first key (in iteration order) is the header's value
     * without the parameters.
     *
     * @param name the header name (case insensitive)
     * @return the header's parameter names and values
     */
    public Map<String, String> getParams(String name) {
        Map<String, String> params = new LinkedHashMap<String, String>();
        for (String param : HTTPServer.split(get(name), ";", -1)) {
            String[] pair = HTTPServer.split(param, "=", 2);
            String val = pair.length == 1 ? "" : HTTPServer.trimLeft(HTTPServer.trimRight(pair[1], '"'), '"');
            params.put(pair[0], val);
        }
        return params;
    }

    /**
     * Returns an iterator over the headers, in their insertion order.
     * If the headers collection is modified during iteration, the
     * iteration result is undefined. The remove operation is unsupported.
     *
     * @return an Iterator over the headers
     */
    public Iterator<Header> iterator() {
        // we use the built-in wrapper instead of a trivial custom implementation
        // since even a tiny anonymous class here compiles to a 1.5K class file
        return Arrays.asList(headers).subList(0, count).iterator();
    }
}