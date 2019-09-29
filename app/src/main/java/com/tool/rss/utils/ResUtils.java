package com.tool.rss.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.Utils;

import java.lang.reflect.Field;

/**
 * 资源类相关工具
 *
 * @author mos
 * @date 2017/3/10 15:58
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ResUtils {
    /**
     * 构造类
     */
    private ResUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取字符串资源
     *
     * @param resId 资源id
     * @return 字符串
     */
    public static String getString(@StringRes int resId) {
        return Utils.getApp().getResources().getString(resId);
    }

    /**
     * 获取字符串资源
     *
     * @param resId 资源id
     * @return 字符串
     */
    public static String getString(@StringRes int resId, Object... formatArgs) {
        return Utils.getApp().getResources().getString(resId, formatArgs);
    }

    /**
     * 获取字符串数组资源
     *
     * @param resId 资源id
     * @return 字符串
     */
    public static String[] getStringArray(int resId) {
        return Utils.getApp().getResources().getStringArray(resId);
    }

    /**
     * 获取int数组资源
     *
     * @param resId 资源id
     * @return 字符串
     */
    public static int[] getIntArray(int resId) {
        return Utils.getApp().getResources().getIntArray(resId);
    }

    /**
     * 获得color资源
     *
     * @param resId 资源id
     * @return 颜色
     */
    public static int getColor(int resId) {
        return ContextCompat.getColor(Utils.getApp(), resId);
    }

    /**
     * 获得drawable资源
     *
     * @param resId 资源id
     * @return 图
     */
    public static Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(Utils.getApp(), resId);
    }

    /**
     * 获取尺寸
     *
     * @param resId 资源id
     * @return 尺寸
     */
    public static float getDimension(int resId) {
        return Utils.getApp().getResources().getDimension(resId);
    }

    /**
     * 获取像素尺寸
     *
     * @param resId 资源id
     * @return 尺寸
     */
    public static float getDimensionPixelSize(int resId) {
        return Utils.getApp().getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取MetaData
     *
     * @param metaName meta名称
     * @return 数据
     */
    public static String getMetaData(String metaName) {
        ApplicationInfo appInfo = null;
        String msg = null;
        try {
            appInfo = Utils.getApp().getPackageManager()
                    .getApplicationInfo(Utils.getApp().getPackageName(),
                            PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString(metaName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 通过Res的命名获得ID
     *
     * @param name R.drawable.ic_luncher,传ic_luncher就行
     * @return resId
     */
    public static int getResIdFromName(String name) {
        return getResIdFromName(name, Utils.getApp().getPackageName());
    }

    /**
     * 通过Res的命名获得ID
     *
     * @param name R.drawable.ic_luncher,传ic_luncher就行
     * @return resId
     */
    public static int getResIdFromName(String name, String packageName) {
        return Utils.getApp().getResources()
                .getIdentifier(name, "drawable", packageName);
    }

    /**
     * 通过Res的命名获得ID
     *
     * @param name 资源名
     * @param clazz 资源类(例如R.drawable.class, R.id.class)
     * @return 资源id
     */
    public static int getResIdFromName(String name, Class<?> clazz) {
        try {
            Field idField = clazz.getDeclaredField(name);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }
}