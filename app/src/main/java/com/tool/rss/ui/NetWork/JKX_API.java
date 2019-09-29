package com.tool.rss.ui.NetWork;

import com.blankj.utilcode.util.LogUtils;
import com.tool.rss.base.BaseActivity;
import com.tool.rss.base.BaseFragment;
import com.tool.rss.base.BaseJackActivity;
import com.tool.rss.base.BaseJackFragment;
import com.tool.rss.ui.NetWork.error.LenientGsonConverterFactory;
import com.tool.rss.utils.txsign.TxSignUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 * JKX2018
 * Created by xyrzx on 2018/5/4.
 */
public class JKX_API {

    private static JKX_Interface SERVICE;
    /**
     * 请求超时时间
     */
    private static final int DEFAULT_TIMEOUT = 10;

    //获取单例
    public static JKX_API getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final JKX_API INSTANCE = new JKX_API();
    }

    private void toSubscribe(Observable o, Observer s) {
        o.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 页面销毁时，中断网络请求
     */
    private void toSubscribe(Observable o, Observer s, BaseJackActivity view) {
        o.compose(view.applySchedulers()).subscribe(s);
    }

    /**
     * 页面销毁时，中断网络请求
     */
    private void toSubscribe(Observable o, Observer s, BaseJackFragment view) {
        o.compose(view.applySchedulers()).subscribe(s);
    }


    //私有构造方法
    private JKX_API() {

    }

    private void PostClink() {
        String HOST = AppManager.getInstance().getHost();

        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        // 添加公共参数拦截器
        JKX_HttpCommonInterceptor.Builder commonInterceptorBuilder = new JKX_HttpCommonInterceptor.Builder();
//        for (Map.Entry<String, String> entry : JackHeaderUtil.getDefaultHeaders().entrySet()) {
//            commonInterceptorBuilder.addHeaderParams(entry.getKey(), entry.getValue());
//        }
        commonInterceptorBuilder.build();
        httpClientBuilder.addInterceptor(commonInterceptorBuilder.build());
        httpClientBuilder.addInterceptor(new LoggerInterceptor());

        SERVICE = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(HOST)
                .build()
                .create(JKX_Interface.class);
    }

    private void PostComment(String url) {

        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        SERVICE = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.addConverterFactory(new LenientGsonConverterFactory.create())
                .baseUrl(url)
                .build()
                .create(JKX_Interface.class);
    }

    private void PostComment(String url, Map<String, Object> map) {

        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 添加公共参数拦截器
        JKX_HttpCommonInterceptor.Builder commonInterceptorBuilder = new JKX_HttpCommonInterceptor.Builder();
        for (String key : map.keySet()) {
            commonInterceptorBuilder.addHeaderParams(key, map.get(key).toString());
        }
        //'Content-Type: application/x-www-form-urlencoded'
        commonInterceptorBuilder.addHeaderParams("Content-Type", "application/x-www-form-urlencoded");
        commonInterceptorBuilder.build();
        httpClientBuilder.addInterceptor(commonInterceptorBuilder.build());
        SERVICE = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.addConverterFactory(new LenientGsonConverterFactory.create())
                .baseUrl(url)
                .build()
                .create(JKX_Interface.class);
    }


    /*  更新  */
    public void getAppVersion(String ver, Observer subscriber) {
        PostClink();
        Observable observable = SERVICE.getAppVersion(ver);
        toSubscribe(observable, subscriber);
    }


    /*  热门搜索 */
    public void getRefuseHotSearch(Observer subscriber) {
        PostClink();
        Observable observable = SERVICE.getRefuseHotSearch();
        toSubscribe(observable, subscriber);
    }

    /** 搜索 */
    public void search(String url, Observer subscriber) {
        PostComment(url);
        Observable observable = SERVICE.searchType();
        toSubscribe(observable, subscriber);
    }

    /** 搜索 */
    public void test(String url, String action, String language, Observer subscriber) {
        PostComment(url);
        Observable observable = SERVICE.test(action, language);
        toSubscribe(observable, subscriber);
    }
}