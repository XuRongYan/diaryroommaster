package com.example.administrator.diary_room_master.contracts;

import com.example.administrator.diary_room_master.base.BasePresenter;
import com.example.administrator.diary_room_master.base.BaseView;
import com.example.administrator.diary_room_master.data.bean.Diary;

import java.util.List;

/**
 * 搜索的契约类
 * Created by Administrator on 2018/1/5 0005 -20:51.
 */

public class SearchContract {
    public interface Presenter extends BasePresenter {
        void search(String key);
    }

    public interface View extends BaseView<Presenter> {
        void showResult(List<Diary> diaries);
    }
}
