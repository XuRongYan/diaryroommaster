package com.rongyant.commonlib.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 全局线程执行器，用于整个应用
 * Created by Administrator on 2018/1/4 0004.
 */

public class AppExecutorsUtil {
    private static final int ThREAD_COUNT = 3;

    private final Executor diskIO;      //新的后台线程

    private final Executor networkIO;   //网络专用线程

    private final Executor mainThread;  //主线程

    @VisibleForTesting
    AppExecutorsUtil(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public AppExecutorsUtil() {
        this(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(ThREAD_COUNT),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    /**
     * 主线程执行器
     */
    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());


        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }


}
