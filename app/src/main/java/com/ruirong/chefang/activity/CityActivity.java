package com.ruirong.chefang.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;

import com.ruirong.chefang.adapter.ChoiceAreaAdapter;
import com.ruirong.chefang.bean.AreaBean;
import com.ruirong.chefang.http.RemoteApi;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by guo on 2017/8/18.
 * 地区选择之市级
 */

public class CityActivity extends BaseActivity implements OnRVItemClickListener {
    @BindView(R.id.rv_city)
    RecyclerView rvCity;
    private ChoiceAreaAdapter choiceAreaAdapter;
    private String pid;
    private String cityId;
    private String cityName;

    @Override
    public int getContentView() {
        return R.layout.activity_city;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Loading);
        titleBar.setTitleText("选择地区");

        pid = getIntent().getStringExtra("pid");

        choiceAreaAdapter = new ChoiceAreaAdapter(rvCity);
        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.addItemDecoration(new RecycleViewDivider(this));
        rvCity.setAdapter(choiceAreaAdapter);
        choiceAreaAdapter.setOnRVItemClickListener(this);
    }

    @Override
    public void getData() {
        BaseSubscriber<BaseBean<List<AreaBean>>> areaListSubscriber = new BaseSubscriber<BaseBean<List<AreaBean>>>(this, loadingLayout){
            @Override
            public void onNext(BaseBean<List<AreaBean>> listBaseBean) {
                super.onNext(listBaseBean);
                if (listBaseBean.code==0){
                    choiceAreaAdapter.setData(listBaseBean.data);
                }else {
                    ToastUtil.showToast(CityActivity.this,getString(R.string.net_error));
                }
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).getAreaList(pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(areaListSubscriber);
    }



    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

        AreaBean item = choiceAreaAdapter.getItem(position);
        cityId = item.getId();
        cityName = item.getName();
        CountyActivity.startActivityForResult(this,cityId,0);
    }

    public static void startActivityForResult(Activity context, String pid, int requestCode){
        Intent intent = new Intent(context, CityActivity.class);
        intent.putExtra("pid",pid);
        context.startActivityForResult(intent,requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){

            data.putExtra("cityId",cityId);
            data.putExtra("cityName",cityName);

            setResult(RESULT_OK,data);
            finish();
        }
    }
}
