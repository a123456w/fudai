package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 美食 确认订单
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener{


    @BindView(R.id.iv_subtract)
    ImageView ivSubtract;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    int count=1;

    @Override
    public int getContentView() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("确认订单");
        tvCount.setText(count+"");
        ivAdd.setOnClickListener(this);
        ivSubtract.setOnClickListener(this);

    }

    @Override
    public void getData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_subtract:

                    if (count>=0&&count!=-1){
                        count--;
                        tvCount.setText(count+"");

                    }

                break;
            case R.id.iv_add:

                    count++;
                tvCount.setText(count+"");

                break;

        }
    }
}
