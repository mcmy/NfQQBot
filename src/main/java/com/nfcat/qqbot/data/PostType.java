package com.nfcat.qqbot.data;

import lombok.Getter;

public enum PostType {
    MESSAGE("message", "消息"),
    MESSAGE_SENT("message_sent", "消息发送"),
    REQUEST("request", "请求"),
    NOTICE("notice", "通知"),
    META_EVENT("meta_event", "元事件");

    @Getter
    final String jsonString;
    @Getter
    final String tag;

    PostType(String jsonString, String tag) {
        this.jsonString = jsonString;
        this.tag = tag;
    }
}
