package com.flying.dragon.Biz.BizType;

/**
 * @描述 测试业务枚举，在书写正式代码时可以删除
 **/
public enum  TestBizEnum {
    /** 空业务，每个业务类中必须设置，否则会出错 */
    NULL_BIZ( null),

    /** 测试业务，在书写正式代码时可以删除，在书写正式代码前先运行测试业务，如果测试业务无问题说明各模块正常 */
    TEST_PARAMS( "多种输入参数的测试"),

    TEST_WRITE_LOG("定时日志测试"),

    TEST_LOGIN_PERMISSION("登录控制测试"),

    TEST_ADMIN_PERMISSION("管理员权限控制测试"),

    TEST_REDIS_OPT( "redis缓存测试"),

    TEST_TIMER_OPT( "定时器测试"),

    TEST_ERROR_THROW( "内部报错测试"),

    TEST_FILE_UPLOAD( "文件上传测试"),

    TEST_MONGODB_OPT( "mongodb数据库功能测试")
    ;
    

    TestBizEnum(String description) {
        this.description = description;
    }

    /** 业务描述 */
    String description;

    public String getDescription() {
        return description;
    }
}
