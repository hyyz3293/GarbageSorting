package com.tool.rss.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tool.rss.R;
import com.tool.rss.base.BaseJackActivity;

public class SettingActivity extends BaseJackActivity implements View.OnClickListener {
    private ImageView backBtn;
    private TextView mStatus;
    private ImageView mImgRedVerios;
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {
        initStatusWindows();
        backBtn = (ImageView) findViewById(R.id.camera_back_toolBar);
        backBtn.setOnClickListener(this);
        mStatus =  (TextView) findViewById(R.id.tvStatusBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mStatus.setVisibility(View.VISIBLE);
            initStatusHeight(mStatus);
        }
        findViewById(R.id.ll_setting_app_info).setOnClickListener(this);
        findViewById(R.id.ll_setting_app_comment).setOnClickListener(this);

        mImgRedVerios = (ImageView) findViewById(R.id.iv_setting_red_point);

        swipeAnnel();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean is_update = SPUtils.getInstance().getBoolean("is_update", false);
        if (is_update)
            mImgRedVerios.setVisibility(View.VISIBLE);
        else mImgRedVerios.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_back_toolBar:
                finish();
                break;
            case R.id.ll_setting_app_info:
                startActivity(new Intent(this, AppInfoActivity.class));
                break;
            case R.id.ll_setting_app_comment:
                //评价
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + AppUtils.getAppPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
    }
}
