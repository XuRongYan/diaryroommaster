package com.example.administrator.diary_room_master.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.administrator.diary_room_master.data.bean.Diary;

import java.util.List;

/**
 * Diary数据库的Dao操作
 * Created by Administrator on 2018/1/4.
 */

@Dao
public interface DiariesDao {
    /**
     * 获取所有日记
     * @return 所有日记
     */
    @Query("SELECT * FROM DIARIES")
    List<Diary> getDiaries();

    /**
     * 获取指定id的日记
     * @param diaryId   日记id
     * @return          指定id的日记
     */
    @Query("SELECT * FROM DIARIES WHERE id = :diaryId")
    Diary getDiaryById(String diaryId);

    /**
     * 往数据库中插入一条日记，如果该日记已经存在，覆盖之
     * @param diary
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDiary(Diary diary);

    /**
     * 更新一条日记
     * @param diary 要更新的日记
     * @return      更新的条数，这里应该总为1
     */
    @Update
    int updateDiary(Diary diary);

    /**
     * 根据id删除一条日记
     * @param diaryId   日记id
     * @return          删除日记的条数，这里应该总为1
     */
    @Query("DELETE FROM DIARIES WHERE id = :diaryId")
    int deleteDiaryById(String diaryId);

    /**
     * 删除所有的日记
     * @return  删除日记的条数
     */
    @Query("DELETE FROM DIARIES")
    int deleteDiaries();

    @Query("SELECT * FROM DIARIES WHERE title LIKE :key")
    List<Diary> search(String key);
}
