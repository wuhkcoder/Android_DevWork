package com.wuhk.devworklib.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  view注解注入
 * Created by wuhk on 2016/5/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectView {

    /**view的id索引
     *
     * @return
     */
    int value() default -1;

    /**备注
     *
     * @return
     */
    String  tag() default "";
}
