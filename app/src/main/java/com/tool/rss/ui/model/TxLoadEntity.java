package com.tool.rss.ui.model;

import java.util.List;

public class TxLoadEntity {
    /**
     * ret : 0
     * msg : ok
     * data : {"tag_list":[{"tag_name":"小孩","tag_confidence":25},{"tag_name":"手脚","tag_confidence":18}]}
     */
    public int ret;
    public String msg;
    public DataBean data;
    public static class DataBean {
        public List<TagListBean> tag_list;
        public static class TagListBean {
            /*** tag_name : 小孩* tag_confidence : 25*/
            public String tag_name;
            public int tag_confidence;
        }
    }
}
