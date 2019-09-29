package com.tool.rss.ui.model;

public class VersionEntity {

    /**
     * data : {"isForce":false,"version":1,"message":"就是想更新了","url":"https://www.pgyer.com/apiv2/app/install?appKey=10d23555bf124ac18341b93f3cd797ad&_api_key=140e4c2cf3bac3595b7d980c802a24b0"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * isForce : false
         * version : 1
         * message : 就是想更新了
         * url : https://www.pgyer.com/apiv2/app/install?appKey=10d23555bf124ac18341b93f3cd797ad&_api_key=140e4c2cf3bac3595b7d980c802a24b0
         */

        public boolean isForce;
        public int version;
        public String message;
        public String url;
    }
}
