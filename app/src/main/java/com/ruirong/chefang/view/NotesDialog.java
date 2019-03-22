
package com.ruirong.chefang.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ruirong.chefang.R;
import com.ruirong.chefang.util.DialogUtil;

import java.util.List;


/*
 *  Created by 李  on 2018/10/18.
 */
public class NotesDialog extends Dialog implements View.OnClickListener{
    private TextView tvTitle;
    private RecyclerView rvView;
    private Button btnDismiss;
    private Context mContext;
//    private NotesAdapter mAdapter;

    public NotesDialog( Context context,List list) {
        super(context, R.style.MyDialogNotesStyle);
//        this.mContext = context;
//        DialogUtil.initDialog(this,context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_notes, null, false);
        setContentView(inflate);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        initView(inflate);
        initRecyclerView();

    }

    private void initView(View v) {
        tvTitle=v.findViewById(R.id.tv_title);
        rvView=v.findViewById(R.id.rv_view);
        btnDismiss=v.findViewById(R.id.btn_dismiss);
        btnDismiss.setOnClickListener(this);
    }

    /**
     * 设置dialog的RecyclerView
     */
    private void initRecyclerView() {
//        mAdapter=new NotesAdapter(rvView);
//        rvView.setLayoutManager(new LinearLayoutManager(mContext));
//        rvView.setAdapter(mAdapter);
    }


    private void setData(List list) {
//        tvTitle.setText("111");
//        mAdapter.setData(list);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dismiss:
                if (this!=null&&this.isShowing())this.dismiss();
                break;
        }
    }
}



