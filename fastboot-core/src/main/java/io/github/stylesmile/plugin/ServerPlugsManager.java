package io.github.stylesmile.plugin;


import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * 插件管理器
 *
 * @author Stylesmile
 */
public class ServerPlugsManager implements ServerPlugin {
    private final Set<String> plugPackages = new HashSet<>();
    private final Set<Plugin> obj = new HashSet<>();
    /**
     * 启动时 初始化所有插件
     */
    public ServerPlugsManager() {
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
    public void start(Class applicationClass, String[] args) {
        for (Plugin plugAdapter : obj) {
            plugAdapter.start();
            // 对于服务插件，只会启动一个
            break;
        }
    }
}
