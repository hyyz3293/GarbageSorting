package com.tool.rss.ui.NetWork;

import com.blankj.utilcode.util.StringUtils;
import com.tool.rss.ui.model.HotSearchEntity;
import com.tool.rss.ui.model.VersionEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * JKX2018
 * Created by xyrzx on 2018/5/7.
 */
public interface JKX_Interface {

    /*垃圾分类更新-垃圾分类更新 */
    @GET("trashupdate")
    Observable<VersionEntity> getAppVersion(@Query("version") String orderId);

    /*垃圾分类更新-垃圾分类更新 */
    @GET("getRefuseHotSearch")
    Observable<HotSearchEntity> getRefuseHotSearch();


    /* 请求Oauth */
    @GET("searchList.jsp?uID=AAEExdgvKAAAAAqKJmVZugEAZAM%3D&v=5&dp=1&pid=sogou-waps-7880d7226e872b77&w=1283&t=1561362565111&s_t=1561362573442&s_from=result_up&htprequery=%E5%9E%83%E5%9C%BE%E5%88%86%E7%B1%BB&keyword=%E5%9E%83%E5%9C%BE%E5%88%86%E7%B1%BB&pg=webSearchList&s=%E6%90%9C%E7%B4%A2&suguuid=0c0c451f-204a-4653-b3f1-a30c8451e048&sugsuv=AAEExdgvKAAAAAqKJmVZugEAZAM&sugtime=1561362573441")
    Observable<Object> searchType();

    /* 腾讯图片鉴权 */
    @FormUrlEncoded
    @POST("image_tag") // https://api.ai.qq.com/fcgi-bin/image/image_tag
    Observable<Object> image_tag(@Field("app_id") int app_id,
                                 @Field("time_stamp") long time_stamp,
                                 @Field("nonce_str") String nonce_str,
                                 @Field("sign") String sign,
                                 @Field("image") String image);

    /* 请求Oauth */
    @FormUrlEncoded
    @POST("cities/search")
    Observable<Object> test(@Field("action") String action,
                            @Field("language") String language);
}
