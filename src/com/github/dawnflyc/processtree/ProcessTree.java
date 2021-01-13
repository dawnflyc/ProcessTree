package com.github.dawnflyc.processtree;

import com.github.dawnflyc.processtree.data.ScanNodeData;

import java.util.HashSet;
import java.util.Set;

import static com.github.dawnflyc.processtree.util.ScanHelper.*;

/**
 * 入口
 */
public class ProcessTree {

    private final String packageName;

    public ProcessTree(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 开始扫描服务
     */
    public void start() {
        Set<Class<?>> scanNodes = scanPackage(this.packageName, true);
        Set<ScanNodeData> scanNodeDatas = new HashSet<>();
        //获取扫描节点数据
        scanNodes.forEach(aClass -> {
            ScanNodeData scanNodeData = resolveScanNodeData(aClass);
            if (scanNodeData != null) {
                scanNodeDatas.add(scanNodeData);
            }
        });

        scanNodeDatas.forEach(scanNodeData -> scanNodeStart(scanNodeData));


    }

    /**
     * 扫描节点开始运行
     *
     * @param scanNodeData 节点数据
     */
    public void scanNodeStart(ScanNodeData scanNodeData) {
        //扫描
        Set<Class<?>> classSet = scanPackage(scanNodeData.getPackageName(), scanNodeData.isRecursive());
        Set<Class<?>> scanResults = filter(classSet, scanNodeData.getTarget());
        resultHandle(scanNodeData, scanResults);

    }

    /**
     * 节点扫描完成，开始处理扫描结果
     *
     * @param scanNodeData 节点数据
     * @param scanResult   扫描结果
     */
    protected void resultHandle(ScanNodeData scanNodeData, Set<Class<?>> scanResult) {
        if (IScanResultHandler.class.isAssignableFrom(scanNodeData.getScanClass())) {
            Result result = new Result(scanNodeData, scanResult);
            try {
                ((IScanResultHandler) (scanNodeData.getScanClass().newInstance())).handle(result);
            } catch (InstantiationException | IllegalAccessException e) {
                System.err.println("<" + scanNodeData.getScanClass().getName() + "> 扫描类必须为无参构造，否则无法扫描！");
            }
        }
    }
}
