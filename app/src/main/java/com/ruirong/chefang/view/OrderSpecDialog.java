package com.ruirong.chefang.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.OrderDetailAdapter;
import com.ruirong.chefang.adapter.SpecAdapter;
import com.ruirong.chefang.adapter.SpecBean;
import com.ruirong.chefang.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

//import com.maning.calendarlibrary.view.GridItemDividerDecoration;

/*
 *  Created by 李  on 2018/10/22.
 */
public class OrderSpecDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private Button btn_dismiss;
    private RecyclerView rvPrice;
    private TextView tv1,tv2,tv3,tv4;
    private TextView[] tv=new TextView[4];
    private SpecAdapter mAdapter;
    private LinearLayoutManager llm=null;
    private String mLastPosition="";
    private int lastTv=-1;
    private OnSelectionListener OnSelectionListener;

    public OrderSpecDialog(@NonNull Context context) {
        super(context, R.style.MyDialogNotesStyle);
        this.mContext = context;
        DialogUtil.initDialog(this,context,0.4f);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_order_spec, null, false);
        setContentView(inflate);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        initView(inflate);
        initRecyclerView();
        initListener();
        initData();
    }

    private void initData() {
        List<SpecBean> mList=new ArrayList<>();
        for (int i = 6; i >=0; i--) {
            SpecBean specBean = new SpecBean();
            if (i==6){
                specBean.setSelect(false);
                specBean.setSpecId(""+(i+1));
                specBean.setName("不限");
            }else {
                specBean.setName(String.valueOf((i)*100));
                specBean.setSelect(false);
                specBean.setSpecId(""+(i+1));
            }
            mList.add(specBean);
        }
        mAdapter.setData(mList);
    }

    private void initListener() {
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        btn_dismiss.setOnClickListener(this);
        mAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                if (mLastPosition!=null){
                    if ("".equals(mLastPosition)){
                        //第一次点击
                        SpecBean specBean = mAdapter.getData().get(position);
                        specBean.setSelect(true);
                        mLastPosition=""+position;
                        mAdapter.notifyItemChanged(position);
                    }else if (mLastPosition.equals(String.valueOf(position))){
                        //点击同一个
                        return;
                    }else{
                        //点击不同的item
                        SpecBean specBean1 = mAdapter.getData().get(Integer.valueOf(mLastPosition));
                        specBean1.setSelect(false);
                        mAdapter.notifyItemChanged(Integer.valueOf(mLastPosition));
                        SpecBean specBean = mAdapter.getData().get(position);
                        specBean.setSelect(true);
                        mLastPosition=""+position;
                        mAdapter.notifyItemChanged(position);

                    }
                }
            }
        });
    }

    private void initView(View v) {
        tv1=v.findViewById(R.id.tv_1);
        tv2=v.findViewById(R.id.tv_2);
        tv3=v.findViewById(R.id.tv_3);
        tv4=v.findViewById(R.id.tv_4);
        rvPrice=v.findViewById(R.id.rv_price);
        btn_dismiss=v.findViewById(R.id.btn_dismiss);
        tv[0]=tv1;
        tv[1]=tv2;
        tv[2]=tv3;
        tv[3]=tv4;
    }

    /**
     * 设置dialog的RecyclerView
     */
    private void initRecyclerView() {
        llm=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,true);
        mAdapter=new SpecAdapter(rvPrice);
        rvPrice.setAdapter(mAdapter);
        rvPrice.setLayoutManager(llm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commitOrder:

                break;
            case R.id.tv_1:
                update(0);
                break;
            case R.id.tv_2:
                update(1);
                break;
            case R.id.tv_3:
                update(2);
                break;
            case R.id.tv_4:
                update(3);
                break;
            case R.id.btn_dismiss:
                SpecBean select = getSelect();
                if (select!=null&&lastTv!=-1){
                    String s = tv[lastTv].getText().toString();

                    if (OnSelectionListener!=null)OnSelectionListener.onSelection(select.getName()+","+s);
                }else {
                    ToastUtil.showToast(mContext,"请选择规格");
                    return;
                }
                if (this!=null&&this.isShowing())dismiss();
                break;
        }
    }

    /**
     * 获取被选中的item
     * @return
     */
    private SpecBean getSelect() {
        if (!"".equals(mLastPosition)){
            SpecBean resout = mAdapter.getData().get(Integer.valueOf(mLastPosition));
            return resout;
        }else {
            return null;
        }
    }

    /**
     * 更新被选中的Ui
     * @param i
     */
    private void update(int i) {
        if (lastTv==-1){
            tv[i].setBackground(mContext.getResources().getDrawable(R.drawable.bg_selector_order_spec_text1));
        }else {
            tv[i].setBackground(mContext.getResources().getDrawable(R.drawable.bg_selector_order_spec_text1));
            tv[lastTv].setBackground(mContext.getResources().getDrawable(R.drawable.bg_selector_order_spec_text));
        }
        lastTv=i;
    }

    /**
     * 选中的回调
     */
    public interface OnSelectionListener{
        void onSelection(String select);
    }

    public OrderSpecDialog.OnSelectionListener getOnSelectionListener() {
        return OnSelectionListener;
    }

    public void setOnSelectionListener(OrderSpecDialog.OnSelectionListener onSelectionListener) {
        OnSelectionListener = onSelectionListener;
    }


}
