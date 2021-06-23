package com.flying.dragon.Biz.Entrance.Controller;

import com.flying.dragon.Biz.BizType.TestBizEnum;
import com.flying.dragon.Biz.Dispatcher;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Biz.Params.CommonOutParams;
import com.flying.dragon.Biz.Params.TestParams.in.TestInParams;
import com.flying.dragon.Biz.Params.TestParams.in.UploadFileInParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @描述 测试控制器，处理外部传入的操作测试业务的http请求
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    Dispatcher dispatcher;

    /**
     * 参数测试业务
     * Spring WebFlux 会自动地将所有参数包括 路径参数、查询参数、POST请求体中的参数都解析到入参中
     * 入参中的属性名称要和请求参数的名称完全一致
     */
    @RequestMapping(value="/params/{id}")
    public Mono<String> test(TestInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_PARAMS.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }

    @RequestMapping(value="/loginPermission")
    public Mono<String> loginPermission(CommonInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_LOGIN_PERMISSION.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }

    @RequestMapping(value="/adminPermission")
    public Mono<String> adminPermission(CommonInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_ADMIN_PERMISSION.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }

    @RequestMapping(value="/timer")
    public Mono<String> timer(CommonInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_TIMER_OPT.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }

    @RequestMapping(value="/error")
    public Mono<String> error(CommonInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_ERROR_THROW.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }

    @RequestMapping(value="/redis")
    public Mono<String> redis(CommonInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_REDIS_OPT.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }

    @RequestMapping(value="/fileUpload")
    public Mono<String> fileUpload(UploadFileInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_FILE_UPLOAD.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }

    @RequestMapping(value="/mongodb")
    public Mono<String> mongodb(CommonInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_MONGODB_OPT.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }


}


