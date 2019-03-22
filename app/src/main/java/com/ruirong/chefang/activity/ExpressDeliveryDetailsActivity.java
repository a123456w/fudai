package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ExpressDeliveryDetailsAdapter;
import com.ruirong.chefang.bean.Showlogistics;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/26.
 * 快递
 */

public class ExpressDeliveryDetailsActivity extends BaseActivity {
    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    @BindView(R.id.order_state)
    TextView orderState;
    @BindView(R.id.order_source)
    TextView orderSource;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.place_list)
    NoScrollListView placeList;
    private String number_bh;
    private ExpressDeliveryDetailsAdapter adapter;
    private List<Showlogistics.TracesBean> list = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_express_delivery;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("物流详情");

        number_bh = getIntent().getStringExtra("number_bh");
        Log.i("XXX", number_bh);
        if (number_bh == null) {
            finish();
        }

        adapter = new ExpressDeliveryDetailsAdapter(placeList);
        placeList.setAdapter(adapter);

    }

    @Override
    public void getData() {
        showlogistics(new PreferencesHelper(ExpressDeliveryDetailsActivity.this).getToken(), number_bh);
    }

    /**
     * \
     * 快递信息
     *
     * @param token
     * @param number_bh
     */
    private void showlogistics(String token, String number_bh) {
        HttpHelp.getInstance().create(RemoteApi.class).showlogistics(token, number_bh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Showlogistics>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Showlogistics> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                GlideUtil.display(ExpressDeliveryDetailsActivity.this,
                                        Constants.IMG_HOST + baseBean.data.getPic(), goodsPic);
                                orderSource.setText("承接来源:" + baseBean.data.getExpressname());
                                orderNumber.setText("订单编号:" + baseBean.data.getLogisticCode());
                                phone.setText("官方电话:" + baseBean.data.getExpressphone());
                                if ("1".equals(baseBean.data.getState())) {

                                    orderState.setText("已揽收");

                                } else if ("2".equals(baseBean.data.getState())) {

                                    orderState.setText("在途中");

                                } else if ("3".equals(baseBean.data.getState())) {

                                    orderState.setText("签收");

                                } else if ("4".equals(baseBean.data.getState())) {

                                    orderState.setText("问题件");

                                } else if ("0".equals(baseBean.data.getState())) {

                                    orderState.setText("无轨迹");

                                }
                                if (baseBean.data.getTraces() != null && baseBean.data.getTraces().size() > 0) {
                                    Collections.reverse(baseBean.data.getTraces());
                                    adapter.setData(baseBean.data.getTraces());
                                }
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(ExpressDeliveryDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

}
