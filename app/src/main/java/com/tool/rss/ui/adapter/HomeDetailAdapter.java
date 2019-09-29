package com.tool.rss.ui.adapter;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tool.rss.R;
import com.tool.rss.ui.model.DetailAllEntity;

public class HomeDetailAdapter extends BaseQuickAdapter<DetailAllEntity.DataBean, BaseViewHolder> {
    public HomeDetailAdapter() {
        super(R.layout.adapter_home_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, DetailAllEntity.DataBean item) {
        helper.setText(R.id.detail_name, item.name);
        LinearLayout mLlTops = helper.getView(R.id.ll_detail_tops);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(54));
        LogUtils.e(helper.getPosition() +"-----------------" + helper.getPosition() % 2);
        if (helper.getAdapterPosition() % 2 == 1) {
            layoutParams.leftMargin  = SizeUtils.dp2px(15);
            layoutParams.rightMargin  = SizeUtils.dp2px(9);
        } else {
            layoutParams.leftMargin  = SizeUtils.dp2px(9);
            layoutParams.rightMargin  = SizeUtils.dp2px(15);
        }
        layoutParams.topMargin = SizeUtils.dp2px(15);
        mLlTops.setLayoutParams(layoutParams);
    }
}
