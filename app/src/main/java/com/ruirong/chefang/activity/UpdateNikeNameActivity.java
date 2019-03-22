package com.ruirong.chefang.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.UpdateInformationEvent;
import com.ruirong.chefang.event.UpdateNickNameEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.LimitEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 修改昵称
 * Created by dillon on 2017/4/12.
 */

public class UpdateNikeNameActivity extends BaseActivity {
    @BindView(R.id.et_nickname)
    LimitEditText etNickname;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private BaseSubscriber<BaseBean<String>> updateNickNameSubscriber ;

    @Override
    public int getContentView() {
        return R.layout.activity_updatenickname;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("修改昵称");
    }

    @Override
    public void getData() {

    }

    public void commitUpdateNickName(final String nickName){
        updateNickNameSubscriber = new BaseSubscriber<BaseBean<String>>(this,loadingLayout){
            @Override
            public void onNext(BaseBean<String> stringBaseBean){
                super.onNext(stringBaseBean);
                if (stringBaseBean.code==0){
                    EventBusUtil.post(new UpdateInformationEvent());
                    EventBusUtil.post(new UpdateNickNameEvent());
                    Intent intent = new Intent();
                    intent.putExtra(Constants.UPDATENICKNAME,nickName);
                    setResult(RESULT_OK,intent);
                    finish();
                } else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(UpdateNikeNameActivity.this);
                }
                ToastUtil.showToast(UpdateNikeNameActivity.this,stringBaseBean.message);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                finish();
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).updateNickname(nickName,new PreferencesHelper(UpdateNikeNameActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateNickNameSubscriber);
    }


    @OnClick({R.id.iv_clear, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etNickname.getText().clear();
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(etNickname.getText().toString())){
                    ToastUtil.showToast(UpdateNikeNameActivity.this,"昵称不能为空");
                    return;
                }
                UserInforBean userInforBean =  new Gson().fromJson(new PreferencesHelper(UpdateNikeNameActivity.this).getUserInfo(), UserInforBean.class);
                if (etNickname.getText().toString().equals(userInforBean.getName())){
                    ToastUtil.showToast(UpdateNikeNameActivity.this,"修改昵称和原昵称不能一样");
                    return;
                }
                commitUpdateNickName(etNickname.getText().toString());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateNickNameSubscriber!=null&&updateNickNameSubscriber.isUnsubscribed()){
            updateNickNameSubscriber.unsubscribe();
        }
    }
}
