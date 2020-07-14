package dawnfly;

/**
 * @program: ProcessTree
 * @description: 支注解
 * @author: xiaofei
 * @create: 2018-12-10 16:19
 **/

import java.lang.annotation.*;

/**
 * 支注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TreeHandle {
    /**
     * 操作类的范围
     * @return
     */
    String packageName() default "";

    /**
     * 为了更好的控制，一般是个接口，子支实现
     * @return
     */
    Class API();
}
