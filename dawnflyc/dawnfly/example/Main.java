package dawnfly.example;

import dawnfly.Tree;

import java.lang.reflect.InvocationTargetException;

/**
 * @program: ProcessTree
 * @description: main
 * @author: xiaofei
 * @create: 2018-12-10 17:42
 **/
public class Main {

    public static void main(String[] args) {
        //树结构初始化，传入支的类型和实例
        Tree tree=new Tree(Root.class,new Root());
        try {
            //开始根据注解执行
            tree.run();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
