package dawnfly.example;

import dawnfly.TreeHandle;
import dawnfly.TreeNotice;

/**
 * @program: ProcessTree
 * @description: root支
 * @author: xiaofei
 * @create: 2018-12-10 15:25
 **/
@TreeHandle(packageName = "dawnfly.example",API = RootAPI.class)
public  class Root {

    /**
     * 读取到本支的分支后返回处理
     * @param rootAPIClass 参数可以是Class \ Class[],调用方法根据参数类型调用的
     *                     Class是foreach调用的，分支有几个调用几次，分别传入参数
     */
    @TreeNotice
    public void print (Class<RootAPI> [] rootAPIClass){
        System.out.println(rootAPIClass.length);
    }

    @TreeNotice
    public void print (Class<RootAPI>  rootAPIClass){
        System.out.println(rootAPIClass.getName());
    }
}
