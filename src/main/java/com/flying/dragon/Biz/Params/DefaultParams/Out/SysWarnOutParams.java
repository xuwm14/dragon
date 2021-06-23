package com.flying.dragon.Biz.Params.DefaultParams.Out;

import com.flying.dragon.Base.Error.MyError;
import com.flying.dragon.Base.Error.MyWarn;
import com.flying.dragon.Biz.Params.CommonOutParams;

/**
 * @描述 默认系统内部警告返回参数
 **/
public class SysWarnOutParams extends CommonOutParams {
    // 错误编码
    private String code;
    // 错误描述信息
    private String msg;

    public SysWarnOutParams(MyWarn warn) {
        code = warn.getErrorCode();
        msg = warn.getErrorMsg();
        success = false;
    }

    public SysWarnOutParams(MyError warn) {
        code = warn.getErrorCode();
        msg = warn.getErrorMsg();
        success = false;
    }
}
