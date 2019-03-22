package com.ruirong.chefang.personalcenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.widget.WheelView;
import com.ruirong.chefang.widget.wheelview.MyMonthWheelAdapter;
import com.ruirong.chefang.widget.wheelview.OnWheelScrollListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;

/**
 * Created by chenlipeng on 2018/1/11 0011
 * describe:  消费记录的时间选择
 */
public class ConsumeRecordChooseTimeActivity extends BaseActivity {

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_start_line)
    ImageView tvStartLine;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_end_line)
    ImageView tvEndLine;
    @BindView(R.id.ll_wheelview)
    LinearLayout llWheelview;
    //    @BindView(R.id.ll_month_year_wheelview)
//    com.ruirong.chefang.widget.wheelview.WheelView llMonthYearWheelview;
//    @BindView(R.id.ll_month_month_wheelview)
//    com.ruirong.chefang.widget.wheelview.WheelView llMonthMonthWheelview;
//    @BindView(R.id.ll_month_day_wheelview)
//    WheelView llMonthDayWheelview;
    private int currentPosition = 0;
    private int time = 0;//返回上一层的数据
    private String startStr = "";
    private String endStr = "";

    private static final String[] PLANETS = new String[]{"赵云", "关羽", "张飞",
            "吕布", "典韦", "马超", "黄忠", "诸葛亮"};
    private int curMonth;
    private int curYear;

    @Override
    public int getContentView() {
        return R.layout.activity_consume_record_choose_time;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("选择时间");
        titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.setRightTextRes("完成");
        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(startStr)) {
                    ToastUtil.showToast(ConsumeRecordChooseTimeActivity.this, "请选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(endStr)) {
                    ToastUtil.showToast(ConsumeRecordChooseTimeActivity.this, "请选择结束时间");
                    return;
                }

                if (TimeUtil.isDateOneBigger(endStr, startStr)) {
                    Log.i("XXX", TimeUtil.dateToStamp(startStr));
                    Log.i("XXX", TimeUtil.dateToStamp(endStr));

                    Intent intent = new Intent();
                    intent.putExtra("expense_time", startStr + "," + endStr);
                    setResult(2, intent);
                    finish();

                } else {
                    ToastUtil.showToast(ConsumeRecordChooseTimeActivity.this, "结束时间不可小于开始时间");
                }

            }
        });

        Calendar c = Calendar.getInstance();
        //当前年份
        curYear = c.get(Calendar.YEAR);
        //通过Calendar算出的月数要+1
        curMonth = c.get(Calendar.MONTH) + 1;

        final DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH);
        picker.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        picker.setWidth((int) (picker.getScreenWidthPixels() * 1));
        picker.setRangeStart(2016, 1, 14);
        picker.setRangeEnd(curYear, curMonth, 11);
        picker.setSelectedItem(curYear, curMonth);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String chooseYear, String month) {

//                showToast(year + "-" + month);
                if (currentPosition == 0) {
                    tvStartTime.setText(chooseYear + "年" + month + "月");
                    startStr = chooseYear + "-" + month + "-1";
                } else {
                    tvEndTime.setText(chooseYear + "年" + month + "月");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date supportEndDayofMonth = TimeUtil.getSupportEndDayofMonth(Integer.parseInt(chooseYear), Integer.parseInt(month));
                    endStr = dateFormat.format(supportEndDayofMonth);
                }

            }
        });
        picker.setOnWheelListener(new DateTimePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String chooseYear) {
                String month = picker.getSelectedMonth();
                if (currentPosition == 0) {
                    tvStartTime.setText(chooseYear + "年" + month + "月");
                    startStr = chooseYear + "-" + month + "-1";
                } else {
                    tvEndTime.setText(chooseYear + "年" + month + "月");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date supportEndDayofMonth = TimeUtil.getSupportEndDayofMonth(Integer.parseInt(chooseYear), Integer.parseInt(month));
                    endStr = dateFormat.format(supportEndDayofMonth);
                }
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                String chooseYear = picker.getSelectedYear();
                if (currentPosition == 0) {
                    tvStartTime.setText(chooseYear + "年" + month + "月");
                    startStr = chooseYear + "-" + month + "-1";
                } else {
                    tvEndTime.setText(chooseYear + "年" + month + "月");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date supportEndDayofMonth = TimeUtil.getSupportEndDayofMonth(Integer.parseInt(chooseYear), Integer.parseInt(month));
                    endStr = dateFormat.format(supportEndDayofMonth);
                }
            }

            @Override
            public void onDayWheeled(int index, String day) {

            }

            @Override
            public void onHourWheeled(int index, String hour) {

            }

            @Override
            public void onMinuteWheeled(int index, String minute) {

            }
        });


//        picker.show();

        //得到选择器视图，可内嵌到其他视图容器，不需要调用show方法
        View pickerContentView = picker.getContentView();
        llWheelview.addView(pickerContentView);

        // wv.setOffset(0);// 偏移量
//        llMonthDayWheelview.setOffset(2);
//        llMonthDayWheelview.setItems(Arrays.asList(PLANETS));// 实际内容
//        llMonthDayWheelview.setSeletion(0);// 设置默认被选中的项目
//        // wv.setSeletion(3);
//        llMonthDayWheelview.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                // 选中后的处理事件
//                Log.d("XXX", "[Dialog]selectedIndex: " + selectedIndex
//                        + ", item: " + item);
//            }
//        });
//
//        llMonthYearWheelview.setAdapter(new MyMonthWheelAdapter(2016, curYear));
//        llMonthYearWheelview.setLabel("");
//        llMonthYearWheelview.setCyclic(true);
//        llMonthYearWheelview.setVisibleItems(3);
//        llMonthYearWheelview.addScrollingListener(new OnWheelScrollListener() {
//
//
//            @Override
//            public void onScrollingStarted(com.ruirong.chefang.widget.wheelview.WheelView wheel) {
//
//            }
//
//            @Override
//            public void onScrollingFinished(com.ruirong.chefang.widget.wheelview.WheelView wheel) {
//                if (curYear == Integer.parseInt(llMonthYearWheelview.getAdapter().getItem(llMonthYearWheelview.getCurrentItem()))) {
//                    llMonthMonthWheelview.setAdapter(new MyMonthWheelAdapter(1, curMonth));
//                } else {
//                    llMonthMonthWheelview.setAdapter(new MyMonthWheelAdapter(1, 12));
//                }
//                String chooseYear = llMonthYearWheelview.getAdapter().getItem(llMonthYearWheelview.getCurrentItem()) + "";
//                String chooseMonth = llMonthMonthWheelview.getCurrentItem() + 1 + "";
//
//                if (currentPosition == 0) {
//                    tvStartTime.setText(chooseYear + "年" + chooseMonth + "月");
//                    startStr = chooseYear + "-" + chooseMonth + "-1";
//                } else {
//                    tvEndTime.setText(chooseYear + "年" + chooseMonth + "月");
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    Date supportEndDayofMonth = TimeUtil.getSupportEndDayofMonth(Integer.parseInt(chooseYear), Integer.parseInt(chooseMonth));
//                    endStr = dateFormat.format(supportEndDayofMonth);
//                }
//
//            }
//        });
//
//
//        llMonthMonthWheelview.setAdapter(new MyMonthWheelAdapter(1, 12));
//        llMonthMonthWheelview.setLabel("");
//        llMonthMonthWheelview.setCyclic(true);
//        llMonthMonthWheelview.setVisibleItems(3);
//        llMonthMonthWheelview.addScrollingListener(new OnWheelScrollListener() {
//            @Override
//            public void onScrollingStarted(com.ruirong.chefang.widget.wheelview.WheelView wheel) {
//
//            }
//
//            @Override
//            public void onScrollingFinished(com.ruirong.chefang.widget.wheelview.WheelView wheel) {
//                String chooseYear = llMonthYearWheelview.getAdapter().getItem(llMonthYearWheelview.getCurrentItem()) + "";
//                String chooseMonth = llMonthMonthWheelview.getCurrentItem() + 1 + "";
//
//                if (currentPosition == 0) {
//                    tvStartTime.setText(chooseYear + "年" + chooseMonth + "月");
//                    startStr = chooseYear + "-" + chooseMonth + "-1";
//                } else {
//                    tvEndTime.setText(chooseYear + "年" + chooseMonth + "月");
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    Date supportEndDayofMonth = TimeUtil.getSupportEndDayofMonth(Integer.parseInt(chooseYear), Integer.parseInt(chooseMonth));
//                    endStr = dateFormat.format(supportEndDayofMonth);
//                }
//
//            }
//        });


    }


    @OnClick({R.id.tv_start_time, R.id.tv_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                tvStartTime.setTextColor(Color.parseColor("#1390e4"));
                tvEndTime.setTextColor(Color.parseColor("#333333"));
                tvStartLine.setBackgroundColor(Color.parseColor("#1390e4"));
                tvEndLine.setBackgroundColor(Color.parseColor("#cccccc"));

                currentPosition = 0;

                break;
            case R.id.tv_end_time:

                tvEndTime.setTextColor(Color.parseColor("#1390e4"));
                tvStartTime.setTextColor(Color.parseColor("#333333"));
                tvEndLine.setBackgroundColor(Color.parseColor("#1390e4"));
                tvStartLine.setBackgroundColor(Color.parseColor("#cccccc"));

                currentPosition = 1;

                break;

        }


    }

    @Override
    public void getData() {


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
