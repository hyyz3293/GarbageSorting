package com.tool.rss.ui.model;

import com.blankj.utilcode.util.StringUtils;

import java.util.List;

public class TrashResultEntity {
    /**
     * Ok : true
     * Msg : null
     * Data : [{"Name":"鱼骨","Kind":3},{"Name":"鱼骨头","Kind":3},{"Name":"鱼","Kind":3},{"Name":"鱿鱼","Kind":3}]
     * Id : 0
     */
    public boolean Ok;
    public Object Msg;
    public int Id;
    public List<DataBean> Data;

    public static class DataBean {
        /**
         * Name : 鱼骨
         * Kind : 3
         */
        public String Name;
        public int Kind;
        public String msg;
    }
}
