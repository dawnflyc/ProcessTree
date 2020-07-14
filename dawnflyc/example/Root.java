package dawnflyc.example;

import dawnflyc.processtree.annotation.TreeHandle;
import dawnflyc.processtree.annotation.TreeNotice;

/**
 * @program: ProcessTree
 * @description: root支
 * @author: xiaofei
 * @create: 2018-12-10 15:25
 **/
@TreeHandle(packageName = "dawnflyc.example",API = Root.RootAPI.class)
public  class Root {
    public Root() {
        System.out.println("root");
    }

    /**
     * 读取到本支的分支后返回处理，没有标注此注解的方法的话，如果分支中有TreeHandle注解，会继续下去
     * @param rootAPIClass 参数可以是Class \ Class[],调用方法根据参数类型调用的
     *                     Class是foreach调用的，分支有几个调用几次，分别传入参数
     */
    @TreeNotice
    public void print (Class<RootAPI> [] rootAPIClass){
        rootAPIClass[rootAPIClass.length-1].getName();
    }

    @TreeNotice
    public void print (Class<RootAPI>  rootAPIClass){
        System.out.println(rootAPIClass.getName());
    }

    public interface RootAPI{
    }
}