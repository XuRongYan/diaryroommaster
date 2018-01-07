package com.example.administrator.diary_room_master;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.administrator.diary_room_master.adapter.SearchAdapter;
import com.example.administrator.diary_room_master.base.BaseActivity;
import com.example.administrator.diary_room_master.contracts.MainContract;
import com.example.administrator.diary_room_master.contracts.SearchContract;
import com.example.administrator.diary_room_master.data.Injection;
import com.example.administrator.diary_room_master.data.bean.Diary;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索页面
 */
public class SearchDiaryActivity extends BaseActivity implements SearchContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_recycler_view)
    RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private SearchAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_search_diary;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        initRecyclerView();
    }

    /**
     * 初始化搜索框
     */
    private void initSearchView() {
        mSearchView.setQueryHint("输入日记标题查找");
        mSearchView.setIconifiedByDefault(false);
        //设置搜索框搜索时的监听事件
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((SearchContract.Presenter) mPresenter).search(query); //调用搜索方法
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager); //设置布局方式
        mAdapter = new SearchAdapter(this, new ArrayList<Diary>());
        mRecyclerView.setAdapter(mAdapter); //设置适配器
    }

    /**
     * 对于顶端标题栏上菜单的初始化
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu); //获取菜单
        MenuItem item = menu.findItem(R.id.action_search_search_view); //获取搜索框
        mSearchView = (SearchView) item.getActionView(); //实例化搜索框
        initSearchView(); //初始化搜索框
        return true;
    }

    @Override
    protected void initPresenter() {
        Injection.provideSearchPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 展示搜索结果
     * @param diaries 搜索结果
     */
    @Override
    public void showResult(List<Diary> diaries) {
        mAdapter.replaceList(diaries);
    }
}
