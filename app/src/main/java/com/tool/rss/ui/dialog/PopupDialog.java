package com.tool.rss.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.tool.rss.R;

/**
 * 弹出对话框
 *
 * @author mos
 * @date 2017.02.24
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified mos
 * @date 2017.06.09
 * @note 1. 修正设置位置不生效的bug
 * 2. 增加获取对话框位置的函数
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class PopupDialog extends Dialog {
    // 顶层View
    protected View mRootView;
    // 上下文
    protected Context mContext;
    // 窗口对象
    protected Window mWindow;
    // 返回监听
    protected IBackListener mBackListener;

    /**
     * 构造函数
     *
     * @param context 上下文(Activity的Context)
     * @param layoutId 布局id
     * @param dim 是否背景变暗
     */
    public PopupDialog(Context context, int layoutId, boolean dim) {
        super(context, (dim ? R.style.FullScreenDialog : R.style.FullScreenDialogTrans));
        mContext = context;

        initView(layoutId);
    }

    /**
     * 构造函数
     *
     * @param context 上下文(Activity的Context)
     * @param layoutId 布局id
     * @param style 样式
     */
    public PopupDialog(Context context, int layoutId, int style) {
        super(context, style);
        mContext = context;

        initView(layoutId);
    }

    /**
     * 设置View点击监听
     *
     * @param resId view的资源id
     * @param clickListener 点击监听
     */
    public void setViewClickListener(int resId, View.OnClickListener clickListener) {
        View v = mRootView.findViewById(resId);
        if (v != null) {
            v.setOnClickListener(clickListener);
        }
    }

    /**
     * 给指定控件设置文本情况
     *
     * @param resId 布局id
     * @param text 文本
     */
    public void setText(int resId, String text) {
        View v = mRootView.findViewById(resId);
        if (v != null && v instanceof TextView) {
            ((TextView) v).setText(text);
        }
    }

    /**
     * 设置字体颜色
     *
     * @param resId 布局id
     * @param color 颜色
     */
    public void setTextColor(int resId, @ColorInt int color) {
        View v = mRootView.findViewById(resId);
        if (v != null && v instanceof TextView) {
            ((TextView) v).setTextColor(color);
        }
    }

    /**
     * 设置字体大小
     *
     * @param resId 布局id
     * @param size 大小
     */
    public void setTextSize(int resId, float size) {
        View v = mRootView.findViewById(resId);
        if (v != null && v instanceof TextView) {
            ((TextView) v).setTextSize(size);
        }
    }

    /**
     * 给指定控件设置隐藏情况
     *
     * @param resId 布局id
     * @param visibility 显示情况
     */
    public void setVisibility(int resId, int visibility) {
        View v = mRootView.findViewById(resId);
        if (v != null) {
            v.setVisibility(visibility);
        }
    }

    /**
     * 设置窗口动画
     *
     * @param resId 动画资源id
     */
    public void setWindowAnimations(int resId) {
        getWindow().setWindowAnimations(resId);
    }

    /**
     * 初始化View
     *
     * @param layoutId 布局id
     */
    private void initView(int layoutId) {
        // 获取窗口
        mWindow = getWindow();

        // 设置布局id
        mRootView = LayoutInflater.from(mContext).inflate(layoutId, (ViewGroup) mWindow.getDecorView());

        // 默认点击外面可取消
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        // 设置点击返回的监听
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (mBackListener != null) {
                        mBackListener.onBack();
                    }
                }

                return false;
            }
        });
    }

    /**
     * 返回监听
     *
     * @param listener 监听器
     */
    public void setBackListener(IBackListener listener) {
        mBackListener = listener;
    }

    /**
     * 设置Gravity
     *
     * @param gravity 属性值
     */
    public void setGravity(int gravity) {
        mWindow.setGravity(gravity);
    }

    /**
     * 设置透明度
     *
     * @param alpha 透明度(0.0f完全透明，1.0f不透明)
     */
    public void setAlpha(float alpha) {
        LayoutParams lp = mWindow.getAttributes();
        lp.alpha = alpha;
        mWindow.setAttributes(lp);
    }

    /**
     * 设置窗口宽度
     *
     * @param width 宽度
     */
    public void setWindowWidth(int width) {
        LayoutParams lp = mWindow.getAttributes();

        // 设置宽度
        lp.width = width;

        mWindow.setAttributes(lp);
    }

    /**
     * 设置窗口高度
     *
     * @param height 高度
     */
    public void setWindowHeight(int height) {
        LayoutParams lp = mWindow.getAttributes();

        // 设置高度
        lp.height = height;

        mWindow.setAttributes(lp);
    }

    /**
     * 设置窗口占用屏幕的比例
     *
     * @param x x坐标
     * @param y y坐标
     * @param widthRatio 宽度比例(0.0f ~ 1.0f)
     * @param heightRatio 高度比例(0.0f ~ 1.0f)
     */
    public void setWindowRatio(int x, int y, float widthRatio, float heightRatio) {
        // 获取屏幕宽高
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);

        // 设置坐标及大小
        LayoutParams lp = mWindow.getAttributes();

        lp.x = x;
        lp.y = y;
        lp.width = (int) (metrics.widthPixels * widthRatio + 0.5f);
        lp.height = (int) (metrics.heightPixels * heightRatio + 0.5f);

        mWindow.setAttributes(lp);
        setGravity(Gravity.LEFT | Gravity.TOP);
    }

    /**
     * 设置位置
     *
     * @param x x坐标
     * @param y y坐标
     */
    public void setWindowPosition(int x, int y) {
        // 设置坐标
        LayoutParams lp = mWindow.getAttributes();

        lp.x = x;
        lp.y = y;
        mWindow.setAttributes(lp);
        setGravity(Gravity.LEFT | Gravity.TOP);
    }

    /**
     * 获取窗口矩形
     *
     * @return 窗口矩形
     */
    public Rect getWindowRect() {
        LayoutParams lp = mWindow.getAttributes();

        return new Rect(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
    }

    /**
     * 设置窗口矩形区
     *
     * @param rect 矩形结构
     */
    public void setWindowRect(Rect rect) {
        LayoutParams lp = mWindow.getAttributes();

        // 设置坐标及大小
        lp.x = rect.left;
        lp.y = rect.top;
        lp.width = rect.width();
        lp.height = rect.height();

        mWindow.setAttributes(lp);
        setGravity(Gravity.LEFT | Gravity.TOP);
    }

    /**
     * 设置变暗的不透明度
     *
     * @param amount 不透明度(0.0 ~ 完全透明，1.0 ~ 全黑)
     */
    public void setDimAmount(float amount) {
        mWindow.addFlags(LayoutParams.FLAG_DIM_BEHIND);
        LayoutParams layoutParams = mWindow.getAttributes();
        layoutParams.dimAmount = amount;
        mWindow.setAttributes(layoutParams);
    }

    /**
     * 返回监听
     */
    public static interface IBackListener {
        /**
         * 取消键被按下
         */
        public void onBack();
    }
}
