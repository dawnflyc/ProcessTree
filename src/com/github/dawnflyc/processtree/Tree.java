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

    public Tree(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    protected final Map<String,Object> instances=new HashMap<>();

    /**
     * 开始扫描运行
     */
    public void run() {
        Set<Class<?>> set=scan(packageName,true,null);
        List<TreeScanClass> treeScanClasses=sort(set);
        treeScanClasses.forEach(treeScanClass -> handle(treeScanClass.aClass,treeScanClass.treeScan));
    }

    /**
     * 处理
     * @param clazz
     */
    protected void handle(Class<?> clazz,TreeScan treeScan) {
        if (treeScan!=null) {
            String packageName = treeScan.packageName();
            boolean recursive = treeScan.recursive();
            Class<?> method = treeScan.method();
            if (packageName != null && method != null) {
                Set<Class<?>> set = scan(packageName, recursive, method);
                this.instances.putAll(resultHandle(clazz, treeScan, set, this.instances));
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
     * 批量读取注解并排序
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
        System.out.println();
        return treeScans;
    }

    protected List<TreeScanClass> sort(Set<Class<?>> set){
        List<TreeScanClass> list=new ArrayList<>();
        set.forEach(aClass -> {
            TreeScan treeScan=readTreeScan(aClass);
            if (treeScan!=null){
                TreeScanClass treeScanClass=new TreeScanClass(aClass,treeScan);
                list.add(treeScanClass);
            }
        });
        Collections.sort(list,(o1, o2) -> o2.getPriority()-o1.getPriority());
        return list;
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
    protected Map<String,Object> resultHandle(Class<?> clazz, TreeScan treeScan, Set<Class<?>> set,Map<String,Object> map) {
        if (clazz != null) {
            if (ITreeHandler.class.isAssignableFrom(clazz)) {
                Result result=new Result(clazz,treeScan.method(),set,map);
                try {
                    ((ITreeHandler)(clazz.newInstance())).handle(result);
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

        public class TreeScanClass{
            private int priority;
            private Class<?> aClass;
            private TreeScan treeScan;

            public TreeScanClass(Class<?> aClass, TreeScan treeScan) {
                this.aClass = aClass;
                this.treeScan = treeScan;
                this.priority=treeScan.priority();
            }

            public void setaClass(Class<?> aClass) {
                this.aClass = aClass;
            }

            public void setTreeScan(TreeScan treeScan) {
                this.treeScan = treeScan;
                this.priority=treeScan.priority();
            }

            public int getPriority() {
                return priority;
            }

            public Class<?> getaClass() {
                return aClass;
            }

            public TreeScan getTreeScan() {
                return treeScan;
            }
        }

    }
