package com.github.dawnflyc.example;

import com.github.dawnflyc.processtree.ITreeHandler;
import com.github.dawnflyc.processtree.Result;
import com.github.dawnflyc.processtree.TreeScan;

import java.util.Set;

@TreeScan(packageName = "com.github.dawnflyc.example",priority = -100,method =Item.class)
public class ItemRegister implements ITreeHandler<Item> {

    @Override
    public void handle(Result<Item> result) {
       Set<Item> set= result.build();
       set.forEach(item -> item.setCanRegistry(true));
    }
}
