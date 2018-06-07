package com.xx.gamelibrary.callback;

import com.xx.gamelibrary.entity.WeChatLoginEntity;

/**
 * WeChatLoginCallBack
 * (。・∀・)ノ
 * Describe：
 * Created by 雷小星🍀 on 2017/7/24 17:33.
 */

public interface WeChatLoginCallBack {
    /**
     * 微信登录结果回调
     *
     * @return
     */
    void onWeChatLoginResult(WeChatLoginEntity loginEntity);
}
