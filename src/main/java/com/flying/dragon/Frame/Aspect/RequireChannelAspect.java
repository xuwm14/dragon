package com.flying.dragon.Frame.Aspect;

import com.flying.dragon.Base.Annotation.RequireChannel;
import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Base.Error.MyError;
import com.flying.dragon.Base.Error.SystemErrorEnum;
import com.flying.dragon.Frame.Util.ThreadUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @描述 渠道限制切面，在指定的渠道中才可以访问对应的函数
 **/
@Aspect
@Component
public class RequireChannelAspect {
    /** 渠道限制控制器，所有带 com.flying.dragon.Base.Annotation.RequireChannel 注解的函数在执行前都会进入此切面 */
    @Around("@annotation(com.flying.dragon.Base.Annotation.RequireChannel)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        /** 获取用户 */
        AccessChannel channel = ThreadUtil.getChannel();
        if (channel == null)
            throw new MyError(SystemErrorEnum.ACCESS_CHANNEL_ERROR);
        /** 获取能够访问的用户类型 */
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        RequireChannel requireChannel = signature.getMethod().getAnnotation(RequireChannel.class);
        AccessChannel[] channels = requireChannel.value();

        /** 用户类型长度为0说明不限制用户权限 */
        boolean canAccess = false;
        for (AccessChannel c:channels)
            if (c.equals(channel)) {
                canAccess = true;
                break;
            }
        if (!canAccess)
            throw new MyError(SystemErrorEnum.ACCESS_CHANNEL_ERROR);
        /** 执行业务内部逻辑 */
        return pjp.proceed();
    }
}
