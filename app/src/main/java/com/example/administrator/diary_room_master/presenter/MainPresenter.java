package com.example.administrator.diary_room_master.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.administrator.diary_room_master.contracts.MainContract;
import com.example.administrator.diary_room_master.data.DiaryDataSource;
import com.example.administrator.diary_room_master.data.DiaryRepository;
import com.example.administrator.diary_room_master.data.bean.Diary;

import java.util.List;

/**
 * 主页面Presenter
 * Created by Administrator on 2018/1/5 0005 -17:09.
 */

public class MainPresenter implements MainContract.Presenter {
    private  Context mContext;             //上下文
    private  MainContract.View mView;      //持有View层实例
    private  DiaryRepository mRepository;  //持有Model层实例

    public MainPresenter(Context mContext, MainContract.View mView, DiaryRepository mRepository) {
        this.mContext = mContext;
        this.mView = mView;
        this.mRepository = mRepository;
        mView.setPresenter(this);
    }

    @Override
    public void onCreate() {
        loadDiaries();
    }

    @Override
    public void onDestroy() {
        mView = null;
        mContext = null;
        if (mRepository != null) {
            mRepository = null;
        }
    }

    @Override
    public void loadDiaries() {
        mView.setSwipeRefresh(true);
        mRepository.getDiaries(new DiaryDataSource.LoadDiariesCallback() {
            @Override
            public void onDiariesLoaded(List<Diary> diaries) {
                mView.showDiaries(diaries);
                mView.setSwipeRefresh(false);
            }

            @Override
            public void onDataNotAvailable() {
                mView.setSwipeRefresh(false);
                //Toast.makeText(mContext, "获取数据失败,数据库无法连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteAll(DiaryDataSource.OnCompleteCallback callback) {
        mRepository.deleteAllDiaries(callback);
    }
}
