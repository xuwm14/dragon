package com.flying.dragon.Biz.Entrance.Controller;

import com.flying.dragon.Biz.BizType.TestBizEnum;
import com.flying.dragon.Biz.Dispatcher;
import com.flying.dragon.Biz.Params.CommonInParams;
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

    @RequestMapping(value="/fileUpload")
    public Mono<String> fileUpload(UploadFileInParams inParams) {
        inParams.setBizTypeStr(TestBizEnum.TEST_FILE_UPLOAD.toString());
        return Mono.just(dispatcher.dispatch(inParams));
    }
}


