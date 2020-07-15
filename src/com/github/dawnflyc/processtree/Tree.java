package com.github.dawnflyc.processtree;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 树流程主类
 */
public class Tree {
    /**
     * 扫描包名
     */
    protected final String packageName;

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
        Set<Class<?>> set=scan(packageName,true,null);
        set.forEach(aClass -> {
            TreeScan scan=readTreeScan(aClass);
            if (scan!=null){handle(scan,aClass);}
        });
    }

    /**
     * 递归处理
     * @param treeScan
     * @param clazz
     */
    protected void handle(TreeScan treeScan,Class<?> clazz) {
        String packageName=treeScan.packageName();
        boolean recursive=treeScan.recursive();
        Class<?> method=treeScan.method();
        if (packageName!=null && method!=null){
            Set<Class<?>> set=scan(packageName,recursive,method);
            if (!resultHandle(clazz,set)){
                set.forEach(aClass -> {
                    TreeScan scan=readTreeScan(aClass);
                    if (scan!=null){handle(scan,aClass);}
                });
            }
        }

    }

    /**
     * 读取注解
     * @param scan
     * @return
     */
    protected TreeScan readTreeScan(Class<?> scan) {
        if (scan.isAnnotationPresent(TreeScan.class)){
            return scan.getAnnotation(TreeScan.class);
        }
        return null;
    }

    /**
     * 批量读取注解
     * @param scans set
     * @return
     */
    protected Set<TreeScan> readTreeScan(Set<Class<?>> scans) {
        Set<TreeScan> treeScans = new HashSet<>();
        scans.forEach(aClass -> {
            TreeScan treeScan=readTreeScan(aClass);
            if (treeScan!=null){
                treeScans.add(treeScan);
            }
        });
        return treeScans;
    }

    /**
     * 扫描
     * @param packageName 包名
     * @param recursive 是否扫描包内包
     * @param method 继承此类的被扫描
     * @return
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
     * set过滤
     * @param set
     * @param method
     */
    protected Set<Class<?>> filter(Set<Class<?>> set, Class<?> method) {
        Set<Class<?>> set1=new HashSet<>();
        if (set != null && method != null) {
            set.forEach(aClass -> {
                if (method.isAssignableFrom(aClass) && !method.equals(aClass)){
                    set1.add(aClass);
                }
            });
            return set1;
        }
        return set;
    }

    /**
     * 结果处理
     * @param clazz 处理类
     * @param set 处理set
     * @return
     */
    protected boolean resultHandle(Class<?> clazz, Set<Class<?>> set) {
        if (clazz != null) {
            if (ITreeHandler.class.isAssignableFrom(clazz)) {
                try {
                    ((ITreeHandler)(clazz.newInstance())).handle(new Result(set));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;
                }
                return false;
            }
        throw new NullPointerException("要处理的类为空！");
        }

    }
