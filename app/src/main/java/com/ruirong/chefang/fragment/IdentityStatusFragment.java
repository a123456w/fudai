package com.ruirong.chefang.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.IdentityStatusActivity;
import com.ruirong.chefang.bean.IdentityStatusBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dillon on 2017/7/14.
 */

public class IdentityStatusFragment extends BaseFragment {
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_again)
    Button btnAgain;
    Unbinder unbinder;

    BaseSubscriber<BaseBean<IdentityStatusBean>> identityStatuSubscriber ;
    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_identitystatus, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
    }

    @Override
    public void getData() {
        identityStatuSubscriber = new BaseSubscriber<BaseBean<IdentityStatusBean>>(getActivity(),null){
            @Override
            public void onNext(BaseBean<IdentityStatusBean> identityStatusBeanBaseBean) {
                super.onNext(identityStatusBeanBaseBean);
                if (identityStatusBeanBaseBean!=null&&identityStatusBeanBaseBean.code==0){
                    switch (identityStatusBeanBaseBean.data.getStatus()){
                        case 0:
                            GlideUtil.display(getActivity(), R.drawable.icon_6,ivStatus);
                            tvStatus.setText("未认证");
                            btnAgain.setText("去认证");
                            break;
                        case 1:
                            GlideUtil.display(getActivity(), R.drawable.ic_examine,ivStatus);
                            tvStatus.setText("待审核");
                            btnAgain.setVisibility(View.GONE);
                            break;
                        case 2:
                            GlideUtil.display(getActivity(), R.drawable.icon_5,ivStatus);
                            tvStatus.setText("已审核");
                            btnAgain.setVisibility(View.GONE);
                            break;
                        case 3:
                            GlideUtil.display(getActivity(), R.drawable.icon_6,ivStatus);
                            tvStatus.setText("身份审核被拒绝");
                            break;
                    }
                }else if (identityStatusBeanBaseBean.code==4){
                    ToolUtil.loseToLogin(mContext);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).identityStatus( new PreferencesHelper(getActivity()).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(identityStatuSubscriber);
    }

    @OnClick(R.id.btn_again)
    public void click(){
      //  startActivity(new Intent(IdentityStatusActivity.this, IdentityActivity.class));
        IdentityStatusActivity identityStatusActivity = (IdentityStatusActivity) getActivity();
        identityStatusActivity.changeFragment();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (identityStatuSubscriber!=null&&identityStatuSubscriber.isUnsubscribed()){
            identityStatuSubscriber.unsubscribe();
        }
    }
}
