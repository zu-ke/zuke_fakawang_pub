package com.zk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LanTuPayConfig {

    @Value("${lantu.mchId}")
    private String mchId;

    @Value("${lantu.notifyUrl}")
    private String notifyUrl;

    @Value("${lantu.key}")
    private String key;

    public String getMchId() {
        return mchId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getKey() {
        return key;
    }
}

