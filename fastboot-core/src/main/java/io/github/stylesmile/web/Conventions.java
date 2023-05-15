//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.stylesmile.web;

import com.sun.istack.internal.Nullable;
import io.github.stylesmile.tool.ClassUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Iterator;

public final class Conventions {
    private static final String PLURAL_SUFFIX = "List";

    private Conventions() {
    }

    public static String getVariableName(Object value) {
        if(value == null){
            throw new RuntimeException("Value must not be null");
        }
        // Assert.notNull(value, "Value must not be null");
        boolean pluralize = false;
        Class valueClass;
        if (value.getClass().isArray()) {
            valueClass = value.getClass().getComponentType();
            pluralize = true;
        } else if (value instanceof Collection) {
            Collection<?> collection = (Collection)value;
            if (collection.isEmpty()) {
                throw new IllegalArgumentException("Cannot generate variable name for an empty Collection");
            }

            Object valueToCheck = peekAhead(collection);
            valueClass = getClassForValue(valueToCheck);
            pluralize = true;
        } else {
            valueClass = getClassForValue(value);
        }

        String name = ClassUtil.getShortNameAsProperty(valueClass);
        return pluralize ? pluralize(name) : name;
    }


    private static Class<?> getClassForValue(Object value) {
        Class<?> valueClass = value.getClass();
        if (Proxy.isProxyClass(valueClass)) {
            Class<?>[] ifcs = valueClass.getInterfaces();
//            Class[] var3 = ifcs;
//            int var4 = ifcs.length;

//            for(int var5 = 0; var5 < var4; ++var5) {
//                Class<?> ifc = var3[var5];
////                if (!ClassUtils.isJavaLanguageInterface(ifc)) {
//                if (!isJavaLanguageInterface(ifc)) {
//                    return ifc;
//                }
//            }
        } else if (valueClass.getName().lastIndexOf(36) != -1 && valueClass.getDeclaringClass() == null) {
            valueClass = valueClass.getSuperclass();
        }

        return valueClass;
    }
//    public static boolean isJavaLanguageInterface(Class<?> ifc) {
//        return javaLanguageInterfaces.contains(ifc);
//    }

    private static String pluralize(String name) {
        return name + "List";
    }

    private static <E> E peekAhead(Collection<E> collection) {
        Iterator<E> it = collection.iterator();
        if (!it.hasNext()) {
            throw new IllegalStateException("Unable to peek ahead in non-empty collection - no element found");
        } else {
            E value = it.next();
            if (value == null) {
                throw new IllegalStateException("Unable to peek ahead in non-empty collection - only null element found");
            } else {
                return value;
            }
        }
    }
}
