package com.tool.rss.base;

import java.util.List;

/**
 * @author jack
 * @description: 权限申请回调的接口
 */
public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
