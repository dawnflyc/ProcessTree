package com.github.dawnflyc.processtree;

import java.util.Set;
import java.util.stream.Stream;

public class Result<T> {

    protected final Set<T> set;

    public Result(Set<T> set) {
        this.set = set;
    }

    public Set<T> getSet() {
        return set;
    }

    private Stream<T> getStream(){
        return set.stream();
    }
}
