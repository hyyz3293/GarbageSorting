package com.tool.rss.ui.activity;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tool.rss.R;
import com.tool.rss.base.BaseJackActivity;
import com.tool.rss.ui.NetWork.JKX_API;
import com.tool.rss.ui.model.VersionEntity;
import com.tool.rss.utils.update.AppUpdateUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AppInfoActivity extends BaseJackActivity implements View.OnClickListener {

    private ImageView backBtn;
    private TextView mStatus, mTvUpdate, mTvVersion;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_app_info);
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
        mTvUpdate = (TextView) findViewById(R.id.tv_check_update);
        mTvVersion = (TextView) findViewById(R.id.tv_app_version);
        mTvUpdate.setOnClickListener(this);
        mTvVersion.setText("v" + AppUtils.getAppVersionName());
        swipeAnnel();
    }

    @Override
    public void initData() {
        super.initData();

    }

    /** 获取app 更新信息*/
    private void loadData() {
        JKX_API.getInstance().getAppVersion("", new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Object o) {
                try {
                    VersionEntity versionEntity = (VersionEntity) o;
                    String newVersion = versionEntity.data.version + "";
                    boolean isShowUpdate = AppUpdateUtils.newInstance().isCanUpdateApp(newVersion, getBaseActivity());
                    SPUtils.getInstance().put("is_update", isShowUpdate);

                    if (isShowUpdate)
                        AppUpdateUtils.newInstance().updateApp(AppInfoActivity.this, versionEntity.data.version + "", versionEntity.data.isForce
                                , versionEntity.data.message, versionEntity.data.url);
                    else
                        showToast("你已经是最新版本");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable e) {
                LogUtils.e(e);
                SPUtils.getInstance().put("is_update", false);
            }
            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_back_toolBar:
                finish();
                break;
            case R.id.tv_check_update:
                loadData();
                break;
        }
    }
}
