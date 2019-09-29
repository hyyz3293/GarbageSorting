package com.tool.rss.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;

import rx_activity_result2.RxActivityResult;

/**
 * Application基类
 *
 * @author mos
 * @date 2017.01.23
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    /** 单例 */
    private static BaseApplication sInstance = null;
    /** 登录检测拦截 */
    private BaseActivity.ICheckLoginInterceptor mCheckLoginInterceptor = null;
    /**
     * 单例模式
     *
     * @return BaseApplication
     */
    public static BaseApplication getInstance() {
        return sInstance;
    }

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        Utils.init(this);

        // 注册生命周期的回调
        registerActivityLifecycleCallbacks(this);

        // RxActivityResult
        RxActivityResult.register(this);
    }

    /**
     * 在下一步活动之前，检测登录状态
     *
     * @param actionAfterLogin 登录后，活动的action
     * @param data 参数
     * @return false -- 未登录  true -- 已登录
     */
    public boolean checkLoginBeforeAction(String actionAfterLogin, Bundle data) {
        if (mCheckLoginInterceptor != null) {
            return mCheckLoginInterceptor.checkLoginBeforeAction(actionAfterLogin, data);
        }
        return false;
    }

    /**
     * 设置登录检测拦截器
     *
     * @param interceptor 拦截器
     */
    public void setCheckLoginInterceptor(BaseActivity.ICheckLoginInterceptor interceptor) {
        mCheckLoginInterceptor = interceptor;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
