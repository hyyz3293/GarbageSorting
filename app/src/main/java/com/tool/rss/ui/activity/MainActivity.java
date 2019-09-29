package com.tool.rss.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.jaeger.library.StatusBarUtil;
import com.tool.rss.R;
import com.tool.rss.base.BaseFragment;
import com.tool.rss.base.BaseJackActivity;
import com.tool.rss.ui.fragment.ClassFragment;
import com.tool.rss.ui.fragment.HomeFragment;
import com.tool.rss.ui.widget.view.NoScrollViewPager;
import com.tool.rss.ui.widget.view.TabIndicator;


public class MainActivity extends BaseJackActivity {
    private FrameLayout mFlMainContainer;
    /*** 缓存容器*/
    private SparseArray<BaseFragment> mCacheContainer;
    private NoScrollViewPager viewPager;
    private TabIndicator mHomeTab, mMineTab;
    private HomeFragment mHomeFragment;
    private ClassFragment mMineFragment;

    private long firstTime = 0;

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        mFlMainContainer = (FrameLayout) findViewById(R.id.mFlMainContainer);
        viewPager = LayoutInflater.from(this)
                .inflate(R.layout.item_main_view_pager, mFlMainContainer, true).findViewById(R.id.vpMain);
        mHomeTab = (TabIndicator) findViewById(R.id.mHomeIndicator);
        mMineTab = (TabIndicator) findViewById(R.id.mUserIndicator);
        setStatusBar();
        initData();
        KeyboardUtils.showSoftInput(this);

        String soundFile = "1111.saf";
        String extensionRegx = "\\..*$";
        String newSoundFile = soundFile.replaceAll(extensionRegx, "");
        newSoundFile = newSoundFile + ".mp3";
        LogUtils.e(newSoundFile);
    }

    /**
     * 状态栏
     */
    private void setStatusBar() {
        //6.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void initData() {
        mCacheContainer = new SparseArray<>();
        mHomeFragment = new HomeFragment();
        mMineFragment = new ClassFragment();

        mCacheContainer.put(0, mHomeFragment);
        mCacheContainer.put(1, mMineFragment);

        viewPager.setScroll(true);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mCacheContainer.get(position);
            }

            @Override
            public int getCount() {
                return mCacheContainer.size();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition;
            @Override
            public void onPageScrolled(int i, float v, int i1) { }
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                // ViewPager.SCROLL_STATE_IDLE 标识的状态是当前页面完全展现，并且没有动画正在进行中，如果不
                // 是此状态下执行 setCurrentItem 方法回在首位替换的时候会出现跳动！
                if (state != ViewPager.SCROLL_STATE_IDLE) return;
                setCurrentTab(currentPosition);
            }
        });

        initTab();
        setCurrentTab(0);

    }

    private void initTab() {
        mHomeTab.setTabIcon(R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        mHomeTab.setTabTitle("首页");
        mMineTab.setTabIcon(R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        mMineTab.setTabTitle("分类");

        mHomeTab.setOnClickListener(v -> startCenterAnimation(mHomeTab, 0));

        mMineTab.setOnClickListener(v -> startCenterAnimation(mMineTab, 1));
    }

    private void startCenterAnimation(View mView, int i) {
        AnimationSet animation = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        animation.addAnimation(scaleAnimation);
        mView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setCurrentTab(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setCurrentTab(int i) {
        switch (i) {
            case 0:
                StatusBarUtil.setLightMode(getBaseActivity());
                viewPager.setCurrentItem(0);
                mHomeTab.setCurrentFocus(true);
                mHomeTab.setTextColor(true);
                mMineTab.setCurrentFocus(false);
                mMineTab.setTextColor(false);
                break;
            case 1:
                StatusBarUtil.setDarkMode(getBaseActivity());
                viewPager.setCurrentItem(1);
                mHomeTab.setCurrentFocus(false);
                mHomeTab.setTextColor(false);
                mMineTab.setCurrentFocus(true);
                mMineTab.setTextColor(true);
                break;
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
}
