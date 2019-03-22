package com.ruirong.chefang.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.widget.MyCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenlipeng on 2018/1/4 0004
 * describe:  入住/离店
 */
public class EnterAndLeaveHotelActivity extends BaseActivity implements MyCalendar.OnDaySelectListener {
    @BindView(R.id.week_indcation)
    LinearLayout weekIndcation;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.osv_my_scrollview)
    ScrollView osvMyScrollview;
    private int mYear;


    @Override
    public int getContentView() {
        return R.layout.activity_enter_and_leave_hotel;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("入住/离店");

        Calendar c = Calendar.getInstance();//首先要获取日历对象
        mYear = c.get(Calendar.YEAR); // 获取当前年份

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void getData() {

        new DownTask().execute("dddd");
        return;

//        List<String> listDate = new ArrayList<>();
//
//        for (int i = 2018; i <= 2018; i++) {
//            for (int j = 1; j < 4; j++) {
//                listDate.add(i + "-" + j + "-" + getDaysByYearMonth(i, j));
//            }
//        }
//
//        myListDate.clear();
//        myListDate.addAll(listDate);
//
//        for (int i = 0; i < 10; i++) {
//            c1 = new MyCalendar(getApplication());
//            c1.setLayoutParams(params);
//            Date date = null;
//            try {
//                date = simpleDateFormat.parse(listDate.get(i));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (!"".equals(sp_inday)) {
//                c1.setInDay(sp_inday);
//            }
//            if (!"".equals(sp_outday)) {
//                c1.setOutDay(sp_outday);
//            }
//            c1.setTheDay(date);
//            c1.setOnDaySelectListener(EnterAndLeaveHotelActivity.this);
//            ll.addView(c1);
//        }
    }

    @Override
    public void onDaySelectListener(View view, String date) {

        String dateDay = date.split("-")[2];
        if (Integer.parseInt(dateDay) < 10) {
            dateDay = date.split("-")[2].replace("0", "");
        }
        TextView textDayView = (TextView) view.findViewById(R.id.tv_calendar_day);
//        TextView textView = (TextView) view.findViewById(R.id.tv_calendar);
        view.setBackgroundColor(Color.parseColor("#33B5E5"));
        textDayView.setTextColor(Color.WHITE);
        if (null == inday || inday.equals("")) {
            textDayView.setText(dateDay);
            inday = date;

//            Intent intent = new Intent();
//            intent.putExtra("inday", inday);
//            setResult(RESULT_OK, intent);
//
//            finish();
        } else {
            if (inday.equals(date)) {

                view.setBackgroundColor(Color.WHITE);
                textDayView.setText(dateDay);
                textDayView.setTextColor(getResources().getColor(R.color.color_default));
                inday = "";
            } else {
                try {
                    if (simpleDateFormat.parse(date).getTime() < simpleDateFormat.parse(inday).getTime()) {
                        view.setBackgroundColor(Color.WHITE);
                        textDayView.setTextColor(getResources().getColor(R.color.color_default));
                        Toast.makeText(EnterAndLeaveHotelActivity.this, "结束日期不能小于开始日期", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                textDayView.setText(dateDay);
//
//                outday = date;
//
//                Intent intent = new Intent();
//                intent.putExtra("outday", outday);
//                setResult(RESULT_OK, intent);
//
//                finish();


            }
        }
    }


    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    List<String> myListDate = new ArrayList<>();
    private MyCalendar c1;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    SimpleDateFormat simpleDateFormat, formatYear, formatDay;

    private String inday, outday, sp_inday, sp_outday;
    /*
 * 异步任务执行网络下载图片
 * */
    public class DownTask extends AsyncTask<String, Void, List<String>> {
        //上面的方法中，第一个参数：网络图片的路径，第二个参数的包装类：进度的刻度，第三个参数：任务执行的返回结果
        @Override
        //在界面上显示进度条
        protected void onPreExecute() {
        }

        ;

        protected List<String> doInBackground(String... params) {  //三个点，代表可变参数
            List<String> listDate = new ArrayList<>();
            for (int i = 2018; i <= 2018; i++) {
                for (int j = 1; j < 4; j++) {
                    listDate.add(i + "-" + j + "-" + getDaysByYearMonth(i, j));
                }
            }
            return listDate;
        }

        //主要是更新UI
        @Override
        protected void onPostExecute(List<String> listDate) {
            super.onPostExecute(listDate);

            myListDate.clear();
            myListDate.addAll(listDate);

            for (int i = 0; i < listDate.size(); i++) {
                c1 = new MyCalendar(getApplication());
                c1.setLayoutParams(params);
                Date date = null;
                try {
                    date = simpleDateFormat.parse(listDate.get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!"".equals(sp_inday)) {
                    c1.setInDay(sp_inday);
                }
                if (!"".equals(sp_outday)) {
                    c1.setOutDay(sp_outday);
                }
                c1.setTheDay(date);
                c1.setOnDaySelectListener(EnterAndLeaveHotelActivity.this);
                ll.addView(c1);
            }
        }
    }
}
