package com.github.dawnflyc.example;

import com.github.dawnflyc.processtree.ITreeHandler;
import com.github.dawnflyc.processtree.Result;
import com.github.dawnflyc.processtree.Tree;
import com.github.dawnflyc.processtree.TreeScan;

/**
 * 扫描整个jar包内所有类
 * 方法为Scan，下面的接口
 * 那么扫描结果就是所有继承了method的类
 *
 * 实现了ITreeHandle，那么就可以用它来处理数据了
 */
@TreeScan(packageName = "",recursive = true,method = Test.Scan.class)
public class Test implements ITreeHandler<Test.Scan> {
    public static void main(String[] args){
        Tree tree=new Tree("");
        tree.run();
    }

    /**
     * \处理扫描集合
     * @param result
     */
    @Override
    public void handle(Result<Scan> result) {
    result.getNonParamSet().forEach(scan -> System.out.println(scan.getName()));
    }


    public static interface Scan {
        String getName();
    }
}
