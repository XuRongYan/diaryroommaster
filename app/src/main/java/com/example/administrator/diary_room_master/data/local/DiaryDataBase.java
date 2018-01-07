package com.example.administrator.diary_room_master.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.administrator.diary_room_master.data.bean.Diary;

/**
 * 包含diary表的数据库
 * Created by Administrator on 2018/1/4
 */

@Database(entities = {Diary.class}, version = 1, exportSchema = false)
public abstract class DiaryDataBase extends RoomDatabase{
    private static DiaryDataBase INSTANCE;

    public abstract DiariesDao diariesDao();

    private static final Object sLock = new Object();

    public static DiaryDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        DiaryDataBase.class,
                        "Diaries.db")
                        .build();
            }
        }
        return INSTANCE;
    }
}
