package com.github.dawnflyc.example.model;

import com.github.dawnflyc.processtree.Single;

@Single
public interface IModel {

    boolean canModelRegistry();

    void setModelRegistry(boolean mr);
}
