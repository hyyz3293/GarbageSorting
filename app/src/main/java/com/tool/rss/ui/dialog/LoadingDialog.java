package com.tool.rss.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tool.rss.R;
import com.tool.rss.utils.EmptyUtils;

/**
 * 正在加载对话框
 *
 * @author mos
 * @date 2017.02.24
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class LoadingDialog {
    /** 单例 */
    private static LoadingDialog sLoadingDialog = new LoadingDialog();
    /** 对话框对象 */
    private PopupDialog sDialog;

    /**
     * 私有化构造函数
     */
    private LoadingDialog() {
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static LoadingDialog getInstance() {
        return sLoadingDialog;
    }

    /**
     * 显示
     *
     * @param activity activity
     */
    public void show(Activity activity) {
        show(activity, null);
    }

    /**
     * 显示
     *
     * @param activity activity
     * @param option 参数
     */
    public void show(Activity activity, Option option) {
        // 参数检查
        if (activity == null) {

            return;
        }
        if (sDialog != null && sDialog.isShowing()) {

            return;
        }
        if (sDialog == null) {
            Resources res = activity.getApplicationContext().getResources();
            sDialog = new PopupDialog(activity, R.layout.loading_dialog, false);
            sDialog.setAlpha(1.0f);
            sDialog.setWindowRect(new Rect(0, 0,
                    res.getDimensionPixelSize(R.dimen.loading_dialog_width),
                    res.getDimensionPixelSize(R.dimen.loading_dialog_height)));
            sDialog.setGravity(Gravity.CENTER);
            sDialog.setCanceledOnTouchOutside(false);
        }

        TextView msgText = sDialog.findViewById(R.id.tv_loading_dialog_msg);
        if (!EmptyUtils.isEmpty(option)) {
            sDialog.setCancelable(option.mCancelable);
            sDialog.setOnCancelListener(option.mListener);
            sDialog.setBackListener(option.mBackListener);
            if (option.mMsg != null && option.mMsg.length() > 0) {
                msgText.setText(option.mMsg);
                msgText.setVisibility(View.VISIBLE);
            } else {
                msgText.setVisibility(View.GONE);
            }
        } else {
            // 默认不能按返回取消，若需要按返回取消，则有义务实现取消的逻辑
            sDialog.setCancelable(false);
            sDialog.setOnCancelListener(null);
            msgText.setVisibility(View.GONE);
        }

        try {
            sDialog.show();
        } catch (Exception ignored) {
        }
    }

    /**
     * 关闭正在加载对话框
     */
    public void close() {
        try {
            if (sDialog != null) {
                sDialog.cancel();
                sDialog = null;
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 选项参数
     */
    public static class Option {
        /** 取消监听 */
        private DialogInterface.OnCancelListener mListener = null;
        /** 返回监听 */
        private PopupDialog.IBackListener mBackListener = null;
        /** 是否可以取消 */
        private boolean mCancelable = true;
        /** 消息 */
        private String mMsg = "";

        /**
         * 设置监听
         *
         * @param listener 监听
         * @return 选项参数
         */
        public Option setListener(DialogInterface.OnCancelListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * 设置是否可取消
         *
         * @param cancelable 是否可取消
         * @return 选项参数
         */
        public Option setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        /**
         * 设置消息
         *
         * @param message 消息
         * @return 选项参数
         */
        public Option setMessage(String message) {
            mMsg = message;
            return this;
        }

        /**
         * 设置返回监听
         *
         * @param listener 监听器
         * @return 选项参数
         */
        public Option setBackListener(PopupDialog.IBackListener listener) {
            mBackListener = listener;
            return this;
        }
    }
}
