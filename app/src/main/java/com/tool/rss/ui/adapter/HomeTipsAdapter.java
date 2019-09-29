package com.tool.rss.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tool.rss.R;
import com.tool.rss.ui.model.ItemEntity;

public class HomeTipsAdapter extends BaseQuickAdapter<ItemEntity, BaseViewHolder> {
    public HomeTipsAdapter() {
        super(R.layout.adapter_tips);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemEntity item) {
        helper.setText(R.id.adapter_tips_txt, item.title);
    }
}
