package com.flying.dragon.Base.Annotation;

import com.flying.dragon.Base.Enum.UserType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @描述 登录注解，用户需要登录才可以访问对应内容
 **/
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedLogin {
    /** 需要对应的用户权限才可以访问该接口 */
    UserType[] value() default {};
}
