package com.flying.dragon.Frame.Aspect;

import com.flying.dragon.Base.Constant.NameConstant;
import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Biz.Processor.UserProcessor;
import com.flying.dragon.Frame.Util.SocketUtil;
import com.flying.dragon.Frame.Util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @描述 http请求拦截器
 **/
@Configuration
public class CustomWebFilter implements WebFilter {
    @Autowired
    UserProcessor userProcessor;

    /** 所有http请求在执行前都会进入此函数中 */
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        /** 清空线程变量，然后将请求体设置到线程变量中，并设置访问渠道为HTTP */
        ThreadUtil.clean();
        ThreadUtil.setWebExchange(serverWebExchange);
        ThreadUtil.setChannel(AccessChannel.HTTP);
        /** 设置请求头 */
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();
        String clientIp = request.getHeaders().getFirst(HttpHeaders.ORIGIN);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, NameConstant.DEFAULT_CONTENT);
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        if (clientIp != null)
            response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, clientIp);
        /** 第一次访问需要设置Cookie，并判断用户是否登录 */
        HttpCookie httpCookie = request.getCookies().getFirst(NameConstant.HTTP_SESSION_NAME);
        if (httpCookie == null) {
            UUID uuid = UUID.randomUUID();
            ResponseCookie responseCookie = ResponseCookie.from(NameConstant.HTTP_SESSION_NAME, uuid.toString()).path("/").httpOnly(true).build();
            response.addCookie(responseCookie);
        } else {
            String username = SocketUtil.getCookieUser(httpCookie.getValue());
            if (username != null)
                ThreadUtil.setUser(userProcessor.getUserByUsername(username));
        }
        return webFilterChain.filter(serverWebExchange);
    }
}
