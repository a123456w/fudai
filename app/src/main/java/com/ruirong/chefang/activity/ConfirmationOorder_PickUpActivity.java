package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 确认订单--到店自取
 * Created by BX on 2018/1/2.
 */

public class ConfirmationOorder_PickUpActivity extends BaseActivity {
    @BindView(R.id.iv_subtract)
    ImageView ivSubtract;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    public int getContentView() {
        return R.layout.activity_confirmation_order_pick_up;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("确认订单");
    }

    @Override
    public void getData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
