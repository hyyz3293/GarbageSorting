package com.tool.rss;

import com.liulishuo.filedownloader.FileDownloader;
import com.tool.rss.base.BaseApplication;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class RkApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //友盟相关
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5d1d7566570df3611f000f56");
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.onResume(this);

        //文件下载器
        FileDownloader.setupOnApplicationOnCreate(this);
    }
}
