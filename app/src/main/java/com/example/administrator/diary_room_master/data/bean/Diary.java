package com.example.administrator.diary_room_master.data.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rongyant.commonlib.util.StringUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * 日记实体类
 * Created by Administrator on 2018/1/4 0004.
 */

@Entity(tableName = "diaries")
public final class Diary implements Serializable{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String mId;          //日记id

    @Nullable
    @ColumnInfo(name = "title")
    private final String mTitle;    //日记标题

    @Nullable
    @ColumnInfo(name = "content")
    private final String mContent;  //日记内容

    @NonNull
    @ColumnInfo(name = "time")
    private final String mTime;     //日记发表日期

    /**
     * 利用这个构造方法创建一个新的日记实例
     * @param title
     * @param content
     * @param time
     */
    @Ignore
    public Diary(@Nullable String title, @Nullable String content, @NonNull String time) {
        this(UUID.randomUUID().toString(), title, content, time);
    }




    /**
     * 由于构造已经存在的日记（已经分配id）
     * @param mId       日记id
     * @param mTitle    日记标题
     * @param mContent  日记内容
     * @param mTime     日记发表时间
     */
    public Diary(@NonNull String mId, @Nullable String mTitle, @Nullable String mContent, @NonNull String mTime) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mTime = mTime;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getContent() {
        return mContent;
    }

    @NonNull
    public String getTime() {
        return mTime;
    }

    public boolean isEmpty() {
        return StringUtil.isNullOrEmpty(mTitle) && StringUtil.isNullOrEmpty(mContent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Diary diary = (Diary) o;

        if (!mId.equals(diary.mId)) return false;
        if (mTitle != null ? !mTitle.equals(diary.mTitle) : diary.mTitle != null) return false;
        if (mContent != null ? !mContent.equals(diary.mContent) : diary.mContent != null)
            return false;
        return mTime.equals(diary.mTime);
    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mContent != null ? mContent.hashCode() : 0);
        result = 31 * result + mTime.hashCode();
        return result;
    }
}
