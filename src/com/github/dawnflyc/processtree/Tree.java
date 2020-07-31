package com.github.dawnflyc.processtree;

import java.io.IOException;
import java.util.*;

/**
 * 树流程主类
 */
public class Tree {

    /**
     * 扫描包名
     */
    protected final String packageName;
    protected final Map<String, Object> instances = new HashMap<>();

    public Tree(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    /**
     * 开始扫描运行
     */
    public void run() {
        Set<Class<?>> set = scan(packageName, true, null);
        List<TreeScanClass> treeScanClasses = sort(set);
        treeScanClasses.forEach(treeScanClass -> handle(treeScanClass));
        System.out.println();
    }

    /**
     * 处理
     *
     * @param treeScan
     */
    protected void handle(TreeScanClass treeScan) {
        if (treeScan != null && treeScan.getPackageName() != null && treeScan.getMethon() != null) {
            Set<Class<?>> set = scan(treeScan.getPackageName(), treeScan.isRecursive(), treeScan.getMethon());
            this.instances.putAll(resultHandle(treeScan.getAnnotation(), treeScan, set, this.instances));
        }
    }

    /**
     * 包名转义
     *
     * @param clazz 注解类
     * @param str   包名
     * @return
     */
    protected String escape(Class<?> clazz, String str) {
        switch (str) {
            case "auto":
                str = clazz.getPackage().getName();
                break;
            case "all":
                str = "";
                break;
        }
        return str;
    }

    /**
     * 读取注解
     *
     * @param scan
     * @return
     */
    protected TreeScan readTreeScan(Class<?> scan) {
        if (scan.isAnnotationPresent(TreeScan.class)) {
            return scan.getAnnotation(TreeScan.class);
        }
        return null;
    }

    /**
     * 注解转为TreeScanClass并根据优先级排序
     *
     * @param set
     * @return
     */
    protected List<TreeScanClass> sort(Set<Class<?>> set) {
        List<TreeScanClass> list = new ArrayList<>();
        set.forEach(aClass -> {
            TreeScan treeScan = readTreeScan(aClass);
            if (treeScan != null) {
                TreeScanClass treeScanClass = new TreeScanClass(aClass, treeScan);
                list.add(treeScanClass);
            }
        });
        Collections.sort(list, (o1, o2) -> o2.getPriority() - o1.getPriority());
        return list;
    }

    /**
     * 扫描
     *
     * @param packageName 包名
     * @param recursive   是否扫描包内包
     * @param method      继承此类的被扫描
     * @return 扫描结果
     */
    protected Set<Class<?>> scan(String packageName, boolean recursive, Class<?> method) {
        Set<Class<?>> set = null;
        try {
            set = ClassHelper.getInstance().FindClass(packageName, recursive);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return filter(set, method);
    }


    /**
     * set根据method过滤
     *
     * @param set    扫描结果
     * @param method 扫描方法
     * @return 过滤后的set
     */
    protected Set<Class<?>> filter(Set<Class<?>> set, Class<?> method) {
        Set<Class<?>> set1 = new HashSet<>();
        if (set != null && method != null) {
            set.forEach(aClass -> {
                if (method.isAssignableFrom(aClass) && !method.equals(aClass) && !aClass.isInterface()) {
                    set1.add(aClass);
                }
            });
            return set1;
        }
        return set;
    }


    /**
     * 扫描结果处理
     *
     * @param clazz    注解类
     * @param treeScan 扫描实体
     * @param set      扫描结果
     * @param map      实例map
     * @return 实例map
     */
    protected Map<String, Object> resultHandle(Class<?> clazz, TreeScanClass treeScan, Set<Class<?>> set, Map<String, Object> map) {
        if (clazz != null) {
            if (ITreeHandler.class.isAssignableFrom(clazz)) {
                Result result = new Result(clazz, treeScan.getMethon(), set, map);
                try {
                    ((ITreeHandler) (clazz.newInstance())).handle(result);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return result.getInstance();
            }
        }
        throw new NullPointerException("要处理的类为空！");
    }

    /**
     * 扫描实体类
     */
    public class TreeScanClass {
        /**
         * 包名
         */
        private final String packageName;
        /**
         * 迭代循环
         */
        private final boolean recursive;
        /**
         * 优先级
         */
        private final int priority;
        /**
         * 扫描方法
         */
        private final Class<?> methon;
        /**
         * 注解类
         */
        private final Class<?> annotation;

        public TreeScanClass(Class<?> aClass, TreeScan treeScan) {
            this.packageName = escape(aClass, treeScan.packageName());
            this.recursive = treeScan.recursive();
            this.priority = treeScan.priority();
            this.methon = treeScan.method();
            this.annotation = aClass;
        }

        public String getPackageName() {
            return packageName;
        }

        public boolean isRecursive() {
            return recursive;
        }

        public int getPriority() {
            return priority;
        }

        public Class<?> getMethon() {
            return methon;
        }

        public Class<?> getAnnotation() {
            return annotation;
        }
    }

}
