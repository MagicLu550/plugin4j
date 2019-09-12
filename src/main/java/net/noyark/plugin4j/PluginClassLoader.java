package net.noyark.plugin4j;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@NoyarkCore(name = "plugin4j",version = "beta01",description = "a plugin load framework")
public class PluginClassLoader extends URLClassLoader implements IPluginClassLoader{

    private static Map<String,Map<JarFile,JarEntry>> entriesMap = new HashMap<>();

    private PluginConfig config;

    private List<Handler> handlers = new ArrayList<Handler>();


    public PluginClassLoader(PluginConfig config) throws IOException {
        super(urls(config),getClassLoader());
        this.config = config;
    }

    /**
     * 获取urls
     * @param config
     * @return
     * @throws IOException
     */
    private static URL[] urls(PluginConfig config) throws IOException{

        File file = new File(config.getFile());
        File[] files = file.listFiles();

        if(files!=null){
            URL[] urls = new URL[files.length];
            int index = 0;
            for(File f:files){
                if(checkIsThisFile(config,f.getName())) {
                    JarFile jarFile = new JarFile(f);

                    urls[index] = f.toURI().toURL();

                    init(jarFile,config.getFile());
                }
            }
            return urls;
        }
        return new URL[0];
    }



    private static void init(JarFile jarFile,String parent){
        if(entriesMap.get(parent).containsKey(jarFile)){
            return;//已经加载过了则不需要了
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            if(entriesMap.get(parent)==null)entriesMap.put(parent,new HashMap<>());
            else entriesMap.get(parent).put(jarFile,entry);
        }
    }

    private static ClassLoader getClassLoader(){
        return PluginClassLoader.class.getClassLoader();
    }

    private String getEntryClassName(JarEntry entry){
        return entry.getName().substring(0,entry.getName().lastIndexOf(".")).replace("/",".");
    }

    public Map<JarFile, JarEntry> getEntriesMap() {
        return entriesMap.get(config.getFile());
    }



    public void setConfig(PluginConfig config) {
        this.config = config;
    }

    @Override
    public List<IPluginData> loadPlugins()  {
        List<IPluginData> datas = new ArrayList<>();
        //加载插件,扫描每个文件的类文件
        Map<JarFile,JarEntry> entryMap = entriesMap.get(config.getFile());
        for(JarFile f:entryMap.keySet()){
            PluginData data = new PluginData();
            Enumeration<JarEntry> entries = f.entries();
            while (entries.hasMoreElements()){
                JarEntry e = entries.nextElement();
                if(e.getName().endsWith(".class")){
                    handleClass(data,e);
                }else{
                    handleResources(data,e,f);
                }
            }
            datas.add(data);
        }
        return datas;
    }


    private void handleResources(PluginData data,JarEntry e,JarFile f){
        try {
            data.getResources().add(f.getInputStream(e));
        }catch (IOException e2){
            e2.printStackTrace();
        }
    }

    private void handleClass(PluginData data,JarEntry e){
        String className = getEntryClassName(e);
        try {
            Class<?> clz = this.loadClass(className);
            if(config.getAnnotation()==null){
                if(config.getBase().equals(getSuperClass(clz))){
                    handle(true,clz);
                    data.setMainClass(clz);
                }else{
                    handle(false,clz);
                    data.getClasses().add(clz);
                }

            }else{
                if(clz.getAnnotation(config.getAnnotation())!=null){
                    handle(true,clz);
                    data.setMainClass(clz);
                }else{
                    handle(false,clz);
                    data.getClasses().add(clz);
                }
            }

        }catch (ClassNotFoundException e1){
            e1.printStackTrace();
        }
    }
    private void handle(boolean isMain,Class clz){
        if(isMain){
            handlers.forEach((x)->x.handleMain(clz));
        }else{
            handlers.forEach((x)->x.handle(clz));
        }
    }

    //检查这个文件名称是否符合,不符合返回false
    private static boolean checkIsThisFile(PluginConfig config,String fileName){
        List<String> patterns = config.getPatterns();
        boolean isFile = false;
        if(patterns==null||patterns.isEmpty()){
            isFile = true;
        }else{
            for(String s:patterns){
                if(fileName.matches(s)){
                    isFile = true;
                    if(config.getGetFilePattern()==PluginConfig.OR)
                        break;
                }else{
                    isFile = false;
                }
            }
        }

        return isFile;
    }



    private static Class getSuperClass(Class<?> clz){
        if(clz.getSuperclass()==Object.class){
            return Object.class;
        }
        while (true){
            clz = clz.getSuperclass();
            if(clz.getSuperclass()==Object.class){
                return clz;
            }
        }
    }

    public List<Handler> getHandlers() {
        return handlers;
    }
}
