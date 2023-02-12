package com.nfcat.qqbot.utils;

import com.alibaba.fastjson.JSONObject;
import com.nfcat.qqbot.data.ErrorType;
import com.nfcat.qqbot.data.PostType;
import com.nfcat.qqbot.listener.Heartbeat;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

@Slf4j
public abstract class SocketRecvUtil implements Runnable {
    @Getter
    WebSocketClient webSocketClient;
    String wsUrl;
    Heartbeat heartbeat = new Heartbeat() {
        @Override
        public void close(long qq) {
            onError(ErrorType.OFFLINE_ERROR, String.valueOf(qq), null);
        }
    };

    public abstract void onOpen(ServerHandshake data);

    public abstract void onMessage(JSONObject json);

    public abstract void onError(ErrorType errorType, String msg, @Nullable Exception e);

    public abstract void onClose();

    public boolean send(String text) {
        try {
            webSocketClient.send(text);
            return true;
        } catch (Exception e) {
            onError(ErrorType.SEND_DATA_ERROR, text, null);
        }
        return false;
    }

    public SocketRecvUtil(String ws) {
        wsUrl = ws;
    }

    @Override
    public void run() {
        try {
            webSocketClient = new WebSocketClient(new URI(wsUrl), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    SocketRecvUtil.this.onOpen(serverHandshake);
                }

                @Override
                public void onMessage(String message) {
                    JSONObject jsonObject = JSONObject.parseObject(message);
                    if (jsonObject == null) {
                        SocketRecvUtil.this.onError(ErrorType.JSON_FORMAT_ERROR, message, null);
                        return;
                    }
                    String postType = jsonObject.getString("post_type");
                    postType = postType == null ? "other" : postType;
                    switch (postType) {
                        case "meta_event" -> {
                            switch (jsonObject.getString("meta_event_type")) {
                                case "heartbeat" -> heartbeat.updateState(jsonObject.getInteger("self_id"));
                                default -> SocketRecvUtil.this.onMessage(jsonObject);
                            }
                        }
                        default -> SocketRecvUtil.this.onMessage(jsonObject);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    SocketRecvUtil.this.onClose();
                }

                @Override
                public void onError(Exception ex) {
                    SocketRecvUtil.this.onError(ErrorType.FATAL_ERROR, ex.getMessage(), ex);
                }
            };
            webSocketClient.connect();
            heartbeat.run();
        } catch (Exception e) {
            onError(ErrorType.FATAL_ERROR, "启动失败", e);
        }
    }

}
