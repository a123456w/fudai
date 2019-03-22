package com.ruirong.chefang.personalcenter;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.LogisticsListviewAdapter1;
import com.ruirong.chefang.bean.LogisticsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenlipeng on 2017/12/27 0027
 * describe:  物流详情
 */
public class LogisticsDetailsActivity extends BaseActivity

{


    @BindView(R.id.logistics_tv_courier)
    TextView logisticsTvCourier;
    @BindView(R.id.nslv_exposure)
    NoScrollListView nslvExposure;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    private LogisticsListviewAdapter1 lightListHouseAdapter;
    private List<LogisticsBean> listData=new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_logistics_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitleText("物流详情");




    }

    @Override
    public void getData() {
        for (int i = 0; i < 15; i++) {
            LogisticsBean bean = new LogisticsBean();
            listData.add(bean);
        }
//        lightListHouseAdapter.setData(listData);

        //        最下面的listview的
        lightListHouseAdapter = new LogisticsListviewAdapter1(LogisticsDetailsActivity.this,listData);
        nslvExposure.setFocusable(false);
        nslvExposure.setAdapter(lightListHouseAdapter);
        nslvExposure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                CarAndRoomsBean lightHouseBeans = (CarAndRoomsBean) lightListHouseAdapter.getItem(i);
//                LightHouseDetialActivity.startActivityWithParameter(getActivity(), lightHouseBeans.getId(), choseType);

                Toast.makeText(getApplication(), "下面listview的点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @OnClick({R.id.rl_bankcard_image})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//
//            case R.id.rl_bankcard_image:
//                Intent intent = new Intent(LookExpressageActivity.this, BankCardManagerActivity.class);
//                intent.putExtra("int_data", 100);
//                startActivity(intent);
//                break;
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
