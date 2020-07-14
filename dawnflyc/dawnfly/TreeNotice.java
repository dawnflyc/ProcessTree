package dawnfly;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: ProcessTree
 * @description: 方法通知订阅的方法，Tree获取到子支后，通过此注解注解的方法，根据参数的不同，传入不同的参数执行
 * @author: xiaofei
 * @create: 2018-12-10 18:17
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TreeNotice {
}
