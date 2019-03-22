package com.ruirong.chefang.activity;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ConfirmationOrderBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确认订单
 * Created by BX on 2017/12/29.
 */

public class ConfirmationOrderActivity extends BaseActivity {
    @BindView(R.id.gv_package)
    GridView gvPackage;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_pay_fortunella_venosa)
    TextView tvPayFortunellaVenosa;
    private List<ConfirmationOrderBean> lists = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_confirmation_order;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("确认订单");
        initData();
    }

    private void initData() {
    }

    @Override
    public void getData() {
    }

    @OnClick({R.id.tv_submit, R.id.tv_pay_fortunella_venosa})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                Intent intent1 = new Intent(ConfirmationOrderActivity.this, LmmediatelyBindingActivity.class);
                intent1.putExtra("int_data", 100);
                startActivity(intent1);
                break;
            case R.id.tv_pay_fortunella_venosa:
                Intent intent2 = new Intent(ConfirmationOrderActivity.this, RechargeActivity.class);
                intent2.putExtra("int_data", 100);
                startActivity(intent2);
                break;

        }
    }
}
