package net.noyark.plugin4j;

import java.io.InputStream;
import java.util.List;

public interface IPluginData {

    Class getMainClass();

    List<Class> getClasses();

    List<InputStream> getResources();
}
