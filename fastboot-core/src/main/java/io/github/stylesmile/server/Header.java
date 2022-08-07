package io.github.stylesmile.server;

/**
 * The {@code Header} class encapsulates a single HTTP header.
 */
public class Header {

    protected final String name;
    protected final String value;

    /**
     * Constructs a header with the given name and value.
     * Leading and trailing whitespace are trimmed.
     *
     * @param name  the header name
     * @param value the header value
     * @throws NullPointerException     if name or value is null
     * @throws IllegalArgumentException if name is empty
     */
    public Header(String name, String value) {
        this.name = name.trim();
        this.value = value.trim();
        // RFC2616#14.23 - header can have an empty value (e.g. Host)
        if (this.name.length() == 0) // but name cannot be empty
            throw new IllegalArgumentException("name cannot be empty");
    }

    /**
     * Returns this header's name.
     *
     * @return this header's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this header's value.
     *
     * @return this header's value
     */
    public String getValue() {
        return value;
    }
}