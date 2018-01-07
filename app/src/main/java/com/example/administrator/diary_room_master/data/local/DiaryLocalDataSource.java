package com.example.administrator.diary_room_master.data.local;

import android.support.annotation.NonNull;

import com.example.administrator.diary_room_master.data.DiaryDataSource;
import com.example.administrator.diary_room_master.data.bean.Diary;
import com.rongyant.commonlib.util.AppExecutorsUtil;

import java.util.List;

/**
 * 本地数据库实现本地日记数据源
 * Created by Administrator on 2018/1/4.
 */

public class DiaryLocalDataSource implements DiaryDataSource {
    private static volatile DiaryLocalDataSource INSTANCE;

    private DiariesDao mDiariesDao;         //数据库操作接口

    private AppExecutorsUtil mAppExecutors; //线程执行调度器

    //单例模式
    private DiaryLocalDataSource(DiariesDao diariesDao, AppExecutorsUtil appExecutors) {
        this.mDiariesDao = diariesDao;
        this.mAppExecutors = appExecutors;
    }

    public static DiaryLocalDataSource getInstance(@NonNull AppExecutorsUtil appExecutors,
                                                   @NonNull DiariesDao diariesDao) {
        if (INSTANCE == null) {
            synchronized (DiaryLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DiaryLocalDataSource(diariesDao, appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note：{@link LoadDiariesCallback#onDataNotAvailable()} 会在数据库不存在或者数据表为空时触发
     * @param callback 加载多个数据时的回调接口
     */
    @Override
    public void getDiaries(@NonNull final LoadDiariesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Diary> diaries = mDiariesDao.getDiaries();
                //得到的结果在主线程回调
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (diaries.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDiariesLoaded(diaries);
                        }
                    }
                });
            }
        };
        //在后台执行数据库查询操作
        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Note:{@link GetDiaryCallback#onDataNotAvailable()}会在{@link Diary}为空时触发
     * @param diaryId   日记id
     * @param callback  加载单条数据时的回调接口
     */
    @Override
    public void getDiary(final String diaryId, @NonNull final GetDiaryCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Diary diary = mDiariesDao.getDiaryById(diaryId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (diary.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDiaryLoaded(diary);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveDiary(@NonNull final Diary diary, @NonNull final OnCompleteCallback callback) {
        if (diary.isEmpty()) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mDiariesDao.insertDiary(diary);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onComplete();
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshDiaries() {

    }

    @Override
    public void deleteAllDiaries(@NonNull final OnCompleteCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mDiariesDao.deleteDiaries();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onComplete();
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteDiaryById(final String id, @NonNull final OnCompleteCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mDiariesDao.deleteDiaryById(id);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onComplete();
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void updateDiary(final Diary diary, @NonNull final OnCompleteCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mDiariesDao.updateDiary(diary);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onComplete();
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void search(final String key, @NonNull final LoadDiariesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Diary> diaries = mDiariesDao.search(key);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (diaries == null) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onDiariesLoaded(diaries);
                        }
                    }
                });

            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    static void clearInstance() {
        INSTANCE = null;
    }
}
