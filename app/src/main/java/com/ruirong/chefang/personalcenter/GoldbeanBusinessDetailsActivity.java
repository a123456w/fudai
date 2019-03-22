package com.ruirong.chefang.personalcenter;

import android.os.Bundle;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.OrderDetailBean;
import com.ruirong.chefang.bean.WithDrawBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/11 0011
 * describe:  金豆交易订单详情
 */
public class GoldbeanBusinessDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_goldbean_number)
    TextView tvGoldbeanNumber;
    @BindView(R.id.tv_goldbean_time)
    TextView tvGoldbeanTime;
    @BindView(R.id.tv_goldbean_type)
    TextView tvGoldbeanType;
    @BindView(R.id.tv_goldbean_money)
    TextView tvGoldbeanMemory;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_goldbean_shouxumoney)
    TextView tvGoldbeanShouxuMoney;
    @BindView(R.id.tv_goldbean_shijimoney)
    TextView getTvGoldbeanShijiMoney;

    private String type = "3";
    private String id;
    private OrderDetailBean data;
    private WithDrawBean withDrawBean;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_goldbean_business_details);
//    }


    @Override
    public int getContentView() {
        return R.layout.activity_goldbean_business_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        id = getIntent().getStringExtra(Constants.ORDERID);
        type = getIntent().getStringExtra("type");
        if ("4".equals(type)) {
            titleBar.setTitleText("提现详情");
        }

    }

    @Override
    public void getData() {

        if ("4".equals(type)) {
            withDraw();
        }

    }

    /**
     * 充值记录详情
     */
    public void rechargeMoneyDetail() {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).orderDetail(type, id, new PreferencesHelper(this).getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<OrderDetailBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<OrderDetailBean> orderDetailBeanBaseBean) {
                        super.onNext(orderDetailBeanBaseBean);
                        if (orderDetailBeanBaseBean.code == 0) {
                            data = orderDetailBeanBaseBean.data;
                            initdata(data.getCreate_time(), data.getTypeName(), data.getMoney(), data.getOrder_id(), data.getStatus());
                        } else if (orderDetailBeanBaseBean.code == 4) {
                            ToolUtil.loseToLogin(GoldbeanBusinessDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(GoldbeanBusinessDetailsActivity.this, "获取数据失败");
                    }
                });

    }

    /**
     * 提现详情
     *
     * @param
     * @param
     */
    public void withDraw() {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).withDrawDetail(id, new PreferencesHelper(this).getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<WithDrawBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<WithDrawBean> orderDetailBeanBaseBean) {
                        super.onNext(orderDetailBeanBaseBean);
                        if (orderDetailBeanBaseBean.code == 0) {
                            withDrawBean = orderDetailBeanBaseBean.data;
                            if (withDrawBean != null) {
                                initWithdrawdata(withDrawBean.getCreate_time(), withDrawBean.getTypename(), withDrawBean.getMoney(), withDrawBean.getOrder_id(), withDrawBean.getStatus(), withDrawBean.getShouxufei(), withDrawBean.getFinal_money());
                            }
                        } else if (orderDetailBeanBaseBean.code == 4) {
                            ToolUtil.loseToLogin(GoldbeanBusinessDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(GoldbeanBusinessDetailsActivity.this, "获取数据失败");
                    }
                });
    }

    private void initdata(String create_time, String typeName, String money, String order_id, String status) {
        tvGoldbeanNumber.setText(order_id);
        tvGoldbeanTime.setText(DateUtil.toDate(create_time, DateUtil.FORMAT_YMDHMS));
        tvGoldbeanType.setText(typeName);
        tvGoldbeanMemory.setText(money);
        tvPrice.setText(money);
        tvState.setText(status);
    }

    /**
     * 提现详情
     *
     * @param create_time
     * @param typeName
     * @param money
     * @param order_id
     * @param status
     */
    private void initWithdrawdata(String create_time, String typeName, String money, String order_id, String status, String shouxufei, String shijimoney) {
        tvGoldbeanNumber.setText(order_id);
        tvGoldbeanTime.setText(DateUtil.toDate(create_time, DateUtil.FORMAT_YMDHMS));
        tvGoldbeanType.setText(typeName);
        tvGoldbeanMemory.setText(money);
        tvPrice.setText(money);
        tvState.setText(status);
        tvGoldbeanShouxuMoney.setText(shouxufei);
        getTvGoldbeanShijiMoney.setText(shijimoney);
    }

}