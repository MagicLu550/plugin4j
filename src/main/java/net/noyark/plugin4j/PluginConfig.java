package net.noyark.plugin4j;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class PluginConfig {

    private List<String> files;

    private List<String> ends;

    private Class base;

    private Annotation annotation;

    private List<String> packages;//拦截的包名前缀

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setRegex(String... file){
        this.files = Arrays.asList(file);
    }

    public void setFileEnd(String... ends){
        this.ends = Arrays.asList(ends);
    }

    public void setPluginBase(Class base){
        this.base = base;
    }

    public void setBaseAnnotation(Annotation annotation){
        this.annotation = annotation;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Class getBase() {
        return base;
    }

    public List<String> getEnds() {
        return ends;
    }

    public List<String> getFiles() {
        return files;
    }
}
