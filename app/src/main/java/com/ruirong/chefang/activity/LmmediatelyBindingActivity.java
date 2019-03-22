package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.PaymentMethodAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  立即绑定
 */
public class LmmediatelyBindingActivity
        extends BaseActivity

{


    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.lv_payment)
    ListView lvPayment;

    private PaymentMethodAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_lmmediately_binding;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("立即预订");

        adapter=new PaymentMethodAdapter(lvPayment);
        lvPayment.setAdapter(adapter);

    }

    @Override
    public void getData() {
        getListData();
    }

    public void getListData() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
