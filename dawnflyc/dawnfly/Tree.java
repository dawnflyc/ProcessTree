package dawnfly;

import dawnfly.util.ClassUtil;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: ProcessTree
 * @description: 树主业务
 * @author: xiaofei
 * @create: 2018-12-10 16:43
 **/
public class Tree {

    private Class handle;

    private Object returnInstance;

    private String packageNane;

    private Class api;

    public Tree(Class handle,Object returnInstance) {
        this.handle = handle;
        this.returnInstance=returnInstance;
        init();
    }

    private void init(){
        TreeHandle treeHandle= (TreeHandle) handle.getAnnotation(TreeHandle.class);
        this.packageNane=treeHandle.packageName();
        this.api=treeHandle.API();
    }

    public void run() throws InvocationTargetException, IllegalAccessException {
        //获取所有的分支
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> aClass : ClassUtil.getClasses(this.packageNane)) {
            if(this.api.isAssignableFrom(aClass) && !this.api.equals(aClass)){
                classes.add(aClass);
            }
        }
        //获取带TreeNotice的方法
        for (Method method : handle.getMethods()) {
            if(method.isAnnotationPresent(TreeNotice.class)){
                if(method.getParameters().length==1){
                    Parameter parameter=method.getParameters()[0];
                    //根据参数的类型执行方法
                    if(parameter.getType().equals(this.api.getClass())){
                        for (Class<?> aClass : classes) {
                            method.invoke(this.returnInstance,new Class[]{aClass});
                        }
                    }else if (parameter.getType().equals(Array.newInstance(this.api.getClass(),0).getClass())){
                        method.invoke(this.returnInstance,new Class[][]{(Class[]) classes.toArray(new Class[0])});
                    }
                }
            }
        }

    }


    public Class getHandle() {
        return handle;
    }

    public void setHandle(Class handle) {
        this.handle = handle;
        init();
    }

    public String getPackageNane() {
        return packageNane;
    }

    public Class getApi() {
        return api;
    }
}
