package com.flying.dragon.Biz.BizType;

import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Base.Enum.UserType;

import java.util.Collections;
import java.util.List;

/**
 * @描述 用户业务枚举，所有的用户业务都需要枚举在此类中
 **/
public enum UserBizEnum {
    /** 空业务，每个业务类中必须设置，否则会出错 */
    NULL_BIZ(null),
    /** 枚举所有与用户相关的业务 */
    USER_LOG_IN("用户登录业务"),
    USER_LOG_OUT("用户退出登录的业务"),
    ;

    UserBizEnum(String description) {
        this.description = description;
    }

    List<AccessChannel> channels;
    /** 业务描述 */
    String description;

    public String getDescription() {
        return description;
    }
}
