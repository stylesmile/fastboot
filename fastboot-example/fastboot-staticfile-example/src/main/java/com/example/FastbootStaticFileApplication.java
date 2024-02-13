
package com.example;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.app.App;

@Controller
public class FastbootStaticFileApplication {
    public static void main(String[] args) {
        App.start(FastbootStaticFileApplication.class, args);
    }
}
