package com.flying.dragon.Biz.Params.UserParams.in;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Base.Annotation.Required;
import com.flying.dragon.Biz.BizType.UserBizEnum;
import com.flying.dragon.Biz.Params.CommonInParams;

/**
 * @创建人 许为民
 * @创建时间 2021/5/27
 * @描述
 **/
@BizType(userBiz = UserBizEnum.USER_LOG_IN)
public class LoginInParams extends CommonInParams {
    // 登录用的密码
    @Required
    private String username;
    // 登录用的密码
    @Required
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
