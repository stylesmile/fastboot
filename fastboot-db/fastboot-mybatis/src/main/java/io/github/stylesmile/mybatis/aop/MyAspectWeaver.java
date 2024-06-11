package io.github.stylesmile.mybatis.aop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.annotation.Annotation;

public class MyAspectWeaver extends ClassVisitor {
        private String className;

    public MyAspectWeaver(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        if (mv != null) {
            for (Annotation annotation : getAnnotations()) {

                if (annotation.toString().endsWith("Mapper")) {
//                if (annotation.toString().endsWith("Fastboot")("LMapper;")) {
                    return new MyAspectMethodVisitor(api, mv);
                }
            }
        }
        return mv;
    }

    private Annotation[] getAnnotations() {
        // 获取当前方法的注解列表
        try {
            return Class.forName(className).getAnnotations();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
