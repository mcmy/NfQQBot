package com.nfcat.qqbot.data;

import lombok.Getter;

public enum ErrorType {
    FATAL_ERROR("致命错误"),
    RUNTIME_ERROR("运行允许正常错误"),
    JSON_FORMAT_ERROR("json格式化错误"),
    SEND_DATA_ERROR("发送消息错误"),
    OFFLINE_ERROR("账号离线");

    @Getter
    final String tag;

    ErrorType(String tag) {
        this.tag = tag;
    }
}
