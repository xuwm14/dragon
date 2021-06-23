package com.flying.dragon.Frame.Aspect;

import com.flying.dragon.Base.Annotation.Required;
import com.flying.dragon.Base.Error.MyWarn;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Frame.Util.ParseUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @描述 参数检查器，检查业务的入参是否都包含了必要元素
 **/
@Aspect
@Component
public class CheckParamsAspect {
    /** 参数检查器，所有带 com.flying.dragon.Base.Annotation.BizType 注解的函数在执行前都会进入此切面 */
    @Around("@annotation(com.flying.dragon.Base.Annotation.BizType)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        /** 获取参数 */
        Object[] params = pjp.getArgs();
        /** 如果第一个参数是标准入参，判断里面需要的元素是否都存在 */
        if (params.length > 0 && CommonInParams.class.isAssignableFrom(params[0].getClass())) {
            CommonInParams inParams = (CommonInParams)params[0];
            Field[] fields = ParseUtil.getAllFields(inParams.getClass());
            for (Field field:fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Required.class) && field.get(inParams) == null)
                    throw new MyWarn("CheckParamsError", field.getName() + "不能为空！");
            }
        }
        /** 执行业务内部逻辑 */
        return pjp.proceed();
    }
}
