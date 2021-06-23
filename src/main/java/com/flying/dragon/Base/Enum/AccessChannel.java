package com.flying.dragon.Base.Enum;

/**
 * @描述 渠道枚举，目前有http请求，WebSocket请求和定时器三种渠道
 **/
public enum AccessChannel {
    HTTP("http请求"),
    WEBSOCKET("websocket请求"),
    TIMER("定时器任务"),
    ;

    String description;

    AccessChannel(String description) {
        this.description = description;
    }
}
