package com.example.administrator.diary_room_master.base;

import android.app.Application;

import com.example.administrator.diary_room_master.data.local.DiaryDataBase;

/**
 * application的基类
 * Created by XRY on 2017/9/11.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //DiaryDataBase.getInstance(this);
    }
}
