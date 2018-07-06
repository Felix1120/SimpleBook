package com.felix.simplebook.broadcast;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;

import java.io.File;

/**
 * Created by chaofei.xue on 2018/7/5.
 */

public class UpdateBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {
        MyLog.info("UpdateBroadcastReceiver", "download successful");
        long myDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long downloadID = context.getSharedPreferences("config.sb",
                Context.MODE_PRIVATE).getLong("downloadId", 0);
        if (downloadID != myDownloadID) {
            return;
        }
        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadFileUri = dManager.getUriForDownloadedFile(myDownloadID);
        installAPK(context, downloadFileUri);
    }

    private void installAPK(Context context, Uri apk) {
        if (Build.VERSION.SDK_INT < 23) {
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intents);
        } else {
            File file = queryDownloadedApk(context);
            if (file.exists()) {
                openFile(file, context);
            }
        }
    }

    //6.0 download
    public static File queryDownloadedApk(Context context) {
        MyLog.info("queryDownloadedApk", "start");
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadID = context.getSharedPreferences("config.sb",
                Context.MODE_PRIVATE).getLong("downloadId", 0);
        if (downloadID != 0) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadID);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(
                            DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    private void openFile(File file, Context context) {
        //Intent intent = new Intent();
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ///intent.setAction("android.intent.action.VIEW");
        //String type = getMIMEType(file);
        //intent.setDataAndType(Uri.fromFile(file), type);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intent);

        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            MyToast.makeText(context, "亲，新版本下载完成，请手动安装", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMIMEType(File var0) {
        String var1;
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }
}
