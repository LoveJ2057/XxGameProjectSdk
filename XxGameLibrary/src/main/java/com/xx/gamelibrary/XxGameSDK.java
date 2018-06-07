package com.xx.gamelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xx.gamelibrary.callback.OnBatteryChangeListener;
import com.xx.gamelibrary.callback.ShareWeChatResultCallBack;
import com.xx.gamelibrary.callback.WeChatLoginCallBack;
import com.xx.gamelibrary.entity.WeChatLoginEntity;
import com.xx.gamelibrary.image.ImageChooseHelper;
import com.xx.gamelibrary.provider.WeChatConfigProvider;
import com.xx.gamelibrary.receiver.BatteryReceiver;
import com.xx.gamelibrary.utils.BitmapUtil;
import com.antiphon.paysdk.AntiphonPaySDK;
import com.antiphon.paysdk.PayResultCallBack;
import com.antiphon.paysdk.WeChatAppIDProvider;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

import static com.xx.gamelibrary.utils.BitmapUtil.bmpToByteArray;
import static com.xx.gamelibrary.utils.BitmapUtil.bmpToByteArrayN;

/**
 * XxGameSDK
 * (。・∀・)ノ
 * Describe： XxGameSDK
 * Created by 雷小星🍀 on 2017/7/24 17:12.
 */

public class XxGameSDK extends XxGameBridge {
    private static XxGameSDK instance;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    double latitude = amapLocation.getLatitude();//获取纬度
                    double longitude = amapLocation.getLongitude();//获取经度
                    //回调定位到U3d
                    String lng_lat = String.valueOf(longitude) + "," + String.valueOf(latitude);
                    Log.d("onLocationChanged", "onLocationChanged: " + lng_lat);
                    onGetLngAndLat(lng_lat);
                    onGetLocation(amapLocation.getAddress());
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    private Context mContext;
    private WeChatConfigProvider weChatConfigProvider;
    private IWXAPI wxApi;
    private WeChatLoginCallBack loginCallBack;//微信登录回调
    private ShareWeChatResultCallBack shareWeChatResultCallBack;//微信分享结果回调
    private BatteryReceiver receiver;
    private IntentFilter filter;
    private ImageChooseHelper imageChooseHelper;

    private XxGameSDK() {
    }

    /**
     * 使用双验证单例模式
     *
     * @return
     */
    public static XxGameSDK getInstance() {
        if (instance == null) {
            synchronized (XxGameSDK.class) {
                if (instance == null) {
                    instance = new XxGameSDK();
                }
            }
        }
        return instance;
    }

    /**
     * 销毁定位
     */
    public void onDestroyLocation() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        onDestroyLocation();
        getBattery(false);
    }

    /**
     * 获取微信登录回调
     *
     * @return
     */
    public WeChatLoginCallBack getLoginCallBack() {
        return loginCallBack != null ? loginCallBack : new WeChatLoginCallBack() {
            @Override
            public void onWeChatLoginResult(WeChatLoginEntity loginEntity) {
                //默认操作
                onWeChatLogin(new Gson().toJson(loginEntity));
            }
        };
    }

    /**
     * 设置自定义微信登录回调操作
     *
     * @param loginCallBack 微信登录回调
     */
    public void setLoginCallBack(WeChatLoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    /**
     * 获取微信配置信息提供者
     *
     * @return 微信配置信息提供者
     */
    public WeChatConfigProvider getWeChatConfigProvider() {
        return weChatConfigProvider != null ? weChatConfigProvider : new WeChatConfigProvider() {
            @Override
            public String getAppID() {
                return null;
            }

            @Override
            public String getAppSecret() {
                return null;
            }
        };
    }

    /**
     * 设置微信配置信息提供者
     *
     * @param weChatConfigProvider 微信配置信息提供者
     */
    public void setWeChatConfigProvider(WeChatConfigProvider weChatConfigProvider) {
        this.weChatConfigProvider = weChatConfigProvider;
        AntiphonPaySDK.getIntance().setWeChatAppIDProvider(new WeChatAppIDProvider() {
            @Override
            public String getWeChatAppID() {
                return getWeChatConfigProvider().getAppID();
            }
        });
        //初始化
        wxApi = WXAPIFactory.createWXAPI(mContext, getWeChatConfigProvider().getAppID(), true);
        wxApi.registerApp(getWeChatConfigProvider().getAppID());
    }

    /**
     * 设置图片操作提供者
     *
     * @param imageConfigProvider
     */
    public void setImageConfigProvider(ImageChooseHelper.ImageConfigProvider imageConfigProvider) {
        imageChooseHelper.setImageConfigProvider(imageConfigProvider);
    }

    /**
     * 初始化，建议放在App入口处
     *
     * @param context 上下文对象
     */
    public void init(Context context) {
        this.mContext = context;
        AntiphonPaySDK.getIntance().init(context);
        initLocation();//初始化定位
        initImageChooseHelper();
    }

    /**
     * 初始化图片选择帮助类
     */
    private void initImageChooseHelper() {
        imageChooseHelper = new ImageChooseHelper((Activity) mContext);
        imageChooseHelper.setOnFinishChooseAndCropImageListener(new ImageChooseHelper.OnFinishChooseAndCropImageListener() {
            @Override
            public void onFinish(Bitmap bitmap, File file) {
                onGetPhoto(BitmapUtil.base64File(file));
            }
        });
        imageChooseHelper.setOnFinishChooseImageListener(new ImageChooseHelper.OnFinishChooseImageListener() {
            @Override
            public void onFinish(Uri uri, File file) {
                onGetPhoto(BitmapUtil.base64File(file));
            }
        });
    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void HideSplash() {

    }

    @Override
    public void weChatLogin() {
        //微信登录
        wxApi.registerApp(getWeChatConfigProvider().getAppID());
        if (wxApi != null && wxApi.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "antiphon";
            wxApi.sendReq(req);
        } else {
            Toast.makeText(mContext, "请先安装微信", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取微信分享回调结果
     *
     * @return 微信分享回调结果
     */
    public ShareWeChatResultCallBack getShareWeChatResultCallBack() {
        return shareWeChatResultCallBack != null ? shareWeChatResultCallBack : new ShareWeChatResultCallBack() {
            @Override
            public void onShareWeChat(boolean success) {
                XxGameSDK.this.onShareWeChatResult(String.valueOf(success));
            }
        };
    }

    @Override
    public void weChatPay(String payJson) {
        AntiphonPaySDK.getIntance()
                .createWxPay(payJson, new PayResultCallBack() {
                    @Override
                    public void onSuccess() {
                        onPayResult("true");
                    }

                    @Override
                    public void onFiale(String s) {
                        onPayResult("false");
                    }
                });
    }

    @Override
    public void aliPay(String payJson) {
        AntiphonPaySDK.getIntance()
                .createAliPay(payJson, new PayResultCallBack() {
                    @Override
                    public void onSuccess() {
                        onPayResult("true");
                    }

                    @Override
                    public void onFiale(String s) {
                        onPayResult("false");
                    }
                });
    }

    @Override
    public void otherPay(String payJson) {

    }


    @Override
    @Deprecated
    public void shareWeChat(String url, String title, String desc, boolean isTimeline) {
    }

    /**
     * 分享到微信
     *
     * @param url
     * @param title
     * @param desc
     * @param isTimeline
     */
    public void shareWeChat(Bitmap bitmap, String url, String title, String desc, boolean isTimeline) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
        msg.thumbData = bmpToByteArrayN(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxApi.sendReq(req);
    }

    /**
     * 分享到微信
     *
     * @param url
     * @param title
     * @param desc
     * @param isTimeline
     */
    public void shareWeChat(Bitmap bitmap, String url, String title, String desc, boolean isTimeline,
                            ShareWeChatResultCallBack shareWeChatResultCallBack) {
        this.shareWeChatResultCallBack = shareWeChatResultCallBack;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
        msg.thumbData = bmpToByteArrayN(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxApi.sendReq(req);
    }


    /**
     * 分享图片到微信
     *
     * @param screenJpg
     * @param desc
     * @param isTimeline
     * @param shareWeChatResultCallBack
     */
    public void shareBitmap(byte[] screenJpg, String desc, boolean isTimeline, ShareWeChatResultCallBack shareWeChatResultCallBack) {
        this.shareWeChatResultCallBack = shareWeChatResultCallBack;
        Bitmap bmp = BitmapFactory.decodeByteArray(screenJpg, 0, screenJpg.length);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.description = desc;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 128, 128, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);  //缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxApi.sendReq(req);
    }

    @Override
    public void shareBitmap(byte[] screenJpg, String desc, boolean isTimeline) {
        Bitmap bmp = BitmapFactory.decodeByteArray(screenJpg, 0, screenJpg.length);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.description = desc;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 128, 128, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);  //缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxApi.sendReq(req);
    }

    @Override
    public void getLngAndLat() {
        mLocationClient.startLocation();
    }


    @Override
    public void getBattery(boolean enable) {
        if (enable) {
            if (receiver == null) {
                //初始化电量广播接收者
                receiver = new BatteryReceiver(new OnBatteryChangeListener() {
                    @Override
                    public void onBatteryChange(String battery) {
                        onGetBattery(battery);
                    }
                });
                filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            }
            mContext.registerReceiver(receiver, filter);//注册BroadcastReceiver
        } else {
            if (receiver != null) {
                mContext.unregisterReceiver(receiver);//解除注册
                receiver = null;
            }
        }
    }

    @Override
    public void getPhotoFormCamare(boolean isCrop) {
        imageChooseHelper.setCrop(isCrop);
        imageChooseHelper.startCamearPic();
    }

    @Override
    public void getPhotoFormGallery(boolean isCrop) {
        imageChooseHelper.setCrop(isCrop);
        imageChooseHelper.startImageChoose();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageChooseHelper.onActivityResult(requestCode, resultCode, data);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
