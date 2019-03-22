package com.ruirong.chefang.personalcenter;

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
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 充值详情
 */
public class RechargeDetailActivity extends BaseActivity {
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
    private String type = "3";
    private String id;
    private OrderDetailBean data;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_goldbean_business_details);
//    }


    @Override
    public int getContentView() {
        return R.layout.activity_rechargedetail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        id = getIntent().getStringExtra(Constants.ORDERID);
        type = getIntent().getStringExtra("type");
        if ("3".equals(type)){
            titleBar.setTitleText("充值详情");
        }else if ("4".equals(type)){
            titleBar.setTitleText("提现详情");
        }

    }

    @Override
    public void getData() {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).orderDetail(type, id, new PreferencesHelper(this).getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<OrderDetailBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<OrderDetailBean> orderDetailBeanBaseBean) {
                        super.onNext(orderDetailBeanBaseBean);
                        if (orderDetailBeanBaseBean.code == 0) {
                            data = orderDetailBeanBaseBean.data;
                            initdata(data.getCreate_time(), data.getTypeName(), data.getMoney(), data.getOrder_id(), data.getStatus());
                        }else if (orderDetailBeanBaseBean.code==4){
                            ToolUtil.loseToLogin(RechargeDetailActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(RechargeDetailActivity.this, "获取数据失败");
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

//          @OnClick({R.id.tv_cancle})
//          public void onViewClicked(View view) {
//              switch (view.getId()) {
//                  case R.id.tv_cancle:
//                      finish();
//                      break;
//
//              }
//          }

}
