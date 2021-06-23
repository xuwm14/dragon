package com.flying.dragon.Frame.Util;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Base.Constant.GlobalConstant;
import com.flying.dragon.Base.Constant.NameConstant;
import com.flying.dragon.Base.Error.MyWarn;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @描述 解析组件，实现各种各样的解析功能
 **/
public class ParseUtil {

    /** 警告日志不重要，只需要记录一层堆栈信息 */
    public static String getWarnStackInfo(MyWarn myWarn) {
        if (myWarn == null)
            return null;
        String stackInfo = null;
        StackTraceElement[] stackTrace = myWarn.getStackTrace();
        for (StackTraceElement stackTraceElement:stackTrace) {
            String className = stackTraceElement.getClassName();
            /** 优先记录 Manager 层的堆栈信息 */
            if (className.startsWith(NameConstant.PACKAGE_NAME + ".Biz.Manager")) {
                stackInfo = stackTraceElement.getClassName() + GlobalConstant.ARG_SPLIT
                        + stackTraceElement.getMethodName() + GlobalConstant.ARG_SPLIT
                        + stackTraceElement.getLineNumber() + GlobalConstant.STACK_SPLIT;
                break;
            }
        }
        if (stackInfo == null) {
            /** 如果没有 Manager 层堆栈信息则记录其它层的信息 */
            for (StackTraceElement stackTraceElement:stackTrace) {
                String className = stackTraceElement.getClassName();
                if (className.startsWith(NameConstant.PACKAGE_NAME)) {
                    stackInfo = stackTraceElement.getClassName() + GlobalConstant.ARG_SPLIT
                            + stackTraceElement.getMethodName() + GlobalConstant.ARG_SPLIT
                            + stackTraceElement.getLineNumber() + GlobalConstant.STACK_SPLIT;
                    break;
                }
            }
        }
        return stackInfo;
    }

    /** 错误日志很重要，需要记录所有堆栈信息 */
    public static String getErrorStackInfo(Throwable recordError) {
        if (recordError == null)
            return null;
        StringBuilder stackInfo = new StringBuilder();
        StackTraceElement[] stackTrace = recordError.getStackTrace();
        for (StackTraceElement stackTraceElement:stackTrace) {
            String className = stackTraceElement.getClassName();
            if (className.startsWith(NameConstant.PACKAGE_NAME)) {
                stackInfo.append(stackTraceElement.getClassName())
                        .append(GlobalConstant.ARG_SPLIT)
                        .append(stackTraceElement.getMethodName())
                        .append(GlobalConstant.ARG_SPLIT)
                        .append(stackTraceElement.getLineNumber())
                        .append(GlobalConstant.STACK_SPLIT);
            }
        }
        return stackInfo.toString();
    }

    /** 获取类中定义的所有内部变量 */
    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }

    /** 获取类中定义的所有内部变量 */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                /** 不转换静态变量 */
                if (!Modifier.isStatic(field.getModifiers()))
                    allFields.add(field);
            }
            /** 循环获取父类定义的内部变量 */
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /** 获取注解中真正的业务 */
    public static String getBizStr(BizType bizType) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = BizType.class.getDeclaredMethods();
        for (Method method : methods) {
            String bizStr = method.invoke(bizType).toString();
            if (!bizStr.equals(NameConstant.NULL_BIZ)) {
                /** 找到第一个不为空的业务就放入map中 */
                return bizStr;
            }
        }
        return NameConstant.NULL_BIZ;
    }
}
