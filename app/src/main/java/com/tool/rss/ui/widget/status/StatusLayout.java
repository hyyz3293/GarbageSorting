package com.tool.rss.ui.widget.status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.security.InvalidParameterException;
import java.util.TimerTask;

/**
 * 状态布局
 *
 * @author jack
 * @date 2018.06.03
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class StatusLayout extends RelativeLayout {
    /**
     * 构造函数
     *
     * @param context 上下文
     * @param originView 原始视图
     */
    public StatusLayout(final Context context, final View originView) {
        super(context);

        if (context == null) {
            throw new InvalidParameterException("context can not be null.");
        }
        if (originView == null) {
            throw new InvalidParameterException("view can not be null.");
        }

        setVisibility(View.GONE);

        originView.post(new TimerTask() {
            @Override
            public void run() {
                if (originView.getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) originView.getParent();
                    // 移除原始控件，用FrameLayout替换
                    parent.removeView(originView);
                    FrameLayout child = new FrameLayout(context);
                    child.setLayoutParams(originView.getLayoutParams());
                    // 原本放在FrameLayout底层
                    child.addView(originView);
                    // StatusLayout放在FrameLayout顶层
                    child.addView(StatusLayout.this);
                    parent.addView(child, parent.indexOfChild(originView));
                }
            }
        });
    }

    /**
     * 显示状态
     *
     * @param status 状态页
     */
    public void showStatus(AbstractStatus status) {
        View statusView = LayoutInflater.from(status.getContext()).inflate(status.getContentView(), this, false);
        OnClickListener listener = status.getOnRootViewClickListener();
        if (listener != null) {
            statusView.setOnClickListener(listener);
        }
        status.onCreateView(statusView);

        // 清除当前视图
        if (getChildAt(0) != null) {
            removeViewAt(0);
        }
        // 防止点击穿透
        statusView.setClickable(true);
        // 添加状态视图
        addView(statusView);
        setVisibility(VISIBLE);
    }

    /**
     * 隐藏状态
     */
    public void hideStatus() {
        setVisibility(View.GONE);
    }
}
