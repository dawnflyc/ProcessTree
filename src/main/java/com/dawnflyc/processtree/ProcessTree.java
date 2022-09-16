package com.dawnflyc.processtree;

import com.dawnflyc.processtree.data.ScanNodeData;
import com.dawnflyc.processtree.util.ScanHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dawnflyc.processtree.util.ScanHelper.*;

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
        Set<Class<?>> scanNodes = scanByAnnotated(packageName,ScanNode.class);
        List<ScanNodeData> data = nodeToData(scanNodes);
        data.forEach(this::scanNodeStart);
    }

    /**
     * 注解转换Data
     * @param annotation
     * @return
     */
    public List<ScanNodeData> nodeToData(Set<Class<?>> annotation){
        List<ScanNodeData> collect = annotation.stream().map(aClass -> new ScanNodeData(aClass, aClass.getAnnotation(ScanNode.class))).collect(Collectors.toList());
        collect.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        return collect;
    }

    /**
     * 扫描节点开始运行
     *
     * @param scanNodeData 节点数据
     */
    public void scanNodeStart(ScanNodeData scanNodeData) {
        Set<Class<?>> classes = scanByExtend(scanNodeData.getPackageName(), (Class<Object>) scanNodeData.getTarget());
        resultHandle(scanNodeData, classes);
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
                System.err.println("<" + scanNodeData.getScanClass().getName() + "> The scanning class must be non-parameter constructor, otherwise it cannot be scanned!");
            }
        }
    }
}
