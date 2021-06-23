package com.flying.dragon.Biz.Manager;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @描述 所有管理类的父类
 **/
public abstract class CommonManager {
    /** 业务类型到具体执行业务的映射 */
    public static ConcurrentHashMap<String, Method> managerMap = new ConcurrentHashMap<>();
}
