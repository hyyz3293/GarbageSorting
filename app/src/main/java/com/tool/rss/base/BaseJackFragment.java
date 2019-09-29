package com.tool.rss.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
/**
 * Fragment基类
 *
 * @author jack
 * @date 2018.05.17
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class BaseJackFragment extends BaseFragment {
    /** 根布局 */
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(getContentView(), container, false);
        initView(mView);
        initListener();
        initData();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 获得根布局
     *
     * @return View 根布局
     */
    @Override
    public View getView() {
        return mView;
    }

    /**
     * 设置ContentView的ResId
     *
     * @return ContentView的ResId
     */
    public abstract int getContentView();

    /**
     * 初始化布局
     *
     * @param rootView 根布局
     */
    public abstract void initView(View rootView);

    public void initData() {

    }

    public void initListener() {

    }
}
