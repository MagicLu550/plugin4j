package net.noyark.test;

public class Super {

    public static void main(String[] args) {
        System.out.println(getSuperClass(Super.class));
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
}
