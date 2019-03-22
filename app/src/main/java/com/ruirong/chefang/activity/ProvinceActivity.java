package com.ruirong.chefang.activity;

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
 * 地区选择之省级
 */

public class ProvinceActivity extends BaseActivity implements OnRVItemClickListener {
    @BindView(R.id.rv_province)
    RecyclerView rvProvince;
    private ChoiceAreaAdapter choiceAreaAdapter;
    private String provinceId;
    private String provinceName;

    @Override
    public int getContentView() {
        return R.layout.activity_province;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Loading);
        titleBar.setTitleText("选择地区");

        choiceAreaAdapter = new ChoiceAreaAdapter(rvProvince);
        rvProvince.setLayoutManager(new LinearLayoutManager(this));
        rvProvince.addItemDecoration(new RecycleViewDivider(this));
        rvProvince.setAdapter(choiceAreaAdapter);
        choiceAreaAdapter.setOnRVItemClickListener(this);

    }


    @Override
    public void getData() {
        BaseSubscriber<BaseBean<List<AreaBean>>> areaListSubscriber = new BaseSubscriber<BaseBean<List<AreaBean>>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<List<AreaBean>> listBaseBean) {
                super.onNext(listBaseBean);
                if (listBaseBean.code == 0) {
                    choiceAreaAdapter.setData(listBaseBean.data);
                } else {
                    ToastUtil.showToast(ProvinceActivity.this, getString(R.string.net_error));
                }
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).getAreaList("0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(areaListSubscriber);
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        AreaBean item = choiceAreaAdapter.getItem(position);
        provinceId = item.getId();
        provinceName = item.getName();
        CityActivity.startActivityForResult(this, provinceId, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            data.putExtra("provinceId", provinceId);
            data.putExtra("provinceName", provinceName);
            setResult(RESULT_OK, data);
            finish();
        }
    }


}
