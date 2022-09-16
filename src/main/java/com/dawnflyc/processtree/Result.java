package com.dawnflyc.processtree;


import com.dawnflyc.processtree.data.ScanNodeData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * 扫描结果类
 *
 * @param <T>
 */
public class Result<T> {

    /**
     * 扫描类
     */
    protected final ScanNodeData scanNodeData;

    /**
     * 扫描结果
     */
    protected final Set<Class<T>> scanResultList;
    /**
     * 参数类型
     */
    protected Class<?>[] parameterTypes;
    /**
     * 参数数据
     */
    protected Object[] parameter;


    public Result(ScanNodeData scanNodeData, Set<Class<T>> scanResultList) {
        this.scanNodeData = scanNodeData;
        this.scanResultList = scanResultList;
    }

    /**
     * 获取扫描结果
     *
     * @return
     */
    public Set<Class<T>> getClassSet() {
        return scanResultList;
    }

    /**
     * 设置构造参数
     *
     * @param parameterTypes
     * @param parameter
     */
    public void setConstructorParameter(Class<?>[] parameterTypes, Object[] parameter) {
        this.parameterTypes = parameterTypes;
        this.parameter = parameter;
    }

    /**
     * 构造对象
     * 构造前需设置构造参数，无参构造不用设置
     *
     * @return
     */
    public Set<T> build() {
        Set<T> objects = new HashSet<>();
        this.scanResultList.forEach(aClass -> {
            T object = buildInstance(aClass);
            objects.add(object);
        });
        return objects;
    }

    /**
     * 构建实例
     *
     * @param clazz
     * @return
     */
    protected T buildInstance(Class<T> clazz) {
        if (this.parameterTypes != null && this.parameter != null) {
            return buildParamInstance(clazz, this.parameterTypes, this.parameter);
        } else {
            return buildNonParamInstance(clazz);
        }
    }

    /**
     * 构建无参构造实例
     *
     * @param clazz
     * @return
     */
    protected T buildNonParamInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("<" + clazz.getName() + "> Failed to construct an instance according to [no parameter construction]!");
        }
        return null;
    }

    /**
     * 构建有参构造实例
     *
     * @param clazz
     * @param parameterTypes
     * @param parameter
     * @return
     */
    protected T buildParamInstance(Class<T> clazz, Class<?>[] parameterTypes, Object[] parameter) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getConstructor(parameterTypes);
            return constructor.newInstance(parameter);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.err.println("<" + clazz.getName() + "> Failed to construct an instance according to [parameter construction]!");
        }
        return null;
    }

}
