package com.daily.news;

import android.app.Application;

import com.zjrb.core.utils.UIUtils;

/**
 * Created by gaoyangzhen on 2018/4/26.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        UIUtils.init(this);
    }
}
