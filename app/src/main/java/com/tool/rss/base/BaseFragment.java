package com.tool.rss.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.tool.rss.ui.dialog.LoadingDialog;
import com.tool.rss.ui.widget.TopSnackBar.TSnackbar;
import com.tool.rss.ui.widget.status.AbstractStatus;
import com.tool.rss.ui.widget.status.StatusLayout;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.tool.rss.base.BaseActivity.hookRequestLogin;


/**
 * fragment基类
 *
 * @author mos
 * @date 2017.01.23
 * @note 1. 项目中所有子类必须继承自此基类
 * -------------------------------------------------------------------------------------------------
 * @modified - lwc
 * @date - 2017.5.4
 * @note - loadData - 懒加载数据，使用如下：
 * 1. 重写loadData方法，将加载数据的逻辑放在方法内。通过firstLoad变量，判断loadData是否是第一次调用。
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BaseFragment extends Fragment implements BaseView, LifecycleProvider<FragmentEvent> {
    /** 生命周期管理 */
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    /** 状态 */
    private HashMap<View, StatusLayout> mStatusMap = new HashMap<>();
    /** 绑定上下文 */
    public Context mContext;
    /** 布局是否初始化完成 */
    private boolean mIsViewCreated;
    /** 数据在进入的是否刷新 */
    private boolean mIsFirstLoadData = true;

    public PermissionListener mPermissionListener;


    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getBaseActivity(), permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
        }
    }

    @Override
    public Observable<FragmentEvent> lifecycle() {
        return mLifecycleSubject.hide();
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(mLifecycleSubject);
    }

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mLifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mLifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        // 布局创建成功
        mIsViewCreated = true;
        return rootView;
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(FragmentEvent.START);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(FragmentEvent.RESUME);
        if (!isHidden() && getUserVisibleHint()) {
            // 加载数据
            loadData(mIsFirstLoadData);
            mIsFirstLoadData = false;
        }
    }

    @CallSuper
    @Override
    public void onPause() {
        mLifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @CallSuper
    @Override
    public void onStop() {
        mLifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @CallSuper
    @Override
    public void onDetach() {
        mLifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            // 加载数据
            loadData(mIsFirstLoadData);
            mIsFirstLoadData = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mIsViewCreated) {
            // 加载数据
            loadData(mIsFirstLoadData);
            mIsFirstLoadData = false;
        }
    }

    /**
     * Fragment懒加载数据，主要为了解决ViewPage忽略Fragment生命周期的问题
     *
     * @param firstLoad 是否第一次加载数据
     */
    public void loadData(boolean firstLoad) {
    }

    @Override
    public BaseActivity getBaseActivity() {
        if (mContext == null) {
            throw new RuntimeException("This is an empty object");
        }
        if (mContext instanceof BaseActivity) {
            return (BaseActivity) mContext;
        } else {
            throw new RuntimeException("please let all Activity extends BaseActivity");
        }
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
        LoadingDialog.getInstance().show(getActivity());
    }

    @Override
    public void hideLoading() {
        LoadingDialog.getInstance().close();
    }

    @Override
    public void showStatus(View originView, AbstractStatus status) {
        StatusLayout statusLayout = mStatusMap.get(originView);
        if (statusLayout == null) {
            statusLayout = new StatusLayout(getBaseActivity(), originView);
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
        return ContextCompat.getColor(getActivity(), resId);
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
        return ContextCompat.getDrawable(getActivity(), resId);
    }

    /**
     * 使用调度器
     *
     * @param event 生命周期
     * @return 调度转换器
     */
    public <T> ObservableTransformer<T, T> applySchedulers(final FragmentEvent event) {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                // 若不绑定到View的生命周期，则直接子线程中处理 -> UI线程中回调
                if (event != null) {
                    return upstream.compose(BaseFragment.this.<T>bindUntilEvent(event)).subscribeOn(Schedulers.io())
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
                return upstream.compose(BaseFragment.this.<T>bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void startActivity(Class<?> clazz) {
        this.startActivity(new Intent(getContext(), clazz));
    }

    @Override
    public void startActivity(Intent intent) {
        // TODO: 2018/5/23 添加拦截
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        // TODO: 2018/5/23 添加拦截
        super.startActivity(intent, options);
    }

    /**
     * 有结果的跳转Activity
     *
     * @param clazz 类对象
     * @param requestCode 请求码
     */
    @Override
    public void startActivityForResult(Class<?> clazz, int requestCode) {
        this.startActivityForResult(new Intent(getContext(), clazz), requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO: 2018/5/23 添加拦截
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        // TODO: 2018/5/23 添加拦截
        super.startActivityForResult(intent, requestCode, options);
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
}
