package com.tool.rss.base;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import com.tool.rss.ui.widget.status.AbstractStatus;

import io.reactivex.ObservableTransformer;

/**
 * 基础视图类
 *
 * @author jack
 * @date 2018.05.23
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public interface BaseView {
    /**
     * 获取BaseActivity
     *
     * @return BaseActivity
     */
    BaseActivity getBaseActivity();

    /**
     * 显示Toast
     *
     * @param msg 消息
     */
    void showToast(String msg);

    /**
     * 显示Toast
     *
     * @param msg 消息
     */
    void showToast(String msg, ViewGroup viewGroup);

    /**
     * 显示Loading
     */
    void showLoading();

    /**
     * 隐藏Loading
     */
    void hideLoading();

    /**
     * 显示状态
     *
     * @param originView 原始视图
     * @param status 状态
     */
    void showStatus(View originView, AbstractStatus status);

    /**
     * 隐藏状态
     *
     * @param originView 原始视图
     */
    void hideStatus(View originView);

    /**
     * 通过兼容取Color
     *
     * @param resId ColorRes
     * @return ColorInt
     */
    int getCompatColor(@ColorRes int resId);

    /**
     * 通过兼容取DimensionPixelOffset
     *
     * @param resId DimenRes
     * @return px
     */
    float getDimensionPixelOffset(@DimenRes int resId);

    /**
     * 通过兼容取String
     *
     * @param resId StringRes
     * @return String
     */
    String getCompatString(@StringRes int resId);

    /**
     * 通过兼容器取Drawable
     *
     * @param resId DrawableRes
     * @return Drawable
     */
    Drawable getCompatDrawable(@DrawableRes int resId);

    /**
     * 跳转Activity
     *
     * @param clazz 类对象
     */
    void startActivity(Class<?> clazz);

    /**
     * 跳转Activity获取结果
     *
     * @param clazz 类对象
     * @param requestCode 请求码
     */
    void startActivityForResult(Class<?> clazz, int requestCode);

    /**
     * 使用调度器
     *
     * @return 调度转换器
     * @note 默认生命周期DESTROY
     */
    <T> ObservableTransformer<T, T> applySchedulers();
}
