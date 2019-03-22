package com.ruirong.chefang.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车房发布
 * Created by dillon on 2017/12/28.
 */

public class GarageReleaseActivity extends BaseActivity {
    @BindView(R.id.et_corporate_name)
    EditText etCorporateName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.rb_newcar)
    RadioButton rbNewcar;
    @BindView(R.id.rb_olddcar)
    RadioButton rbOlddcar;
    @BindView(R.id.rb_newhouse)
    RadioButton rbNewhouse;
    @BindView(R.id.rb_oldhouse)
    RadioButton rbOldhouse;
    @BindView(R.id.rb_commercial_property)
    RadioButton rbCommercialProperty;
    @BindView(R.id.rg_garade)
    RadioGroup rgGarade;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.iv_garade)
    RelativeLayout ivGarade;
    @BindView(R.id.tv_garade)
    TextView tvGarade;


    @Override
    public int getContentView() {
        return R.layout.activity_garage_release;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        ButterKnife.bind(this);
        titleBar.setTitleText("车房发布");

    }

    @Override
    public void getData() {

    }

    @OnClick({R.id.iv_garade, R.id.rb_newcar,R.id.rb_olddcar,R.id.rb_newhouse,R.id.rb_oldhouse,R.id.rb_commercial_property})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_garade:
                Garade(view);
//                rgGarade.setVisibility(view.VISIBLE);
                break;
            case R.id.rb_newcar:
                tvGarade.setText("新车");
                break;
            case R.id.rb_olddcar:
                tvGarade.setText("二手车");
                break;
            case R.id.rb_newhouse:
                tvGarade.setText("新房");
                break;
            case R.id.rb_oldhouse:
                tvGarade.setText("二手房");
                break;
            case R.id.rb_commercial_property:
                tvGarade.setText("商业房产");
                break;
        }
    }
    //RadioGroup的显示和隐藏
    private void Garade(View view) {
        view.setSelected(!view.isSelected());
        if (rgGarade.getVisibility() == view.GONE) {
            rgGarade.setVisibility(view.VISIBLE);
        } else {
            rgGarade.setVisibility(view.GONE);
        }
    }

}
