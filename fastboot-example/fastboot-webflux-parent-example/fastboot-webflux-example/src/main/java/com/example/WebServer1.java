package org.example.test;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class WebServer1 {
    public static final int PORT = 8080;

    public static void main(String[] args) {
        DisposableServer server = HttpServer.create()
                .port(PORT)
                .route(routes -> routes.get("/", (request, response) ->
                        response.sendString(Mono.just("Hello, World!"))))
                .bindNow();
        System.out.println("start server port :" + PORT);

        server.onDispose().block();
    }
}
