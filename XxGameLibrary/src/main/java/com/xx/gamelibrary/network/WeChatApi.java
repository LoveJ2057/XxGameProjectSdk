package com.xx.gamelibrary.network;


import com.xx.gamelibrary.entity.WeChatLoginEntity;
import com.xx.gamelibrary.entity.WeChatLoginResultEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * WeChatApi
 * (๑• . •๑)
 * 类描述: 微信登陆API
 * * Created by 雷小星🍀 on 2017/7/20 13:43.
 */

public class WeChatApi {
    private static final String BASE_URL = "https://api.weixin.qq.com/sns/";
    private static Api api;

    public static WeChatApi.Api getApi() {
        if (api == null) {
            api = RetrofitManager.createApi(Api.class, BASE_URL);
        }
        return api;
    }

    /**
     * 获取基础url
     *
     * @return 基础url
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }

    public interface Api {

        @GET("oauth2/access_token")
        Observable<WeChatLoginResultEntity> access_token(@Query("appid") String appid
                , @Query("secret") String secret
                , @Query("code") String code
                , @Query("grant_type") String grant_type);

        @GET("userinfo")
        Observable<WeChatLoginEntity> userInfo(@Query("access_token") String access_token
                , @Query("openid") String openid);
    }
}