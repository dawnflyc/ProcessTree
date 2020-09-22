package com.github.dawnflyc.processtree;

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
    protected final Class<?> scanClass;

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


    public Result(Class<?> scanClass, Set<Class<T>> scanResultList) {
        this.scanClass = scanClass;
        this.scanResultList = scanResultList;
    }

    public Set<Class<T>> getClassSet() {
        return scanResultList;
    }

    public void setConstructorParameter(Class<?>[] parameterTypes, Object[] parameter) {
        this.parameterTypes = parameterTypes;
        this.parameter = parameter;
    }

    public Set<T> build() {
        Set<T> objects = new HashSet<>();
        this.scanResultList.forEach(aClass -> {
            T object = buildInstance(aClass);
            objects.add(object);
        });
        return objects;
    }

    protected T buildInstance(Class<T> clazz) {
        if (this.parameterTypes != null && this.parameter != null) {
            return newParamInstance(clazz, this.parameterTypes, this.parameter);
        } else {
            return newNonParamInstance(clazz);
        }
    }

    protected T newNonParamInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected T newParamInstance(Class<T> clazz, Class<?>[] parameterTypes, Object[] parameter) {
        Constructor<T> constructor = null;
        try {
            constructor = clazz.getConstructor(parameterTypes);
            return constructor.newInstance(parameter);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
