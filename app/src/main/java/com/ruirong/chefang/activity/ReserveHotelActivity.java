package com.ruirong.chefang.activity;

import android.content.Intent;

import com.maning.calendarlibrary.MNCalendarVertical;
import com.maning.calendarlibrary.listeners.OnCalendarRangeChooseListener;
import com.maning.calendarlibrary.model.MNCalendarVerticalConfig;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预订房间的日期
 * Created by dillon on 2018/5/9.
 */

public class ReserveHotelActivity extends BaseActivity {
    @BindView(R.id.mnCalendarVertical)
    MNCalendarVertical mnCalendarVertical;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String currentState = "1";    // 等于 1 的时候不是入店离店不在同一天 ，等于2的时候入店离店在同一天 。

    @Override
    public int getContentView() {
        return R.layout.activity_reservehotel;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("选择如离店日期");
        currentState = getIntent().getStringExtra("YESHENCHECK");
        /**
         * 区间选取完成监听
         */
        mnCalendarVertical.setOnCalendarRangeChooseListener(new OnCalendarRangeChooseListener() {
            @Override
            public void onRangeDate(Date startDate, Date endDate) {
                String startTime = sdf.format(startDate);
                String endTime = sdf.format(endDate);
                Intent intent = new Intent();
                intent.putExtra("startservertime",startTime);
                intent.putExtra("endservertime",endTime);

                setResult(RESULT_OK,intent);
                finish();
            }
        });

        /**
         *  自定义设置相关
         */
        MNCalendarVerticalConfig mnCalendarVerticalConfig = new MNCalendarVerticalConfig.Builder()
                .setMnCalendar_showWeek(true)                   //是否显示星期栏
                .setMnCalendar_showLunar(false)                  //是否显示阴历
                .setMnCalendar_colorWeek("#B07219")             //星期栏的颜色
                .setMnCalendar_titleFormat("yyyy-MM")           //每个月的标题样式
                .setMnCalendar_colorTitle("#FF0000")            //每个月标题的颜色
                .setMnCalendar_colorSolar("#FF0000")            //阳历的颜色
                .setMnCalendar_colorLunar("#00ff00")            //阴历的颜色
                .setMnCalendar_colorBeforeToday("#F1EDBD")      //今天之前的日期的颜色
                .setMnCalendar_colorRangeBg("#9930C553")        //区间中间的背景颜色
                .setMnCalendar_colorRangeText("#000000")        //区间文字的颜色
                .setMnCalendar_colorStartAndEndBg("#258C3E")    //开始结束的背景颜色
                .setMnCalendar_countMonth(12)                    //显示多少月(默认6个月)
                .build();
        mnCalendarVertical.setConfig(mnCalendarVerticalConfig);
        mnCalendarVertical.setCurrentState(currentState);
    }

    @Override
    public void getData() {

    }
}
