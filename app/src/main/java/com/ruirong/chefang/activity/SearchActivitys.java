package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.SearchAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  第二版搜索页面
 *  Created by 李  on 2018/11/19.
 */
public class SearchActivitys extends BaseActivity {
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_left_arrows)
    ImageView ivLeftArrows;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    private SearchAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_searchs;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        ButterKnife.bind(this);
        hideTitleBar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter=new SearchAdapter(rvSearch);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvSearch.setLayoutManager(llm);
        rvSearch.setAdapter(mAdapter);
        ArrayList<String> strings = new ArrayList<>();
        for (int i=0;i<5;i++){
            strings.add("");
        }
        mAdapter.setData(strings);

    }

    @Override
    public void getData() {

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
