package com.flying.dragon.Base.Annotation;

import com.flying.dragon.Biz.BizType.TestBizEnum;
import com.flying.dragon.Biz.BizType.UserBizEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务类型注解，注解在类名上为指定业务的入参类，注解在方法上为指定业务的执行函数。
 * 每新建一个业务枚举类，都需要在注解里面设置对应的参数和默认业务。
 **/
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizType {
    /** 测试业务 */
    TestBizEnum testBiz() default TestBizEnum.NULL_BIZ;
    /** 用户业务 */
    UserBizEnum userBiz() default UserBizEnum.NULL_BIZ;
}
