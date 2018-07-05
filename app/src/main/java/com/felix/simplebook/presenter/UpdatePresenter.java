package com.felix.simplebook.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.felix.simplebook.model.IUpdateModel;
import com.felix.simplebook.model.UpdateModel;
import com.felix.simplebook.view.IUpdateView;

import java.io.File;

/**
 * Created by chaofei.xue on 2018/7/5.
 */

public class UpdatePresenter implements IUpdatePresenter {
    private IUpdateModel updateModel;
    private IUpdateView updateView;
    private Context context;

    public UpdatePresenter(IUpdateView updateView, Context context) {
        this.updateView = updateView;
        updateModel = new UpdateModel();
        this.context = context;
    }

    @Override
    public void localDownload(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        DownloadManager dm = (DownloadManager) context.getSystemService(
                Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(path);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request
                .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        File file = new File(Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/SimpleBook/", "simplebook.apk");
        if (file.exists()) {
            file.delete();
        }
        request.setDestinationInExternalPublicDir(Environment.
                getExternalStorageDirectory().getAbsolutePath() + "/SimpleBook/", "simplebook.apk");
        long downloadId = dm.enqueue(request);

        context.getSharedPreferences("config.sb",
                Context.MODE_PRIVATE).edit().putLong("downloadId", downloadId)
                .apply();
    }
}
