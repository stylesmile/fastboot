//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.stylesmile.web;


public enum HttpStatus {
    CONTINUE(100, HttpStatus.Series.INFORMATIONAL, "Continue"),
    SWITCHING_PROTOCOLS(101, HttpStatus.Series.INFORMATIONAL, "Switching Protocols"),
    PROCESSING(102, HttpStatus.Series.INFORMATIONAL, "Processing"),
    CHECKPOINT(103, HttpStatus.Series.INFORMATIONAL, "Checkpoint"),
    OK(200, HttpStatus.Series.SUCCESSFUL, "OK"),
    CREATED(201, HttpStatus.Series.SUCCESSFUL, "Created"),
    ACCEPTED(202, HttpStatus.Series.SUCCESSFUL, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, HttpStatus.Series.SUCCESSFUL, "Non-Authoritative Information"),
    NO_CONTENT(204, HttpStatus.Series.SUCCESSFUL, "No Content"),
    RESET_CONTENT(205, HttpStatus.Series.SUCCESSFUL, "Reset Content"),
    PARTIAL_CONTENT(206, HttpStatus.Series.SUCCESSFUL, "Partial Content"),
    MULTI_STATUS(207, HttpStatus.Series.SUCCESSFUL, "Multi-Status"),
    ALREADY_REPORTED(208, HttpStatus.Series.SUCCESSFUL, "Already Reported"),
    IM_USED(226, HttpStatus.Series.SUCCESSFUL, "IM Used"),
    MULTIPLE_CHOICES(300, HttpStatus.Series.REDIRECTION, "Multiple Choices"),
    MOVED_PERMANENTLY(301, HttpStatus.Series.REDIRECTION, "Moved Permanently"),
    FOUND(302, HttpStatus.Series.REDIRECTION, "Found"),
    /**
     * @deprecated
     */
    @Deprecated
    MOVED_TEMPORARILY(302, HttpStatus.Series.REDIRECTION, "Moved Temporarily"),
    SEE_OTHER(303, HttpStatus.Series.REDIRECTION, "See Other"),
    NOT_MODIFIED(304, HttpStatus.Series.REDIRECTION, "Not Modified"),
    /**
     * @deprecated
     */
    @Deprecated
    USE_PROXY(305, HttpStatus.Series.REDIRECTION, "Use Proxy"),
    TEMPORARY_REDIRECT(307, HttpStatus.Series.REDIRECTION, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, HttpStatus.Series.REDIRECTION, "Permanent Redirect"),
    BAD_REQUEST(400, HttpStatus.Series.CLIENT_ERROR, "Bad Request"),
    UNAUTHORIZED(401, HttpStatus.Series.CLIENT_ERROR, "Unauthorized"),
    PAYMENT_REQUIRED(402, HttpStatus.Series.CLIENT_ERROR, "Payment Required"),
    FORBIDDEN(403, HttpStatus.Series.CLIENT_ERROR, "Forbidden"),
    NOT_FOUND(404, HttpStatus.Series.CLIENT_ERROR, "Not Found"),
    METHOD_NOT_ALLOWED(405, HttpStatus.Series.CLIENT_ERROR, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, HttpStatus.Series.CLIENT_ERROR, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, HttpStatus.Series.CLIENT_ERROR, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, HttpStatus.Series.CLIENT_ERROR, "Request Timeout"),
    CONFLICT(409, HttpStatus.Series.CLIENT_ERROR, "Conflict"),
    GONE(410, HttpStatus.Series.CLIENT_ERROR, "Gone"),
    LENGTH_REQUIRED(411, HttpStatus.Series.CLIENT_ERROR, "Length Required"),
    PRECONDITION_FAILED(412, HttpStatus.Series.CLIENT_ERROR, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413, HttpStatus.Series.CLIENT_ERROR, "Payload Too Large"),
    /**
     * @deprecated
     */
    @Deprecated
    REQUEST_ENTITY_TOO_LARGE(413, HttpStatus.Series.CLIENT_ERROR, "Request Entity Too Large"),
    URI_TOO_LONG(414, HttpStatus.Series.CLIENT_ERROR, "URI Too Long"),
    /**
     * @deprecated
     */
    @Deprecated
    REQUEST_URI_TOO_LONG(414, HttpStatus.Series.CLIENT_ERROR, "Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, HttpStatus.Series.CLIENT_ERROR, "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, HttpStatus.Series.CLIENT_ERROR, "Requested range not satisfiable"),
    EXPECTATION_FAILED(417, HttpStatus.Series.CLIENT_ERROR, "Expectation Failed"),
    I_AM_A_TEAPOT(418, HttpStatus.Series.CLIENT_ERROR, "I'm a teapot"),
    /**
     * @deprecated
     */
    @Deprecated
    INSUFFICIENT_SPACE_ON_RESOURCE(419, HttpStatus.Series.CLIENT_ERROR, "Insufficient Space On Resource"),
    /**
     * @deprecated
     */
    @Deprecated
    METHOD_FAILURE(420, HttpStatus.Series.CLIENT_ERROR, "Method Failure"),
    /**
     * @deprecated
     */
    @Deprecated
    DESTINATION_LOCKED(421, HttpStatus.Series.CLIENT_ERROR, "Destination Locked"),
    UNPROCESSABLE_ENTITY(422, HttpStatus.Series.CLIENT_ERROR, "Unprocessable Entity"),
    LOCKED(423, HttpStatus.Series.CLIENT_ERROR, "Locked"),
    FAILED_DEPENDENCY(424, HttpStatus.Series.CLIENT_ERROR, "Failed Dependency"),
    TOO_EARLY(425, HttpStatus.Series.CLIENT_ERROR, "Too Early"),
    UPGRADE_REQUIRED(426, HttpStatus.Series.CLIENT_ERROR, "Upgrade Required"),
    PRECONDITION_REQUIRED(428, HttpStatus.Series.CLIENT_ERROR, "Precondition Required"),
    TOO_MANY_REQUESTS(429, HttpStatus.Series.CLIENT_ERROR, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, HttpStatus.Series.CLIENT_ERROR, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, HttpStatus.Series.CLIENT_ERROR, "Unavailable For Legal Reasons"),
    INTERNAL_SERVER_ERROR(500, HttpStatus.Series.SERVER_ERROR, "Internal Server Error"),
    NOT_IMPLEMENTED(501, HttpStatus.Series.SERVER_ERROR, "Not Implemented"),
    BAD_GATEWAY(502, HttpStatus.Series.SERVER_ERROR, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, HttpStatus.Series.SERVER_ERROR, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, HttpStatus.Series.SERVER_ERROR, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, HttpStatus.Series.SERVER_ERROR, "HTTP Version not supported"),
    VARIANT_ALSO_NEGOTIATES(506, HttpStatus.Series.SERVER_ERROR, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, HttpStatus.Series.SERVER_ERROR, "Insufficient Storage"),
    LOOP_DETECTED(508, HttpStatus.Series.SERVER_ERROR, "Loop Detected"),
    BANDWIDTH_LIMIT_EXCEEDED(509, HttpStatus.Series.SERVER_ERROR, "Bandwidth Limit Exceeded"),
    NOT_EXTENDED(510, HttpStatus.Series.SERVER_ERROR, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, HttpStatus.Series.SERVER_ERROR, "Network Authentication Required");

    private static final HttpStatus[] VALUES = values();
    private final int value;
    private final Series series;
    private final String reasonPhrase;

    private HttpStatus(int value, Series series, String reasonPhrase) {
        this.value = value;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public Series series() {
        return this.series;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public boolean is1xxInformational() {
        return this.series() == HttpStatus.Series.INFORMATIONAL;
    }

    public boolean is2xxSuccessful() {
        return this.series() == HttpStatus.Series.SUCCESSFUL;
    }

    public boolean is3xxRedirection() {
        return this.series() == HttpStatus.Series.REDIRECTION;
    }

    public boolean is4xxClientError() {
        return this.series() == HttpStatus.Series.CLIENT_ERROR;
    }

    public boolean is5xxServerError() {
        return this.series() == HttpStatus.Series.SERVER_ERROR;
    }

    public boolean isError() {
        return this.is4xxClientError() || this.is5xxServerError();
    }

    public String toString() {
        return this.value + " " + this.name();
    }

    public static HttpStatus valueOf(int statusCode) {
        HttpStatus status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        } else {
            return status;
        }
    }


    public static HttpStatus resolve(int statusCode) {
        HttpStatus[] var1 = VALUES;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            HttpStatus status = var1[var3];
            if (status.value == statusCode) {
                return status;
            }
        }

        return null;
    }

    public static enum Series {
        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        private Series(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        /**
         * @deprecated
         */
        @Deprecated
        public static Series valueOf(HttpStatus status) {
            return status.series;
        }

        public static Series valueOf(int statusCode) {
            Series series = resolve(statusCode);
            if (series == null) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            } else {
                return series;
            }
        }


        public static Series resolve(int statusCode) {
            int seriesCode = statusCode / 100;
            Series[] var2 = values();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Series series = var2[var4];
                if (series.value == seriesCode) {
                    return series;
                }
            }

            return null;
        }
    }
}
