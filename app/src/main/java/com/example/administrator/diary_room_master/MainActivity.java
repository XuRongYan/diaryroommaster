package com.example.administrator.diary_room_master;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.diary_room_master.adapter.DiariesAdapter;
import com.example.administrator.diary_room_master.base.BaseActivity;
import com.example.administrator.diary_room_master.contracts.MainContract;
import com.example.administrator.diary_room_master.data.DiaryDataSource;
import com.example.administrator.diary_room_master.data.Injection;
import com.example.administrator.diary_room_master.data.bean.Diary;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements MainContract.View{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.main_float_btn_add_diary)
    FloatingActionButton mFloatBtnAddDiary;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private AlertDialog mAlterDialog = null;
    private List<Diary> mList = new ArrayList<>();
    private SearchView mSearchView;
    private DiariesAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setToolbar(toolbar);
        setDoubleBackExit(true);
        //初始化recyclerView的布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        //初始化适配器
        mAdapter = new DiariesAdapter(this, mList);
        //绑定适配器
        mRecyclerView.setAdapter(mAdapter);
        //初始化上拉刷新
        initSwipeRefresh();

    }

    @Override
    protected void initPresenter() {
        Injection.provideMainPresenter(this, this);
    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.main_float_btn_add_diary})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.main_float_btn_add_diary:
                //设置android5.0以上界面跳转时共享元素的效果
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    goActivityShareElements(AddDiaryActivity.class, "floatBtn", view);
                } else {
                    goActivity(AddDiaryActivity.class);
                }
                break;
        }
    }

    /**
     * 初始化下拉刷新
     */
    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        //设置下拉刷新触发时重新加载数据
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainContract.Presenter) mPresenter).loadDiaries();
            }
        });
    }



    /**
     * 为toolbar设定menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * 设置菜单项被点击后触发的事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                AlertDialog alertDialog = generateAlterDialog();
                alertDialog.show();
                break;
            case R.id.action_search_view:
                goActivity(SearchDiaryActivity.class);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }



    /**
     * 生成测试数据（仅调试时使用）
     *
     * @param adapter recyclerView的适配器
     */
    private void addTestDiary(DiariesAdapter adapter) {
        for (int i = 0; i < 20; i++) {
            adapter.addListBeanAtStart(new Diary("666", "666", "666"));
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 展示日记
     * @param diaries
     */
    @Override
    public void showDiaries(List<Diary> diaries) {
        mAdapter.replaceList(diaries);
    }

    /**
     * 往列表中添加日记（多个）
     * @param diaries
     */
    @Override
    public void addDiaries(List<Diary> diaries) {
        mAdapter.addListAtStart(diaries);
    }

    /**
     * 往列表中添加日记（单个）
     * @param diary
     */
    @Override
    public void addDiary(Diary diary) {
        mAdapter.addListBeanAtStart(diary);
    }

    /**
     * 设置刷新控件的状态
     * @param isRefresh
     */
    @Override
    public void setSwipeRefresh(boolean isRefresh) {
        mSwipeRefresh.setRefreshing(isRefresh);
    }

    /**
     * 生成对话框（单例）
     * @return
     */
    private AlertDialog generateAlterDialog() {
        if (mAlterDialog == null) {
            synchronized (this) {
                if (mAlterDialog == null) {
                    mAlterDialog = new AlertDialog.Builder(this)
                            .setTitle("是否删除所有日记记录？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((MainContract.Presenter) mPresenter).deleteAll(new DiaryDataSource.OnCompleteCallback() {
                                        @Override
                                        public void onComplete() {
                                            mAdapter.removeAll();
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                }
            }
        }
        return mAlterDialog;
    }
}
