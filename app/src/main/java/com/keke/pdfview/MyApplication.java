package com.keke.pdfview;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastQQStyle;

/**
 * @author : Luffy Harris
 * e-mail : 744423651@qq.com
 * phone  : 13002903389
 * date   : 2019/12/12-14:24
 * desc   : 本项目的Application
 * version: 1.0
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化ToastUtils,并设置为QQ的风格
        ToastUtils.init(this, new ToastQQStyle(this));
    }
}
