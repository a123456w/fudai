
package com.ruirong.chefang.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.util.DialogUtil;
import com.ruirong.chefang.util.city.CityPicker;
import com.ruirong.chefang.wheel.WheelView;
//import com.wx.wheelview.adapter.ArrayWheelAdapter;
//import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
 *  Created by 李  on 2018/10/18.
 */
public class SelectDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private WheelView wv1,wv2,wv3;
    private TextView tv_confirm,tv_dismiss;
    private CityPicker cityPicker;
    private onClickConfirm onClickConfirm;


    public SelectDialog(Context context) {
        super(context, R.style.MyDialogNotesStyle);
        this.mContext = context;
        DialogUtil.initDialog(this,context,0.6f);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_select, null, false);
        setContentView(inflate);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        initView(inflate);
    }


    private void initView(View v) {
        tv_confirm=v.findViewById(R.id.tv_confirm);
        tv_dismiss=v.findViewById(R.id.tv_dismiss);
        tv_dismiss.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        cityPicker=v.findViewById(R.id.cp_city);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dismiss:
                if (this!=null&&this.isShowing())this.dismiss();
                break;
            case R.id.tv_confirm:
                String city_string = cityPicker.getCity_string();
                if (onClickConfirm!=null)onClickConfirm.onConfirm(city_string);
//                ToastUtil.showToast(mContext,city_string);
                break;
        }
    }
    public interface onClickConfirm{
        void onConfirm(String city);
    }
    public SelectDialog.onClickConfirm getOnClickConfirm() {
        return onClickConfirm;
    }

    public void setOnClickConfirm(SelectDialog.onClickConfirm onClickConfirm) {
        this.onClickConfirm = onClickConfirm;
    }
}



