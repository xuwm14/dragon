package com.flying.dragon.Frame.Util;

import com.alibaba.fastjson.JSONArray;
import com.flying.dragon.Base.Constant.KeyConstant;
import com.flying.dragon.Biz.Params.CommonOutParams;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护WebSocket长连接、HttpCookie和对应用户的组件
 * socket是长连接，多次请求的管道不会变，可以直接用管道当key。
 * httpCookie则需要通过创建UUID到用户名的map来记录登录状态，这个UUID会设置到http请求的Cookie中，每次请求都会被带上来。
 **/
public class SocketUtil {
    /** 用户名到管道的map */
    private static ConcurrentHashMap<String, WebSocketSession> user2Socket = new ConcurrentHashMap<>();
    /** cookie到用户的map */
    private static ConcurrentHashMap<String, String> cookie2User = new ConcurrentHashMap<>();

    /** 设置cookie到用户的匹配 */
    public static void setUserCookie(String cookie, String username) {
        cookie2User.put(cookie, username);
    }

    /** 获取cookie对应的用户 */
    public static String getCookieUser(String cookie) {
        return cookie2User.get(cookie);
    }

    /** 移除cookie对应的用户 */
    public static void removeCookieUser(String cookie) {
        cookie2User.remove(cookie);
    }

    /** 设置用户管道 */
    public static void setUserSocket(String username, WebSocketSession session) {
        session.getAttributes().put(KeyConstant.USERNAME, username);
        user2Socket.put(username, session);
    }

    /** 移除用户管道 */
    public static void removeUserSocket(String username) {
        WebSocketSession session = user2Socket.get(username);
        user2Socket.remove(username);
        session.getAttributes().remove(KeyConstant.USERNAME);
    }

    /** 获取管道中的用户名称 */
    public static String getSocketUser(WebSocketSession session) {
        Object userObj = session.getAttributes().get(KeyConstant.USERNAME);
        String username = null;
        if (userObj != null) {
            username = (String)userObj;
        }
        return username;
    }

    /** 向单个用户发送单个信息 */
    public static void sendMessageToUser(String username, CommonOutParams msg) throws Exception {
        sendMessageToUser(username, Collections.singletonList(msg));
    }
    /** 向多个用户发送单个信息 */
    public static void sendMessageToUsers(Collection<String> usernames, CommonOutParams msgs) throws Exception {
        sendMessageToUsers(usernames, Collections.singletonList(msgs));
    }
    /** 向多个用户发送多个信息 */
    public static void sendMessageToUsers(Collection<String> usernames, Collection<CommonOutParams> msgs) throws Exception {
        for (String username : usernames)
            sendMessageToUser(username, msgs);
    }
    /** 向单个用户发送多个信息 */
    public static void sendMessageToUser(String username, Collection<CommonOutParams> msgs) throws Exception {
        WebSocketSession socketSession = user2Socket.get(username);
        JSONArray jsonArray = new JSONArray();
        for (CommonOutParams params:msgs)
            jsonArray.add(params.toJsonObject());
        if (socketSession != null && socketSession.isOpen()) {
            socketSession.send(Flux.just(socketSession.textMessage(jsonArray.toString()))).subscribe();
        }
    }
}
