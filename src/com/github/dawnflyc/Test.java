package com.github.dawnflyc;

import com.github.dawnflyc.processtree.ClassHelper;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Set set= ClassHelper.getInstance().FindClass("",true);
            Map map=ClassHelper.getInstance().getCache();
            System.out.println(map);
    }
}
