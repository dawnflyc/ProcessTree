package com.github.dawnflyc.processtree;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 根据包获取类
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
        this.CACHE = new HashMap<>();
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
     * 查找类,如果包名是“”那么查找范围为jar包
     *
     * @param packageName 包名
     * @param recursive   是否迭代循环
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Set<Class<?>> FindClass(String packageName, final boolean recursive) throws IOException, ClassNotFoundException {
        if (CACHE.containsKey(packageName)) {
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
            }
        }
        return set;
    }

    /**
     * 递归查找类
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
                    findClassByFile(packageName + (packageName.trim().length()>0 ? "." : "") + classFile.getName(), classFile.getAbsolutePath(), recursive, set);
                } else {
                    String className = classFile.getName().substring(0, classFile.getName().length() - 6);
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                    set.add(clazz);
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
                            CACHE.put(p, set1);
                        }
                        set1.add(clazz);
                    }
                }
            }
        }
    }
}
