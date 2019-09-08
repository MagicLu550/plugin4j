package net.noyark.plugin4j;

public interface Handler {

    Object handle(Class<?> type);

    void handleMain(Class<?> main);
}
