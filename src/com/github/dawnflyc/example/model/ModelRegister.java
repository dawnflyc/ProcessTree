package com.github.dawnflyc.example.model;

import com.github.dawnflyc.processtree.ITreeHandler;
import com.github.dawnflyc.processtree.Result;
import com.github.dawnflyc.processtree.TreeScan;

import java.util.Set;

@TreeScan(packageName = "com.github.dawnflyc.example.model",method = IModel.class)
public class ModelRegister implements ITreeHandler<IModel> {
    @Override
    public void handle(Result<IModel> result) {
        Set<IModel> set=result.build();
        set.forEach(iModel -> iModel.setModelRegistry(true));
        System.out.println();
    }
}
