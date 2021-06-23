package com.flying.dragon.Biz.Manager;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Base.Annotation.RequireChannel;
import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Biz.BizType.TestBizEnum;
import com.flying.dragon.Biz.Params.TestParams.in.LogTestInParams;
import com.flying.dragon.Frame.Util.LogUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @描述 定时任务控制器，用于执行定时任务
 **/
public class TimerManager extends CommonManager {
    /** 定时任务的具体执行载体，限制只能通过定时器访问业务 */
    @RequireChannel({ AccessChannel.TIMER })
    @BizType(testBiz = TestBizEnum.TEST_WRITE_LOG)
    public void logTest(LogTestInParams inParams) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LogUtil.INFO("定时任务启动时间：" + dateFormat.format(inParams.getStartTime()) + "；定时任务执行时间：" + dateFormat.format(new Date()));
    }
}
