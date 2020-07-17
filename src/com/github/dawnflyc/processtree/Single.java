package com.github.dawnflyc.processtree;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 单例
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface Single {
    /**
     * 名单
     * @return
     */
    Class<?> [] list() default {};

    /**
     * 白名单或者黑名单
     * @return
     */
    boolean white() default false;
}
