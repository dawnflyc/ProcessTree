package dawnflyc.example;

import dawnflyc.processtree.ProcessTree;

import java.lang.reflect.InvocationTargetException;

/**
 * @program: ProcessTree
 * @description: main
 * @author: xiaofei
 * @create: 2018-12-10 17:42
 **/
public class Main {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //树结构初始化，传入支的类型和实例
            //开始根据注解执行
            new ProcessTree(new Root()).run();

    }
}
