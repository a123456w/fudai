package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.SelectPeopleNumAdapter;
import com.ruirong.chefang.bean.PeopleBean;
import com.ruirong.chefang.http.RemoteApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.bigkoo.pickerview.view.OptionsPickerView;

/**
 * 店外预订
 * Created by dillon on 2018/5/5.
 */

public class OutsideOrderActivity extends BaseActivity {


    private static final String OUTSIDEORDERSHOPID = "OUTSIDEORDERSHOPID";
    @BindView(R.id.et_call)
    EditText etCall;
    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.ry_recommendpeoplenum)
    RecyclerView ryRecommendPeopleNum;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.sp_time)
    TextView spTime;
    @BindView(R.id.ll_time_choose)
    LinearLayout llTimeChoose;
    private String myShopid = "";

    private String eatTime = "";

    private List<String> list = new ArrayList<>();


    public SelectPeopleNumAdapter adapter;
    public List<PeopleBean> baseList = new ArrayList<>();

    private String chosePeopleNum = "";

    @Override
    public int getContentView() {
        return R.layout.activity_outsideorder;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("店外预约点餐");
        spTime.getPaint().setFakeBoldText(true);

        myShopid = getIntent().getStringExtra(OUTSIDEORDERSHOPID);

        setData();

        ryRecommendPeopleNum.setLayoutManager(new GridLayoutManager(OutsideOrderActivity.this, 4));
        adapter = new SelectPeopleNumAdapter(ryRecommendPeopleNum);
        ryRecommendPeopleNum.setAdapter(adapter);

        adapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                adapter.selectedPosition = position;
                adapter.notifyDataSetChanged();
                chosePeopleNum = adapter.getItem(adapter.selectedPosition).getName();

            }
        });

    }

    @Override
    public void getData() {
        getRecomendPeopleList(myShopid);
    }

    @OnClick(R.id.ll_time_choose)
    public void ll_time_choose() {
        getNoLinkData();
    }
    /**
     * 选择时间
     */
    private void getNoLinkData() {

        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        int time = Integer.parseInt(formatter.format(curDate));
        SimpleDateFormat timeformatter = new SimpleDateFormat("mm");
        Date timecurDate = new Date(System.currentTimeMillis());//获取当前时间
        int branch = Integer.parseInt(timeformatter.format(timecurDate));

        int currentBranch = 0;
        int currentTime = time;
        currentBranch = branch;

        final ArrayList<String> daysArr = new ArrayList<>();
        final ArrayList<ArrayList<String>> daysArr_AreaList = new ArrayList<>();

        daysArr.clear();
        daysArr_AreaList.clear();

        for (int j = 0; j < 24; j++) {

            if (0 == 0) {
                if (j > currentTime - 1) {
                    if (j < 10) {
                        daysArr.add("0" + j + "时");
                    } else {
                        daysArr.add(j + "时");
                    }

                    ArrayList<String> minuteArr = new ArrayList<>();
                    minuteArr.clear();
                    for (int k = 0; k < 60; k++) {
                        if (j == currentTime) {
                            if (k >= currentBranch) {
                                if (k < 10) {
                                    minuteArr.add("0" + k + "分");
                                } else {
                                    minuteArr.add(k + "分");
                                }
                            }
                        } else {

                            if (k < 10) {
                                minuteArr.add("0" + k + "分");
                            } else {
                                minuteArr.add(k + "分");
                            }


                        }
                    }

                    daysArr_AreaList.add(minuteArr);

                }
            }


        }


        OptionsPickerView  pvNoLinkOptions =
                new OptionsPickerBuilder(OutsideOrderActivity.this,
                        new OnOptionsSelectListener () {
                            @Override
                            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                                //时间回调
                                String str = daysArr.get(options1)
                                        + " " + daysArr_AreaList.get(options1).get(options2);


                                spTime.setText(str);
                                eatTime = str ;


                            }
                        }

                )
                .setCyclic(true, true, true)
                .setTitleText("时间选择")
                .setContentTextSize(20)
                .setTextColorCenter(Color.parseColor("#FF0000")) //设置选中项文字颜色
                .build();
        pvNoLinkOptions.setPicker(daysArr, daysArr_AreaList, null);
        pvNoLinkOptions.show();




//        //条件选择器
//        OptionsPickerView pvOptions = new OptionsPickerBuilder(ReservationsActivity.this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                String str = days.get(options1).getTitle_time()
//                        + " " + hour.get(options1).get(option2).getTitle_time()
//                        + "" + minute.get(options1).get(option2).get(options3).getTitle_time();
//                etTime.setText(str);
//                String strc = days.get(options1).getTime()
//                        + " " + hour.get(options1).get(option2).getTime()
//                        + "-" + minute.get(options1).get(option2).get(options3).getTime();
//                times = strc;
//            }
//        }).setCyclic(true, true, true)
//                .setTitleText("时间选择")
//                .setContentTextSize(20)
//                .setTextColorCenter(Color.parseColor("#FF0000")) //设置选中项文字颜色
//                .build();
//        pvOptions.setPicker(days, hour, minute);
//        pvOptions.show();
    }

    /**
     * 获取数据
     */
    public void setData() {

    }

    /**
     * 获取就餐人数
     *
     * @param shopid
     */
    public void getRecomendPeopleList(String shopid) {
        HttpHelp.getInstance().create(RemoteApi.class).getPeopleList(shopid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<PeopleBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<PeopleBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            baseList = baseBean.data;
                            if (baseList.size()>0){
                                adapter.setData(baseList);
                                chosePeopleNum = adapter.getItem(0).getName();
                                adapter.selectedPosition = 0;
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }


    /**
     * 获取店铺id
     *
     * @param context
     * @param shopid
     */
    public static void startActivityWithParmeter(Context context, String shopid) {
        Intent intent = new Intent(context, OutsideOrderActivity.class);
        intent.putExtra(OUTSIDEORDERSHOPID, shopid);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etCall.getText().toString().trim())) {
            ToastUtil.showToast(OutsideOrderActivity.this, "称呼不能为空");
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtil.showToast(OutsideOrderActivity.this, "电话号不能为空");
            return;
        }
        if (StringUtil.isValidPhone(etPhone.getText().toString().trim())) {
            ToastUtil.showToast(OutsideOrderActivity.this, "电话号格式不对");
            return;
        }

        if ("点我选择时间".equals(spTime.getText().toString().trim())){
            ToastUtil.showToast(OutsideOrderActivity.this, "请选择点餐时间");
            return;
        }
        AllProductActivity.startActivityWithParmeter(OutsideOrderActivity.this, etCall.getText().toString().trim(), etPhone.getText().toString().trim(), eatTime, chosePeopleNum,myShopid,"1");

    }

}
