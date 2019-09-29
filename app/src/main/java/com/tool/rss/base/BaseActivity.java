package com.tool.rss.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.tool.rss.annotation.ActivityOption;
import com.tool.rss.ui.dialog.LoadingDialog;
import com.tool.rss.ui.widget.TopSnackBar.TSnackbar;
import com.tool.rss.ui.widget.status.AbstractStatus;
import com.tool.rss.ui.widget.status.StatusLayout;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
/**
 * Activity基类
 *
 * @author jack
 * @date 2018.05.23
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BaseActivity extends AppCompatActivity implements BaseView, LifecycleProvider<ActivityEvent> {
    /** RxJava生命周期管理 */
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    /** 状态 */
    private HashMap<View, StatusLayout> mStatusMap = new HashMap<>();

    @Override
    public Observable<ActivityEvent> lifecycle() {
        return mLifecycleSubject.hide();
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(mLifecycleSubject);
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleSubject.onNext(ActivityEvent.CREATE);
    }





    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(ActivityEvent.START);
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @CallSuper
    @Override
    protected void onPause() {
        mLifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @CallSuper
    @Override
    protected void onStop() {
        mLifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        mLifecycleSubject.onNext(ActivityEvent.DESTROY);
        getWindow().getDecorView().removeCallbacks(null);
        super.onDestroy();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showToast(String msg, ViewGroup viewGroup) {
        TSnackbar.make(viewGroup, msg, TSnackbar.LENGTH_SHORT, TSnackbar.APPEAR_FROM_TOP_TO_DOWN)
                .setBackgroundColor(Color.parseColor("#F2f65a2e")).show();
    }

    @Override
    public void showLoading() {
        LoadingDialog.getInstance().show(this);
    }

    @Override
    public void hideLoading() {
        LoadingDialog.getInstance().close();
    }

    @Override
    public void showStatus(View originView, AbstractStatus status) {
        StatusLayout statusLayout = mStatusMap.get(originView);
        if (statusLayout == null) {
            statusLayout = new StatusLayout(this, originView);
            mStatusMap.put(originView, statusLayout);
        }
        statusLayout.showStatus(status);
    }

    @Override
    public void hideStatus(View originView) {
        StatusLayout statusLayout = mStatusMap.get(originView);
        if (statusLayout != null) {
            statusLayout.hideStatus();
        }
    }

    @Override
    public int getCompatColor(int resId) {
        return ContextCompat.getColor(this, resId);
    }

    @Override
    public float getDimensionPixelOffset(int resId) {
        return getResources().getDimensionPixelOffset(resId);
    }

    @Override
    public String getCompatString(int resId) {
        return getString(resId);
    }

    @Override
    public Drawable getCompatDrawable(int resId) {
        return ContextCompat.getDrawable(this, resId);
    }

    @Override
    public void startActivity(Class<?> clazz) {
        this.startActivity(new Intent(this, clazz));
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
    }

    @Override
    public void startActivityForResult(Class<?> clazz, int requestCode) {
        this.startActivityForResult(new Intent(this, clazz), requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }


    /**
     * 使用调度器
     *
     * @param event 生命周期
     * @return 调度转换器
     */
    public <T> ObservableTransformer<T, T> applySchedulers(final ActivityEvent event) {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                // 若不绑定到View的生命周期，则直接子线程中处理 -> UI线程中回调
                if (event != null) {
                    return upstream.compose(BaseActivity.this.<T>bindUntilEvent(event)).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.compose(BaseActivity.this.<T>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 启动Activity(扩展)
     *
     * @param context 上下文
     * @param activityClass Activity的class
     * @param intentFlags intent标识
     */
    public static void startActivityEx(Context context, Class<?> activityClass, int intentFlags) {
        startActivityEx(context, activityClass, null, intentFlags);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param context 上下文
     * @param activityClass Activity的class
     * @param data 参数
     * @param intentFlags intent标识
     */
    public static void startActivityEx(Context context, Class<?> activityClass, Bundle data, int intentFlags) {
        if (!hookRequestLogin(activityClass, data)) {
            Intent intent = new Intent(context, activityClass);
            intent.setFlags(intentFlags);
            if (data != null) {
                intent.putExtras(data);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 带转场的启动Activity(扩展)
     *
     * @param context 上下文
     * @param activityClass Activity的class
     * @param data Intent携带的数据
     * @param transitionActivityOptions 转场参数
     * @param intentFlags intent标识
     */
    public static void startActivityTranslationEx(Context context, Class<?> activityClass, Bundle data, ActivityOptionsCompat transitionActivityOptions, int intentFlags) {
        if (!hookRequestLogin(activityClass, data)) {
            Intent intent = new Intent(context, activityClass);
            intent.setFlags(intentFlags);
            if (data != null) {
                intent.putExtras(data);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, transitionActivityOptions.toBundle());
            } else {
                context.startActivity(intent);
            }
        }
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activity activity
     * @param activityClass Activity的class
     * @param requestCode 请求码
     * @param intentFlags intent标识
     */
    public static void startActivityForResultEx(AppCompatActivity activity, Class<?> activityClass,
                                                int requestCode, int intentFlags) {
        startActivityForResultEx(activity, activityClass, requestCode, null, intentFlags);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activity activity
     * @param activityClass Activity的class
     * @param data 参数
     * @param requestCode 请求码
     * @param intentFlags intent标识
     */
    public static void startActivityForResultEx(AppCompatActivity activity, Class<?> activityClass, int requestCode,
                                                Bundle data, int intentFlags) {
        if (!hookRequestLogin(activityClass, data)) {
            Intent intent = new Intent(activity, activityClass);
            intent.setFlags(intentFlags);
            if (data != null) {
                intent.putExtras(data);
            }
            activity.startActivityForResult(intent, requestCode);
        }
    }


    /**
     * 拦截请求登录的页面
     *
     * @param activityClass 页面的类
     * @param data 参数
     * @return true -- 拦截成功  false -- 没有拦截
     */
    public static boolean hookRequestLogin(Class<?> activityClass, Bundle data) {
        // 查看该activity是否需要登录
        ActivityOption option = activityClass.getAnnotation(ActivityOption.class);
        if (option != null && option.reqLogin()) {

            return !BaseApplication.getInstance().checkLoginBeforeAction(activityClass.getName(), data);
        }

        return false;
    }

    /**
     * 显示Fragment
     *
     * @param containerId 容器id
     * @param fragment fragment
     */
    protected void showFragment(int containerId, Fragment fragment) {
        if (fragment == null) {
            return;
        }

        String tag = fragment.getClass().getSimpleName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment cache = fm.findFragmentByTag(fragment.getTag());
        if (cache != null) {
            ft.show(cache);
        } else {
            ft.add(containerId, fragment, tag);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 替换Fragment
     *
     * @param containerId 容器id
     * @param fragment fragment
     */
    protected void replaceFragment(int containerId, Fragment fragment) {
        if (fragment == null) {
            return;
        }

        String tag = fragment.getClass().getSimpleName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * 隐藏fragment
     *
     * @param fragment fragment
     */
    protected void hideFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment cache = fm.findFragmentByTag(fragment.getTag());
        if (cache != null) {
            ft.hide(cache);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = super.dispatchTouchEvent(ev);
        /*
         * 若点击Activity的任何区域(除了输入框之外，应隐藏键盘)
         */
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();

            if (v != null && shouldHideInput(v, ev)) {
                hideInput(v);
            }
        }

        return ret;
    }

    /**
     * 是否应该隐藏输入
     *
     * @param v 焦点控件
     * @param event 动作事件
     * @return true -- 是  false -- 否
     */
    protected boolean shouldHideInput(View v, MotionEvent event) {
        boolean should = true;

        // 仅点击到输入框时，键盘不隐藏
        if (v != null && v instanceof EditText) {
            int[] loc = new int[2];
            v.getLocationOnScreen(loc);

            // 焦点控件位置
            int left = loc[0];
            int top = loc[1];
            int right = left + v.getWidth();
            int bottom = top + v.getHeight();

            int touchX = (int) event.getRawX();
            int touchY = (int) event.getRawY();

            // 是否点击到输入框
            if ((touchX >= left && touchX <= right) &&
                    (touchY >= top && touchY <= bottom)) {

                should = false;
            }
        }

        return should;
    }

    /**
     * 隐藏键盘
     *
     * @param v 控件
     */
    private void hideInput(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 检测登录的拦截器接口
     */
    public static interface ICheckLoginInterceptor {
        /**
         * 在活动之前检测登录情况
         *
         * @param actionAfterLogin 登录后的action
         * @param data 参数
         * @return true -- 已经登录 false -- 没有登录
         * @note 1. 实现者若检测到没有登录，则需要自己的处理未登录的逻辑
         * 2. 实现者若自己处理了未登录的逻辑，可执行actionAfterLogin实现隐式启动活动
         * 3. 若需要通过actionAfterLogin隐式启动活动，则需将该活动名作为action注册到
         * intent filter之中
         */
        public boolean checkLoginBeforeAction(String actionAfterLogin, Bundle data);
    }
}
