package com.flying.dragon.Biz.Params.TestParams.in;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Biz.BizType.TestBizEnum;
import com.flying.dragon.Biz.Params.CommonInParams;

import java.util.Date;

/**
 * @描述 登录权限测试返回类型
 **/
@BizType(testBiz = TestBizEnum.TEST_WRITE_LOG)
public class LogTestInParams extends CommonInParams {
    // 定时任务开始时间
    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
