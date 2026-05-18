package io.github.stylesmile.test;

import io.github.stylesmile.annotation.Service;

/**
 * Test service for unit testing.
 */
@Service
public class GreetingService {

    public String greet(String name) {
        return "Hello, " + name + "!";
    }

    public String farewell(String name) {
        return "Goodbye, " + name + "!";
    }
}
