package com.github.dawnflyc.processtree;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 扫描结果类
 *
 * @param <T>
 */
public class Result<T> {

    protected final Class<?> ScanClass;

    /**
     * 扫描method字段
     */
    protected final Class<?> method;

    /**
     * 扫描结果
     */
    protected final Set<Class<?>> set;
    /**
     * 参数类型
     */
    protected Class<?>[] parameterTypes;
    /**
     * 参数数据
     */
    protected Object[] parameter;
    /**
     * 实例
     */
    protected Set<Object> instances=new HashSet<>();

    protected Map<String,Object> superInstance;


    public Result(Class<?> ScanClass,Class<?> method, Set<Class<?>> set,Map<String,Object> superInstance) {
        this.ScanClass=ScanClass;
        this.method = method;
        this.set = set;
        this.superInstance=superInstance;
    }

    public Set<Class<?>> getClassSet() {
        return set;
    }

    public void setConstructorParameter(Class<?>[] parameterTypes, Object[] parameter){
        this.parameterTypes=parameterTypes;
        this.parameter=parameter;
    }

    public Set<T> build(){
        Set<T> objects=new HashSet<>();
        this.set.forEach(aClass -> {
            T object = null;
            if (aClass.isAnnotationPresent(Single.class) || method.isAnnotationPresent(Single.class)){
                Single aSingle=aClass.getAnnotation(Single.class);
                Single mSingle=method.getAnnotation(Single.class);
                Single single=mSingle!=null ? mSingle : aSingle!=null ? aSingle : null;
                List<Class<?>> list=Arrays.asList(single.list());
                boolean allow=false;
                if (single.white()){
                    allow=list.contains(this.ScanClass);
                }else {
                    allow=!list.contains(this.ScanClass);
                }
                if (allow){
                    object= (T) superInstance.get(aClass.getName());
                }
                if (object==null){
                    object=buildInstance(aClass);
                }
            }else {
                object=buildInstance(aClass);
            }
            objects.add(object);
            instances.add(object);
        });
        return objects;
    }

    public Map<String,Object> getInstance(){
        Map<String,Object> map=new HashMap<>();
        this.instances.forEach(o -> map.put(o.getClass().getName(),o));
        return map;
    }

    protected T buildInstance(Class<?> clazz){
        if (this.parameterTypes!=null && this.parameter!=null){
            return newParamInstance(clazz,this.parameterTypes,this.parameter);
        }else {
            return newNonParamInstance(clazz);
        }
    }

    protected T newNonParamInstance(Class<?> clazz){
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected T newParamInstance(Class<?> clazz,Class<?>[] parameterTypes, Object[] parameter){
        Constructor constructor = null;
        try {
            constructor = clazz.getConstructor(parameterTypes);
            return (T) constructor.newInstance(parameter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
