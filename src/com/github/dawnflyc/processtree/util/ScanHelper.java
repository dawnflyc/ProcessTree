package com.github.dawnflyc.processtree.util;

import com.github.dawnflyc.processtree.ScanNode;
import com.github.dawnflyc.processtree.data.ScanNodeData;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 帮助扫描的方法
 */
public class ScanHelper {

    /**
     * 扫描包
     *
     * @param packageName 包名
     * @param recursive   是否扫描包内包
     * @return 扫描结果
     */
    public static Set<Class<?>> scanPackage(String packageName, boolean recursive) {
        Set<Class<?>> set = null;
        try {
            set = ClassHelper.getInstance().FindClass(packageName, recursive);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Scan based on package name failed :");
            e.printStackTrace();
        }
        return set;
    }

    /**
     * 过滤set
     *
     * @param set    扫描结果
     * @param target 过滤目标
     * @return 过滤后的set
     */
    public static Set<Class<?>> filter(Set<Class<?>> set, Class<?> target) {
        Set<Class<?>> filter = new HashSet<>();
        if (set != null && target != null) {
            set.forEach(aClass -> {
                if (target.isAssignableFrom(aClass) && !target.equals(aClass) && !aClass.isInterface()) {
                    filter.add(aClass);
                }
            });
            return filter;
        }
        return set;
    }

    /**
     * 解析注解
     *
     * @param scan 读取的类
     * @return 扫描注解
     */
    public static ScanNode resolveScanNode(Class<?> scan) {
        if (scan.isAnnotationPresent(ScanNode.class)) {
            return scan.getAnnotation(ScanNode.class);
        }
        return null;
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

    /**
     * 将注解转换为数据类
     *
     * @param scanClass 扫描类
     * @return 数据类
     */
    public static ScanNodeData resolveScanNodeData(Class<?> scanClass) {
        if (scanClass != null) {
            ScanNode scanNode = resolveScanNode(scanClass);
            if (scanNode != null) {
                return new ScanNodeData(scanClass, scanNode);
            }
        }
        return null;
    }

    /**
     * 排序
     *
     * @param scanDataList 扫描数据类
     */
    public static void sort(List<ScanNodeData> scanDataList) {
        scanDataList.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
    }


}
