package com.xx.gamelibrary.entity;

import java.util.List;

/**
 * WeChatLoginEntity
 * (。・∀・)ノ
 * Describe：微信登录获取到的用户资料
 * Created by 雷小星🍀 on 2017/7/20 13:46.
 */

public class WeChatLoginEntity {

    /**
     * openid : ocDGb04SI5cess-ekU_Ke4ydubXc
     * nickname : 雷小星
     * sex : 1
     * language : zh_CN
     * city : Bishan
     * province : Chongqing
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmhead/L3Qib0nCc28lqUe2U4VlKsibeiaSmDVX6iciaFSrJURlC2MtZibos5qQ7D3g/0
     * privilege : []
     * unionid : oi7wWwMz6fIB4-GcXaeAqUU3k-fY
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;
    /**
     * errcode : 41001
     * errmsg : access_token missing, hints: [ req_id: XVVbGa0713s165 ]
     */

    private int errcode;
    private String errmsg;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "WeChatLoginEntity{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                ", privilege=" + privilege +
                '}';
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
