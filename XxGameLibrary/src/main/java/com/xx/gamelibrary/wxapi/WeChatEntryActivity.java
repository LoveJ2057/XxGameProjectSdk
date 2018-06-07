/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.xx.gamelibrary.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.xx.gamelibrary.XxGameSDK;
import com.xx.gamelibrary.entity.WeChatLoginEntity;
import com.xx.gamelibrary.entity.WeChatLoginResultEntity;
import com.xx.gamelibrary.network.WeChatApi;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 微信登录回调页
 */
public class WeChatEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mWeixinAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, XxGameSDK.getInstance().getWeChatConfigProvider().getAppID(), true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话

    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {
    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            //微信分享
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    XxGameSDK.getInstance().getShareWeChatResultCallBack().onShareWeChat(true);
                    break;
                default:
                    XxGameSDK.getInstance().getShareWeChatResultCallBack().onShareWeChat(false);
                    break;
            }
            finish();
        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            //获取微信登录信息
            SendAuth.Resp sendResp = (SendAuth.Resp) resp;
            String code = sendResp.code;
            if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                //用户取消
                finish();
            } else {
                getAccess_token(code);
            }
        } else {
            Toast.makeText(this, "操作失败", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取微信资料
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        //获取微信资料
        WeChatApi.getApi()
                .access_token(XxGameSDK.getInstance().getWeChatConfigProvider().getAppID(),
                        XxGameSDK.getInstance().getWeChatConfigProvider().getAppSecret(),
                        code,
                        "authorization_code")
                .flatMap(new Function<WeChatLoginResultEntity, ObservableSource<WeChatLoginEntity>>() {
                    @Override
                    public ObservableSource<WeChatLoginEntity> apply(@NonNull WeChatLoginResultEntity entity) throws Exception {

                        if (entity.getErrcode() != 0) {
                            return Observable.error(new Throwable(entity.getErrmsg()));
                        }

                        //获取微信用户信息
                        return WeChatApi.getApi()
                                .userInfo(entity.getAccess_token()
                                        , entity.getOpenid());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeChatLoginEntity>() {
                    @Override
                    public void accept(WeChatLoginEntity loginEntity) throws Exception {
                        if (!TextUtils.isEmpty(loginEntity.getOpenid())) {
                            XxGameSDK.getInstance().getLoginCallBack().onWeChatLoginResult(loginEntity);
                            finish();
                        } else if (loginEntity.getErrcode() != 0) {
                            Toast.makeText(WeChatEntryActivity.this, loginEntity.getErrmsg(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(WeChatEntryActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
