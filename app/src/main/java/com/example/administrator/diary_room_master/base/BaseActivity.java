package com.example.administrator.diary_room_master.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


import com.example.administrator.diary_room_master.R;
import com.rongyant.commonlib.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * Activity基类
 * Created by XRY on 2017/9/11.
 */

public abstract class BaseActivity extends AppCompatActivity{
    private static String TAG = "BaseActivity";
    protected BasePresenter mPresenter; //持有presenter层实例
    private static BaseAppManager mBaseAppManager = BaseAppManager.getInstance(); //管理activity堆栈
    private static PermissionListener mListener; //6.0后权限监听接口
    protected Context mContext;
    private View mToolbar;
    private long exitTime = 0; //记录两次返回键时间间隔的变量
    private boolean doubleBackExit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();

        mContext = this;
        mBaseAppManager.addActivity(this);
        //获取contentView
        if (getContentView() != 0) {
            View contentView = getLayoutInflater().inflate(getContentView(), null);
            setContentView(contentView);
            ButterKnife.bind(this);
        }
//        //5.0以上的沉浸式状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        initPresenter();
        initViews(savedInstanceState);
//        //TODO 优化android4.4时沉浸式状态栏效果，但是没有效果
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && mToolbar != null) {
//            mStatusBarStub = getLayoutInflater().inflate(R.layout.act_main, null);
//            mStatusBarOverlay = mStatusBarStub.findViewById(R.id.main_status_bar_stub);
//            mStatusBarOverlay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    mStatusBarOverlay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    ViewGroup.LayoutParams layoutParams = mStatusBarOverlay.getLayoutParams();
//                    layoutParams.height = mToolbar.getPaddingTop();
//                }
//            });
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化presenter
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseAppManager.removeActivity(this);
        //当Activity销毁时销毁presenter实例，防止内存泄漏
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    /**
     * 在子类中实现此方法，返回layout
     * @return
     */
    protected abstract int getContentView();

    /**
     * 在子类中实现此方法，初始化控件
     * @param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 在子类中实现此方法，实例化presenter
     */
    protected abstract void initPresenter();

    /**
     * 设置toolbar
     * @param toolbar
     */
    protected void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        setSupportActionBar(toolbar);
    }

    /**
     * 设置该activity是否可以双击返回键退出程序
     * @param doubleBackExit 默认为false
     */
    protected void setDoubleBackExit(boolean doubleBackExit) {
        this.doubleBackExit = doubleBackExit;
    }

    /**
     * 设置toolbar是否自带返回按钮
     * @param bool true为有，false为无
     */
    protected void setDisplayHomeAsUpEnabled(boolean bool) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(bool);
        }

    }

    /**
     * activity跳转
     * @param cls 目标activity
     */
    public void goActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * activity跳转
     * @param cls 目标activity
     * @param bundle 传到目标activity的包
     */
    public void goActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * activity跳转
     * @param cls 目标activity
     * @param requestCode 请求码
     */
    @SuppressLint("RestrictedApi")
    public void goActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(intent, requestCode,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    /**
     * activity跳转
     * @param cls 目标activity
     * @param bundle 传到目标activity的包
     * @param requestCode 请求码
     */
    @SuppressLint("RestrictedApi")
    public void goActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(intent, requestCode,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivityForResult(intent, requestCode, bundle);
        }
    }

    /**
     * 单个共享元素跳转
     * @param cls
     * @param transitionName
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void goActivityShareElements(Class<?> cls, String transitionName, View view) {
        startActivity(new Intent(mContext, cls),
                ActivityOptions.makeSceneTransitionAnimation(this, view, transitionName).toBundle());
    }

    /**
     * 运行时权限处理
     */
    public static void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mBaseAppManager.getTopActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(mBaseAppManager.getTopActivity(),
                    permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            //实现权限接口
            mListener.onGranted();
        }
    }

    /**
     * 权限申请的回调
     * @param requestCode 请求码
     * @param permissions 请求的权限集合
     * @param grantResults 请求权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (permissions.length > 0) {
                    List<String> deniedPermission = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermission.add(permission);
                        }
                    }
                    if (deniedPermission.isEmpty()) {
                        mListener.onGranted();
                    } else {
                        mListener.onDenied(
                                deniedPermission.toArray(new String[deniedPermission.size()]));
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 返回键点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 按两次BACK键退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && doubleBackExit) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.showShort(this, getString(R.string.text_one_more_click));
                exitTime = System.currentTimeMillis();
            } else {
                BaseAppManager.getInstance().clearAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
