package com.dawnflyc.processtree.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 帮助扫描的方法
 */
public class ScanHelper  {

    /**
     * 查找继承该类的类
     * @param packageName 包名
     * @param target 目标类
     */
    public static <T> Set<Class<? extends T>> scanByExtend(String packageName, Class<T> target){
       return new Reflections(packageName).getSubTypesOf(target);
    }

    /**
     * 根据注解扫
     * @param packageName 包名
     * @param target 目标注解
     * @return
     */
    public static Set<Class<?>> scanByAnnotated(String packageName, Class<? extends Annotation> target){
        return new Reflections(packageName).getTypesAnnotatedWith(target);
    }

    /**
     * 包转义
     *
     * @param scanClass   注解类
     * @param packageName 包名
     * @return 转义后的包
     */
    public static String escape(Class<?> scanClass, String packageName) {
        String newPackageName = null;
        switch (packageName) {
            case "auto":
                newPackageName = scanClass.getPackage().getName();
                break;
            case "all":
                newPackageName = "";
                break;
        }
        return newPackageName == null ? packageName : newPackageName;
    }


}
