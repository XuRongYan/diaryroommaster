package com.example.administrator.diary_room_master.data;

import android.support.annotation.NonNull;

import com.example.administrator.diary_room_master.data.bean.Diary;
import com.rongyant.commonlib.util.LogUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 对上层暴露的日记数据接口
 * Created by Administrator on 2018/1/4 0004.
 */

public class DiaryRepository implements DiaryDataSource {

    private static final String TAG = "DiaryRepository";

    private static DiaryRepository INSTANCE = null;

    private final DiaryDataSource mDiaryLocalDataSource;

    Map<String, Diary> mCachedDiaries;

    //单例模式
    private DiaryRepository(@NonNull DiaryDataSource mDiaryLocalDataSource) {
        this.mDiaryLocalDataSource = mDiaryLocalDataSource;
    }

    public static DiaryRepository getInstance(DiaryDataSource diaryLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (DiaryRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DiaryRepository(diaryLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getDiaries(@NonNull final LoadDiariesCallback callback) {
        if (mCachedDiaries != null) {
            callback.onDiariesLoaded(new ArrayList<>(mCachedDiaries.values()));
            return;
        }
        mDiaryLocalDataSource.getDiaries(new LoadDiariesCallback() {
            @Override
            public void onDiariesLoaded(List<Diary> diaries) {
                refreshCache(diaries);
                callback.onDiariesLoaded(new ArrayList<>(mCachedDiaries.values()));
            }

            @Override
            public void onDataNotAvailable() {
                LogUtils.e(TAG, "getDiaries", "fail to get data from database");
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getDiary(final String diaryId, @NonNull final GetDiaryCallback callback) {
        final Diary cachedDiary = getDiaryById(diaryId);
        if (cachedDiary != null) {
            callback.onDiaryLoaded(cachedDiary);
            return;
        }

        mDiaryLocalDataSource.getDiary(diaryId, new GetDiaryCallback() {
            @Override
            public void onDiaryLoaded(Diary diary) {
                if (mCachedDiaries == null) {
                    mCachedDiaries = new LinkedHashMap<>();
                }
                mCachedDiaries.put(diary.getId(), diary);
                callback.onDiaryLoaded(diary);
            }

            @Override
            public void onDataNotAvailable() {
                LogUtils.e(TAG, "getDiary", "fail to get data from database");
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveDiary(@NonNull Diary diary , @NonNull OnCompleteCallback callback) {
        mDiaryLocalDataSource.saveDiary(diary, callback);
        if (mCachedDiaries == null) {
            mCachedDiaries = new LinkedHashMap<>();
        }
        mCachedDiaries.put(diary.getId(), diary);
    }

    @Override
    public void refreshDiaries() {
        mDiaryLocalDataSource.getDiaries(new LoadDiariesCallback() {
            @Override
            public void onDiariesLoaded(List<Diary> diaries) {
                refreshCache(diaries);

            }

            @Override
            public void onDataNotAvailable() {
                LogUtils.e(TAG, "refreshDiaries", "fail to get data from database");
            }
        });
    }

    @Override
    public void deleteAllDiaries(@NonNull OnCompleteCallback callback) {
        mDiaryLocalDataSource.deleteAllDiaries(callback);
        if (mCachedDiaries == null) {
            mCachedDiaries = new LinkedHashMap<>();
        }
        mCachedDiaries.clear();
    }

    @Override
    public void deleteDiaryById(String id, @NonNull OnCompleteCallback callback) {
        mDiaryLocalDataSource.deleteDiaryById(id, callback);
        mCachedDiaries.remove(id);
    }

    @Override
    public void updateDiary(Diary diary, @NonNull OnCompleteCallback callback) {
        mDiaryLocalDataSource.updateDiary(diary, callback);
        mCachedDiaries.remove(diary.getId());
        mCachedDiaries.put(diary.getId(), diary);
    }

    @Override
    public void search(String key, @NonNull LoadDiariesCallback callback) {
        mDiaryLocalDataSource.search("%" + key + "%", callback);
    }

    private void refreshCache(List<Diary> diaries) {
        if (mCachedDiaries == null) {
            mCachedDiaries = new LinkedHashMap<>();
        }
        mCachedDiaries.clear();
        for (Diary diary : diaries) {
            mCachedDiaries.put(diary.getId(), diary);
        }
    }

    private Diary getDiaryById(@NonNull String id) {
        if (mCachedDiaries == null || mCachedDiaries.isEmpty()) {
            return null;
        } else {
            return mCachedDiaries.get(id);
        }
    }
}
