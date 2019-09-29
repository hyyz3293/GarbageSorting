package com.tool.rss.ui.NetWork;

import android.text.TextUtils;

import java.util.UUID;

/**
 * JKX2018
 * Created by xyrzx on 2018/5/8.
 */
public class AppManager {
    private static volatile AppManager instance;
    private String skey = "";
    private String fakeIMEI = "";
    private String channelId = "";

    public static synchronized AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    private AppManager() {
        //loadData();
    }


    /* 判断登录状态 */
    public boolean isLogin() {
        return !TextUtils.isEmpty(skey);
    }

    /* 获取skey */
    public String getSkey() {
        return skey;
    }

    /* 设置skey */
    public void login(String skey) {
        this.skey = skey;
        //SharedPreferencesHelper.put("skey", skey);
    }

    /* 获取channelId */
    public String getChannelId() {
        //channelId =  SharedPreferencesHelper.get("ChannelId","0").toString();
        return channelId;
    }

    /* 设置channelId */
    public void ChannelId(String channelId) {
        this.channelId = channelId;
        //SharedPreferencesHelper.put("ChannelId", channelId);
    }

    /* 获取UUID唯一识别码 */
    public String getFakeIMEI() {
        if (TextUtils.isEmpty(fakeIMEI)) {
            fakeIMEI = UUID.randomUUID().toString().replace("-", "");
            //SharedPreferencesHelper.put("fakeIMEI", fakeIMEI);
        }
        return fakeIMEI;
    }

    /*获取请求地址*/
    public String getHost() {
        return "https://www.easy-mock.com/mock/5d1f129e68117d4c69ec494d/trashclass/";
    }




}
