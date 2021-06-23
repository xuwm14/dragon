package com.flying.dragon.Frame.Util;

import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Base.Model.User;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.server.ServerWebExchange;

/**
 * @描述 线程本地变量的操作组件，使用线程本地变量可以避免线程不安全的问题
 **/
public class ThreadUtil {
    private static class ThreadParams {
        /** WebSocket管道，在WebSocket长连接中生效 */
        private WebSocketSession socketSession = null;
        /** 获取HTTP原始请求的类，在http请求中生效 */
        private ServerWebExchange exchange = null;
        /** 当前登录的用户 */
        private User user = null;
        /** 当前访问的渠道，分为http，webSocket和定时器三种 */
        private AccessChannel channel = null;

        public WebSocketSession getSocketSession() {
            return socketSession;
        }

        public void setSocketSession(WebSocketSession socketSession) {
            this.socketSession = socketSession;
        }

        public ServerWebExchange getExchange() {
            return exchange;
        }

        public void setExchange(ServerWebExchange exchange) {
            this.exchange = exchange;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public AccessChannel getChannel() {
            return channel;
        }

        public void setChannel(AccessChannel channel) {
            this.channel = channel;
        }
    }

    private static ThreadLocal<ThreadParams> paramsThreadLocal = new ThreadLocal<>();

    private static ThreadParams getThreadParams() {
        ThreadParams threadParams = paramsThreadLocal.get();
        if (threadParams == null) {
            threadParams = new ThreadParams();
            paramsThreadLocal.set(threadParams);
        }
        return threadParams;
    }

    public static void setWebExchange(ServerWebExchange exchange) {
        getThreadParams().setExchange(exchange);
    }

    public static ServerWebExchange getWebExchange() {
        return getThreadParams().getExchange();
    }

    public static void setSocketSession(WebSocketSession session) {
        getThreadParams().setSocketSession(session);
    }

    public static WebSocketSession getSocketSession() {
        return getThreadParams().getSocketSession();
    }

    public static User getUser() {
        return getThreadParams().getUser();
    }

    public static void setUser(User user) {
        getThreadParams().setUser(user);
    }

    public static AccessChannel getChannel() {
        return getThreadParams().getChannel();
    }

    public static void setChannel(AccessChannel channel) {
        getThreadParams().setChannel(channel);
    }

    /** 清除本地线程变量，因为执行业务的线程存在复用情况，所以每次进入业务前需要调用此函数，防止两个业务使用一样的线程变量，导致内部逻辑出错 */
    public static void clean() {
        paramsThreadLocal.remove();
    }
}
