# plugin4j
##### Plugin4j提供了简便快捷的插件加载方案

```java
public class Test{
    public void test(){
        PluginConfig config = new PluginConfig();
        config.setBase(Plugin.class);
        config.setFile("./plugins");
        IPluginClassLoader loader = new PluginClassLoader();
        loader.setConfig(config);
        List<PluginData> datas = loader.loadPlugins();
    }
}

```
