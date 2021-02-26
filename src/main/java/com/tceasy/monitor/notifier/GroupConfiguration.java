package com.tceasy.monitor.notifier;


import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class GroupConfiguration {

    @Nullable
    private String accessToken;

    @Nullable
    private String secret;  /*密钥*/

    private String[] atMobiles;  /*被@人的手机号*/

    private boolean isAtAll = false;  /*是否@所有人*/

    public static boolean valid(GroupConfiguration groupConfiguration) {
        return groupConfiguration != null && StringUtils.hasText(groupConfiguration.getAccessToken()) && StringUtils.hasText(groupConfiguration.getSecret());
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String[] getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(String[] atMobiles) {
        this.atMobiles = atMobiles;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll(boolean atAll) {
        isAtAll = atAll;
    }

    @Nullable
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(@Nullable String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "GroupConfiguration{" +
                "secret='" + secret + '\'' +
                ", atMobiles=" + Arrays.toString(atMobiles) +
                ", isAtAll=" + isAtAll +
                '}';
    }
}
