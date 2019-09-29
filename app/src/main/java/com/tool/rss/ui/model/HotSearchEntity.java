package com.tool.rss.ui.model;

import java.util.List;

public class HotSearchEntity {

    /**
     * success : true
     * data : [{"id":15,"name":"测试1"},{"id":1,"name":"测试2"}]
     */

    public boolean success;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 15
         * name : 测试1
         */
        public int id;
        public String name;
    }
}
