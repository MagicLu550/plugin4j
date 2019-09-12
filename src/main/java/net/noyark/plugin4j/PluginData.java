package net.noyark.plugin4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PluginData implements IPluginData{

    private Class mainClass;

    private List<Class> classes = new ArrayList<>();

    private List<InputStream> resources = new ArrayList<>();


    void setMainClass(Class mainClass) {
        this.mainClass = mainClass;
    }

    public Class getMainClass() {
        return mainClass;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public List<InputStream> getResources() {
        return resources;
    }
}
