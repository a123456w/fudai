package com.ruirong.chefang.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.BankCardItem;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 添加银行卡
 * Created by dillon on 2017/4/11.
 */
public class AddBankCardActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.et_cardholder)
    EditText etCardholder;
    @BindView(R.id.et_cardnum)
    EditText etCardnum;
    @BindView(R.id.et_idcardnum)
    EditText etIdCardNum;
    @BindView(R.id.sp_bankname)
    Spinner spBankname;
    @BindView(R.id.btn_sureadd)
    Button btnSureadd;


    private BaseSubscriber<BaseBean<String>> addBankCardSubscriber;
    private BaseSubscriber<BaseBean<List<BankCardItem>>> bankCardSubscriber;
    private ArrayAdapter<String> adapter;
    private List<String> bankList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_addbank;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("添加银行卡");


    }

    @Override
    public void getData() {
        getBankList();
    }

    public void addBankCard(String name, String bank_card_number, String bank, String bankType) {
        addBankCardSubscriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
               // ToastUtil.showToast(AddBankCardActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    ToastUtil.showToast(AddBankCardActivity.this, stringBaseBean.message);
                    Intent intent = new Intent(AddBankCardActivity.this, BankCardActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
                }else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(AddBankCardActivity.this);
                }else {
                    ToastUtil.showToast(AddBankCardActivity.this, stringBaseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).addBankCard(name, bank_card_number, bank, new PreferencesHelper(AddBankCardActivity.this).getToken(), bankType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addBankCardSubscriber);
    }

    @OnClick(R.id.btn_sureadd)
    public void onClick() {
        if (TextUtils.isEmpty(etCardholder.getText().toString())) {
            ToastUtil.showToast(AddBankCardActivity.this, "开户人姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(etCardnum.getText().toString())) {
            ToastUtil.showToast(AddBankCardActivity.this, "银行卡号不能为空");
            return;
        }
        if (TextUtils.isEmpty(etIdCardNum.getText().toString())) {
            ToastUtil.showToast(AddBankCardActivity.this, "证件号不能为空");
            return;
        }
        addBankCard(etCardholder.getText().toString().trim(), etCardnum.getText().toString().trim(), etIdCardNum.getText().toString().trim(), adapter.getItem(spBankname.getSelectedItemPosition()).toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addBankCardSubscriber != null && addBankCardSubscriber.isUnsubscribed()) {
            addBankCardSubscriber.unsubscribe();
        }
        if (bankCardSubscriber != null && bankCardSubscriber.isUnsubscribed()) {
            bankCardSubscriber.unsubscribe();
        }
    }

    public void getBankList() {
        bankCardSubscriber = new BaseSubscriber<BaseBean<List<BankCardItem>>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<List<BankCardItem>> bankCardItemBaseBean) {
                super.onNext(bankCardItemBaseBean);
                if (bankCardItemBaseBean.code == 0) {
                    bankList.clear();
                    for (BankCardItem bankCardItem : bankCardItemBaseBean.data) {
                        bankList.add(bankCardItem.getName());
                    }
                    if (bankList.size() > 0) {
                        adapter = new ArrayAdapter<String>(AddBankCardActivity.this, android.R.layout.simple_list_item_1, bankList);
                        spBankname.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).bankCardList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bankCardSubscriber);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_person:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("持卡人说明");
                builder.setMessage("持卡人的真实姓名，只能是认证用户身份证本人的姓名");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.iv_id:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("帐号说明");
                builder1.setMessage("为了确证账户资金安全，只能绑定实名认证用户本人的银行卡。");
                builder1.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.create().show();
                break;
        }
    }
}
