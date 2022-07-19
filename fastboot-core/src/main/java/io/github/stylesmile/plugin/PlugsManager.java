package io.github.stylesmile.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * 插件管理器
 *
 * @author hxm
 */
public class PlugsManager implements Plugin {
    private static final Logger log = LoggerFactory.getLogger(PlugsManager.class);

    private Set<String> plugPackages = new HashSet<>();
    private Set<Plugin> obj = new HashSet<>();

    public PlugsManager() {
        //通过spi加载所有插件
        ServiceLoader<Plugin> loadedParsers = ServiceLoader.load(Plugin.class);
        for (Plugin Plugin : loadedParsers) {
            obj.add(Plugin);
            plugPackages.add(Plugin.getClass().getPackage().getName());
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
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void start() throws IOException {
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
