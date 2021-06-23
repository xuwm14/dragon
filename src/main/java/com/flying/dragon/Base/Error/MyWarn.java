package com.flying.dragon.Base.Error;

/**
 * @描述 自定义警告类，此错误会被记录在警告日志中
 **/
public class MyWarn extends Exception {
    /** 错误编码 */
    private String errorCode;
    /** 错误描述 */
    private String errorMsg;

    public MyWarn(ExceptionInterface exceptionInterface) {
        errorCode = exceptionInterface.getErrorCode();
        errorMsg = exceptionInterface.getErrorMessage();
    }

    public MyWarn(String code, String msg) {
        errorCode = code;
        errorMsg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
