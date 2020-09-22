package com.github.dawnflyc.processtree;


/**
 * 处理扫描数据
 * 扫描类如果继承此接口便可获取扫描数据
 * 如果不实现，那么扫描结果中有类注解扫描将会继续扫描
 */
public interface ITreeHandler<T> {

    /**
     * 处理方法
     *
     * @param result 扫描返回的接口
     */
    void handle(Result<T> result);
}
