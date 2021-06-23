package com.flying.dragon;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Base.Constant.NameConstant;
import com.flying.dragon.Biz.Manager.CommonManager;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Frame.Util.ParseUtil;
import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/** SpringBoot的主启动类 */
@SpringBootApplication
public class DragonApplication {

    /** 启动服务器 */
    public static void main(String[] args) {
        try {
            SpringApplication.run(DragonApplication.class, args);
            initParamMap();
            initMethodMap();
            System.out.println("服务器启动成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 初始化从业务类型到入参的map */
    public static void initParamMap() throws InvocationTargetException, IllegalAccessException {
        /** 获取所有带业务类型注解的入参 */
        Reflections reflections = new Reflections(NameConstant.PACKAGE_NAME + ".Biz.Params");
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(BizType.class);
        /** 构建map */
        for (Class<?> cls:classSet) {
            /** 只对入参构建map */
            if (!CommonInParams.class.isAssignableFrom(cls))
                continue;
            Class<CommonInParams> paramCls = (Class<CommonInParams>)cls;
            /** 获取注解的业务类型值 */
            BizType bizType1 = paramCls.getAnnotation(BizType.class);
            String bizStr = ParseUtil.getBizStr(bizType1);
            CommonInParams.paramsMap.put(bizStr, paramCls);
        }
    }

    /** 初始化从业务类型到执行函数的map */
    public static void initMethodMap() throws InvocationTargetException, IllegalAccessException {
        /** 获取所有执行业务的管理器 */
        Reflections reflections = new Reflections(NameConstant.PACKAGE_NAME + ".Biz.Manager");
        Set<Class<? extends CommonManager>> classSet = reflections.getSubTypesOf(CommonManager.class);
        for (Class<? extends CommonManager> cls:classSet) {
            /** 获取所有执行业务的函数 */
            Method[] methods = cls.getDeclaredMethods();
            /** 构建map */
            for (Method method:methods) {
                if (method.isAnnotationPresent(BizType.class)) {
                    BizType bizType1 = method.getAnnotation(BizType.class);
                    String bizStr = ParseUtil.getBizStr(bizType1);
                    CommonManager.managerMap.put(bizStr, method);
                }
            }
        }
    }

}
