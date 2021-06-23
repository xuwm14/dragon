package com.flying.dragon.Base.Annotation;

import com.flying.dragon.Base.Enum.AccessChannel;

/**
 * @描述 渠道限制注解，使用此注解注释以后，只有指定的渠道才可以执行业务
 **/
public @interface RequireChannel {
    /** 需要对应的渠道才可以访问该接口 */
    AccessChannel[] value() default {};
}
