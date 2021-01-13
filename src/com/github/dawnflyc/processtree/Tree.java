/*
package com.github.dawnflyc.processtree;

import com.github.dawnflyc.processtree.util.ClassHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

*/
/**
 * 树流程主类
 * <p>
 * 扫描包名
 * <p>
 * 是否迭代包扫描
 * <p>
 * 开始扫描运行
 * <p>
 * 处理
 *
 * @param treeScan 扫描实体
 * <p>
 * 包名转义
 * @param clazz 注解类
 * @param str   包名
 * @return 转义后的包
 * <p>
 * 读取注解
 * @param scan 读取的类
 * @return 扫描注解
 * <p>
 * 注解转为TreeScanClass并根据优先级排序
 * @param set 排序集合
 * @return 排序返回
 * <p>
 * 扫描包
 * @param packageName 包名
 * @param recursive   是否扫描包内包
 * @return 扫描结果
 * <p>
 * set根据目标过滤
 * @param set    扫描结果
 * @param method 方法
 * @return 过滤后的set
 * <p>
 * 扫描结果处理
 * @param clazz    注解类
 * @param treeScan 扫描实体
 * @param set      扫描结果
 * <p>
 * 扫描实体类
 * <p>
 * 包名
 * <p>
 * 迭代循环
 * <p>
 * 优先级
 * <p>
 * 扫描目标
 * <p>
 * 注解类
 *//*

public class Tree {

    */
/**
 * 扫描包名
 * <p>
 * 是否迭代包扫描
 * <p>
 * 开始扫描运行
 * <p>
 * 处理
 *
 * @param treeScan 扫描实体
 * <p>
 * 包名转义
 * @param clazz 注解类
 * @param str   包名
 * @return 转义后的包
 * <p>
 * 读取注解
 * @param scan 读取的类
 * @return 扫描注解
 * <p>
 * 注解转为TreeScanClass并根据优先级排序
 * @param set 排序集合
 * @return 排序返回
 * <p>
 * 扫描包
 * @param packageName 包名
 * @param recursive   是否扫描包内包
 * @return 扫描结果
 * <p>
 * set根据目标过滤
 * @param set    扫描结果
 * @param method 方法
 * @return 过滤后的set
 * <p>
 * 扫描结果处理
 * @param clazz    注解类
 * @param treeScan 扫描实体
 * @param set      扫描结果
 * <p>
 * 扫描实体类
 * <p>
 * 包名
 * <p>
 * 迭代循环
 * <p>
 * 优先级
 * <p>
 * 扫描目标
 * <p>
 * 注解类
 *//*

    protected final String packageName;
    */
/**
 * 是否迭代包扫描
 *//*

    protected final boolean recursive;

    public Tree(String packageName) {
        this.packageName = packageName;
        this.recursive = true;
    }

    public Tree(String packageName, boolean recursive) {
        this.packageName = packageName;
        this.recursive = recursive;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isRecursive() {
        return recursive;
    }

    */
/**
 * 开始扫描运行
 *//*

    public void run() {
        Set<Class<?>> set = scanPackage(packageName, recursive);
        List<TreeScanClass> treeScanClasses = findAndSort(set);
        treeScanClasses.forEach(this::handle);
    }

    */
/**
 * 处理
 *
 * @param treeScan 扫描实体
 *//*

    protected void handle(TreeScanClass treeScan) {
        if (treeScan != null && treeScan.getPackageName() != null && treeScan.getMethon() != null) {
            Set<Class<?>> set = filterMetod(scanPackage(treeScan.getPackageName(), treeScan.isRecursive()), treeScan.getMethon());
            resultHandle(treeScan.getAnnotation(), treeScan, set);
        }
    }

    */
/**
 * 包名转义
 *
 * @param clazz 注解类
 * @param str   包名
 * @return 转义后的包
 *//*

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

    */
/**
 * 读取注解
 *
 * @param scan 读取的类
 * @return 扫描注解
 *//*

    protected ScanNode readTreeScan(Class<?> scan) {
        if (scan.isAnnotationPresent(ScanNode.class)) {
            return scan.getAnnotation(ScanNode.class);
        }
        return null;
    }

    */
/**
 * 注解转为TreeScanClass并根据优先级排序
 *
 * @param set 排序集合
 * @return 排序返回
 *//*

    protected List<TreeScanClass> findAndSort(Set<Class<?>> set) {
        List<TreeScanClass> list = new ArrayList<>();
        set.forEach(aClass -> {
            ScanNode treeScan = readTreeScan(aClass);
            if (treeScan != null) {
                TreeScanClass treeScanClass = new TreeScanClass(aClass, treeScan);
                list.add(treeScanClass);
            }
        });
        list.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        return list;
    }

    */
/**
 * 扫描包
 *
 * @param packageName 包名
 * @param recursive   是否扫描包内包
 * @return 扫描结果
 *//*

    protected Set<Class<?>> scanPackage(String packageName, boolean recursive) {
        Set<Class<?>> set = null;
        try {
            set = ClassHelper.getInstance().FindClass(packageName, recursive);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return set;
    }


    */
/**
 * set根据目标过滤
 *
 * @param set    扫描结果
 * @param method 方法
 * @return 过滤后的set
 *//*

    protected Set<Class<?>> filterMetod(Set<Class<?>> set, Class<?> method) {
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


    */
/**
 * 扫描结果处理
 *
 * @param clazz    注解类
 * @param treeScan 扫描实体
 * @param set      扫描结果
 *//*

    protected void resultHandle(Class<?> clazz, TreeScanClass treeScan, Set<Class<?>> set) {
        if (clazz != null) {
            if (IScanResultHandler.class.isAssignableFrom(clazz)) {
                Result result = new Result(clazz, set);
                try {
                    ((IScanResultHandler) (clazz.newInstance())).handle(result);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    */
/**
 * 扫描实体类
 *//*

    public class TreeScanClass {
        */
/**
 * 包名
 *//*

        private final String packageName;
        */
/**
 * 迭代循环
 *//*

        private final boolean recursive;
        */
/**
 * 优先级
 *//*

        private final int priority;
        */
/**
 * 扫描目标
 *//*

        private final Class<?> target;
        */
/**
 * 注解类
 *//*

        private final Class<?> annotation;

        public TreeScanClass(Class<?> aClass, ScanNode treeScan) {
            this.packageName = escape(aClass, treeScan.packageName());
            this.recursive = treeScan.recursive();
            this.priority = treeScan.priority();
            this.target = treeScan.target();
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
            return target;
        }

        public Class<?> getAnnotation() {
            return annotation;
        }
    }

}
*/
