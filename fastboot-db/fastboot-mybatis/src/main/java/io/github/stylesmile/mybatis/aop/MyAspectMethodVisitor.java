package io.github.stylesmile.mybatis.aop;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MyAspectMethodVisitor extends MethodVisitor {
    public MyAspectMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        // 在方法开始前插入切面逻辑，例如打印日志
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Before method execution");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == Opcodes.RETURN) {
            // 在方法结束后插入切面逻辑，例如打印日志
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("After method execution");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitInsn(opcode);
    }
}
