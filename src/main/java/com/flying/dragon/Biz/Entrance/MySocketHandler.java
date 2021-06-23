package com.flying.dragon.Biz.Entrance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flying.dragon.Base.Constant.KeyConstant;
import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Base.Error.MyWarn;
import com.flying.dragon.Base.Error.SystemErrorEnum;
import com.flying.dragon.Biz.Dispatcher;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Biz.Params.DefaultParams.Out.SysErrorOutParams;
import com.flying.dragon.Biz.Params.DefaultParams.Out.SysWarnOutParams;
import com.flying.dragon.Biz.Processor.UserProcessor;
import com.flying.dragon.Frame.Util.LogUtil;
import com.flying.dragon.Frame.Util.SocketUtil;
import com.flying.dragon.Frame.Util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @描述 处理WebSocket长连接的类，只处理文本消息
 **/
@Component
public class MySocketHandler implements WebSocketHandler {

    @Autowired
    private UserProcessor userProcessor;

    @Autowired
    private Dispatcher dispatcher;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        /** 配置socket处理方式 */
        Mono<Void> input = session.receive()
                .doOnTerminate(() -> {
                    /** socket连接中断时的处理 */
                    String username = SocketUtil.getSocketUser(session);
                    if (username != null)
                        SocketUtil.removeUserSocket(username);
                })
                /** 收到消息时候的处理 */
                .map(WebSocketMessage::getPayloadAsText)
                .map(msg -> {
                    processMsg(session, msg);
                    return msg;
                }).then();
        return  input.then();
    }

    /** 处理socket文本消息的载体 */
    private void processMsg(WebSocketSession session, String msg) {
        String retStr = new SysErrorOutParams().toString(),
                username = null,
                bizTypeStr = null;
        try {
            /** 清除线程变量，设置Socket的session和访问渠道，并判断用户是否登录 */
            ThreadUtil.clean();
            ThreadUtil.setSocketSession(session);
            ThreadUtil.setChannel(AccessChannel.WEBSOCKET);
            username = SocketUtil.getSocketUser(session);
            if (username != null)
                ThreadUtil.setUser(userProcessor.getUserByUsername(username));
            /** 获取参数和操作类型 */
            CommonInParams inParams;
            try {
                JSONObject jsonMsg;
                jsonMsg = JSON.parseObject(msg);
                bizTypeStr = jsonMsg.getString(KeyConstant.BIZ_TYPE_STR);
                Class<CommonInParams> inParamsClass = CommonInParams.paramsMap.get(bizTypeStr);
                if (inParamsClass == null)
                    inParamsClass = CommonInParams.class;
                inParams = JSON.toJavaObject(jsonMsg, inParamsClass);
            } catch (Exception e) {
                throw new MyWarn(SystemErrorEnum.PARAMS_TRANSFER_ERROR);
            }
            retStr = dispatcher.dispatch(inParams);
        } catch (Exception e) {
            /** 处理出错，记录警告日志或者错误日志 */
            if (e instanceof MyWarn) {
                MyWarn warn = (MyWarn)e;
                retStr = new SysWarnOutParams(warn).toString();
                LogUtil.WARN(username, bizTypeStr, msg, warn);
            } else {
                retStr = new SysErrorOutParams().toString();
                LogUtil.ERROR(username, bizTypeStr, msg, e);
            }
        } finally {
            /** 将处理结果返回给客户端 */
            session.send(Flux.just(session.textMessage(retStr))).subscribe();
        }

    }
}
