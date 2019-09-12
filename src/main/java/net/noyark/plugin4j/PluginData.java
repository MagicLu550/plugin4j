package net.noyark.plugin4j;

import java.util.ArrayList;
import java.util.List;

public class PluginData {

    private Class mainClass;

    private List<Class> classes = new ArrayList<>();


    void setMainClass(Class mainClass) {
        this.mainClass = mainClass;
    }

    public Class getMainClass() {
        return mainClass;
    }

    public List<Class> getClasses() {
        return classes;
    }
}
