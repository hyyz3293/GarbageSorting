package com.tool.rss.utils.update;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.tool.rss.BuildConfig;
import com.tool.rss.R;
import com.tool.rss.base.BaseActivity;
import com.tool.rss.ui.dialog.IosPopupDialog;

import java.io.File;
import java.lang.ref.WeakReference;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


/**
 * 下载工具类
 *
 * @author wanghao
 * @date 2018-6-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class AppUpdateUtils {
    /** 静态引用 */
    private static AppUpdateUtils sInstance;
    private BaseActivity mActivity;
    private BaseDownloadTask baseDownloadTask;

    /**
     * 私有化构造
     */
    private AppUpdateUtils() {
    }

    /**
     * 单例
     *
     * @return this
     */
    public static AppUpdateUtils newInstance() {
        if (null == sInstance) {
            synchronized (AppUpdateUtils.class) {
                if (null == sInstance) {
                    sInstance = new AppUpdateUtils();
                }
            }
        }

        return sInstance;
    }

    /**
     * 是否跟新APP
     *
     * @param newVersion 新的版本号
     * @param activity
     * @return 是|否
     */
    public boolean isCanUpdateApp(String newVersion, BaseActivity activity) {
        int nowVersion = AppUtils.getAppVersionCode();
        int newVersion1 = Integer.parseInt(newVersion);
        if (newVersion1 > nowVersion) {
            return true;
        } else {
//            activity.showToast("已经是最新版本");
            return false;
        }
    }


    /**
     * 更新APP
     */
    public void updateApp(BaseActivity activity, String newVersion, boolean isForce, String message, String url) {
        mActivity = activity;
        if (isCanUpdateApp(newVersion, activity)) {
            showDownLoadDialog(activity, isForce, message, url);
        }
    }


    /**
     * 显示更新dialog
     *
     * @param activity 页面上下文
     * @param isForce 是否强制
     */
    public void showDownLoadDialog(final BaseActivity activity, final boolean isForce, String message, final String url) {
        final WeakReference<BaseActivity> activityWeak = new WeakReference<BaseActivity>(activity);
        final IosPopupDialog dialog = new IosPopupDialog(activity);
        if (isForce){
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }else {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
        }

        dialog.setTextColor(R.id.tv_title, Color.parseColor("#333333"));
        dialog.setTextColor(R.id.tv_message, Color.parseColor("#333333"));
        dialog.setTitle("新版本更新说明")
                .setMessage(message)
                .setPositiveButton("确定", true, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downLoadApk(activityWeak, isForce, url);
                    }
                });
        if (!isForce) {
            dialog.setNegativeButton("稍后再说", true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }


    /**
     * 下载文件
     */
    private void downLoadApk(WeakReference<BaseActivity> activityWeak, boolean isForce, String url) {
        final BaseActivity activity = activityWeak.get();
        final IosPopupDialog downDialog = new IosPopupDialog(activity);
        if (isForce){
            downDialog.setCanceledOnTouchOutside(false);
            downDialog.setCancelable(false);
        }else {
            downDialog.setCanceledOnTouchOutside(false);
            downDialog.setCancelable(true);
            downDialog.setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downDialog.dismiss();
                    baseDownloadTask.pause();
                }
            });
        }
        downDialog.setCenterView(R.layout.item_update_progress_dialog);

        final MaterialProgressBar bar = (MaterialProgressBar) downDialog.findViewById(R.id.mpb_update_app_progress);
        final TextView progress = (TextView) downDialog.findViewById(R.id.tv_update_app_progress);
        baseDownloadTask = FileDownloader.getImpl()
                .create(url)
                .setPath(FileDownloadUtils.getDefaultSaveRootPath() + "/" + "download")
                .setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        LogUtils.d("BaseDownloadTask:pending");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int percent = (int) ((double) soFarBytes / (double) totalBytes * 100);
                        LogUtils.d("BaseDownloadTask:"+percent);
                        bar.setProgress(percent);
                        progress.setText(percent + "%");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        LogUtils.d("BaseDownloadTask:completed");
                        downDialog.dismiss();
                        //判读版本是否在7.0以上
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            File file = FileUtils.getFileByPath(task.getPath());
                            Uri apkUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                            Intent install = new Intent(Intent.ACTION_VIEW);
                            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            mActivity.startActivity(install);
                        } else {
                            AppUtils.installApp(task.getPath());
                        }

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        downDialog.dismiss();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        downDialog.dismiss();
                    }
                });
        baseDownloadTask.start();

        downDialog.show();

    }


}
