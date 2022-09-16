package com.dawnflyc.processtree.data;

import com.dawnflyc.processtree.ScanNode;
import com.dawnflyc.processtree.util.ScanHelper;

/**
 * 扫描数据类
 */
public class ScanNodeData {
    /**
     * 包名
     */
    private final String packageName;
    /**
     * 扫描类
     */
    private final Class<?> scanClass;
    /**
     * 优先级
     */
    private final int priority;
    /**
     * 扫描目标
     */
    private final Class<?> target;

    public ScanNodeData(Class<?> scanClass, ScanNode scanNode) {
        this.packageName = ScanHelper.escape(scanClass, scanNode.packageName());
        this.priority = scanNode.priority();
        this.target = scanNode.target();
        this.scanClass = scanClass;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getPriority() {
        return priority;
    }

    public Class<?> getScanClass() {
        return scanClass;
    }

    public Class<?> getTarget() {
        return target;
    }
}
