package net.noyark.plugin4j;


import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 用于加载插件的类加载器,这里定义了插件类加载器的特质部分
 * 也是加载框架的核心部分
 * @author magiclu550
 */
public interface IPluginClassLoader {

    /**
     * 用于设置配置信息,以控制加载流程
     * @param config 插件加载信息的配置文件
     */
    void setConfig(PluginConfig config);

    /**
     * 把父文件下的插件全部加载出来
     * @return
     */
    List<IPluginData> loadPlugins();

    /**
     * 加入切面监听器,监听类被加载,并进行相应的处理
     * @return 类加载器对象
     */
    List<Handler> getHandlers();

    Map<JarFile, JarEntry> getEntriesMap();
}
