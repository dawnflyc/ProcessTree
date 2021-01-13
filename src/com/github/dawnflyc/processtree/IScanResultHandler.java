package com.github.dawnflyc.processtree;


/**
 * 处理扫描数据
 * 扫描类如果实现此接口便可获取扫描数据，从而处理扫描数据
 */
public interface IScanResultHandler<T> {

    /**
     * 处理方法
     *
     * @param result 扫描返回的接口
     */
    void handle(Result<T> result);
}
