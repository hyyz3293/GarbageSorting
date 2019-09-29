package com.tool.rss.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.tool.rss.R;
import com.tool.rss.base.BaseJackFragment;
import com.tool.rss.ui.activity.HomeActivity;
import com.tool.rss.ui.activity.SettingActivity;

public class ClassFragment extends BaseJackFragment implements View.OnClickListener {

    @Override
    public int getContentView() {
        return R.layout.fragment_class;
    }

    @Override
    public void initView(View rootView) {
        rootView.findViewById(R.id.class_setting).setOnClickListener(this);
        rootView.findViewById(R.id.class_new).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.class_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.class_new:
                startActivity(new Intent(mContext, HomeActivity.class));
                break;
        }
    }
}
