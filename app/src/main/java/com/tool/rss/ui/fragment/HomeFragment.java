package com.tool.rss.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tool.rss.R;
import com.tool.rss.RkApplication;
import com.tool.rss.base.BaseJackFragment;
import com.tool.rss.ui.NetWork.OrcLoadUtils;
import com.tool.rss.ui.adapter.HomeAdapter;
import com.tool.rss.ui.bdAudio.RecogResult;
import com.tool.rss.ui.model.TrashResultEntity;
import com.tool.rss.ui.widget.status.LoadAbstract;
import com.tool.rss.ui.widget.view.LastInputEditText;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseJackFragment implements View.OnClickListener {

    private RelativeLayout mRelTop;
    private LastInputEditText mEtSearch;
    private RecyclerView mRecycleView;
    private TextView mTvErrorTip;
    private View mViewLottie;

    private LottieAnimationView mLottieView;
    private ImageView mImgVoice;

    private LinearLayout mLlSearTip;//搜索提示

    private HomeAdapter mAdapter;
    private EventListener mBdEventListener;
    private EventManager asr;

    private HomeFragment mFragment;

    List<LocalMedia> selectList = new ArrayList<>();

    @Override
    public void onDestroy() {
        super.onDestroy();
        asr.unregisterListener(mBdEventListener);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(View rootView) {
        mFragment = this;
        mRelTop = (RelativeLayout) rootView.findViewById(R.id.top_rel);
        mTvErrorTip = (TextView) rootView.findViewById(R.id.error_tip);
        mEtSearch = (LastInputEditText) rootView.findViewById(R.id.et_search);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recycleview_home);
        rootView.findViewById(R.id.audio_start).setOnClickListener(this);
        rootView.findViewById(R.id.img_camera).setOnClickListener(this);
        mViewLottie = rootView.findViewById(R.id.home_bg_lottie);

        mImgVoice = rootView.findViewById(R.id.home_voice);
        mLottieView = rootView.findViewById(R.id.home_lottie);

        mLlSearTip = rootView.findViewById(R.id.tip_search);
    }

    @Override
    public void initData() {
        super.initData();
        asr = EventManagerFactory.create(mContext, "asr");
        asr.registerListener(mBdEventListener);

        mAdapter = new HomeAdapter();
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        mBdEventListener = (name, params, data, offset, length) -> {
            if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)){
                // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
                LogUtils.e("引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了");
                mLottieView.setVisibility(View.VISIBLE);
                mImgVoice.setVisibility(View.GONE);
            }
            if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)){
                // 识别结束
                LogUtils.e("识别结束");
                mLottieView.setVisibility(View.GONE);
                mImgVoice.setVisibility(View.VISIBLE);
            }
            if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)){
                //LogUtils.e(params);
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
                    mLlSearTip.setVisibility(View.VISIBLE);
                    mTvErrorTip.setVisibility(View.GONE);
                    mAdapter.setNewData(null);
                }
            }});
    }

    /** 加载数据 */
    @SuppressLint("CheckResult")
    private void loadData() {
        String serachStr = mEtSearch.getText().toString();
        LogUtils.e("search________", serachStr);
        if (StringUtils.isEmpty(serachStr)) {
            showToast("请输入搜索内容", mRelTop);
        } else {
            if (OrcLoadUtils.isWifiProxy(RkApplication.getInstance())) {
                showToast("请先关闭代理等", mRelTop);
            } else {
                OrcLoadUtils.searchName(serachStr).subscribe(o -> {
                    try {
                        hideStatus(mViewLottie);
                        TrashResultEntity entity = (TrashResultEntity) o;
                        if (entity != null) {
                            if (entity.Ok) {
                                if (entity.Data != null && entity.Data.size() > 0) {
                                    mLlSearTip.setVisibility(View.GONE);
                                    mTvErrorTip.setVisibility(View.GONE);
                                    String serachStr2 = mEtSearch.getText().toString();
                                    if (!StringUtils.isEmpty(serachStr2)) {
                                        mAdapter.setNewData(entity.Data);
                                    }
                                } else {
                                    loadDataTwo(serachStr);
                                }
                            } else {
                                loadDataTwo(serachStr);
                                LogUtils.e("error");
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
        showStatus(mViewLottie, new LoadAbstract(mContext));
        OrcLoadUtils.sougouSearch(serachStr).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                hideStatus(mViewLottie);
                TrashResultEntity entity = (TrashResultEntity) o;
                if (entity != null) {
                    if (entity.Ok) {
                        if (entity.Data != null && entity.Data.size() > 0) {
                            mLlSearTip.setVisibility(View.GONE);
                            mTvErrorTip.setVisibility(View.GONE);
                            mAdapter.setNewData(entity.Data);
                        } else {
                            mTvErrorTip.setText(entity.Msg.toString());
                            setEtVisi();
                        }
                    } else {
                        mTvErrorTip.setText(entity.Msg.toString());
                        setEtVisi();
                    }
                } else {
                    setEtVisi();
                }
            }
        });
    }

    private void setEtVisi() {
        //mLlSearTip.setVisibility(View.VISIBLE);
        String serachStr = mEtSearch.getText().toString();
        if (StringUtils.isEmpty(serachStr)) {
            mTvErrorTip.setVisibility(View.GONE);
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
        }
        mAdapter.setNewData(null);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio_start:
                if (mImgVoice.getVisibility() == View.VISIBLE) {
                    openBdAudio();
                } else {
                    asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
                    mLottieView.setVisibility(View.GONE);
                    mImgVoice.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.img_camera: //图片识别
                PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> permissionsGranted) {
                                PictureSelector.create(mFragment)
                                        .openGallery(PictureMimeType.ofImage())
                                        .maxSelectNum(1)
                                        .selectionMode(PictureConfig.SINGLE)
                                        .forResult(9);
                            }
                            @Override
                            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                            }
                        }).request();
                break;
        }
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
                        asr.send(SpeechConstant.ASR_START, "{}", null, 0, 0);
                    }
                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        LogUtils.e("权限不足");
                    }
                }).request();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 9:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (null != selectList && selectList.size() > 0) {
                        String path = selectList.get(0).getPath();
                        LogUtils.e(path);
                        loadImage(path);
                    }
                    break;
            }
        }
    }

    /** 图片识别接口 */
    @SuppressLint("CheckResult")
    private void loadImage(String image) {
        showStatus(mViewLottie, new LoadAbstract(mContext));
        OrcLoadUtils.imageTAg(image).subscribe(o -> {
            if (!StringUtils.isEmpty(o.toString())) {
                mEtSearch.setText(o.toString());
            } else {
                hideStatus(mViewLottie);
            }
        });
    }

}
