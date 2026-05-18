package io.github.stylesmile.test;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.annotation.RequestParam;

/**
 * Test controller that uses dependency injection.
 */
@Controller
public class GreetingController {

    @AutoWired
    private GreetingService greetingService;

    @RequestMapping("/greet")
    public String greet(@RequestParam("name") String name) {
        return greetingService.greet(name);
    }

    @RequestMapping("/farewell")
    public String farewell(@RequestParam("name") String name) {
        return greetingService.farewell(name);
    }
}
