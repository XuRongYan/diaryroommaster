package com.example.administrator.diary_room_master.data;

import android.support.annotation.NonNull;

import com.example.administrator.diary_room_master.data.bean.Diary;

import java.util.List;

/**
 * 访问日记数据的主要入口
 * Created by Administrator on 2018/1/4.
 */

public interface DiaryDataSource {
    /**
     * 加载多个数据时的回调接口
     */
    interface LoadDiariesCallback {
        void onDiariesLoaded(List<Diary> diaries);

        void onDataNotAvailable();
    }

    /**
     * 加载单条数据时的回调接口
     */
    interface GetDiaryCallback {
        void onDiaryLoaded(Diary diary);

        void onDataNotAvailable();
    }

    /**
     * 事务完成回调接口
     */
    interface OnCompleteCallback {
        void onComplete();
    }

    /**
     * 获取所有日记
     * @param callback 加载多个数据时的回调接口
     */
    void getDiaries(@NonNull LoadDiariesCallback callback);

    /**
     * 根据id获取单条日记
     * @param diaryId   日记id
     * @param callback  加载单条数据时的回调接口
     */
    void getDiary(String diaryId, @NonNull GetDiaryCallback callback);

    /**
     * 将一条日记存入数据库
     * @param diary 想要保存的日记
     */
    void saveDiary(@NonNull Diary diary, @NonNull OnCompleteCallback callback);

    /**
     * 刷新日记列表
     */
    void refreshDiaries();

    /**
     * 删除所有日记
     */
    void deleteAllDiaries(@NonNull OnCompleteCallback callback);

    /**
     * 根据id删除日记
     * @param id
     */
    void deleteDiaryById(String id, @NonNull OnCompleteCallback callback);

    /**
     * 更新日记
     * @param diary     即将更新的日记
     * @param callback  完成时回调
     */
    void updateDiary(Diary diary, @NonNull OnCompleteCallback callback);

    /**
     * 根据关键词查找日记
     * @param key       关键词
     * @param callback  回调
     */
    void search(String key, @NonNull LoadDiariesCallback callback);

}
