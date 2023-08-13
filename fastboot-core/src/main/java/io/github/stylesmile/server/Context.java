package io.github.stylesmile.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Context} annotation decorates methods which are mapped
 * to a context (path) within the server, and provide its contents.
 * <p>
 * The annotated methods must have the same signature and contract
 * as {@link ContextHandler#serve}, but can have arbitrary names.
 *
 * @see JdkHTTPServer.VirtualHost#addContexts(Object)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Context {

    /**
     * The context (path) that this field maps to (must begin with '/').
     *
     * @return the context (path) that this field maps to
     */
    String value();

    /**
     * The HTTP methods supported by this context handler (default is "GET").
     *
     * @return the HTTP methods supported by this context handler
     */
    String[] methods() default "GET";
}