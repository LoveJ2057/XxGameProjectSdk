package com.xx.gamelibrary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.unity3d.player.UnityPlayerActivity;


/**
 * AdapterActivity
 * (。・∀・)ノ
 * Describe：
 * Created by 雷小星🍀 on 2017/8/10 15:59.
 */

public abstract class XxGameActivity extends UnityPlayerActivity implements XxGameContract {

    private ImageView bgView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSplash() !=0){
            bgView = new ImageView(this);
            bgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bgView.setBackgroundResource(getSplash());
            mUnityPlayer.addView(bgView);
        }

        //获取启动参数
        if (getIntent() != null && getIntent().getData() != null) {
            String query = getIntent().getData().getQuery();
            if (!TextUtils.isEmpty(query)) {
                XxGameSDK.getInstance().onGetLaunchParmars(query);
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取启动参数
        if (getIntent() != null && getIntent().getData() != null) {
            String query = getIntent().getData().getQuery();
            if (!TextUtils.isEmpty(query)) {
                XxGameSDK.getInstance().onGetLaunchParmars(query);
            }
        }
    }

    public void HideSplash() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(bgView !=null){
                    mUnityPlayer.removeView(bgView);
                    bgView = null;
                }
            }
        });
    }

    protected abstract int getSplash();

    @Override
    public void onGetLaunchParmars(String str) {
        XxGameSDK.getInstance().onGetLaunchParmars(str);
    }

    @Override
    public void weChatLogin() {
        XxGameSDK.getInstance().weChatLogin();
    }

    @Override
    public void weChatPay(String payJson) {
        XxGameSDK.getInstance().weChatPay(payJson);
    }

    @Override
    public void aliPay(String payJson) {
        XxGameSDK.getInstance().aliPay(payJson);
    }

    @Override
    public void otherPay(String payJson) {

    }

    @Override
    public void onPayResult(String result) {
        XxGameSDK.getInstance().onPayResult(result);
    }

    @Override
    public void onWeChatLogin(String weChatInfo) {
        XxGameSDK.getInstance().onWeChatLogin(weChatInfo);
    }


    @Override
    public void shareWeChat(String url, String title, String desc, boolean isTimeline) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getSharedIcon());
        XxGameSDK.getInstance().shareWeChat(bitmap, url, title, desc, isTimeline);
    }

    @Override
    public void onShareWeChatResult(String resp) {
        XxGameSDK.getInstance().onShareWeChatResult(resp);
    }

    protected abstract int getSharedIcon();

    @Override
    public void shareBitmap(byte[] screenJpg, String desc, boolean isTimeline) {
        XxGameSDK.getInstance().shareBitmap(screenJpg, desc, isTimeline);
    }

    @Override
    public void getLngAndLat() {
        XxGameSDK.getInstance().getLngAndLat();
    }

    @Override
    public void onGetLngAndLat(String lng_lat) {
        XxGameSDK.getInstance().onGetLngAndLat(lng_lat);
    }

    @Override
    public void onGetLocation(String location) {
        XxGameSDK.getInstance().onGetLocation(location);
    }


    @Override
    public void getBattery(boolean enable) {
        XxGameSDK.getInstance().getBattery(enable);
    }

    @Override
    public void onGetBattery(String battery) {
        XxGameSDK.getInstance().onGetBattery(battery);
    }

    @Override
    public void getPhotoFormCamare(boolean isCrop) {
        XxGameSDK.getInstance().getPhotoFormCamare(isCrop);
    }

    @Override
    public void getPhotoFormGallery(boolean isCrop) {
        XxGameSDK.getInstance().getPhotoFormGallery(isCrop);
    }

    @Override
    public void onGetPhoto(String base64) {
        XxGameSDK.getInstance().onGetPhoto(base64);
    }

    @Override
    protected void onDestroy() {
        XxGameSDK.getInstance().onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        XxGameSDK.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
