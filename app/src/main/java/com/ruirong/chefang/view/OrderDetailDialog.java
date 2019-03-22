package com.ruirong.chefang.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.maning.calendarlibrary.view.GridItemDividerDecoration;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ExplainAdapter;
import com.ruirong.chefang.adapter.OrderDetailAdapter;
import com.ruirong.chefang.util.DialogUtil;

import java.util.ArrayList;

/*
 *  Created by 李  on 2018/10/22.
 */
public class OrderDetailDialog extends Dialog implements View.OnClickListener {
    private TextView tvTitle;
    private RecyclerView rvView;
    private TextView tv_commitOrder;
    private Context mContext;
    private OrderDetailAdapter mAdapter;

    public OrderDetailDialog(@NonNull Context context) {
        super(context, R.style.MyDialogNotesStyle);
        this.mContext = context;
        DialogUtil.initDialog(this,context,0.38f);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_order_detail, null, false);
        setContentView(inflate);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        initView(inflate);
        initRecyclerView();

    }
    private void initView(View v) {
        tvTitle=v.findViewById(R.id.tv_title);
        rvView=v.findViewById(R.id.rv_order_dialog);
        tv_commitOrder=v.findViewById(R.id.tv_commitOrder);
        tv_commitOrder.setOnClickListener(this);
    }

    /**
     * 设置dialog的RecyclerView
     */
    private void initRecyclerView() {
        mAdapter=new OrderDetailAdapter(rvView);
        rvView.setLayoutManager(new LinearLayoutManager(mContext));
        rvView.setAdapter(mAdapter);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("");
        objects.add("");
        mAdapter.setData(objects);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commitOrder:
                /**
                 * 确认提交订单
                 */
                tv_commitOrder.setEnabled(false);
                commitOrder();
                break;
        }
    }

    /**
     * 确认订单网络请求
     */
    private void commitOrder() {

    }
}
