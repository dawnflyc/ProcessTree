package com.github.dawnflyc.processtree;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE})
/**
 * 扫描注解，注解的类或接口必须继承ITreeHandle方可处理数据
 */
public @interface TreeScan {
    /**
     * 扫描包
     * @return
     */
    String packageName() default "";

    /**
     * 是否迭代循环，是否扫描包内包
     * @return
     */
    boolean recursive() default true;

    /**
     * 扫描方法,接口或类
     * @return
     */
    Class method();


}
