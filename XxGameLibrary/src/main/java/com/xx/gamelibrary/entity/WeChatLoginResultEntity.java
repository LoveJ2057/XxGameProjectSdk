package com.xx.gamelibrary.entity;

/**
 * WeChatLoginResultEntity
 * (。・∀・)ノ
 * Describe：微信登录结果
 * Created by 雷小星🍀 on 2017/7/20 13:47.
 */

public class WeChatLoginResultEntity {
    /**
     * access_token : 3fSyQQZMvezLXmmIqNqtXCBykOuoj6rsCBYLFTmGEGRiQEqoPeSXpswAfpUL9B76njcO4ZYhpHoCipjXdeLUKWn_qJ5YTKJHOzitN3cOOdk
     * expires_in : 7200
     * refresh_token : 2g6BVOcR1kPnE7e4aLV2trkOcsnnnECckv5RgUCm6k02-1AU22Jnxyntnjo6jHZQ7WLwYY1f0Zil9cD_MhTaWY0dn5T_D1Zm1IcfSdvBrR0
     * openid : oBBMzxK0-pNE5Jcstak1-QkoDtho
     * scope : snsapi_userinfo
     * unionid : oKqk-w7GJhuKe_7XKdvETXG5qhYg
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    /**
     * errcode : 41008
     * errmsg : missing code, hints: [ req_id: YkcrmA0273th48 ]
     */

    private int errcode;
    private String errmsg;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}


