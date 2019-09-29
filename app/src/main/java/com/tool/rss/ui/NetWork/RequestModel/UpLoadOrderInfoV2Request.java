package com.tool.rss.ui.NetWork.RequestModel;

import java.io.File;

/**
 * 页面
 *
 * @author jack
 * @date 2018/7/7
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class UpLoadOrderInfoV2Request {
    /** 请求信息 */
    public BodyBean body;

    public static class BodyBean {
        public String orderId;
        public String describe;
        public File file;
        public String fileRuleId;
    }
}
