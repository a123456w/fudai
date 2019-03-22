package com.ruirong.chefang.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * 修改收货地址
 * Created by dillon on 2017/4/6.
 */

public class UpdateReceiveAddressActivity extends BaseActivity {


    @BindView(R.id.et_upreceiver)
    EditText etUpreceiver;
    @BindView(R.id.et_upphonenum)
    EditText etUpphonenum;
    @BindView(R.id.et_upaddress)
    EditText etUpaddress;
    @BindView(R.id.et_uppostcode)
    EditText etUppostcode;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_setdefault)
    Button btnSetdefault;
    private String addressId;   // 地址的ID
    private BaseSubscriber<BaseBean<String>> deleteAddressSubscriber;
    private BaseSubscriber<BaseBean<String>> setdefaultAddressSubscriber;
    private BaseSubscriber<BaseBean<String>> updateAddressSubccriber;


    @Override
    public int getContentView() {
        return R.layout.activity_updatereceiveaddress;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("修改地址");
        titleBar.setRightTextRes("保存");

        AddressItemBean consignee = (AddressItemBean) getIntent().getSerializableExtra("consignee");
        boolean selectAddress = getIntent().getBooleanExtra("selectAddress", true);
        addressId =consignee.getId();
        etUpreceiver.setText(consignee.getRec_name());
        etUpphonenum.setText(consignee.getMobile());
        etUpaddress.setText(consignee.getAddress());
        etUppostcode.setText(consignee.getPos_code());


        if (selectAddress){
            btnDelete.setVisibility(View.INVISIBLE);
            btnSetdefault.setVisibility(View.INVISIBLE);
        }else {
            btnDelete.setVisibility(View.VISIBLE);
            btnSetdefault.setVisibility(consignee.getIs_defa().equals("0")?View.VISIBLE:View.INVISIBLE);
        }



        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etUpreceiver.getText().toString())) {
                    ToastUtil.showToast(UpdateReceiveAddressActivity.this, "收货人的名字不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etUpphonenum.getText().toString())) {
                    ToastUtil.showToast(UpdateReceiveAddressActivity.this, "收货人的手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etUpaddress.getText().toString())) {
                    ToastUtil.showToast(UpdateReceiveAddressActivity.this, "收货人的地址不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etUppostcode.getText().toString())) {
                    ToastUtil.showToast(UpdateReceiveAddressActivity.this, "邮政编码不能为空");
                    return;
                }
                if(!ToolUtil.isPostCode(etUppostcode.getText().toString())){
                    ToastUtil.showToast(UpdateReceiveAddressActivity.this,"邮政编码格式不正确");
                    return;
                }
                updateAddress(addressId, etUpreceiver.getText().toString().trim(), etUpphonenum.getText().toString().trim(), etUpaddress.getText().toString().trim(), etUppostcode.getText().toString().trim());

            }
        });
    }

    @Override
    public void getData() {

    }

    /**
     *
     * @param context
     * @param consignee
     * @param selectAddress 是否是从选择收货地址界面跳转过来的
     * @param requestCode
     */
    public static void startActivityForResult(Activity context, AddressItemBean consignee, boolean selectAddress, int requestCode){
        Intent intent = new Intent(context,UpdateReceiveAddressActivity.class);
        intent.putExtra("consignee",consignee);
        intent.putExtra("selectAddress",selectAddress);
        context.startActivityForResult(intent,requestCode);
    }


    /**
     * 删除当前地址
     */
    public void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("真的要删除么?");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteAddress(addressId);
            }
        });
        builder.create().show();
    }

    @OnClick({R.id.btn_delete, R.id.btn_setdefault})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_setdefault:
                setdefaultAddress(addressId);
                break;
        }
    }

    /**
     * 删除一条地址
     *
     * @param addressid 地址的ID
     */
    public void deleteAddress(String addressid) {
        deleteAddressSubscriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                ToastUtil.showToast(UpdateReceiveAddressActivity.this, stringBaseBean.message);
                if (stringBaseBean.code==0){
                    setResult(RESULT_OK);
                    finish();
                } else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(UpdateReceiveAddressActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).deleteAddress(addressid, new PreferencesHelper(UpdateReceiveAddressActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteAddressSubscriber);
    }

    /**
     * 设置默认地址
     *
     * @param addressid 地址的ID
     */
    public void setdefaultAddress(String addressid) {
        setdefaultAddressSubscriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                ToastUtil.showToast(UpdateReceiveAddressActivity.this, stringBaseBean.message);
                if (stringBaseBean.code==0){
                    setResult(RESULT_OK);
                    finish();
                }else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(UpdateReceiveAddressActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).setdefaultAddress(addressid, new PreferencesHelper(UpdateReceiveAddressActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(setdefaultAddressSubscriber);
    }

    /**
     * 修改地址
     *
     * @param addressIds 地址id
     * @param username   用户名
     * @param mobile     电话号
     * @param address    收货地址
     * @param pos_code   邮政编码
     */
    public void updateAddress(String addressIds, String username, String mobile, String address, String pos_code) {
//        updateAddressSubccriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
//            @Override
//            public void onNext(BaseBean<String> stringBaseBean) {
//                super.onNext(stringBaseBean);
//                ToastUtil.showToast(UpdateReceiveAddressActivity.this, stringBaseBean.message);
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
//        HttpHelp.getInstance().create(RemoteApi.class).updateAddress(addressIds, username, mobile, address, pos_code, new PreferencesHelper(UpdateReceiveAddressActivity.this).getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(updateAddressSubccriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (setdefaultAddressSubscriber != null && setdefaultAddressSubscriber.isUnsubscribed()) {
            setdefaultAddressSubscriber.unsubscribe();
        }
        if (deleteAddressSubscriber != null && deleteAddressSubscriber.isUnsubscribed()) {
            deleteAddressSubscriber.unsubscribe();
        }
        if (updateAddressSubccriber != null && updateAddressSubccriber.isUnsubscribed()) {
            updateAddressSubccriber.unsubscribe();
        }
    }
}
