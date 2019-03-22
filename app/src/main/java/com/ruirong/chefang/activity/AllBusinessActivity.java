package com.ruirong.chefang.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.AllBussinessGridviewAdapter;
import com.ruirong.chefang.bean.AllIndustriesBean;
import com.ruirong.chefang.http.RemoteApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/3 0003
 * describe:  全部行业
 */
public class AllBusinessActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.nsgv_shopmall_label)
    NoScrollGridView nsgvShopmallLabel;
    private AllBussinessGridviewAdapter nearShopAdapter;
    private List<AllIndustriesBean> shopNearList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_all_business;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        titleBar.setTitleText("全部行业");
        titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showLoadingDialog("加载中...");
        nearShopAdapter = new AllBussinessGridviewAdapter(nsgvShopmallLabel);
        nsgvShopmallLabel.setFocusable(false);
        nsgvShopmallLabel.setAdapter(nearShopAdapter);
        nsgvShopmallLabel.setOnItemClickListener(this);

    }

    @Override
    public void getData() {

        allcate();

    }

    private void allcate() {
        HttpHelp.getInstance().create(RemoteApi.class).allcate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<AllIndustriesBean>>>(AllBusinessActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<AllIndustriesBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            shopNearList = baseBean.data;
                            if (shopNearList != null && shopNearList.size() > 0) {
                                nearShopAdapter.setData(shopNearList);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent1 = new Intent(AllBusinessActivity.this, ShopMallClassifyActivity.class);
        intent1.putExtra("titleName", nearShopAdapter.getData().get(i).getName());
        intent1.putExtra("classify_id", nearShopAdapter.getData().get(i).getId());
        startActivity(intent1);
    }
}
