package com.github.dawnflyc.processtree;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE})
/**
 * 扫描注解，注解的类或接口必须继承ITreeHandle方可处理数据
 * 被扫描的类必须要有无参构造
 */
public @interface TreeScan {
    /**
     * 扫描包
     * auto : 标注类的包名
     * all : 全部
     * @return
     */
    String packageName() default "auto";

    /**
     * 是否迭代循环，是否扫描包内包
     * @return
     */
    boolean recursive() default true;

    /**
     * 优先级
     * @return
     */
    int priority() default 0;

    /**
     * 扫描方法,接口或类
     * 如何识别你要的结果就需要此字段了
     * 比如你需要扫描所有物品
     * 那么你的method=Item.class();
     * @return
     */
    Class method();


}
