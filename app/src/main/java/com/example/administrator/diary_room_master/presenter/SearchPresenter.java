package com.example.administrator.diary_room_master.presenter;

import android.content.Context;

import com.example.administrator.diary_room_master.contracts.SearchContract;
import com.example.administrator.diary_room_master.data.DiaryDataSource;
import com.example.administrator.diary_room_master.data.DiaryRepository;
import com.example.administrator.diary_room_master.data.bean.Diary;

import java.util.List;

/**
 * 搜索的Presenter层
 * Created by Administrator on 2018/1/5 0005 -20:57.
 */

public class SearchPresenter implements SearchContract.Presenter {
    private Context mContext;               //上下文
    private SearchContract.View mView;      //view层实例
    private DiaryRepository mRepository;   //model层实例

    public SearchPresenter(Context mContext, SearchContract.View mView, DiaryRepository mRepository) {
        this.mContext = mContext;
        this.mView = mView;
        this.mRepository = mRepository;
        mView.setPresenter(this);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        mContext = null;
        mView = null;
        if (mRepository != null) {
            mRepository = null;
        }
    }

    @Override
    public void search(String key) {
        mRepository.search(key, new DiaryDataSource.LoadDiariesCallback() {
            @Override
            public void onDiariesLoaded(List<Diary> diaries) {
                mView.showResult(diaries);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
}
