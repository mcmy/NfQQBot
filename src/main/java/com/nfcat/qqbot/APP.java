package com.nfcat.qqbot;

import com.alibaba.fastjson.JSONObject;
import com.nfcat.qqbot.data.ErrorType;
import com.nfcat.qqbot.utils.SocketRecvUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class APP {
    public static void main(String[] args) {
        SocketRecvUtil socketRecvUtil = new SocketRecvUtil("ws://127.0.0.1:8080") {

            @Override
            public void onOpen(ServerHandshake data) {
                System.out.println("start");
            }

            @Override
            public void onMessage(JSONObject json) {
                System.out.println(json);
            }

            @Override
            public void onError(ErrorType errorType, String msg, @Nullable Exception e) {
                switch (errorType) {
                    case OFFLINE_ERROR -> log.warn("QQ：（{}）离线", msg);
                    case FATAL_ERROR -> log.warn("致命错误：{}", msg, e);
                    case RUNTIME_ERROR -> log.info("异常：{}", msg);
                    case SEND_DATA_ERROR -> log.info("消息发送失败，内容{}", msg);
                    case JSON_FORMAT_ERROR -> log.info("接收消息格式化失败：{}", msg);
                }
            }

            @Override
            public void onClose() {
                System.out.println("close");
            }
        };
        socketRecvUtil.run();
    }
}
