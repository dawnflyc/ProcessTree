package com.github.dawnflyc.processtree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * 扫描注解，注解的类或接口必须继承ITreeHandle方可处理数据
 */
public @interface TreeScan {
    /**
     * 扫描包
     * @return
     */
    String packageName();

    /**
     * 是否迭代循环，是否扫描包内包
     * @return
     */
    boolean recursive();

    /**
     * 扫描方法
     * @return
     */
    Class method();

    /**
     * 扫描方法类型
     * @return
     */
    EnumMethod methodType();

}
