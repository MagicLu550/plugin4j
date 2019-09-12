package net.noyark.plugin4j;


import java.util.List;

public interface IPluginClassLoader {

    void setConfig(PluginConfig config);

    List<PluginData> loadPlugins();

    List<Handler> getHandlers();
}
