package com.tool.rss.ui.NetWork;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tool.rss.RkApplication;
import com.tool.rss.ui.NetWork.tx.HttpUtil;
import com.tool.rss.ui.activity.MainActivity;
import com.tool.rss.ui.model.TrashResultEntity;
import com.tool.rss.ui.model.TxLoadEntity;
import com.tool.rss.ui.model.TxSignatureEntity;
import com.tool.rss.utils.ImageUtil;
import com.tool.rss.utils.txsign.Base64Util;
import com.tool.rss.utils.txsign.FileUtil;
import com.tool.rss.utils.txsign.TencentAISign;
import com.tool.rss.utils.txsign.TencentAISignSort;
import com.tool.rss.utils.txsign.TencentAPI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class OrcLoadUtils {

    public static Observable searchName(String name) {

        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(name);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Function<String, TrashResultEntity>() {
                    @Override
                    public TrashResultEntity apply(String t) throws Exception {
                        try {
                            String url = "https://rubbish.qbji.top:8001/api/rubbish/search?key=" + name;

                            String random = getRandomStr(16);
                            LogUtils.e(random);
                            URL url2 = new URL(url);
                            HttpURLConnection connection = (HttpURLConnection)url2.openConnection();
                            //默认就是Get，可以采用post，大小写都行，因为源码里都toUpperCase了。
                            connection.setRequestMethod("GET");
                            //是否允许缓存，默认true。
                            connection.setUseCaches(Boolean.FALSE);
                            //是否开启输出输入，如果是post使用true。默认是false
                            connection.addRequestProperty("Content-Type", "application/json");
                            connection.addRequestProperty("Accept", "*/*");
                            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.4(0x17000428) NetType/WIFI Language/zh_CN");
                            connection.addRequestProperty("Referer", "https://servicewechat.com/wx" + random +"/6/page-frame.html");
                            connection.addRequestProperty("Accept-Language", "zh-cn");
                            connection.addRequestProperty("Accept-Encoding", "br, gzip, deflate");
                            connection.addRequestProperty("Connection", "keep-alive");
                            connection.addRequestProperty("cityId", "310001");

                            //设置连接主机超时（单位：毫秒）
                            connection.setConnectTimeout(8000);
                            //设置从主机读取数据超时（单位：毫秒）
                            connection.setReadTimeout(8000);
                            Document doc = Jsoup.parse(connection.getInputStream(), "utf-8", url);
                            String json = doc.body().toString().replace("<body>", "").replace("</body>", "");
                            Gson gson = new Gson();
                            TrashResultEntity tr = gson.fromJson(json, new TypeToken<TrashResultEntity>() {}.getType());
                            LogUtils.e(json);
                            return tr;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        TrashResultEntity tr = new TrashResultEntity();
                        tr.Ok = false;
                        tr.Msg = "网络请求错误";
                        return tr;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }


    /**
     * 获取随机数
     * @param number
     */
    private static String getRandomStr(int number) {
        String random = "";
        for (int i = 0; i < number; i++) {
            random = random + (int) (1 + Math.random() * 10);
        }
        return random;
    }

    /**
     * 搜狗搜索
     * @param name
     * @return
     */
    public static Observable sougouSearch(String name) {

        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(name);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Function<String, TrashResultEntity>() {
                    @Override
                    public TrashResultEntity apply(String t) throws Exception {
                        Document document = null;

                        try {
                            Map<String, String> header = new HashMap<>();
                            header.put("Accept", "application/xml, text/xml");
                            header.put("Accept-Encoding", "gzip, deflate, br");
                            header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
                            header.put("Connection", "keep-alive");
                            header.put("Cookie", "IPLOC=CN5000; SUID=9D88BA3D3220910A000000005BC6A435; SUV=1539744823690531; CXID=1E2341ED8A38C811AEC788918B204355; usid=pbarjsweJsc5rrea; wuid=AAEheXm8JgAAAAqHGmoOJAAAkwA=; userGroupId=18; front_screen_resolution=1920*1080; sct=4; SNUID=1D093BBD80850C1548759615816160AD; sw_uuid=5240000512; ssuid=4247411875; ABTEST=7|1561975509|v1; JSESSIONID=aaaELI-9e4JPS6lT-u0Tw; FREQUENCY=1554876449193_9; ld=QZllllllll2t@LnHlllllV1blaUlllll5b$wXyllllyllllllklll5@@@@@@@@@@");
                            header.put("Host", "wap.sogou.com");
                            header.put("Referer", "https://wap.sogou.com/web/searchList.jsp?uID=AAEExdgvKAAAAAqKJmVZugEAZAM%3D&v=5&dp=1&pid=sogou-waps-7880d7226e872b77&w=1283&t=1561362565111&s_t=1561362573442&s_from=result_up&htprequery=%E5%9E%83%E5%9C%BE%E5%88%86%E7%B1%BB&keyword=%E5%9E%83%E5%9C%BE%E5%88%86%E7%B1%BB&pg=webSearchList&s=%E6%90%9C%E7%B4%A2&suguuid=0c0c451f-204a-4653-b3f1-a30c8451e048&sugsuv");
                            header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
                            header.put("X-Requested-With", "XMLHttpRequest");

                            String url = "https://wap.sogou.com/reventondc/transform" +
                                    "?key=垃圾分类" +
                                    "&type=2" +
                                    "&charset=utf-8" +
                                    "&objid=70197800" +
                                    "&uuid=" +
                                    "&url=";
                            String htturl = "http://devopen.sogou/" +
                                    "?queryString=" +  t + "城p词" +
                                    "&ie=utf8" +
                                    "&reqClassids=70197800" +
                                    "&queryFrom=wap" +
                                    "&vrForQc=false" +
                                    "&retType=xml" +
                                    "&subReq=1" +
                                    "&dataPlatformSource=rubbish_card";
                            htturl = URLEncoder.encode(htturl, "utf-8");

                            document = Jsoup.connect(url + htturl).headers(header).get();
                            String json = document.body().toString().replace("<body>", "").replace("</body>", "");
                            Elements elements = document.select("subdisplay");
                            List<String> list = new ArrayList<>();
                            String result = "";

                            for (Element element : elements) {
                                result = element.select("type").text();
                                LogUtils.e(result);
                            }
                            LogUtils.e(json);
                            TrashResultEntity tr = new TrashResultEntity();
                            if (StringUtils.isEmpty(result)) {
                                tr.Ok = false;
                                tr.Msg = "对不起，暂时无法查询到该垃圾的分类结果";
                            } else if (!result.contains(";")) {
                               tr.Ok = true;
                               tr.Msg = "yes";
                               TrashResultEntity.DataBean dataBean = new TrashResultEntity.DataBean();
                               List<TrashResultEntity.DataBean> dataBeanList = new ArrayList<>();
                               int kind = 1;
                               if (result.contains("可回收物")) {
                                   kind = 1;
                               } else if (result.contains("有害垃圾")) {
                                   kind = 2;
                               } else if (result.contains("易腐垃圾")) {
                                   kind = 3;
                               } else if (result.contains("其他垃圾")) {
                                   kind = 4;
                               }
                               dataBean.Kind = kind;
                               dataBean.Name = name;
                               dataBeanList.add(dataBean);
                               tr.Data = dataBeanList;
                           } else {
                               tr.Ok = false;
                               String[] res = result.split(";");
                               tr.Msg = res[1];
                           }
                            return tr;
                        }catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.e(e);
                        }
                        TrashResultEntity tr = new TrashResultEntity();
                        tr.Ok = false;
                        tr.Msg = "网络请求错误";
                        return tr;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public static Observable imageTAg(String path) {

        Observable observable = Observable.create((ObservableOnSubscribe<String>) emitter ->
                emitter.onNext(path)).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(t -> {
                    try {
                        //时间戳
                        String time_stamp = System.currentTimeMillis() / 1000 + "";
                        //图片的二进制数组数据 2张
                        Bitmap commressB = ImageUtil.compressImage2(t);
                        byte [] imageData1 = FileUtil.readFileByBytes(commressB);
                        //byte [] imageData2 = FileUtil.readFileByBytes("G:/32801.jpg");
                        //图片的base64编码数据
                        String img641 = Base64Util.encode(imageData1);
                        //String img642 = Base64Util.encode(imageData2);
                        //随机字符串
                        String nonce_str = TencentAISign.getRandomString(10);
                        //参数拼接
                        HashMap<String, String> bodys = new HashMap<String, String>();
                        bodys.put("app_id", TencentAPI.APP_ID_AI.toString());
                        bodys.put("time_stamp",time_stamp);
                        bodys.put("nonce_str", nonce_str);
                        bodys.put("image", img641);
                        //计算SIGN
                        String sign = TencentAISignSort.getSignature(bodys);
                        bodys.put("sign", sign);
                        String param = TencentAISignSort.getParams(bodys);
                        String result = HttpUtil.post("https://api.ai.qq.com/fcgi-bin/image/image_tag", param);
                        LogUtils.e(result);
                        if (!StringUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            TxLoadEntity entity = gson.fromJson(result, new TypeToken<TxLoadEntity>() {}.getType());
                            if (entity != null && entity.data != null && entity.data.tag_list != null && entity.data.tag_list.size() > 0) {
                                TxLoadEntity.DataBean.TagListBean tag = entity.data.tag_list.get(0);
                                return tag.tag_name;
                            }
                        }
                        commressB.recycle();
                        return "";

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return "";
                }).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }



    /**
     * 是否使用代理(WiFi状态下的,避免被抓包)
     */
    public static boolean isWifiProxy(Context mContxt){
        final boolean is_ics_or_later = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (is_ics_or_later) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portstr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portstr != null ? portstr : "-1"));
            System.out.println(proxyAddress + "~");
            System.out.println("port = " + proxyPort);
        }else {
            proxyAddress = android.net.Proxy.getHost(mContxt);
            proxyPort = android.net.Proxy.getPort(mContxt);
            Log.e("address = ", proxyAddress + "~");
            Log.e("port = ", proxyPort + "~");
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }
    /**
     * 是否正在使用VPN
     */
    public static boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if(niList != null) {
                for (NetworkInterface intf : (List<NetworkInterface>) Collections.list(niList)) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    Log.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void imageTest() {

        Observable observable = Observable.create((ObservableOnSubscribe<String>) emitter ->
                emitter.onNext("1")).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(t -> {
                    try {
                        JKX_API.getInstance().test("http://tools.bugcode.cn", "countries", "cn", new Observer() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Object o) {
                                LogUtils.e(o);
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtils.e(e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return "";
                }).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }


}
