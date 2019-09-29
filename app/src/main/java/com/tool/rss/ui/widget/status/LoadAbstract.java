package com.tool.rss.ui.widget.status;

import android.content.Context;

import com.tool.rss.R;

public class LoadAbstract extends AbstractStatus{
    public LoadAbstract(Context context) {
        super(context);
    }
    @Override
    public int getContentView() {
        return R.layout.item_status_loading;
    }
}
