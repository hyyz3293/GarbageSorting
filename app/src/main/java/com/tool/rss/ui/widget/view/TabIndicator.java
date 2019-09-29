package com.tool.rss.ui.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tool.rss.R;
import com.tool.rss.utils.ResUtils;


public class TabIndicator extends RelativeLayout {
	private ImageView ivTabIcon;
	private TextView tvTabHint;
	private TextView tvTabUnRead;

	private int normalIconId;
	private int focusIconId;

	public TabIndicator(Context context) {
		this(context, null);
	}

	public TabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 将布局文件和 代码进行绑定
		View.inflate(context, R.layout.tab_indicator, this);

		ivTabIcon = (ImageView) findViewById(R.id.tab_indicator_icon);
		tvTabHint = (TextView) findViewById(R.id.tab_indicator_hint);
		tvTabUnRead = (TextView) findViewById(R.id.tab_indicator_unread);
		
		setTabUnreadCount(0);
	}

	// 设置tab的title
	public void setTabTitle(String title) {
		tvTabHint.setText(title);
	}

	public void setTabHint(int titleId) {
		tvTabHint.setText(titleId);
	}

	// 初始化图标
	public void setTabIcon(int normalIconId, int focusIconId) {
		this.normalIconId = normalIconId;
		this.focusIconId = focusIconId;

		ivTabIcon.setImageResource(normalIconId);
	}

	// 设置未读数
	public void setTabUnreadCount(int unreadCount) {
		if (unreadCount <= 0) {
			tvTabUnRead.setVisibility(View.GONE);
		} else {
			if (unreadCount <= 99) {
				tvTabUnRead.setText(unreadCount + "");
			} else {
				tvTabUnRead.setText("99+");
			}

			tvTabUnRead.setVisibility(View.VISIBLE);
		}
	}

	// 设置选中
	public void setCurrentFocus(boolean selected) {
		if (selected) {
			ivTabIcon.setImageResource(focusIconId);
		} else {
			ivTabIcon.setImageResource(normalIconId);
		}
	}

	// 设置选中
	public void setTextGone(boolean selected) {
		if (selected) {
			tvTabHint.setVisibility(View.GONE);
		}else {
			tvTabHint.setVisibility(View.VISIBLE);
		}
	}

	// 设置选中
	public void setTextColor(boolean selected) {
		if (selected) {
			tvTabHint.setTextColor(ResUtils.getColor(R.color.colorPrimaryT));
		}else {
			tvTabHint.setTextColor(ResUtils.getColor(R.color.font_666666));
		}
	}
}
