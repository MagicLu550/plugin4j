package net.noyark.plugin4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IPluginClassLoader {

    void setConfig(PluginConfig config);

    List<PluginData> loadPlugins(File baseFile) throws IOException;

    List<Handler> getHandlers();
}
