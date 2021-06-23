package com.flying.dragon.Biz.Manager;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Base.Annotation.NeedLogin;
import com.flying.dragon.Base.Annotation.RequireChannel;
import com.flying.dragon.Base.Constant.NameConstant;
import com.flying.dragon.Base.Enum.AccessChannel;
import com.flying.dragon.Base.Enum.UserType;
import com.flying.dragon.Biz.BizType.TestBizEnum;
import com.flying.dragon.Biz.Params.CommonInParams;
import com.flying.dragon.Biz.Params.CommonOutParams;
import com.flying.dragon.Biz.Params.TestParams.in.LogTestInParams;
import com.flying.dragon.Biz.Params.TestParams.in.TestInParams;
import com.flying.dragon.Biz.Params.TestParams.in.UploadFileInParams;
import com.flying.dragon.Biz.Params.TestParams.out.TestOutParams;
import com.flying.dragon.Biz.Params.TestParams.out.UploadFileOutParams;
import com.flying.dragon.Frame.Util.RedisUtil;
import com.flying.dragon.Frame.Util.SchedulerUtil;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述 测试管理器，用于执行测试业务，书写正式代码时可以删除
 **/
@Component
public class TestManager extends CommonManager {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SchedulerUtil schedulerUtil;

    /**
     * 出参和入参的测试业务
     * 出入参的格式是十分灵活的
     * 对于HTTP请求，所有能被Spring WebFLux解析的都可以用作入参；
     * 对于Socket请求，所有能被JSON反序列化的都可以用作入参；
     * 对于Timer定时器，所有对象都可以被用作入参。
     * 对于出参，所有可以被JSON序列化的都可以用作出参。
     */
    @BizType(testBiz = TestBizEnum.TEST_PARAMS)
    public CommonOutParams paramsTest(TestInParams inParams) {
        TestOutParams outParams = new TestOutParams();
        outParams.setNum(1000);
        double[] money = new double[3];
        for (int i = 0; i < 3; ++i)
            money[i] = i;
        outParams.setMoney(money);
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 5; ++i)
            names.add("name" + i);
        outParams.setNames(names);
        Map<String, Integer> studentIds = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            studentIds.put("student" + i, i);
        }
        outParams.setStudentId(studentIds);
        return outParams;
    }

    /** 登录后才可以访问此接口 */
    @NeedLogin
    @BizType(testBiz = TestBizEnum.TEST_LOGIN_PERMISSION)
    public CommonOutParams loginTest(CommonInParams inParams) {
        return new CommonOutParams(true);
    }

    /** 管理员才可以访问此接口 */
    @NeedLogin({ UserType.ADMIN })
    @BizType(testBiz = TestBizEnum.TEST_ADMIN_PERMISSION)
    public CommonOutParams adminTest(CommonInParams inParams) {
        return new CommonOutParams(true);
    }

    /** 测试定时器功能是否正常 */
    @BizType(testBiz = TestBizEnum.TEST_TIMER_OPT)
    public CommonOutParams timerTest(CommonInParams inParams) throws Exception {
        /** 设置定时任务的参数 */
        LogTestInParams testInParams = new LogTestInParams();
        testInParams.setStartTime(new Date());
        testInParams.setUsername(inParams.getUsername());
        testInParams.setBizTypeStr(TestBizEnum.TEST_WRITE_LOG.toString());

        /** 五秒后执行定时任务，定时任务id为testJob */
        schedulerUtil.deleteJob(NameConstant.TEST_JOB); // 先删除已存在的定时任务，否则会报错
        schedulerUtil.addScheduleJob(testInParams, 5000, NameConstant.TEST_JOB);
        return new CommonOutParams(true);
    }

    /** 测试内部报错是否正常 */
    @BizType(testBiz = TestBizEnum.TEST_ERROR_THROW)
    public CommonOutParams errorTest(CommonInParams inParams) {
        /**
         * 此处会报数组越界错误，如果客户端收到 服务器内部错误 的报错，说明功能正常
         * 可以在 ./log/error.log 日志中查看报错的具体信息，方便定位错误
         * 以后遇到服务器内部错误，不需要调试，可以直接通过日志来追踪根源
         */
        int[] testArr = new int[2];
        testArr[3] = 1;
        return new CommonOutParams(true);
    }

    /** 测试是否可以访问redis缓存 */
    @BizType(testBiz = TestBizEnum.TEST_REDIS_OPT)
    public CommonOutParams redisTest(CommonInParams inParams) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        redisUtil.setKey("test", dateFormat.format(new Date()));
        return new CommonOutParams(true);
    }

    /** 测试文件上传是否正常，只能够通过http上传文件 */
    @RequireChannel({ AccessChannel.HTTP })
    @BizType(testBiz = TestBizEnum.TEST_FILE_UPLOAD)
    public CommonOutParams fileTest(UploadFileInParams inParams) throws Exception {
        UploadFileOutParams outParams = new UploadFileOutParams();
        FilePart[] files = inParams.getFiles();
        if (files == null)
            outParams.setSuccess(false);
        else {
            outParams.setNum(files.length);
            File storePath = new File("./files");
            if (!storePath.exists())
                storePath.mkdir();
            List<String> pathList = new ArrayList<>();
            /** 存储所有文件到本地，并返回路径 */
            for (FilePart file:files) {
                Path path = Paths.get("./files/" + file.filename());
                pathList.add(path.toString());
                /** 这句话是存储文件到本地用的 */
                file.transferTo(path).subscribe();
            }
            outParams.setPaths(pathList);
        }
        return outParams;
    }
}
