package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 立即预定
 * Created by BX on 2017/12/29.
 */

public class ImmediateReservationActivity extends BaseActivity{
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_pay_fortunella_venosa)
    TextView tvPayFortunellaVenosa;

    @Override
    public int getContentView() {
        return R.layout.activity_immediatereservation;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("立即预订");
    }

    @Override
    public void getData() {

    }


//      @OnClick({R.id.tv_pay_fortunella_venosa})
//          public void onViewClicked(View view) {
//              switch (view.getId()) {
//                  case R.id.tv_pay_fortunella_venosa:
//                      Intent intent1 = new Intent(ImmediateReservationActivity.this, RechargeActivity.class);
//                                      intent1.putExtra("int_data", 100);
//                                      startActivity(intent1);
//                      break;
//
//              }
//          }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
