package com.tool.rss.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BroadcastReceiver基类
 *
 * @author mos
 * @date 2017.01.23
 * @note 1. 项目中所有子类必须继承自此基类
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        onReceive(context, intent, 0);
    }

    /**
     * onReceive回调
     *
     * @param context 参考回调文档说明
     * @param intent 参考回调文档说明
     * @param flag 标志(暂未使用)
     */
    public abstract void onReceive(Context context, Intent intent, int flag);
}
