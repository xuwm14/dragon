package com.flying.dragon.Biz.Entrance.Controller;

import com.flying.dragon.Biz.BizType.UserBizEnum;
import com.flying.dragon.Biz.Dispatcher;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Biz.Params.UserParams.in.LoginInParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @描述 用户控制器，处理外部传入的操作用户业务的http请求
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    Dispatcher dispatcher;

    /** 用户登录http接口 */
    @RequestMapping(value="/login")
    public Mono<String> login(LoginInParams inParams) {
        inParams.setBizTypeStr(UserBizEnum.USER_LOG_IN.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }

    /** 用户退出登录http接口 */
    @GetMapping(value="/logout") /** 使用注解可以限制请求的方式，此注解限制只能通过 get 方式请求 */
    public Mono<String> logout(CommonInParams inParams) {
        inParams.setBizTypeStr(UserBizEnum.USER_LOG_OUT.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }
}
