package com.example.administrator.diary_room_master.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.diary_room_master.DiaryActivity;
import com.example.administrator.diary_room_master.R;
import com.example.administrator.diary_room_master.data.bean.Diary;
import com.rongyant.commonlib.CommonAdapter.CommonAdapter;
import com.rongyant.commonlib.CommonAdapter.ViewHolder;

import java.util.List;

/**
 * 搜索界面的适配器
 * Created by Administrator on 2018/1/5 0005 -19:47.
 */

public class SearchAdapter extends CommonAdapter<Diary> {
    public SearchAdapter(Context context, List<Diary> list) {
        super(context, list);
    }

    public SearchAdapter(Context context, List<Diary> list, RecyclerView recyclerView) {
        super(context, list, recyclerView);
    }

    @Override
    public int setLayoutId(int position) {
        return R.layout.item_diary;
    }

    @Override
    public void onBindVH(ViewHolder viewHolder, final Diary item, int position) {
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
                });
    }
}
