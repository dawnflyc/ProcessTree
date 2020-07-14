package dawnflyc.example;

import dawnflyc.processtree.annotation.TreeHandle;

/**
 * @program: ProcessTree
 * @description: Root的分支
 * @author: xiaofei
 * @create: 2018-12-10 15:26
 **/
@TreeHandle(packageName = "dawnflyc.example",API = Branch.BranchAPI.class)
public class Branch implements Root.RootAPI {
    public Branch() {
        System.out.println("b");
    }

    public  interface BranchAPI{
    }
}
