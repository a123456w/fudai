package com.ruirong.chefang.view;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.util.EventBusUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ScreenAdapter;
import com.ruirong.chefang.adapter.ScreenDropDownAdapter;
import com.ruirong.chefang.bean.ScreenItem;
import com.ruirong.chefang.util.DialogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 *  Created by 李  on 2018/11/20.
 */
public class ScreenDialog extends Dialog implements View.OnClickListener {
    private TextView tvScreen;
    private RecyclerView rvScreen;
    private TextView tvReset;
    private TextView tvAction;
    private ScreenAdapter mAdapter;
    private LinearLayout ll;
    //R.layout.dialog_screen

    private Context mContext;
    private List<ScreenItem> isSelectList;

    public ScreenDialog(Context context) {
        super(context, R.style.MyDialogNotesStyle);
        this.mContext = context;
        DialogUtil.initDialog(this, context, 0.98f);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_screen, null, false);
        setContentView(inflate);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        initView(inflate);
        initRecyclerView();

    }


    private void initView(View v) {
        tvScreen=v.findViewById(R.id.tv_screen);
        rvScreen=v.findViewById(R.id.rv_screen);
        tvReset=v.findViewById(R.id.tv_reset);
        tvAction=v.findViewById(R.id.tv_action);
        ll=v.findViewById(R.id.ll);

        tvReset.setOnClickListener(this);
        tvAction.setOnClickListener(this);
        tvScreen.setOnClickListener(this);
    }

    /**
     * 设置dialog的RecyclerView
     */
    private void initRecyclerView() {

        mAdapter=new ScreenAdapter(rvScreen);
        mAdapter.setOnUpDataListener(new ScreenAdapter.OnUpDataListener() {
            @Override
            public void notifyData(List<ScreenItem> isSelectList) {
                ll.removeAllViews();
                for (int i = 0; i < isSelectList.size(); i++) {
                    addTextView(isSelectList.get(i));
                }
                ScreenDialog.this.isSelectList=isSelectList;
            }
        });
        rvScreen.setLayoutManager(new LinearLayoutManager(mContext));
        rvScreen.setAdapter(mAdapter);

        mAdapter.setMapData(initData());
    }

    /**
     * 动态添加View
     */
    private void addTextView(ScreenItem item) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(mContext,33));
        layoutParams.setMargins(dp2px(mContext,10),0,0,0);
        textView.setTextColor(mContext.getResources().getColor(R.color.color_666666));
        textView.setPadding(dp2px(mContext,10),dp2px(mContext,5),dp2px(mContext,10),dp2px(mContext,5));
        textView.setTextSize(13);
        textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_hotel));
        textView.setText(item.getName());
        textView.setGravity(Gravity.CENTER);
//        layoutParams.gravity= Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        ll.addView(textView);
    }

    private int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
    /**
     * sp转换成px
     */
    private int sp2px(Context context,float spValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }


    private Map<String, List<ScreenItem>> initData() {
        String[] stirngs1={"多早","双早","单早","含早"};
        ArrayList<ScreenItem> list1=new ArrayList<ScreenItem>();
        ArrayList<ScreenItem> list4=new ArrayList<ScreenItem>();
        String[] stirngs2={"双床","大床"};
        ArrayList<ScreenItem> list2=new ArrayList<ScreenItem>();

        String[] stirngs3={"担保","到店付","在线付"};
        ArrayList<ScreenItem> list3=new ArrayList<ScreenItem>();
        String[] stirngs4={"多间优惠","提前预定","连住"};

        list1.add(new ScreenItem(0,stirngs1[0],String.valueOf(0)));
        list1.add(new ScreenItem(2,stirngs1[1],String.valueOf(1)));
        list1.add(new ScreenItem(0,stirngs1[2],String.valueOf(2)));
        list1.add(new ScreenItem(0,stirngs1[3],String.valueOf(3)));

        list2.add(new ScreenItem(0,stirngs2[0],String.valueOf(0)));
        list2.add(new ScreenItem(2,stirngs2[1],String.valueOf(1)));

        list3.add(new ScreenItem(0,stirngs3[0],String.valueOf(0)));
        list3.add(new ScreenItem(2,stirngs3[1],String.valueOf(1)));
        list3.add(new ScreenItem(0,stirngs3[2],String.valueOf(2)));

        list4.add(new ScreenItem(0,stirngs4[0],String.valueOf(0)));
        list4.add(new ScreenItem(2,stirngs4[1],String.valueOf(1)));
        list4.add(new ScreenItem(0,stirngs4[2],String.valueOf(2)));





        HashMap<String, List<ScreenItem>> map = new HashMap<>();
        map.put("早餐", list1);
        map.put("床型", list2);
        map.put("支付方式",list3);
        map.put("优惠特价", list4);
        return map;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_action:
                //完成
                if (isSelectList!=null)isSelectList.size();
                break;
            case R.id.tv_reset:
                //重置
                mAdapter.setMapData(null);
                mAdapter.setMapData(initData());

                if (isSelectList!=null)isSelectList.clear();
                ll.removeAllViews();

                break;
            case R.id.tv_screen:
                break;
        }
    }
}
