package com.ruirong.chefang.activity;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新增收货地址    设计图上有一个设置默认的按钮，但是程序上是不允许的，如果没有拿到地址的ID，是不能报地址设置成默认的。
 * Created by dillon on 2017/4/6.
 */

public class AddreceiveAddressActivity extends BaseActivity {
    @BindView(R.id.et_receiver)
    EditText etReceiver;
    @BindView(R.id.et_phonenum)
    EditText etPhonenum;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_postcode)
    EditText etPostcode;
  /*  @BindView(R.id.btn_setdefault)
    Button btnSetdefault;
*/

    private BaseSubscriber<BaseBean<String>> addAddressSubscrible ;
    @Override
    public int getContentView() {
        return R.layout.activity_addreceiveadddress;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("新增地址");
        titleBar.setRightTextRes("保存");


        //保存修改后的地址和状态。
        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etReceiver.getText().toString())){
                    ToastUtil.showToast(AddreceiveAddressActivity.this,"收货人不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etPhonenum.getText().toString())){
                    ToastUtil.showToast(AddreceiveAddressActivity.this,"账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etAddress.getText().toString())){
                    ToastUtil.showToast(AddreceiveAddressActivity.this,"收货地址不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etPostcode.getText().toString())){
                    ToastUtil.showToast(AddreceiveAddressActivity.this,"邮政编码不能为空");
                    return;
                }
                if(!ToolUtil.isPostCode(etPostcode.getText().toString())){
                    ToastUtil.showToast(AddreceiveAddressActivity.this,"邮政编码格式不正确");
                    return;
                }
                addAddress(etReceiver.getText().toString(),etPhonenum.getText().toString(),etAddress.getText().toString(),etPostcode.getText().toString());

            }
        });
    }

    @Override
    public void getData() {

    }

    /**
     * 添加地址
     * @param username   收货人名字
     * @param mobile     收货人电话
     * @param address    收货人地址
     * @param pos_code   收货人邮编
     */
    public void addAddress(String username,String mobile,String address,String pos_code){
//        addAddressSubscrible = new BaseSubscriber<BaseBean<String>>(this,loadingLayout){
//            @Override
//            public void onNext(BaseBean<String> stringBaseBean) {
//                super.onNext(stringBaseBean);
//                ToastUtil.showToast(AddreceiveAddressActivity.this,stringBaseBean.message);
//                if (stringBaseBean.code==0){
//                    setResult(RESULT_OK);
//                    finish();
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                super.onError(throwable);
//            }
//        };
//        HttpHelp.getInstance().create(RemoteApi.class).addAddress(username,mobile,address,pos_code,new PreferencesHelper(AddreceiveAddressActivity.this).getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(addAddressSubscrible);
    }


/*
    @OnClick(R.id.btn_setdefault)
    public void onClick() {
        finish();
    }*/

}
