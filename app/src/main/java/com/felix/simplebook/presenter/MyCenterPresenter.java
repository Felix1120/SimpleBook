package com.felix.simplebook.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.felix.simplebook.R;
import com.felix.simplebook.callback.ICallBack;
import com.felix.simplebook.model.IMyCenterModel;
import com.felix.simplebook.model.MyCenterModel;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.view.IMyCenterView;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.felix.simplebook.view.IMyCenterView.REQUEST_CODE_CHOOSE;

/**
 * Created by Felix on 2018/6/5.
 */

public class MyCenterPresenter implements IMyCenterPresenter {

    private IMyCenterView centerView;
    private IMyCenterModel centerModel;
    private Context context;
    private SharedPreferences preferences;

    public MyCenterPresenter(IMyCenterView centerView, Context context){
        this.centerView = centerView;
        this.context = context;
        centerModel = new MyCenterModel();
        preferences = context.getSharedPreferences("config.sb",
                Context.MODE_PRIVATE);
    }

    @Override
    public boolean isLogin() {
        String userName = preferences.getString("isLogin", "noLogin");
        if(userName.equals("noLogin")){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean exitLogin() {
        //清除登录信息
        boolean commit = preferences.edit()
                .putString("username", "未登录")
                .putString("email", "登录后才能使用网络备份")
                .putString("photos", "")
                .putString("isLogin", "noLogin")
                .commit();
        return commit;
    }

    @Override
    public void selectImg(Activity activity) {
        Matisse.from(activity)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.felix.simplebook.fileprovider"))
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .theme(R.style.My_Style)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void activityResult(int requestCode, int resultCode, Intent data, ImageView imageView) {
        MyLog.info("requestCode", requestCode + "   resultCode" + resultCode);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> lists = Matisse.obtainResult(data);
            MyLog.info(lists.get(0).toString());
            String username = preferences.getString("username", "error");
            Uri saveUri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/"+username+".jpg");
            MyLog.info(saveUri.toString());
            UCrop.of(lists.get(0), saveUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(imageView.getMaxWidth(), imageView.getMaxHeight())
                    .start((Activity) context);

        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            MyLog.info("resultUri", resultUri.toString());
            Glide.with(context)
                    .load(resultUri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
            //start update
            updateImg(resultUri.getPath());
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            MyLog.info(cropError.getMessage());
        }
    }

    @Override
    public void downloadImg(ImageView imageView, String imgPath) {
        Glide.with(context)
                .load("http://120.78.138.94:8080/server/Restore?username=" + imgPath)
                .skipMemoryCache(false)
                .into(imageView);
    }

    private void updateImg(String imagePath) {
        centerModel.updateImg(new ICallBack<String>() {
            @Override
            public void successful(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String result = object.getString("result");
                    MyLog.info("result", result);
                } catch (Exception e) {

                }
            }

            @Override
            public void error(String value) {

            }
        }, imagePath);
    }
}
