package com.tool.rss.constant;

public class UserConstant {
    public static final String BAIDU_START = "{\"accept-audio-data\":false,\"disable-punctuation\":false,\"accept-audio-volume\":true,\"pid\":1736}";

    //其他垃圾
    public String[] DryGarbage = new String[] {
            "餐盒", "餐巾纸", "湿纸巾", "卫生间用纸", "塑料袋",
            "食品包装袋", "污染严重的纸", "烟蒂", "纸尿裤", "一次性杯子",
            "大骨头", "贝壳", "花盆"
    };
    //易腐垃圾
    public String[] WetGarbage = new String[] {
            "食材废料", "剩饭剩菜", "过期食品", "蔬菜水果", "瓜皮果核",
            "花卉绿植", "中药残渣"
    };
    //可回收垃圾
    public String[] RecyclableGarbage = new String[] {
            "报纸", "纸箱", "书本", "广告单", "塑料瓶",
            "塑料玩具", "油桶", "酒瓶", "玻璃杯", "易拉罐",
            "旧铁锅", "旧衣服", "包",  "旧玩偶", "旧数码产品",
            "旧家电"
    };
    //有害垃圾
    public String[] HazardousWaste = new String[] {
            "废电池（充电电池、铅酸电池、镍镉电池、纽扣电池等）", "废油漆", "消毒剂", "荧光灯管", "含汞温度计",
            "废药品及其包装物"
    };
}
