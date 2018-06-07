package com.xx.gamelibrary.provider;

/**
 * WeChatConfigProvider
 * (。・∀・)ノ
 * Describe：微信配置信息提供者
 * Created by 雷小星🍀 on 2017/7/24 17:09.
 */

public interface WeChatConfigProvider {
    /**
     * 获取微信AppID
     *
     * @return
     */
    String getAppID();

    /**
     * 获取微信APP_SECRET
     *
     * @return
     */
    String getAppSecret();
}
