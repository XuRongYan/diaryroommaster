package com.example.administrator.diary_room_master.base;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Activity的堆栈管理类
 * Created by XRY on 2017/9/11.
 */

public class BaseAppManager {
    private static final String TAG = "BaseAppManager";

    private static BaseAppManager INSTANCE = null; //单例模式

    private static List<Activity> mActivities = new LinkedList<>(); //activity栈

    private BaseAppManager() {
    }

    /**
     * 获取该类实例（单例模式）
     * @return
     */
    public static BaseAppManager getInstance() {
        if (INSTANCE == null) {
            synchronized (BaseAppManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaseAppManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取当前栈大小
     * @return
     */
    public int size() {
        return mActivities.size();
    }

    /**
     * 获取栈顶的Activity
     * @return
     */
    public synchronized Activity getTopActivity() {
        return size() > 0 ? mActivities.get(size() - 1) : null;
    }

    /**
     * 添加activity到栈顶
     * @param activity
     */
    public synchronized void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    /**
     * 移除指定的activity
     * @param activity
     */
    public synchronized void removeActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    /**
     * 清空堆栈
     */
    public synchronized void clearAll() {
        for (int i = mActivities.size() - 1;i > -1;i--) {
            Activity activity = mActivities.get(i);
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 将栈顶的activity去掉只留最后一个activity
     */
    public synchronized void clearTop() {
        for (int i = mActivities.size() - 2; i > -1; i--) {
            Activity aty = mActivities.get(i);
            removeActivity(aty);
            aty.finish();
            i = mActivities.size() - 1;
        }
    }






}
