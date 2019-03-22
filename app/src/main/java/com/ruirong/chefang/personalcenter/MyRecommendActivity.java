package com.ruirong.chefang.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  我的推荐
 */
public class MyRecommendActivity extends BaseActivity {
    @BindView(R.id.icon_1)
    ImageView icon1;
    @BindView(R.id.rl_referral_history)
    RelativeLayout rlReferralHistory;
    @BindView(R.id.icon_3)
    ImageView icon3;
    @BindView(R.id.rl_referral_code)
    RelativeLayout rlReferralCode;
//    @BindView(R.id.icon_1)
//    ImageView icon1;
//    @BindView(R.id.rl_referral_history)
//    RelativeLayout rlReferralHistory;
//    @BindView(R.id.icon_3)
//    ImageView icon3;
//    @BindView(R.id.rl_referral_code)
//    RelativeLayout rlReferralCode;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_recommend);
//    }

    @Override
    public int getContentView() {
        return R.layout.activity_my_recommend;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("我的推荐");


    }

    @Override
    public void getData() {

    }


    @OnClick({R.id.rl_referral_history, R.id.rl_referral_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_referral_history:  //
                Intent intent1 = new Intent(MyRecommendActivity.this, RecommendRecordActivity.class);

                startActivity(intent1);
                break;
            case R.id.rl_referral_code:    //
                Intent intent2 = new Intent(MyRecommendActivity.this, MyReferralCodeActivity.class);

                startActivity(intent2);
                break;

        }
    }
}
