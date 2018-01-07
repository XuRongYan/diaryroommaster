package com.example.administrator.diary_room_master.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * fragment基类
 * Created by XRY on 2017/9/11.
 */

public abstract class BaseFragment extends Fragment {
    private static String TAG = "BaseFragment";
    protected Context mContext;
    private View contentView; //View
    protected BasePresenter mPresenter; //presenter层实例
    private Unbinder unbinder; //用于解绑butterknife

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewID() != 0) {
            contentView = inflater.inflate(getContentViewID(), null);
            unbinder = ButterKnife.bind(this, contentView);
            initViews(contentView);
            if (mPresenter != null) {
                mPresenter.onCreate();
            }
            return contentView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        unbinder.unbind();
    }

    /**
     * 子类实现此方法初始化控件
     * @param rootView
     */
    protected abstract void initViews(View rootView);

    /**
     * 子类实现此方法返回layoutId
     * @return
     */
    protected abstract int getContentViewID();

    /*-------------------------------------activity跳转方法---------------------------------------*/

    public void goActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    public void goActivity(Class<?> cls, Bundle... bundles) {
        Intent intent = new Intent(getActivity(), cls);
        for (Bundle bundle : bundles) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void goActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
        startActivityForResult(intent, requestCode);
    }
}
