package com.tool.rss.ui.widget.status;

import android.content.Context;
import android.view.View;

/**
 * 抽象状态
 *
 * @author jack
 * @date 2018.06.04
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class AbstractStatus {
    /** 上下文 */
    private Context mContext;
    /** 根布局点击事件 */
    private View.OnClickListener mOnRootViewClickListener;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public AbstractStatus(Context context) {
        mContext = context;
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param onRootViewClickListener 根布局点击事件
     */
    public AbstractStatus(Context context, View.OnClickListener onRootViewClickListener) {
        mContext = context;
        mOnRootViewClickListener = onRootViewClickListener;
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取根布局点击事件
     *
     * @return 根布局点击事件
     */
    public View.OnClickListener getOnRootViewClickListener() {
        return mOnRootViewClickListener;
    }

    /**
     * 获取内部布局
     *
     * @return 内容布局
     */
    public abstract int getContentView();

    /**
     * 当创建了视图
     *
     * @param view 视图
     */
    public void onCreateView(View view) {
        // 不处理
    }
}
