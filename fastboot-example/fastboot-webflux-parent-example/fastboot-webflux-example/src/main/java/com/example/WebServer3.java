package com.example;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.function.BiFunction;

public class WebServer3 {
    public static final int PORT = 8080;

    public static void main(String[] args) {
        DisposableServer server = HttpServer.create()
                .port(PORT)
                .route(WebServer3::configureRoutes)
                .bindNow();
        System.out.println("start server port :" + PORT);

        server.onDispose().block();
    }

    private static void configureRoutes(HttpServerRoutes routes) {
        routes.get("/", WebServer3::helloworld);
        extracted(routes);
    }

    private static void extracted(HttpServerRoutes routes) {
        routes.get("/2", WebServer3::helloworld2);
    }

    private static Publisher<Void> helloworld(HttpServerRequest request, HttpServerResponse response) {
        return response.sendString(Mono.just("Hello, World!"));
    }
    private static Publisher<Void> helloworld2(HttpServerRequest request, HttpServerResponse response) {
        return response.sendString(Mono.just("Hello, World!"));
    }

    private static BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> helloworld2() {
        return (request, response) ->
                response.sendString(Mono.just("Hello, World!"));
    }


}
