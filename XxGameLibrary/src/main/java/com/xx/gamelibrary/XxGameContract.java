package com.xx.gamelibrary;

/**
 * AuBridgeContract
 * (。・∀・)ノ
 * Describe：Android -Unity3D 交互契约
 * Created by 雷小星🍀 on 2017/7/20 9:30.
 */

public interface XxGameContract {


    /**
     * 启动信息回调
     *
     * @param str
     */
    void onGetLaunchParmars(String str);

    /**
     * 隐藏健康公告
     */
    void HideSplash();

    /**
     * 微信登录
     */
    void weChatLogin();

    /**
     * 微信支付
     *
     * @param payJson 支付信息JSON对象
     */
    void weChatPay(String payJson);

    /**
     * 支付宝支付
     *
     * @param payJson 支付宝支付信息JSON字符串
     */
    void aliPay(String payJson);

    /**
     * 其他第三方支付
     *
     * @param payJson 支付信息JSON字符串
     */
    void otherPay(String payJson);

    /**
     * 支付结果回调
     *
     * @param result 支付结果
     */
    void onPayResult(String result);

    /**
     * 微信登录信息 回调
     *
     * @param weChatInfo 微信登录信息
     */
    void onWeChatLogin(String weChatInfo);

    /**
     * 微信分享
     *
     * @param url        分享连接
     * @param title      分享标题
     * @param desc       分享描述
     * @param isTimeline 分享至朋友圈/好友 true:朋友圈 false:好友
     */
    void shareWeChat(String url, String title, String desc, boolean isTimeline);

    /**
     * 微信分享结果回调
     *
     * @param resp resp为布尔值 true为分享成功 false为分享失败
     */
    void onShareWeChatResult(String resp);

    /**
     * 分享图片到微信
     *
     * @param screenJpg  分享的图片字节数组
     * @param desc       分享描述
     * @param isTimeline 分享至朋友圈/好友 true:朋友圈 false:好友
     */
    void shareBitmap(byte[] screenJpg, String desc, boolean isTimeline);

    /**
     * 获取经纬
     */
    void getLngAndLat();

    /**
     * 获取经纬结果回调
     *
     * @param lng_lat 经纬度 使用逗号分隔，如： 116°E,40°N
     */
    void onGetLngAndLat(String lng_lat);

    /**
     * 地址位置信息回调
     *
     * @param location 地址
     */
    void onGetLocation(String location);

    /**
     * 获取电量
     *
     * @param enable 是否开启电量广播
     */
    void getBattery(boolean enable);

    /**
     * 回调电量信息
     *
     * @param battery 电量信息
     */
    void onGetBattery(String battery);


    /**
     * 拍照获取照片不裁剪
     *
     * @param isCrop 是否裁剪
     */
    void getPhotoFormCamare(boolean isCrop);


    /**
     * 从图库获取照片不裁剪
     *
     * @param isCrop 是否裁剪
     */
    void getPhotoFormGallery(boolean isCrop);

    /**
     * 回调给U3d
     *
     * @param base64 文件base64字符串
     */
    void onGetPhoto(String base64);
}
