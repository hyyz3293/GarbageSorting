package com.tool.rss.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tool.rss.R;
import com.tool.rss.ui.model.DetailAllEntity;

public class HomeDetailContentAdapter extends BaseQuickAdapter<DetailAllEntity.ContentBean, BaseViewHolder> {
    public HomeDetailContentAdapter() {
        super(R.layout.adapter_detaill_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, DetailAllEntity.ContentBean item) {
        helper.setText(R.id.detail_content_a, item.name);
    }
}
