package com.flying.dragon.Biz.Manager;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Base.Annotation.NeedLogin;
import com.flying.dragon.Base.Constant.NameConstant;
import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Base.Error.MyWarn;
import com.flying.dragon.Base.Error.UserWarnEnum;
import com.flying.dragon.Base.Model.User;
import com.flying.dragon.Biz.BizType.UserBizEnum;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Biz.Params.CommonOutParams;
import com.flying.dragon.Biz.Params.UserParams.in.LoginInParams;
import com.flying.dragon.Biz.Processor.UserProcessor;
import com.flying.dragon.Frame.Util.SocketUtil;
import com.flying.dragon.Frame.Util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @描述 用户管理器，用于执行所有的用户业务
 **/
@Component
public class UserManager extends CommonManager {
    @Autowired
    UserProcessor userProcessor;

    /** 用户登录业务 */
    @BizType(userBiz = UserBizEnum.USER_LOG_IN)
    public CommonOutParams userLogin(LoginInParams inParams) throws Exception {
        /** 查找用户 */
        String username = inParams.getUsername();
        if (username == null)
            throw new MyWarn(UserWarnEnum.LOGIN_FAILED);
        User user = userProcessor.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(inParams.getPassword()))
            throw new MyWarn(UserWarnEnum.LOGIN_FAILED);

        /** 获取访问渠道 */
        AccessChannel channel = ThreadUtil.getChannel();
        /** 判断记录WebSocket状态还是http状态 */
        if (channel == AccessChannel.WEBSOCKET)
            SocketUtil.setUserSocket(username, ThreadUtil.getSocketSession());
        else {
            ServerWebExchange webExchange = ThreadUtil.getWebExchange();
            if (webExchange != null) {
                HttpCookie httpCookie = webExchange.getRequest().getCookies().getFirst(NameConstant.HTTP_SESSION_NAME);
                if (httpCookie != null)
                    SocketUtil.setUserCookie(httpCookie.getValue(), username);
            }
        }
        return new CommonOutParams(true);
    }

    /** 用户退出登录的业务 */
    @NeedLogin
    @BizType(userBiz = UserBizEnum.USER_LOG_OUT)
    public CommonOutParams userLogout(CommonInParams inParams) throws Exception {
        /** 获取登陆的用户名 */
        String username = ThreadUtil.getUser().getUsername();
        /** 获取访问渠道 */
        AccessChannel channel = ThreadUtil.getChannel();
        /** 判断记录WebSocket状态还是http状态 */
        if (channel == AccessChannel.WEBSOCKET)
            SocketUtil.removeUserSocket(username);
        else {
            ServerWebExchange webExchange = ThreadUtil.getWebExchange();
            if (webExchange != null) {
                HttpCookie httpCookie = webExchange.getRequest().getCookies().getFirst(NameConstant.HTTP_SESSION_NAME);
                if (httpCookie != null)
                    SocketUtil.removeCookieUser(httpCookie.getValue());
            }
        }
        return new CommonOutParams(true);
    }
}
