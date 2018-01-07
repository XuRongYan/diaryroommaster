package com.example.administrator.diary_room_master.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.diary_room_master.DiaryActivity;
import com.example.administrator.diary_room_master.R;
import com.example.administrator.diary_room_master.data.DiaryDataSource;
import com.example.administrator.diary_room_master.data.DiaryRepository;
import com.example.administrator.diary_room_master.data.Injection;
import com.example.administrator.diary_room_master.data.bean.Diary;
import com.rongyant.commonlib.CommonAdapter.CommonAdapter;
import com.rongyant.commonlib.CommonAdapter.ViewHolder;

import java.util.List;

/**
 * 日记列表的适配器
 * Created by Administrator on 2018/1/4 0004.
 */

public class DiariesAdapter extends CommonAdapter<Diary> {

    private AlertDialog mAlertDialog;

    public DiariesAdapter(Context context, List<Diary> list) {
        super(context, list);
    }

    public DiariesAdapter(Context context, List<Diary> list, RecyclerView recyclerView) {
        super(context, list, recyclerView);
    }

    @Override
    public int setLayoutId(int position) {
        return R.layout.item_diary;
    }

    @Override
    public void onBindVH(ViewHolder viewHolder, final Diary item, final int position) {
        viewHolder.setText(R.id.tv_diary_title, item.getTitle())
                .setText(R.id.tv_diary_content, item.getContent())
                .setText(R.id.tv_diary_time, item.getTime())
                .setOnClickListener(R.id.item_cardView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击跳转
                        Intent intent = new Intent(context, DiaryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("diary", item);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                })
                .setOnLongClickListener(R.id.item_cardView, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //长按弹出对话框询问是否删除
                        AlertDialog alertDialog = generateAlterDialog(position, item);
                        alertDialog.show();
                        return true;
                    }
                });
    }

    /**
     * 生成对话框（单例）
     * @param position  recyclerItem的位置
     * @param item      diary实例
     * @return
     */
    public AlertDialog generateAlterDialog(final int position, final Diary item) {
        if (mAlertDialog == null) {
            synchronized (this) {
                if (mAlertDialog == null) {
                    mAlertDialog = new AlertDialog.Builder(context)
                            .setTitle("是否删除该条日记？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DiariesAdapter.this.remove(position);
                                    DiaryRepository diaryRepository = Injection.provideDiariesRepository(context);
                                    diaryRepository.deleteDiaryById(item.getId(), new DiaryDataSource.OnCompleteCallback() {
                                        @Override
                                        public void onComplete() {
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                }
            }
        }
        return mAlertDialog;
    }


}
