package com.tool.rss.ui.activity;

import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tool.rss.R;
import com.tool.rss.base.BaseJackActivity;
import com.tool.rss.ui.adapter.HomeDetailAdapter;
import com.tool.rss.ui.adapter.HomeDetailContentAdapter;
import com.tool.rss.ui.model.DetailAllEntity;
import com.tool.rss.utils.HomeDataUtils;

public class RefuseDetailActivity extends BaseJackActivity {

    private TextView mStatus;
    private RecyclerView mRecycleView;
    private HomeDetailAdapter mAdapter;
    private TextView mTvRoot;
    private ImageView mImgTitle;
    private LinearLayout mLlClose;
    private RelativeLayout mRelTops;
    private TextView mTvContent;//, mTvContent1, mTvContent2, mTvContent3;
    private RecyclerView mRecyclContent;
    private HomeDetailContentAdapter mContentAdapter;
    private View mHeaderView;
    private int type;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_refuse_detail);
    }

    @Override
    public void initView() {
        mStatus =  (TextView) findViewById(R.id.tvStatusBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mStatus.setVisibility(View.VISIBLE);
            initStatusHeight(mStatus);
        }
        mTvRoot = (TextView) findViewById(R.id.detial_refuse_title);
        mLlClose = (LinearLayout) findViewById(R.id.detail_close);
        mRelTops = (RelativeLayout) findViewById(R.id.top_rels);
        mRecycleView = (RecyclerView) findViewById(R.id.recyclerdetail);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.item_homedetail_header, (ViewGroup) mRecycleView.getParent(), false);
        mRecyclContent = mHeaderView.findViewById(R.id.recycler_content);
        mImgTitle = (ImageView) mHeaderView.findViewById(R.id.detail_tile_img);
        mTvContent = (TextView) mHeaderView.findViewById(R.id.detail_content);
        //mTvContent1 = (TextView) mHeaderView.findViewById(R.id.detail_content1);
        //mTvContent2 = (TextView) mHeaderView.findViewById(R.id.detail_content2);
        //mTvContent3 = (TextView) mHeaderView.findViewById(R.id.detail_content3);
        swipeAnnel();
    }

    @Override
    public void initData() {
        super.initData();
        mAdapter = new HomeDetailAdapter();
        mRecycleView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycleView.setAdapter(mAdapter);

        mContentAdapter = new HomeDetailContentAdapter();
        mRecyclContent.setLayoutManager(new LinearLayoutManager(this));
        mRecyclContent.setAdapter(mContentAdapter);

        if (getIntent() != null)
            type = getIntent().getIntExtra("type", 0);

        if (type != 0) {
            DetailAllEntity entity = HomeDataUtils.GetDetailAllData(type);
            mTvRoot.setText(entity.title);
            initStatusWindows2(entity.colorId);
            mImgTitle.setBackgroundResource(entity.drawableId);
            mRelTops.setBackgroundResource(entity.colorId);
            mTvContent.setText(entity.content);
            mAdapter.setHeaderView(mHeaderView);
            mAdapter.setNewData(entity.list);

            mContentAdapter.setNewData(entity.contents);
        }



    }

    @Override
    public void initListener() {
        super.initListener();
        mLlClose.setOnClickListener(v -> finish());
    }
}
