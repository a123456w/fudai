//package com.ruirong.chefang.adapter;
//
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.qlzx.mylibrary.base.RecyclerViewAdapter;
//import com.qlzx.mylibrary.base.ViewHolderHelper;
//import com.qlzx.mylibrary.util.GlideUtil;
//import com.ruirong.chefang.R;
//
//import java.util.ArrayList;
//
///*
// *  Created by 李  on 2018/10/18.
// */
//public class NotesAdapter extends RecyclerViewAdapter {
//    private LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
//
//
//    public NotesAdapter(RecyclerView recyclerView) {
//        super(recyclerView, R.layout.notes_item);
//    }
//
//    @Override
//    protected void fillData(ViewHolderHelper helper, int position, Object model) {
//        GlideUtil.display(mContext,"1sadf1",helper.getImageView(R.id.iv_icon));
//        helper.setText(R.id.tv_title,"1asfg1");
//        initRecyclerView(helper);
//    }
//
//    private void initRecyclerView(ViewHolderHelper helper) {
//        RecyclerView recyclerView=helper.getView(R.id.rv_item);
//        recyclerView.setLayoutManager(layoutManager);
//        Notes2_Adapter adapter=new Notes2_Adapter(recyclerView);
//
//
//        recyclerView.setAdapter(adapter);
//        /**
//         * 等待数据
//         */
//        adapter.setData(new ArrayList());
//    }
//}
