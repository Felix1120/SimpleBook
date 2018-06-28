package com.felix.simplebook.presenter;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

/**
 * Created by Felix on 2018/6/5.
 */

public interface IMyCenterPresenter {
    boolean isLogin();
    boolean exitLogin();
    void selectImg(Activity activity);
    void activityResult(int requestCode, int resultCode, Intent data, ImageView imageView);
    void downloadImg(ImageView imageView, String imgPath);
}
