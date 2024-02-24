package com.example.knife4j;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.annotation.RequestParam;
import io.github.stylesmile.app.App;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * @author chenye
 */
@Controller
public class FastbootKnife4jApplication {
    public static void main(String[] args) {
        App.start(FastbootKnife4jApplication.class, args);
    }
    @Parameter(name = "name",description = "姓名",required = true)
    @Operation(description = "向客人问好")
    @RequestMapping("/sayHi2")
    public String sayHi2(@RequestParam(value = "name")String name){
        return "Hi:"+name;
    }
}
