package net.noyark.plugin4j;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class PluginConfig {

    public static final int AND = 1;

    public static final int OR = 0;

    private String file = "";

    private List<String> patterns;

    private int getFilePattern = 0;

    private Class base;

    private Class<? extends Annotation> annotation;

    private List<String> packages;//拦截的包名前缀

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public void setGetFilePattern(int getFilePattern) {
        this.getFilePattern = getFilePattern;
    }

    public List<String> getPackages() {
        return packages;
    }

    public int getGetFilePattern() {
        return getFilePattern;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setRegex(String... file){
        this.patterns = Arrays.asList(file);
    }


    public void setPluginBase(Class base){
        this.base = base;
    }

    public void setBaseAnnotation(Class<? extends Annotation> annotation){
        this.annotation = annotation;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public Class getBase() {
        return base;
    }


    public List<String> getPatterns() {
        return patterns;
    }
}
