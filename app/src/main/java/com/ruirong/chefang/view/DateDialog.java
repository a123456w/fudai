package com.ruirong.chefang.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.maning.calendarlibrary.MNCalendar;
import com.maning.calendarlibrary.listeners.OnCalendarRangeChooseListener;
import com.ruirong.chefang.R;
import com.ruirong.chefang.util.DialogUtil;

import java.util.Date;

import butterknife.BindView;

/*
 *  Created by 李  on 2018/10/19.
 */
public class DateDialog extends Dialog implements View.OnClickListener{
    private com.maning.calendarlibrary.MNCalendarVertical MNCalendarV;
    private Context mContext;
    private Button btn_dismiss;
    private Date startDate,endDate;
    private onRangeDateListener mOnRangeDateListener;



    public DateDialog(@NonNull Context context) {
        super(context,R.style.MyDialogNotesStyle);
        this.mContext = context;
        DialogUtil.initDialog(this,context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_date, null, false);
        setContentView(inflate);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        initView(inflate);
    }

    private void initView(View v) {
        MNCalendarV=v.findViewById(R.id.MNCalendarV);
        btn_dismiss=v.findViewById(R.id.btn_dismiss);
        btn_dismiss.setOnClickListener(this);
        MNCalendarV.setOnCalendarRangeChooseListener(new OnCalendarRangeChooseListener() {
            @Override
            public void onRangeDate(Date startDate, Date endDate) {
                DateDialog.this.startDate=startDate;
                DateDialog.this.endDate=endDate;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dismiss:
                if (startDate!=null&&endDate!=null){
                    if (mOnRangeDateListener!=null)mOnRangeDateListener.onRangeDate(startDate,endDate);
                }
                break;
        }
    }
    public interface onRangeDateListener{
        void onRangeDate(Date startDate, Date endDate);
    }

    public onRangeDateListener getOnRangeDateListener() {
        return mOnRangeDateListener;
    }

    public void setOnRangeDateListener(onRangeDateListener mOnRangeDateListener) {
        this.mOnRangeDateListener = mOnRangeDateListener;
    }

}
