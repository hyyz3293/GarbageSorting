package com.tool.rss.utils;

import android.content.Context;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tool.rss.R;
import com.tool.rss.base.BaseActivity;
import com.tool.rss.ui.NetWork.JKX_API;
import com.tool.rss.ui.activity.AppInfoActivity;
import com.tool.rss.ui.model.DetailAllEntity;
import com.tool.rss.ui.model.HotSearchEntity;
import com.tool.rss.ui.model.ItemEntity;
import com.tool.rss.ui.model.VersionEntity;
import com.tool.rss.utils.update.AppUpdateUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomeDataUtils {
    public static String[] tips = new String[] {"香蕉皮", "剩菜剩饭", "纸巾", "包装纸"};
    public static int[] tipsType = new int[] {3, 3, 4, 4};
    public static List<ItemEntity> getTipsList() {
        List<ItemEntity> itemList = new ArrayList<>();
        for (int i = 0; i < tips.length; i++) {
            ItemEntity entity = new ItemEntity();
            entity.title = tips[i];
            entity.type = tipsType[i];
            itemList.add(entity);
        }
        return itemList;
    }

    /** 数据转换 */
    public static List<ItemEntity> getTipsList(Object o) {
        List<ItemEntity> itemList = new ArrayList<>();
        try {
            HotSearchEntity entity = (HotSearchEntity) o;
            if (entity.data != null && entity.data.size() > 0) {
                for (HotSearchEntity.DataBean d: entity.data) {
                    ItemEntity t = new ItemEntity();
                    t.type = d.id;
                    t.title = d.name;
                    itemList.add(t);
                }
            } else {
                itemList = getTipsList();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }

    public static String[] types = new String[] {"其他垃圾", "易腐垃圾", "可回收垃圾", "有害垃圾"};
    public static int[] typesImgs = new int[] {R.mipmap.icon_g, R.mipmap.icon_s, R.mipmap.icon_k, R.mipmap.icon_y};
    public static int[] typeSt = new int[] {4, 3, 1, 2};
    public static List<ItemEntity> getTypeList() {
        List<ItemEntity> itemList = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            ItemEntity entity = new ItemEntity();
            entity.img = typesImgs[i];
            entity.title = types[i];
            entity.type = typeSt[i];
            itemList.add(entity);
        }
        return itemList;
    }


    private static String[] contentX = new String[] {
            "除去可回收物、有害垃圾、易腐垃圾之外的所有垃圾的总称，可投入灰色垃圾桶" ,
            "含有毒有害化学物质的垃圾，应投入红色垃圾桶，分选后回收利用或安全填埋",
            "容易腐烂的食物类垃圾及果皮等，投入绿色垃圾桶，可生态掩埋处理，用于沼气发电",
            "再生利用价值较高，能够入废品回收渠道的垃圾，可投入蓝色垃圾桶"};
    private static String[] itemGs = new String[] {
            "餐盒", "餐巾纸", "湿纸巾", "卫生间用纸", "塑料袋",
            "食品包装袋", "污染严重的纸", "烟蒂", "纸尿裤", "一次性杯子", "大骨头", "贝壳", "花盆等"};
    private static String[] itemHarmf = new String[] {
            "废电池", "废油漆", "消毒剂", "荧光灯管", "含汞温度计", "废药品及其包装物"};
    private static String[] itemWet = new String[] {
            "丢弃不用的菜叶", "剩菜", "剩饭", "果皮", "蛋壳", "茶渣", "碎骨头"};
    private static String[] itemCan = new String[] {
            "报纸", "纸箱", "书本", "广告单", "塑料瓶", "塑料玩具", "油桶", "酒瓶", "玻璃杯", "易拉罐", "旧铁锅", "旧衣服", "包", "旧玩偶", "旧数码产品", "旧家电"};

    private static String[] itemCanType = new String[] {
            "·轻投轻放" ,
            "·清洁干燥、避免污染，废纸尽量平整",
            "·立体包装请清空内容物，清洁后压扁投放" ,
            "·有尖锐边角的，应包裹后投放" };
    private static String[] itemHarmfType = new String[] {
            "·投放时请注意轻放" ,
            "·易破损的请连带包装或包裹后轻放",
            "·如易挥发，请密封后投放" };
    private static String[] itemSType = new String[] {
            "·纯流质的食物垃圾，如牛奶等，应直接倒进下水口" ,
            "·有包装物的易腐垃圾应将包装物去除后分类投放，包装物请投放到对应的可回收物或其他垃圾容器"};
    private static String[] itemOtherType = new String[] {
            "·尽量沥干水分" ,
            "·难以辨识类别的生活垃圾投入其他垃圾容器内"};


    /** 返回主题颜色 */
    public static DetailAllEntity GetDetailAllData(int type) {
        DetailAllEntity entity = new DetailAllEntity();
        List<DetailAllEntity.DataBean> list = new ArrayList<>();
        List<DetailAllEntity.ContentBean> cList = new ArrayList<>();
        String garbageName = "";
        int gravageColor = 0;
        int drawableId = R.mipmap.icon_detail_g;
        String content = "";
        switch (type) {
            case 1:
                garbageName = "可回收物";
                gravageColor = R.color.tarch_k;
                drawableId = R.mipmap.icon_detail_k;
                content = contentX[3];
                for (String k: itemCan) {
                    DetailAllEntity.DataBean dataBean = new DetailAllEntity.DataBean();
                    dataBean.name = k;
                    list.add(dataBean);
                }
                for (String s: itemCanType) {
                    DetailAllEntity.ContentBean dataBean = new DetailAllEntity.ContentBean();
                    dataBean.name = s;
                    cList.add(dataBean);
                }
                break;
            case 2:
                garbageName = "有害垃圾";
                gravageColor = R.color.tarch_y;
                drawableId = R.mipmap.icon_detail_y;
                content = contentX[1];
                for (String k: itemHarmf) {
                    DetailAllEntity.DataBean dataBean = new DetailAllEntity.DataBean();
                    dataBean.name = k;
                    list.add(dataBean);
                }
                for (String s: itemHarmfType) {
                    DetailAllEntity.ContentBean dataBean = new DetailAllEntity.ContentBean();
                    dataBean.name = s;
                    cList.add(dataBean);
                }
                break;
            case 3:
                garbageName = "易腐垃圾";
                gravageColor = R.color.tarch_s;
                drawableId = R.mipmap.icon_detail_s;
                content = contentX[2];
                for (String k: itemWet) {
                    DetailAllEntity.DataBean dataBean = new DetailAllEntity.DataBean();
                    dataBean.name = k;
                    list.add(dataBean);
                }
                for (String s: itemSType) {
                    DetailAllEntity.ContentBean dataBean = new DetailAllEntity.ContentBean();
                    dataBean.name = s;
                    cList.add(dataBean);
                }
                break;
            case 4:
                garbageName = "其他垃圾";
                gravageColor = R.color.tarch_g;
                drawableId = R.mipmap.icon_detail_g;
                content = contentX[0];
                for (String k: itemGs) {
                    DetailAllEntity.DataBean dataBean = new DetailAllEntity.DataBean();
                    dataBean.name = k;
                    list.add(dataBean);
                }
                for (String s: itemOtherType) {
                    DetailAllEntity.ContentBean dataBean = new DetailAllEntity.ContentBean();
                    dataBean.name = s;
                    cList.add(dataBean);
                }
                break;
            case 1008:
                garbageName = "不属于日常生活垃圾";
                gravageColor = R.color.tarch_null;
                drawableId = R.mipmap.icon_detail_g;
                break;
            default:
                garbageName = "不属于日常生活垃圾";
                gravageColor = R.color.tarch_null;
                drawableId = R.mipmap.icon_detail_g;
                break;
        }
        entity.colorId = gravageColor;
        entity.drawableId = drawableId;
        entity.title = garbageName;
        entity.content = content;
        entity.list = list;
        entity.contents = cList;
        return entity;
    }

    /**
     *  检测更新
     * @param context
     */
    public static void checkUpdateVersion(final BaseActivity context) {
        JKX_API.getInstance().getAppVersion("", new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Object o) {
                try {
                    VersionEntity versionEntity = (VersionEntity) o;
                    String newVersion = versionEntity.data.version + "";
                    boolean isShowUpdate = AppUpdateUtils.newInstance().isCanUpdateApp(newVersion, context);
                    SPUtils.getInstance().put("is_update", isShowUpdate);

                    if (isShowUpdate)
                        AppUpdateUtils.newInstance().updateApp(context, versionEntity.data.version + "", versionEntity.data.isForce
                                , versionEntity.data.message, versionEntity.data.url);
//                    else
//                        context.showToast("你已经是最新版本");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable e) {
                LogUtils.e(e);
                SPUtils.getInstance().put("is_update", false);
            }
            @Override
            public void onComplete() {
            }
        });
    }

}
