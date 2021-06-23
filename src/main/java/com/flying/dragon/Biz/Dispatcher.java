package com.flying.dragon.Biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flying.dragon.Base.Error.MyError;
import com.flying.dragon.Base.Error.MyWarn;
import com.flying.dragon.Base.Error.SystemErrorEnum;
import com.flying.dragon.Base.Model.User;
import com.flying.dragon.Biz.Manager.CommonManager;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Biz.Params.CommonOutParams;
import com.flying.dragon.Biz.Params.DefaultParams.Out.SysErrorOutParams;
import com.flying.dragon.Biz.Params.DefaultParams.Out.SysWarnOutParams;
import com.flying.dragon.Frame.Util.LogUtil;
import com.flying.dragon.Frame.Util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

/**
 * @描述 事件分发器，根据输入的参数来决定使用哪个类和函数来执行逻辑
 **/
@Component
public class Dispatcher {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 事件分发器
     * 所有的业务在经过 Entrance 处理后转入 Manager 内部执行时都需要经过事件分发器
     * 目前用到分发器的有：①处理web的http请求；②处理socket长连接中客户端发送的请求；③处理定时任务中执行的请求；
     * 由分发器根据业务的参数，决定哪个业务逻辑由哪个类的哪个函数执行
     * 这只是一个最简单的分发器，可以根据业务逻辑的复杂程度定制不同复杂度的分发器
     **/
    public String dispatch(CommonInParams params) {
        /** 业务类型和执行业务的用户 */
        String bizStr = null;
        User nowUser = ThreadUtil.getUser();
        String username = null;
        if (nowUser != null)
            username = nowUser.getUsername();

        try {
            bizStr = params.getBizTypeStr();
            /** 获取类中执行业务的函数 */
            Method exeMethod = CommonManager.managerMap.get(bizStr);
            if (exeMethod == null)
                throw new MyError(SystemErrorEnum.METHOD_NOT_FIND);

            /** 参数正常与否的判断 */
            Class<?>[] methodParams = exeMethod.getParameterTypes();
            if (methodParams.length != 1 || !CommonInParams.class.isAssignableFrom(methodParams[0]))
                throw new MyError(SystemErrorEnum.PARAMS_ERROR);

            /** 获取执行业务的类 */
            Class<?> exeCls = exeMethod.getDeclaringClass();
            Object exeBean;
            try {
                exeBean = applicationContext.getBean(exeCls);
            } catch (Exception e) {
                throw new MyError(SystemErrorEnum.MANAGER_NOT_FIND);
            }
            /** 执行业务，返回结果 */
            Object rlt = exeMethod.invoke(exeBean, params);
            if (rlt == null) /** 定时任务可以不返回参数，因为不需要传参数给客户端 */
                return new JSONObject().toString();
            else if (rlt instanceof List) { /** 如果返回多个参数要封装一下 */
                JSONArray retArr = new JSONArray();
                List<CommonOutParams> rlts = (List<CommonOutParams>)rlt;
                for (CommonOutParams obj : rlts)
                    retArr.add(obj.toJsonObject());
                return retArr.toString();
            } else if (rlt instanceof CommonOutParams) /** 否则直接返回参数 */
                return rlt.toString();
            else /** 不允许其它类型的参数返回值 */
                throw new MyError(SystemErrorEnum.RETURN_PARAMS_ERROR);
        } catch (Exception e) {
            Throwable realError = e;
            boolean isWarning = false, isMyError = false;

            /** 获取报错的类型，判断是警告还是真实错误 */
            if (e instanceof InvocationTargetException) {
                /** InvocationTargetException 代表业务内部逻辑执行有错误 */
                realError = ((InvocationTargetException)e).getTargetException();
                /** UndeclaredThrowableException 代表内部还有错误，获取更内层的错误 */
                if (realError instanceof UndeclaredThrowableException)
                    realError = ((UndeclaredThrowableException)realError).getUndeclaredThrowable();
            }
            if (realError instanceof MyWarn) {
                isWarning = true;
            } else if (realError instanceof MyError) {
                isMyError = true;
            }
            if (isWarning) {
                MyWarn myWarn = (MyWarn)realError;
                /** 记录警告日志，并返回警告的参数 */
                LogUtil.WARN(username, bizStr, params, myWarn);
                return new SysWarnOutParams(myWarn).toString();
            } else if (isMyError) {
                MyError myError = (MyError)realError;
                /** 记录错误日志并返回具体错误 */
                LogUtil.ERROR(username, bizStr, params, realError);
                return new SysWarnOutParams(myError).toString();
            } else {
                /** 记录错误日志并返回服务器内部错误 */
                LogUtil.ERROR(username, bizStr, params, realError);
                return new SysErrorOutParams().toString();
            }
        }
    }
}
