package com.github.dawnflyc.example.model;

import com.github.dawnflyc.example.Item;

public class Model1 extends Item implements IModel {

    private boolean modelRegistry;

    @Override
    public boolean canModelRegistry() {
        return modelRegistry;
    }

    @Override
    public void setModelRegistry(boolean mr) {
        this.modelRegistry=mr;
    }
}
