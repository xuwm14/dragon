package com.flying.dragon.Biz.Entrance;

import com.flying.dragon.Base.Constant.KeyConstant;
import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Biz.Dispatcher;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Frame.Util.LogUtil;
import com.flying.dragon.Frame.Util.ThreadUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @描述 quartz定时任务执行的具体载体，即到执行时间就会运行execute函数
 **/
@Component
public class TimerJobHandler implements Job {
    @Autowired
    Dispatcher dispatcher;

    /** 处理定时器任务的实际函数 */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        CommonInParams params = new CommonInParams();
        try {
            /** 执行业务的线程存在复用情况，需要清除以前的线程变量 */
            ThreadUtil.clean();
            ThreadUtil.setChannel(AccessChannel.TIMER);
            /** 获取参数 */
            params = (CommonInParams) jobExecutionContext.getMergedJobDataMap().get(KeyConstant.PARAMS);
            /** 记录执行日志 */
            LogUtil.TIMER(params.getUsername(), params.getBizTypeStr(), params);
            /** 分发并执行任务 */
            dispatcher.dispatch(params);
        } catch (Exception e) {
            /** 捕获错误并记录日志 */
            LogUtil.ERROR(params.getUsername(), params.getBizTypeStr(), params, e);
        }
    }
}
