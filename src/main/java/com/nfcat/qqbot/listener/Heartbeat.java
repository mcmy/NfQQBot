package com.nfcat.qqbot.listener;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class Heartbeat implements Runnable {

    private final Map<Long, Long> hearts = new HashMap<>();

    public void updateState(long qq) {
        hearts.put(qq, System.currentTimeMillis());
    }

    public abstract void close(long qq);

    @Override
    public void run() {
        while (true) {
            long t = System.currentTimeMillis();
            hearts.forEach((qq, time) -> {
                if (t - time > 6000) {
                    close(qq);
                }
            });
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
