package com.example.administrator.diary_room_master.presenter;

import android.content.Context;

import com.example.administrator.diary_room_master.R;
import com.example.administrator.diary_room_master.contracts.AddDiaryContract;
import com.example.administrator.diary_room_master.data.DiaryDataSource;
import com.example.administrator.diary_room_master.data.DiaryRepository;
import com.example.administrator.diary_room_master.data.Injection;
import com.example.administrator.diary_room_master.data.bean.Diary;

/**
 * 添加日记的Presenter层
 * Created by Administrator on 2018/1/5 0005 -17:38.
 */

public class AddDiaryPresenter implements AddDiaryContract.Presenter {
    private Context mContext;                //上下文
    private AddDiaryContract.View mView;    //持有View层实例
    private DiaryRepository mRepository;    //持有Model层实例

    public AddDiaryPresenter(Context mContext, AddDiaryContract.View mView, DiaryRepository mRepository) {
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
    public void addDiary() {
        attemptAddDiary();
    }

    @Override
    public void changeDiary(Diary diary) {
        mRepository.updateDiary(diary, new DiaryDataSource.OnCompleteCallback() {
            @Override
            public void onComplete() {
                mView.onComplete();
            }
        });
    }

    private void attemptAddDiary() {
        boolean isCancel = false;
        if (!mView.isTitleValid()) {
            isCancel = mView.setTitleError(mContext.getString(R.string.error_title_not_null));
        }
        if (!isCancel) {
            Diary diary = mView.getDiary();
            mRepository.saveDiary(diary, new DiaryDataSource.OnCompleteCallback() {
                @Override
                public void onComplete() {
                    mView.onComplete();
                }
            });
        } else {
            mView.onComplete();
        }
    }
}

