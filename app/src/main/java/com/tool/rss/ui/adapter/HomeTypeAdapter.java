package com.tool.rss.ui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tool.rss.R;
import com.tool.rss.ui.model.ItemEntity;

public class HomeTypeAdapter extends BaseQuickAdapter<ItemEntity, BaseViewHolder> {

    public HomeTypeAdapter() {
        super(R.layout.adapter_home_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemEntity item) {
        ImageView img = helper.getView(R.id.home_type_img);
        TextView txt = helper.getView(R.id.home_type_txt);
        img.setBackgroundResource(item.img);
        txt.setText(item.title);
    }
}
