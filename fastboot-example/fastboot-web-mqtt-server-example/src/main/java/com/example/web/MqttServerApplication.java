//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.web;

import io.github.stylesmile.annotation.*;
import io.github.stylesmile.app.App;
import io.github.stylesmile.file.UploadedFile;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.tool.JsonGsonUtil;
import io.github.stylesmile.tool.PropertyUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 启动时，只需扫描 controller service 和配置，这样可以提高启动速度
 */
@Fastboot
@Controller
public class  MqttServerApplication {


    public static void main(String[] args) {
        for (int a = 0; a < args.length; a++) {
            System.out.println(args[a]);
        }
        App.start(MqttServerApplication.class, args);
    }

}
