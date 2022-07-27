package io.github.stylesmile.ioc;

/**
 * @author Stylesmile
 */
public class BeanKey {
    private Class<?> cla;
    private String name;

    public Class getCla() {
        return cla;
    }

    public void setCla(Class cla) {
        this.cla = cla;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BeanKey(Class<?> cla, String name) {
        this.cla = cla;
        this.name = name;
    }
}