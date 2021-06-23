package com.flying.dragon.Biz.Params;

import com.flying.dragon.Base.Annotation.Required;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @描述 通用入参，所有的入参都需要继承自此类
 **/
public class CommonInParams extends CommonParams {
    /** 业务到入参的映射Map */
    public static ConcurrentHashMap<String, Class<CommonInParams>> paramsMap = new ConcurrentHashMap<>();

    /** 执行的业务类型，必须设置 */
    @Required
    protected String bizTypeStr;

    /** 执行业务的用户名 */
    protected String username;

    @Override
    protected void beforeTransfer() {}

    public String getBizTypeStr() {
        return bizTypeStr;
    }

    public void setBizTypeStr(String bizTypeStr) {
        this.bizTypeStr = bizTypeStr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
