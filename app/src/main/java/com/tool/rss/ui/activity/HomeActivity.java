package com.tool.rss.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tool.rss.R;
import com.tool.rss.RkApplication;
import com.tool.rss.base.BaseJackActivity;
import com.tool.rss.ui.NetWork.JKX_API;
import com.tool.rss.ui.NetWork.OrcLoadUtils;
import com.tool.rss.ui.adapter.HomeAdapter;
import com.tool.rss.ui.adapter.HomeTipsAdapter;
import com.tool.rss.ui.adapter.HomeTypeAdapter;
import com.tool.rss.ui.bdAudio.RecogResult;
import com.tool.rss.ui.model.ItemEntity;
import com.tool.rss.ui.model.TrashResultEntity;
import com.tool.rss.ui.widget.status.LoadAbstract;
import com.tool.rss.utils.HomeDataUtils;
import com.umeng.commonsdk.debug.E;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HomeActivity extends BaseJackActivity implements View.OnClickListener {

    private long firstTime = 0;
    private TextView mStatus;
    private EditText mEtSearch;
    private ImageView mImgPhoto;
    private RelativeLayout mRelTops;
    private View mViewLottie;
    private ImageView mImSound;
    private LottieAnimationView mLottigSound, mLotSetting;

    private TextView mTvSoundTips;

    private RecyclerView mTipsRecycle;
    private HomeTipsAdapter mTipsAdapter;

    private RecyclerView mTypeRecycle;
    private HomeTypeAdapter mTypeAdapter;

    private View headerView;
    private LinearLayout mLlHeaderView;
    private RecyclerView mResultView;
    private HomeAdapter mResAdapter;

    List<LocalMedia> selectList; // 图片返回

    private EventListener mBdEventListener;//语音识别
    private EventManager asr;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asr != null && mBdEventListener != null)
            asr.unregisterListener(mBdEventListener);
    }

    /** 状态栏*/
    private void setStatusBar() {
        //6.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void initView() {
        setStatusBar();
        KeyboardUtils.showSoftInput(this);
        initStatusWindows2(R.color.maincolor);
        mStatus =  (TextView) findViewById(R.id.tvStatusBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mStatus.setVisibility(View.VISIBLE);
            initStatusHeight(mStatus);
        }
        mRelTops = (RelativeLayout) findViewById(R.id.ll_tops);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mViewLottie = (View) findViewById(R.id.home_bg_lottie);

        mImgPhoto = (ImageView) findViewById(R.id.home_t_photo);

        mImSound = (ImageView) findViewById(R.id.home_down_sound_img);
        mLottigSound = (LottieAnimationView) findViewById(R.id.home_down_sound_lottie);
        mLotSetting = (LottieAnimationView) findViewById(R.id.lottie_home_setting);
        mTvSoundTips = (TextView) findViewById(R.id.home_down_sound_tips);

        mTipsRecycle = (RecyclerView) findViewById(R.id.recycler_tips);
        mTypeRecycle = (RecyclerView) findViewById(R.id.recycler_type);
        mResultView =(RecyclerView) findViewById(R.id.recycler_result);

        headerView = LayoutInflater.from(this).inflate(R.layout.item_homeresult_header, (ViewGroup) mResultView.getParent(), false);
        mLlHeaderView = headerView.findViewById(R.id.home_header_del);
    }

    @Override
    public void initData() {
        super.initData();
        initBdSound();
        selectList = new ArrayList<>();

        mTipsAdapter = new HomeTipsAdapter();
        LinearLayoutManager tipsLiManager = new LinearLayoutManager(this);
        tipsLiManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTipsRecycle.setLayoutManager(tipsLiManager);
        mTipsRecycle.setAdapter(mTipsAdapter);
        mTipsAdapter.setNewData(HomeDataUtils.getTipsList());

        mTypeAdapter = new HomeTypeAdapter();
        mTypeRecycle.setLayoutManager(new GridLayoutManager(this, 4));
        mTypeRecycle.setAdapter(mTypeAdapter);
        mTypeAdapter.setNewData(HomeDataUtils.getTypeList());

        mResAdapter = new HomeAdapter();
        mResultView.setLayoutManager(new LinearLayoutManager(this));
        mResultView.setAdapter(mResAdapter);

        loadHotData();

        HomeDataUtils.checkUpdateVersion(this); //检查更新

        ///OrcLoadUtils.imageTest();
    }

    /** 百度语音 */
    private void initBdSound() {
        mBdEventListener = (name, params, data, offset, length) -> {
            if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)){
                // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
                LogUtils.e("引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了");
                mLottigSound.setVisibility(View.VISIBLE);
                mImSound.setVisibility(View.GONE);
            }
            if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)){
                // 识别结束
                LogUtils.e("识别结束");
                mLottigSound.setVisibility(View.GONE);
                mTvSoundTips.setText("按下说话");
                mImSound.setVisibility(View.VISIBLE);
            }
            if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)){
                LogUtils.e(params);
                RecogResult recogResult = RecogResult.parseJson(params);
                // 识别结果
                String[] results = recogResult.getResultsRecognition();
                if (null != results && results.length > 0) {
                    mEtSearch.setText(results[0]);
                }
                //LogUtils.e(results);
            }
            // ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
        };
        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(mBdEventListener);
    }


    @Override
    public void initListener() {
        super.initListener();
        mLotSetting.setOnClickListener(this);

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String serachStr = mEtSearch.getText().toString();
                if (!StringUtils.isEmpty(serachStr)) {
                    loadData();
                } else {
                    mTypeRecycle.setVisibility(View.VISIBLE);
                    mResAdapter.removeAllHeaderView();
                    mResAdapter.setNewData(null);
                }
            }});

        mLlHeaderView.setOnClickListener(v -> {
            mEtSearch.setText("");
        });
        mImgPhoto.setOnClickListener(this);
        mImSound.setOnClickListener(this);
        mLottigSound.setOnClickListener(this);

        mTipsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ItemEntity entity = (ItemEntity) adapter.getData().get(position);
                openDetails(entity.type);
            }
        });

        mTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ItemEntity entity = (ItemEntity) adapter.getData().get(position);
                openDetails(entity.type);
            }
        });
    }

    /** 获取热门搜索 */
    private void loadHotData() {
        JKX_API.getInstance().getRefuseHotSearch(new Observer() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(Object o) {
                List<ItemEntity> list = HomeDataUtils.getTipsList(o);
                mTipsAdapter.setNewData(list);
            }
            @Override
            public void onError(Throwable e) {
                LogUtils.e(e);
            }
            @Override
            public void onComplete(){ }
        });
    }

    /** 图片识别接口 */
    @SuppressLint("CheckResult")
    private void loadImage(String image) {
        showStatus(mViewLottie, new LoadAbstract(this));
        OrcLoadUtils.imageTAg(image).subscribe(o -> {
            LogUtils.e(o.toString());
            if (o != null && !StringUtils.isEmpty(o.toString())) {
                mEtSearch.setText(o.toString());
            } else {
                hideStatus(mViewLottie);
            }
        });
    }

    /** 加载数据 */
    @SuppressLint("CheckResult")
    private void loadData() {
        String serachStr = mEtSearch.getText().toString();
        if (StringUtils.isEmpty(serachStr)) {
            showToast("请输入搜索内容", mRelTops);
        } else {
            if (OrcLoadUtils.isWifiProxy(RkApplication.getInstance())) {
                showToast("请先关闭代理等", mRelTops);
            } else {
                showStatus(mViewLottie, new LoadAbstract(this));
                OrcLoadUtils.searchName(serachStr).subscribe(o -> {
                    try {
                        hideStatus(mViewLottie);
                        TrashResultEntity entity = (TrashResultEntity) o;
                        if (entity != null) {
                            if (entity.Ok) {
                                if (entity.Data != null && entity.Data.size() > 0) {
                                    String serachStr2 = mEtSearch.getText().toString();
                                    if (!StringUtils.isEmpty(serachStr2)) {
                                        setRestData(entity.Data);
                                    }
                                } else {
                                    loadDataTwo(serachStr);
                                }
                            } else {
                                loadDataTwo(serachStr);
                            }
                        }
                    }catch (Exception e) {
                        loadDataTwo(serachStr);
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @SuppressLint("CheckResult")
    private void loadDataTwo(String serachStr) {
        showStatus(mViewLottie, new LoadAbstract(this));
        OrcLoadUtils.sougouSearch(serachStr).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                hideStatus(mViewLottie);
                TrashResultEntity entity = (TrashResultEntity) o;
                if (entity != null) {
                    if (entity.Ok) {
                        if (entity.Data != null && entity.Data.size() > 0) {
                            setRestData(entity.Data);
                        } else {
                            setRestData(serachStr, entity.Msg.toString());
                        }
                    } else {
                        setRestData(serachStr, "");
                    }
                } else {
                    setRestData(serachStr, "");
                }
            }
        });
    }

    /** 设置显示数据 */
    private void setRestData(List<TrashResultEntity.DataBean> data) {
        mTypeRecycle.setVisibility(View.GONE);
        mResAdapter.setHeaderView(headerView);
        mResAdapter.setNewData(data);
    }

    /** 设置显示数据 */
    private void setRestData(String search, String message) {
        if (StringUtils.isEmpty(message)) {
            message = "未查询到结果";
        }
        List<TrashResultEntity.DataBean> list = new ArrayList<>();
        TrashResultEntity.DataBean data = new TrashResultEntity.DataBean();
        data.Kind = 1008;
        data.msg = message;
        data.Name = search;
        list.add(data);
        mTypeRecycle.setVisibility(View.GONE);
        mResAdapter.setHeaderView(headerView);
        mResAdapter.setNewData(list);
    }

    /** 打开相机 */
    @SuppressLint("WrongConstant")
    private void opemCamera() {
        PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        PictureSelector.create(HomeActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(1)
                                .selectionMode(PictureConfig.SINGLE)
                                .forResult(9);
                    }
                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {}
                }).request();
    }

    /** 开启语音识别 */
    @SuppressLint("WrongConstant")
    private void openBdAudio() {
        PermissionUtils.permission(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        mLottigSound.setVisibility(View.VISIBLE);
                        mImSound.setVisibility(View.GONE);
                        asr.send(SpeechConstant.ASR_START, "{}", null, 0, 0);
                        mTvSoundTips.setText("按下结束说话");
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        LogUtils.e("权限不足");
                        showToast("权限不足");
                    }
                }).request();
    }

    /** 打开垃圾详情 */
    private void openDetails(int type) {
        if (type == 1 || type == 2 || type == 3 || type == 4) {
            Intent intent = new Intent(this, RefuseDetailActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long time = System.currentTimeMillis();
            if (time - firstTime > 2000) {
                showToast("再按一次退出");
                firstTime = time;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_t_photo:
                opemCamera();
                break;
                case R.id.home_down_sound_img:
                    openBdAudio();
                    break;
                    case R.id.home_down_sound_lottie:
                        asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
                        mLottigSound.setVisibility(View.GONE);
                        mImSound.setVisibility(View.VISIBLE);
                        mTvSoundTips.setText("按下说话");
                        break;
                        case R.id.lottie_home_setting:
                            startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 9:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (null != selectList && selectList.size() > 0) {
                        String path = selectList.get(0).getPath();
                        loadImage(path);
                    }
                    break;
            }
        }
    }

}
