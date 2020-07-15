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
     * 扫描结果
     */
    protected final Set<Class<?>> set;

    public Result(Set<Class<?>> set) {
        this.set = set;
    }

    public Set<Class<?>> getClassSet() {
        return set;
    }

    /**
     * 获取无参构造的对象集合
     * @return
     */
    public Set<T> getNonParamSet() {
        Set<T> set = new HashSet<>();
        this.set.forEach(aClass -> {
            try {
                set.add((T) aClass.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return set;
    }

    /**
     * 获取有参构造的对象集合
     * @param parameterTypes
     * @param parameter
     * @return
     */
    public Set<T> getParamSet(Class<?>[] parameterTypes, Object[] parameter) {
        Set<T> set = new HashSet<>();
        this.set.forEach(aClass -> {
            Constructor constructor = null;
            try {
                constructor = aClass.getConstructor(parameterTypes);
                set.add((T) constructor.newInstance(parameter));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return set;
    }
}
