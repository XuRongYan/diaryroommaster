package com.example.administrator.diary_room_master.base;

/**
 * 权限申请结果监听
 * Created by XRY on 2017/1/30.
 */

public interface PermissionListener {
    /**
     * 权限被允许时的回调
     */
    void onGranted();

    /**
     * 权限被拒绝时的回调
     * @param deniedPermissions 被拒绝的权限
     */
    void onDenied(String[] deniedPermissions);
}
