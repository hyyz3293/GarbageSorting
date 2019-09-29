package com.tool.rss.ui.model;

import java.util.List;

public class DetailAllEntity {
    public String title;
    public int colorId;
    public int drawableId;
    public String content;
    public List<DataBean> list;
    public List<ContentBean> contents;
    public static class DataBean {
        public String name;
    }
    public static class ContentBean {
        public String name;
    }
}
