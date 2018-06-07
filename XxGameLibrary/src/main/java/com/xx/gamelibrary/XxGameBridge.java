package com.xx.gamelibrary;

import com.unity3d.player.UnityPlayer;

/**
 * XxGameBridge
 * (。・∀・)ノ
 * Describe： XxGameBridge
 * Created by 雷小星🍀 on 2017/7/24 17:12.
 */

public abstract class XxGameBridge implements XxGameContract {


    @Override
    public void onGetLaunchParmars(String str) {
        UnityPlayer.UnitySendMessage("GameMgr", "RespStartParam", str);
    }

    /**
     * 支付结果回调
     *
     * @param result 支付结果
     */
    public void onPayResult(String result) {
        UnityPlayer.UnitySendMessage("GameMgr", "onPayResult", result);
    }

    /**
     * 微信登录信息 回调
     *
     * @param weChatInfo 微信登录信息
     */
    public void onWeChatLogin(String weChatInfo) {
        UnityPlayer.UnitySendMessage("GameMgr", "onWeChatLogin", weChatInfo);
    }

    /**
     * 微信分享结果回调
     *
     * @param resp resp为布尔值 true为分享成功 false为分享失败
     */
    public void onShareWeChatResult(String resp) {
        UnityPlayer.UnitySendMessage("GameMgr", "onShareWeChat", resp);
    }

    /**
     * 获取经纬结果回调
     *
     * @param lng_lat 经纬度 使用逗号分隔，如： 116°E,40°N
     */
    public void onGetLngAndLat(String lng_lat) {
        UnityPlayer.UnitySendMessage("GameMgr", "onGetLngAndLat", lng_lat);
    }

    @Override
    public void onGetLocation(String location) {
        UnityPlayer.UnitySendMessage("GameMgr", "onGetLocation", location);
    }

    /**
     * 回调电量信息
     *
     * @param battery 电量信息
     */
    public void onGetBattery(String battery) {
        UnityPlayer.UnitySendMessage("GameMgr", "onGetBattery", battery);
    }

    @Override
    public void onGetPhoto(String base64) {
        UnityPlayer.UnitySendMessage("GameMgr", "onGetPhoto", base64);
    }
}
