package io.github.stylesmile.mybatis.aop;

/**
 * 将修改后的字节码加载到JVM中：
 */
public class MyAspectLoader extends ClassLoader {
    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
