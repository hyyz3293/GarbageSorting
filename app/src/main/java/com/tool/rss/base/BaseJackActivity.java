package com.tool.rss.base;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jaeger.library.StatusBarUtil;
import com.tool.rss.R;
import com.tool.rss.receiver.NetWorkStateReceiver;
import com.tool.rss.ui.swipe.SwipePanel;
import com.tool.rss.utils.ResUtils;
import com.tool.rss.utils.status.BarTextColorUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 基础活动
 *
 * @author Jack
 * @date 2018.05.13
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class BaseJackActivity extends BaseActivity {

    /** 侧滑关闭  SlidrInterface.lock();锁定手势，此时不能再拖动  SlidrInterface.unlock();解锁 */
//    public SlidrInterface mSlidrInterface;

    /** 网络请求状态广播 */
    private NetWorkStateReceiver mNetWorkStateReceiver = null;
    private WindowManager wm = null;
    public int mHeightPixels = 0;
    /**
     * 初始化布局
     */
    public abstract void initContentView();

    /**
     * 初始化控件
     */
    public abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为竖屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        //侧滑返回
//        SlidrConfig config=new SlidrConfig.Builder()
//                //滑动起始方向
//                .position(SlidrPosition.LEFT)
//                .edge(true)
//                //距离左边界占屏幕大小的45%
//                .edgeSize(0.45f)
//                .build();
//        mSlidrInterface = Slidr.attach(this, config);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !getTitle().equals("登录")){
            //沉浸式状态栏
            StatusBarUtil.setColor(this, ResUtils.getColor(R.color.white),0);
            //设置状态栏白底黑字
            StatusBarUtil.setLightMode(this);
        }

        //注册网络请求广播
        mNetWorkStateReceiver = new NetWorkStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetWorkStateReceiver, filter);

        initContentView();


        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mHeightPixels = dm.widthPixels;

        initView();
        initData();
        initListener();
    }

    public void initData() {

    }

    public void initListener() {

    }



    /** 统一设置状态栏 高度 */
    public void initStatusHeight(TextView view) {
        try {
            ViewGroup.LayoutParams linearParams = view.getLayoutParams();
            linearParams.height = BarUtils.getStatusBarHeight();
            linearParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(linearParams);
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    public void initStatusWindows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            /**状态栏：5.1(不包含5.1)以上改为全屏，背景白色*/
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){/**5.1及5.1以下改为灰色*/
            BarTextColorUtils.setStatusBarColor(this, getResources().getColor(R.color.gray));
        }
        StatusBarUtil.setLightMode(this);
    }

    public void initStatusWindows2(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            /**状态栏：5.1(不包含5.1)以上改为全屏，背景白色*/
            getWindow().setStatusBarColor(getResources().getColor(color));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){/**5.1及5.1以下改为灰色*/
            BarTextColorUtils.setStatusBarColor(this, getResources().getColor(R.color.gray));
        }
        StatusBarUtil.setLightMode(this);
    }

    public void swipeAnnel() {
        final SwipePanel swipePanel = new SwipePanel(this);
        swipePanel.setLeftEdgeSize(SizeUtils.dp2px(100));// 设置左侧触发阈值 100dp
        swipePanel.setLeftDrawable(R.drawable.base_back);// 设置左侧 icon
        swipePanel.wrapView(findViewById(R.id.rootLayout));// 设置嵌套在 rootLayout 外层
        swipePanel.setOnFullSwipeListener(new SwipePanel.OnFullSwipeListener() {// 设置完全划开松手后的监听
            @Override
            public void onFullSwipe(int direction) {
                finish();
                swipePanel.close(direction);// 关闭
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //解除网络请求状态广播
            if (null != mNetWorkStateReceiver) {
                unregisterReceiver(mNetWorkStateReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
