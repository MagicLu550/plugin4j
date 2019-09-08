package net.noyark.plugin4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginClassLoader implements IPluginClassLoader{

    private PluginConfig config;

    private List<Handler> handlers = new ArrayList<Handler>();

    public void setConfig(PluginConfig config) {
        this.config = config;
    }

    public List<PluginData> loadPlugins(File baseFile) throws IOException {
        if(config == null){
            config = new PluginConfig();
        }
        if(config.getBase()==null){
            throw new PluginException("no main 'Plugin' class");
        }
        List<PluginData> pluginDatas = new ArrayList<>();
        try{
            URLClassLoader classLoader = new URLClassLoader(new URL[]{new URL(baseFile.toString())},this.getClass().getClassLoader());
            File[] files = baseFile.listFiles();
            if(files!=null){
                for(File file:files){
                    boolean isFile = false;
                    if(config.getEnds()!=null){
                        for(String s:config.getEnds()){
                            if(file.toString().endsWith(s)){
                                isFile = true;
                                break;
                            }
                        }
                    }
                    if(config.getFiles()!=null){
                        for(String s:config.getFiles()){
                            if(file.getName().matches(s)){
                                isFile = true;
                                break;
                            }
                        }
                    }

                    if(config.getEnds()==null&&config.getFiles()==null){
                        isFile = true;
                    }


                    if(isFile){
                        PluginData data = new PluginData();

                        List<Class> classes = new ArrayList<>();
                        JarFile jarFile = new JarFile(file);
                        jarFile.entries();
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry1 = entries.nextElement();
                            String name = entry1.getName();
                            if (name.endsWith(".class")) {
                                String className = name.substring(0, name.lastIndexOf(".")).replace("/", ".");
                                boolean canScan = true;
                                if(config.getPackages()!=null){
                                    for(String s:config.getPackages()){
                                        if(className.contains(s)){
                                            canScan = false;
                                            break;
                                        }
                                    }
                                }

                                if(canScan){
                                    Class clz = classLoader.loadClass(className);
                                    Annotation annotation = config.getAnnotation();
                                    for(Handler handler:getHandlers()){
                                        boolean isMain = false;
                                        if(annotation==null){
                                            if(getSuperClass(clz)==config.getBase()){
                                                handler.handleMain(clz);
                                                data.setMainClass(clz);
                                                isMain = true;
                                            }
                                        }else{
                                            if(getSuperClass(clz).getAnnotation(annotation.getClass())!=null){
                                                handler.handleMain(clz);
                                                data.setMainClass(clz);
                                                isMain = true;
                                            }
                                        }
                                        if(!isMain) {
                                            handler.handle(clz);
                                            classes.add(clz);
                                        }
                                    }
                                }
                            }
                        }
                        data.setClasses(classes);
                        pluginDatas.add(data);
                    }

                }
            }
            return pluginDatas;
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return null;
    }

    private static Class getSuperClass(Class<?> clz){
        if(clz.getSuperclass()==Object.class){
            return Object.class;
        }
        while (true){
            clz = clz.getSuperclass();
            System.out.println(clz);
            if(clz.getSuperclass()==Object.class){
                return clz;
            }
        }
    }

    public List<Handler> getHandlers() {
        return handlers;
    }
}
