package com.example.demo.tool;

import com.example.demo.context.Context;

public class IpUtil {
    public static String getIp(Context ctx) {
        String ip = ctx.header("X-Real-IP");

        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = ctx.header("X-Forwarded-For");
        }

        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = ctx.ip();
        }

        return ip;
    }
}
