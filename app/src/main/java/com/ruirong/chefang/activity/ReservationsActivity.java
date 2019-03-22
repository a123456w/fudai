package com.ruirong.chefang.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ObjectBean;
import com.ruirong.chefang.bean.ReservationsBean;
import com.ruirong.chefang.bean.TimesBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 预约看房/车
 * Created by BX on 2017/12/27.
 */

public class ReservationsActivity extends BaseActivity {
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rb_grade)
    RatingBar rbGrade;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_junjia)
    TextView tvJunjia;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_time)
    TextView etTime;
    @BindView(R.id.tv_loucheng)
    TextView tvLoucheng;
    private List<ReservationsBean> lists = new ArrayList<>();
    private String times, shop_id;

    private ArrayList<TimesBean> days = new ArrayList<>();
    private ArrayList<ArrayList<TimesBean>> hour = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<TimesBean>>> minute = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_reservations;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("TitleName");
        titleBar.setTitleText("预 约");


    }

    @Override
    public void getData() {
        Intent it = getIntent();
        shop_id = it.getStringExtra("id");
        if (shop_id != null) {
            getListData();
        }

        Log.i("XXX", shop_id);

    }

    /**
     * 预约看房
     */
    private void getListData() {
        HttpHelp.getInstance().create(RemoteApi.class).Propertyshowings(shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReservationsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ReservationsBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            tvTitle.setText(listBaseBean.data.getSp_name());
                            if (listBaseBean.data.getDp_grade() != null) {
                                rbGrade.setRating(Float.parseFloat(listBaseBean.data.getDp_grade()));
                                tvScore.setText(listBaseBean.data.getDp_grade() + "分");
                            }
                            tvAddress.setText(listBaseBean.data.getSp_address());
                            GlideUtil.display(ReservationsActivity.this, Constants.IMG_HOST + listBaseBean.data.getPic(), ivPic);
                            if (listBaseBean.data.getKey() != null) {
                                if (listBaseBean.data.getKey().size() == 1) {
                                    tvJunjia.setText(listBaseBean.data.getKey().get(0).getKey() + ":" + listBaseBean.data.getKey().get(0).getValue());
                                }
                                if (listBaseBean.data.getKey().size() == 2) {
                                    tvJunjia.setText(listBaseBean.data.getKey().get(1).getKey() + ":" + listBaseBean.data.getKey().get(1).getValue());
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    @OnClick({R.id.tv_submit, R.id.et_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    ToastUtil.showToast(this, "姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etMobile.getText().toString().trim())) {
                    ToastUtil.showToast(this, "电话号输入有误!");
                    return;
                }

                if (etMobile.getText().toString().trim().length() != 11) {
                    ToastUtil.showToast(ReservationsActivity.this, "请输入正确的电话号码！！！");
                    return;
                }

                if (TextUtils.isEmpty(times)) {
                    ToastUtil.showToast(this, "时间不能为空");
                    return;
                }

                Log.i("XXX", times);
                Log.i("XXX", TimeUtil.dataOne(times + "-00"));

                reservations(new PreferencesHelper(ReservationsActivity.this).getToken(),
                        shop_id, etName.getText().toString().trim(),
                        etMobile.getText().toString().trim(), TimeUtil.dataOne(times + "-00"));

                break;
            case R.id.et_time:
                getNoLinkData();
                break;
        }
    }

    /**
     * 预约
     *
     * @param token
     * @param id
     * @param name
     * @param mobile
     * @param time
     */
    private void reservations(String token, String id, String name, String mobile, String time) {
        HttpHelp.getInstance().create(RemoteApi.class).Propertymakean(token, id, name, mobile, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ObjectBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ObjectBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ReservationsActivity.this);
                        }
                        ToastUtil.showToast(ReservationsActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.d("xxx", "onError: " + throwable.getLocalizedMessage());
                    }
                });
    }


    /**
     * 选择时间
     */
    private void getNoLinkData() {
        int mDays = 0;
        boolean isLarge = false;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        simpleDateFormat.format(date);
        //获取当前月份最大天数
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day = aCalendar.getActualMaximum(Calendar.DATE);


        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        for (int i = 0; i < 7; i++) {
            TimesBean timesBean = new TimesBean();

            String oldDate = TimeUtil.getOldDate(i);
            // 解析格式，yyyy表示年，MM(大写M)表示月,dd表示天，HH表示小时24小时制，小写的话是12小时制
            // mm，小写，表示分钟，ss表示秒
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                // 用parse方法，可能会异常，所以要try-catch
                Date datec = format.parse(oldDate);
                // 获取日期实例
                Calendar c = Calendar.getInstance();
                // 将日历设置为指定的时间
                c.setTime(datec);
                // 获取年
                int mYear = c.get(Calendar.YEAR);
                // 这里要注意，月份是从0开始。
                int mMonth = c.get(Calendar.MONTH) + 1;
                // 获取天
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                String mcurrentMonth = "";
                if (mMonth < 10) {
                    mcurrentMonth = "0" + mMonth;
                } else {
                    mcurrentMonth = mMonth + "";
                }
                String mcurrentDay = "";
                if (mDay < 10) {
                    mcurrentDay = "0" + mDay;
                } else {
                    mcurrentDay = mDay + "";
                }
                timesBean.setTime(mYear + "-" + mcurrentMonth + "-" + mcurrentDay);
                timesBean.setTitle_time(mcurrentMonth + "月" + mcurrentDay + "日");

            } catch (Exception e) {
                e.printStackTrace();
            }

            days.add(timesBean);
        }


        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        int time = Integer.parseInt(formatter.format(curDate));
        SimpleDateFormat timeformatter = new SimpleDateFormat("mm");
        Date timecurDate = new Date(System.currentTimeMillis());//获取当前时间
        int branch = Integer.parseInt(timeformatter.format(timecurDate));

        for (int i = 0; i < days.size(); i++) {

            ArrayList<TimesBean> daysArr = new ArrayList<>();
            ArrayList<ArrayList<TimesBean>> daysArr_AreaList = new ArrayList<>();

            for (int j = 0; j < 24; j++) {
                TimesBean timesBean = new TimesBean();
                ArrayList<TimesBean> minuteArr = new ArrayList<>();
                if (i == 0) {
                    if (j > time) {

                        if (j < 10) {
                            timesBean.setTitle_time("0" + j + "时");
                            timesBean.setTime("0" + j + "");
                            daysArr.add(timesBean);
                        } else {
                            timesBean.setTitle_time(j + "时");
                            timesBean.setTime(j + "");
                            daysArr.add(timesBean);
                        }

//                        timesBean.setTitle_time(j + "时");
//                        timesBean.setTime(j + "");
//                        daysArr.add(timesBean);
                    }

                } else {

                    if (j < 10) {
                        timesBean.setTitle_time("0" + j + "时");
                        timesBean.setTime("0" + j + "");
                        daysArr.add(timesBean);
                    } else {
                        timesBean.setTitle_time(j + "时");
                        timesBean.setTime(j + "");
                        daysArr.add(timesBean);
                    }

//                    timesBean.setTitle_time(j + "时");
//                    timesBean.setTime(j + "");
//                    daysArr.add(timesBean);
                }

                for (int k = 0; k < 60; k++) {
                    TimesBean timesBeans = new TimesBean();
                    if (i == 0 && j == 0) {
                        if (k > branch) {
                            if (k < 10) {
                                timesBeans.setTitle_time("0" + k + "分");
                                timesBeans.setTime("0" + k + "");
                                minuteArr.add(timesBeans);
                            } else {
                                timesBeans.setTitle_time(k + "分");
                                timesBeans.setTime(k + "");
                                minuteArr.add(timesBeans);
                            }

//                            timesBeans.setTitle_time(k + "分");
//                            timesBeans.setTime(k + "");
//                            minuteArr.add(timesBeans);
                        }
                    } else {

                        if (k < 10) {
                            timesBeans.setTitle_time("0" + k + "分");
                            timesBeans.setTime("0" + k + "");
                            minuteArr.add(timesBeans);
                        } else {
                            timesBeans.setTitle_time(k + "分");
                            timesBeans.setTime(k + "");
                            minuteArr.add(timesBeans);
                        }
//                        timesBeans.setTitle_time(k + "分");
//                        timesBeans.setTime(k + "");
//                        minuteArr.add(timesBeans);
                    }
                }

                daysArr_AreaList.add(minuteArr);

            }
            hour.add(daysArr);

            minute.add(daysArr_AreaList);
        }


        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(ReservationsActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String str = days.get(options1).getTitle_time()
                        + " " + hour.get(options1).get(option2).getTitle_time()
                        + "" + minute.get(options1).get(option2).get(options3).getTitle_time();
                etTime.setText(str);
                String strc = days.get(options1).getTime()
                        + " " + hour.get(options1).get(option2).getTime()
                        + "-" + minute.get(options1).get(option2).get(options3).getTime();
                times = strc;
            }
        }).setCyclic(true, true, true)
                .setTitleText("时间选择")
                .setContentTextSize(20)
                .setTextColorCenter(Color.parseColor("#FF0000")) //设置选中项文字颜色
                .build();
        pvOptions.setPicker(days, hour, minute);
        pvOptions.show();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
