package com.tool.rss.receiver;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.blankj.utilcode.util.Utils;
import com.tool.rss.base.BaseBroadcastReceiver;

/**
 * <p>网络请求状态广播</p><br>
 *
 * @author - lwc
 * @date - 2017/5/31
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class NetWorkStateReceiver extends BaseBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent, int flag) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            // 发送网络改变事件
            ConnectivityManager manager = (ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
            //没有网络链接的时候 activeNetwork=null
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork == null){
//                ToastUtils.showShortToast("没有网络");
            }else {
//                ToastUtils.showShortToast("有网络");
            }
        }
    }
}
