package com.ruirong.chefang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;

import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.AddressItemBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/8 0008
 * describe:  我的地址  新增
 */
public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.et_receiver)
    EditText etReceiver;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_address_ll)
    RelativeLayout tvAddressLl;
    @BindView(R.id.zip_code)
    EditText zipCode;
    @BindView(R.id.detailed_address)
    EditText detailedAddress;
    @BindView(R.id.istrue)
    Switch istrue;
    @BindView(R.id.istrue_rl)
    RelativeLayout istrueRl;
    @BindView(R.id.view_line)
    View viewLine;

    private int type = -1; //1是注册  2是修改
    private final int REQUEST_CODE_CHOOSE_AREA = 300;
    private String provinceId = "";
    private String cityId = "";
    private String countyId = "";
    private String provinceName = "";
    private String cityName = "";
    private String countyName = "";
    private String address_pj;
    private boolean isFalse = false;
    private AddressItemBean addressItemBean;
    private int type_choice;


    @Override
    public int getContentView() {
        return R.layout.activity_add_address;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);


        type = getIntent().getIntExtra("type", -1);
        type_choice = getIntent().getIntExtra("type_choice", -1);
        if (type == 1) {
            titleBar.setTitleText("新增地址");

        } else if (type == 2) {
            titleBar.setTitleText("修改地址");
            istrueRl.setVisibility(View.GONE);
            String address = getIntent().getStringExtra("address");
            addressItemBean = new Gson().fromJson(address, AddressItemBean.class);
            viewLine.setVisibility(View.GONE);
            if (addressItemBean != null) {
                etReceiver.setText(addressItemBean.getRec_name());
                etPhone.setText(addressItemBean.getMobile());
                tvAddress.setText(addressItemBean.getSsx());
                zipCode.setText(addressItemBean.getPos_code());
                address_pj = addressItemBean.getSsx();
                detailedAddress.setText(addressItemBean.getAddress());
            }

        } else {

            finish();

        }

        titleBar.setRightTextRes("保存");
        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etReceiver.getText().toString().trim())) {
                    ToastUtil.showToast(AddAddressActivity.this, "用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                    ToastUtil.showToast(AddAddressActivity.this, "手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(address_pj)) {
                    ToastUtil.showToast(AddAddressActivity.this, "所在地不能为空");
                    return;
                }
                if (TextUtils.isEmpty(zipCode.getText().toString().trim())) {
                    ToastUtil.showToast(AddAddressActivity.this, "邮编不能为空");
                    return;
                }
                if (TextUtils.isEmpty(detailedAddress.getText().toString().trim())) {
                    ToastUtil.showToast(AddAddressActivity.this, "详细地址不能为空");
                    return;
                }
                if (!ToolUtil.isChinaPhoneLegal(etPhone.getText().toString().trim())) {
                    ToastUtil.showToast(AddAddressActivity.this, "手机号格式不正确");
                    return;
                }
                if(!ToolUtil.isPostCode(zipCode.getText().toString())){
                    ToastUtil.showToast(AddAddressActivity.this,"邮政编码格式不正确");
                    return;
                }
                if (type == 1) {
                    if (isFalse) {
                        addAddress(new PreferencesHelper(AddAddressActivity.this).getToken(), etReceiver.getText().toString().trim(),
                                etPhone.getText().toString().trim(), detailedAddress.getText().toString().trim(), zipCode.getText().toString().trim(),
                                address_pj, "1");
                    } else {
                        addAddress(new PreferencesHelper(AddAddressActivity.this).getToken(), etReceiver.getText().toString().trim(),
                                etPhone.getText().toString().trim(), detailedAddress.getText().toString().trim(), zipCode.getText().toString().trim(),
                                address_pj, "2");
                    }
                } else if (type == 2) {

                    updateAddress(new PreferencesHelper(AddAddressActivity.this).getToken(), addressItemBean.getId(), etReceiver.getText().toString().trim(),
                            etPhone.getText().toString().trim(), detailedAddress.getText().toString().trim(), zipCode.getText().toString().trim(),
                            address_pj, addressItemBean.getIs_defa());

                }
            }
        });

        titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        istrue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFalse = isChecked;
            }
        });

    }

    /**
     * 修改
     */
    private void updateAddress(String token, String id, String username, String mobile, String address,
                               String pos_code, String ssx, String is_defa) {

        HttpHelp.getInstance().create(RemoteApi.class).updateAddress(token, id, username, mobile, address, pos_code, ssx, is_defa)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(AddAddressActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            startActivity(AddAddressActivity.this, AddressActivity.class);
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(AddAddressActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    /**
     * 新增
     */
    private void addAddress(String token, String username, String mobile, String address,
                            String pos_code, String ssx, String is_defa) {

        HttpHelp.getInstance().create(RemoteApi.class).addAddress(token, username, mobile, address, pos_code, ssx, is_defa)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(AddAddressActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
                            if (type_choice != -1) {
                                intent.putExtra("type_choice", type_choice);
                            }
                            intent.putExtra("type", 2);
                            startActivity(intent);
                            finish();
                        }else if (baseBean.code==4){
                          ToolUtil.loseToLogin(AddAddressActivity.this);
                        }
                        ToastUtil.showToast(AddAddressActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    @Override
    public void getData() {

    }

    @OnClick(R.id.tv_address_ll)
    public void onViewClicked() {
        startActivityForResult(new Intent(AddAddressActivity.this, ProvinceActivity.class), REQUEST_CODE_CHOOSE_AREA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CODE_CHOOSE_AREA:
                    //选择地区
                    provinceId = data.getStringExtra("provinceId");
                    provinceName = data.getStringExtra("provinceName");
                    cityId = data.getStringExtra("cityId");
                    cityName = data.getStringExtra("cityName");
                    countyId = data.getStringExtra("countyId");
                    countyName = data.getStringExtra("countyName");
                    address_pj = provinceName + "-" + cityName + "-" + countyName;
                    tvAddress.setText(address_pj);
                    break;

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
