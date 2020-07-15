package com.github.dawnflyc.example;

import com.github.dawnflyc.processtree.ITreeHandler;
import com.github.dawnflyc.processtree.Result;
import com.github.dawnflyc.processtree.Tree;
import com.github.dawnflyc.processtree.TreeScan;

@TreeScan(packageName = "",recursive = true,method = Test.Scan.class)
public class Test implements ITreeHandler {
    public static void main(String[] args){
        Tree tree=new Tree("");
        tree.run();
    }

    @Override
    public void handle(Result result) {
        
    }

    public static interface Scan {}
}
