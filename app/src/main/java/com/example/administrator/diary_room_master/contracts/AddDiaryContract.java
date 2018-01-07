package com.example.administrator.diary_room_master.contracts;

import com.example.administrator.diary_room_master.base.BasePresenter;
import com.example.administrator.diary_room_master.base.BaseView;
import com.example.administrator.diary_room_master.data.bean.Diary;

/**
 * 添加日记契约类
 * Created by Administrator on 2018/1/5 0005 -17:32.
 */

public class AddDiaryContract {
    public interface Presenter extends BasePresenter {
        void addDiary();

        void changeDiary(Diary diary);
    }

    public interface View extends BaseView<Presenter> {
        boolean isTitleValid();

        boolean setTitleError(String error);

        Diary getDiary();

        void onComplete();
    }
}
