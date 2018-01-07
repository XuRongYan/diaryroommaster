package com.example.administrator.diary_room_master.contracts;

import com.example.administrator.diary_room_master.base.BasePresenter;
import com.example.administrator.diary_room_master.base.BaseView;
import com.example.administrator.diary_room_master.data.DiaryDataSource;
import com.example.administrator.diary_room_master.data.bean.Diary;

import java.util.List;

/**
 * 主页面契约类
 * Created by Administrator on 2018/1/5 0005 -16:47.
 */

public class MainContract {
    public interface Presenter extends BasePresenter {
        void loadDiaries();

        void deleteAll(DiaryDataSource.OnCompleteCallback callback);
    }

    public interface View extends BaseView<Presenter> {
        void showDiaries(List<Diary> diaries);

        void addDiaries(List<Diary> diaries);

        void addDiary(Diary diary);

        void setSwipeRefresh(boolean isRefresh);
    }
}
