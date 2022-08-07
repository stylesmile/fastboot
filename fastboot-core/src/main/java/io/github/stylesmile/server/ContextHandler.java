package io.github.stylesmile.server;

import java.io.IOException;

/**
 * A {@code ContextHandler} serves the content of resources within a context.
 *
 * @see HTTPServer.VirtualHost#addContext
 */
public interface ContextHandler {

    /**
     * Serves the given request using the given response.
     *
     * @param req  the request to be served
     * @param resp the response to be filled
     * @return an HTTP status code, which will be used in returning
     * a default response appropriate for this status. If this
     * method invocation already sent anything in the response
     * (headers or content), it must return 0, and no further
     * processing will be done
     * @throws IOException if an IO error occurs
     */
    int serve(Request req, Response resp) throws IOException;
}