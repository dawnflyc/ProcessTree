package com.github.dawnflyc.processtree.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 一系列类反射方法
 */
public class ClassHelper {
    /**
     * 缓存
     */
    private static HashMap<String, Set<Class<?>>> CACHE = new HashMap<>();
    /**
     * 单例模式 实例
     */
    private static ClassHelper INSTANCE;

    private ClassHelper() {

    }

    public static ClassHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassHelper();
        }
        return INSTANCE;
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        CACHE = new HashMap<>();
    }

    /**
     * 获取缓存
     *
     * @return
     */
    public HashMap<String, Set<Class<?>>> getCache() {
        return CACHE;
    }

    /**
     * 查找类,如果包名是""那么查找范围为jar包里全部
     *
     * @param packageName 包名
     * @param recursive   是否迭代循环
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Set<Class<?>> FindClass(String packageName, final boolean recursive) throws IOException, ClassNotFoundException {
        if (CACHE.containsKey(packageName+":"+recursive)) {
            return CACHE.get(packageName);
        }
        String packagePath = packageName.replace('.', '/');
        Set<Class<?>> set = new HashSet<>();
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(
                packagePath);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            if ("file".equals(url.getProtocol())) {
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                findClassByFile(packageName, filePath, recursive, set);
            }else if ("jar".equals(url.getProtocol())){
                findClassByJar(url,packageName,set,recursive);
            }
        }
        return set;
    }

    /**
     * 递归查找类文件
     *
     * @param packageName 包名
     * @param packagePath 包路径
     * @param recursive   迭代循环
     * @param set         递归存储
     * @throws ClassNotFoundException
     */
    private void findClassByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> set) throws ClassNotFoundException {
        File PackageFile = new File(packagePath);
        if (PackageFile.exists() && PackageFile.isDirectory()) {
            File[] ClassFiles = PackageFile.listFiles(pathname -> (recursive && pathname.isDirectory()) || (pathname.getName().endsWith(".class")));
            for (File classFile : ClassFiles) {
                if (classFile.isDirectory()) {
                    findClassByFile(packageName + (packageName.trim().length() > 0 ? "." : "") + classFile.getName(), classFile.getAbsolutePath(), recursive, set);
                } else {
                    String className = classFile.getName().substring(0, classFile.getName().length() - 6);
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                    set.add(clazz);
                    saveCache(packageName,clazz,recursive);
                }
            }
        }
    }

    /**
     * 查找jar 类
     * @param url
     * @param packageName
     * @param set
     */
    private void findClassByJar(URL url ,String packageName, Set<Class<?>> set,final boolean recursive){
        try {
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            JarFile jarFile = connection.getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jar = jarEntries.nextElement();
                if(jar.isDirectory() || !jar.getName().endsWith(".class")) {
                    continue;
                }
                String jarName = jar.getName();
                jarName = jarName.replace(".class", "");
                jarName = jarName.replace("/", ".");
                try {
                    Class<?> clazz = Class.forName(jarName);
                    set.add(clazz);
                    saveCache(packageName,clazz,recursive);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存为缓存
     * @param packageName
     * @param clazz
     */
    private void saveCache(String packageName,Class<?> clazz,boolean recursive){
        //缓存
        String[] pack = packageName.split("\\.");
        for (int i = 0; i < pack.length; i++) {
            String p = pack[i];
            String temp = "";
            for (int j = 0; j < i; j++) {
                temp += pack[j] + ".";
            }
            p = temp + p;
            Set<Class<?>> set1 = CACHE.get(p);
            if (set1 == null) {
                set1 = new HashSet<>();
                CACHE.put(p+":"+recursive, set1);
            }
            set1.add(clazz);
        }
    }
}

