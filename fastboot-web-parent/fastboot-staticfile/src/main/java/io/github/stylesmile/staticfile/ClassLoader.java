package io.github.stylesmile.staticfile;

/**
 * ClassLoader
 */
public class ClassLoader extends java.lang.ClassLoader {
    private String[] ignored_packages = {
            "java.", "javax.", "sun."
    };
    private java.lang.ClassLoader deferTo = ClassLoader.getSystemClassLoader();

    public ClassLoader() {
    }

    public ClassLoader(java.lang.ClassLoader deferTo) {
        this.deferTo = deferTo;
    }

    /**
     * @param ignored_packages classes contained in these packages will be loaded
     *                         with the system class loader
     */
    public ClassLoader(String[] ignored_packages) {
        addIgnoredPkgs(ignored_packages);
    }

    public ClassLoader(java.lang.ClassLoader deferTo, String[] ignored_packages) {
        this.deferTo = deferTo;
        addIgnoredPkgs(ignored_packages);
    }

    private void addIgnoredPkgs(String[] ignored_packages) {
        String[] new_p = new String[ignored_packages.length + this.ignored_packages.length];

        System.arraycopy(this.ignored_packages, 0, new_p, 0, this.ignored_packages.length);
        System.arraycopy(ignored_packages, 0, new_p, this.ignored_packages.length,
                ignored_packages.length);

        this.ignored_packages = new_p;
    }
}
