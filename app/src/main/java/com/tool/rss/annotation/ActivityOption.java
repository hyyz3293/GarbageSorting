package com.tool.rss.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Activity选项的注解
 *
 * @author mos
 * @date 2017.02.22
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActivityOption {
    /**
     * 是否需要登录
     *
     * @return true -- 是  false -- 否
     */
    boolean reqLogin() default false;
}
