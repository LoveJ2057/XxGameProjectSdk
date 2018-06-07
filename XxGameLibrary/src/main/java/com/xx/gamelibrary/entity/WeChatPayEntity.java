package com.xx.gamelibrary.entity;

/**
 * WeChatPayEntity
 * (。・∀・)ノ
 * Describe：微信支付信息
 * Created by 雷小星🍀 on 2017/7/20 13:45.
 */

public class WeChatPayEntity {
    /**
     * return_code : SUCCESS
     * return_msg : OK
     * appid : wxaa49732bd3a29bec
     * mch_id : 1460953802
     * nonce_str : 3isqm5ehe2w20mnvegi1n0b1fk0t397y
     * sign : C1FC883D1CCD26F5E7365DBFA15EF709
     * result_code : SUCCESS
     * prepay_id : wx201705081343538ce28ff0260864951793
     * trade_type : APP
     * time : 1494222179
     */

    private String return_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String prepay_id;
    private String trade_type;
    private int time;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
