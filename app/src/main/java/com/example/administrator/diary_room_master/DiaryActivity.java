package com.example.administrator.diary_room_master;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.diary_room_master.base.BaseActivity;
import com.example.administrator.diary_room_master.data.bean.Diary;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 日记展示页面
 */
public class DiaryActivity extends BaseActivity {

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.tv_diary_title)
    TextView mDiaryTitle;
    @BindView(R.id.tv_diary_content)
    TextView mDiaryContent;
    private Diary diary;

    @Override
    protected int getContentView() {
        return R.layout.activity_diary;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //获取上个界面传过来的日记对象
        diary = (Diary) getIntent().getSerializableExtra("diary");
        if (diary == null) {
            Toast.makeText(mContext, "bundle is null", Toast.LENGTH_SHORT).show();
            finish();
        }
        mToolBar.setTitle(diary != null ? diary.getTime() : null);
        mDiaryTitle.setText(diary != null ? diary.getTitle() : null);
        mDiaryContent.setText(diary != null ? diary.getContent() : null);
        setToolbar(mToolBar);
        setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 初始化菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diary_toolbar, menu);
        return true;
    }

    /**
     * 设置菜单项点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Bundle bundle = new Bundle();
                bundle.putSerializable("diary", diary);
                goActivity(AddDiaryActivity.class, bundle);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
