package com.example.administrator.diary_room_master;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.example.administrator.diary_room_master.base.BaseActivity;
import com.example.administrator.diary_room_master.contracts.AddDiaryContract;
import com.example.administrator.diary_room_master.contracts.MainContract;
import com.example.administrator.diary_room_master.data.Injection;
import com.example.administrator.diary_room_master.data.bean.Diary;
import com.example.administrator.diary_room_master.presenter.AddDiaryPresenter;
import com.rongyant.commonlib.util.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加日记和编辑日记界面
 */
public class AddDiaryActivity extends BaseActivity implements AddDiaryContract.View{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.add_diary_input_title)
    TextInputLayout mAddDiaryInputTitle;
    @BindView(R.id.et_input_diary_content)
    TextInputLayout mEtInputDiaryContent;
    @BindView(R.id.add_diary_float_btn)
    FloatingActionButton mAddDiaryFloatBtn;
    private Diary mDiary;
    private String date = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_diary;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mDiary = (Diary) getIntent().getSerializableExtra("diary");
        if (mDiary != null) {
            mToolbar.setTitle("编辑");
            date = mDiary.getTime();
            mAddDiaryInputTitle.getEditText().setText(mDiary.getTitle());
            mEtInputDiaryContent.getEditText().setText(mDiary.getContent());
        }
        setToolbar(mToolbar);
        setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initPresenter() {
        Injection.provideAddDiaryPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void setPresenter(AddDiaryContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean isTitleValid() {
        return !TextUtils.isEmpty(mAddDiaryInputTitle.getEditText().getText().toString());
    }

    @Override
    public boolean setTitleError(String error) {
        mAddDiaryInputTitle.setError(error);
        return true;
    }

    @Override
    public Diary getDiary() {
        return new Diary(mAddDiaryInputTitle.getEditText().getText().toString(),
                mEtInputDiaryContent.getEditText().getText().toString(),
                TimeUtils.getCurrentTimeInString());
    }

    @Override
    public void onComplete() {
        finish();
    }

    @OnClick({R.id.add_diary_float_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_diary_float_btn:
                if (mDiary == null) {
                    ((AddDiaryContract.Presenter) mPresenter).addDiary();
                } else {
                    Diary diary = new Diary(mDiary.getId(), mAddDiaryInputTitle.getEditText().getText().toString(),
                            mEtInputDiaryContent.getEditText().getText().toString(),
                            mDiary.getTime());
                    ((AddDiaryContract.Presenter) mPresenter).changeDiary(diary);
                }
                break;
        }
    }


}
