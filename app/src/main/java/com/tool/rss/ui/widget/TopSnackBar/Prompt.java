package com.tool.rss.ui.widget.TopSnackBar;


import com.tool.rss.R;

public enum Prompt {
    /**
     * 红色,错误
     */
    ERROR(R.mipmap.common_bounced_icon_error, R.color.black),

    /**
     * 红色,警告
     */
    WARNING(R.mipmap.common_bounced_icon_warning, R.color.black),

    /**
     * 绿色,成功
     */
    SUCCESS(R.mipmap.common_bounced_icon_successful, R.color.black);

    private int resIcon;
    private int backgroundColor;

    Prompt(int resIcon, int backgroundColor) {
        this.resIcon = resIcon;
        this.backgroundColor = backgroundColor;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
