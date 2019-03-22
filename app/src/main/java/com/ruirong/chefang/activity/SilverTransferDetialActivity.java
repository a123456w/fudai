package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SilverTransferDetailBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 金豆转账详情
 * Created by dillon on 2017/5/5.
 */

public class SilverTransferDetialActivity extends BaseActivity {
    @BindView(R.id.tv_ordernum)
    TextView tvOrdernum;
    @BindView(R.id.tv_paycount)
    TextView tvPaycount;
    @BindView(R.id.tv_datetishi)
    TextView tvDatetishi;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_cashcount)
    TextView tvCashcount;
    @BindView(R.id.tv_price)
    TextView tvPrice;

    @Override
    public int getContentView() {
        return R.layout.activity_silvertransferdetail;
    }

    private int type;
    private String id;
    private BaseSubscriber<BaseBean<SilverTransferDetailBean>> silverTransferDetailSubscriber;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Loading);
        titleBar.setTitleText("转账详情");
        type = getIntent().getIntExtra("type", 1);
        id = getIntent().getStringExtra("id");

    }

    @Override
    public void getData() {
        silverTransferDetailSubscriber = new BaseSubscriber<BaseBean<SilverTransferDetailBean>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<SilverTransferDetailBean> silverTransferDetailBeanBaseBean) {
                super.onNext(silverTransferDetailBeanBaseBean);
                if (silverTransferDetailBeanBaseBean.code == 0) {
                    SilverTransferDetailBean silverTransferDetailBean = silverTransferDetailBeanBaseBean.data;
                    if (silverTransferDetailBean.getType() == 1) {
                        tvPaycount.setText(silverTransferDetailBean.getTransfer_mob());
                        tvDatetishi.setText("转出时间:");
                        tvType.setText("转出");
                        tvPrice.setText(silverTransferDetailBean.getTransfer_money());
                    } else if (silverTransferDetailBean.getType() == 2) {
                        tvPaycount.setText(silverTransferDetailBean.getRace_mob());
                        tvDatetishi.setText("转入时间:");
                        tvType.setText("转入");
                        tvPrice.setText(silverTransferDetailBean.getTransfer_money());
                    }
                    tvDate.setText(DateUtil.toDate(silverTransferDetailBean.getCreate_time(), DateUtil.FORMAT_YMDHM));
                    tvOrdernum.setText(silverTransferDetailBean.getOrder_id());
                    tvCashcount.setText(silverTransferDetailBean.getTransfer_money());

                }else if (silverTransferDetailBeanBaseBean.code==4){
                    ToolUtil.loseToLogin(SilverTransferDetialActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).silverTransferDetail(type, id, new PreferencesHelper(SilverTransferDetialActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(silverTransferDetailSubscriber);

    }

    public static void startActivityResult(Context context, int type, String id) {
        Intent intent = new Intent(context, SilverTransferDetialActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (silverTransferDetailSubscriber != null && silverTransferDetailSubscriber.isUnsubscribed()) {
            silverTransferDetailSubscriber.unsubscribe();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
