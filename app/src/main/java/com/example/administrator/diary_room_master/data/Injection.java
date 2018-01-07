package com.example.administrator.diary_room_master.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.administrator.diary_room_master.contracts.AddDiaryContract;
import com.example.administrator.diary_room_master.contracts.MainContract;
import com.example.administrator.diary_room_master.contracts.SearchContract;
import com.example.administrator.diary_room_master.data.local.DiaryDataBase;
import com.example.administrator.diary_room_master.data.local.DiaryLocalDataSource;
import com.example.administrator.diary_room_master.presenter.AddDiaryPresenter;
import com.example.administrator.diary_room_master.presenter.MainPresenter;
import com.example.administrator.diary_room_master.presenter.SearchPresenter;
import com.rongyant.commonlib.util.AppExecutorsUtil;

/**
 * 注入类
 * Created by Administrator on 2018/1/4 0004.
 */

public class Injection {
    /**
     * 提供Diary model层的实例
     * @param context 上下文环境
     * @return  model层实例
     */
    public static DiaryRepository provideDiariesRepository(@NonNull Context context) {
        DiaryDataBase dataBase = DiaryDataBase.getInstance(context);
        return DiaryRepository.getInstance(DiaryLocalDataSource.getInstance(new AppExecutorsUtil(),
                dataBase.diariesDao()));
    }

    /**
     * 提供main页面presenter层实例
     * @param context   上下文环境
     * @param view      view层实例
     * @return  presenter层实例
     */
    public static MainPresenter provideMainPresenter(@NonNull Context context,
                                                     @NonNull MainContract.View view) {
        return new MainPresenter(context, view, provideDiariesRepository(context));
    }

    /**
     * 提供AddDiary页面presenter层实例
     * @param context   上下文环境
     * @param view      view层实例
     * @return  presenter层实例
     */
    public static AddDiaryPresenter provideAddDiaryPresenter(@NonNull Context context,
                                                             @NonNull AddDiaryContract.View view) {
        return new AddDiaryPresenter(context, view, provideDiariesRepository(context));
    }

    /**
     * 提供Search页面presenter层实例
     * @param context   上下文环境
     * @param view      view层实例
     * @return  presenter层实例
     */
    public static SearchPresenter provideSearchPresenter(@NonNull Context context,
                                                         @NonNull SearchContract.View view) {
        return new SearchPresenter(context, view, provideDiariesRepository(context));
    }


}
