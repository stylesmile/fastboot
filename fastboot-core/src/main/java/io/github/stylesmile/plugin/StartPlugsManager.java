package io.github.stylesmile.plugin;


import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * 插件管理器
 *
 * @author Stylesmile
 */
public class StartPlugsManager implements Plugin {
    private final Set<String> plugPackages = new HashSet<>();
    private final Set<Plugin> obj = new HashSet<>();

    /**
     * 启动时 初始化所有插件
     */
    public StartPlugsManager() {
        //通过spi加载所有插件
        ServiceLoader<Plugin> loadedParsers = ServiceLoader.load(Plugin.class);
        for (Plugin plugin : loadedParsers) {
            obj.add(plugin);
            plugPackages.add(plugin.getClass().getPackage().getName());
        }
    }

    public Set<String> getPlugPackages() {
        return plugPackages;
    }

    public void addPlugins(Class... plugsClass) {
        for (Class aClass : plugsClass) {
            try {
                obj.add((Plugin) aClass.newInstance());
                plugPackages.add(aClass.getPackage().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        for (Plugin plugAdapter : obj) {
            plugAdapter.start();
        }
    }

    @Override
    public void init() {
        for (Plugin plugAdapter : obj) {
            plugAdapter.init();
        }
    }

    @Override
    public void end() {
        for (Plugin plugAdapter : obj) {
            plugAdapter.end();
        }
    }

}
