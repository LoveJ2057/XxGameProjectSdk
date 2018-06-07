package com.xx.gamelibrary.template;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xx.gamelibrary.XxGameActivity;
import com.xx.gamelibrary.XxGameSDK;
import com.xx.gamelibrary.provider.WeChatConfigProvider;

/**
 * MainActivity
 * (。・∀・)ノ
 * Describe：
 * Created by 雷小星🍀 on 2017/11/21 10:51.
 */

public class MainActivity extends XxGameActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XxGameSDK.getInstance().init(this);
        XxGameSDK.getInstance().setWeChatConfigProvider(new WeChatConfigProvider() {
            @Override
            public String getAppID() {
                return null;
            }

            @Override
            public String getAppSecret() {
                return null;
            }
        });
    }

    @Override
    protected int getSplash() {
        return 0;
    }

    @Override
    protected int getSharedIcon() {
        return 0;
    }
}
