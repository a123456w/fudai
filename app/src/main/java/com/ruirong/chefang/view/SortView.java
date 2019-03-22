package com.ruirong.chefang.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.qlzx.mylibrary.util.PreferencesHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.NearbyCityMessageAdapter;
import com.ruirong.chefang.adapter.SortDropDownAdapter;
import com.ruirong.chefang.event.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * create by xuxx on 2018/11/9
 */
public class SortView implements NearbyCityMessageAdapter.OnItemClickListener{
    private Context context;
    private MyItemClickListener listener;
    private Object object;
    private LinearLayoutManager lm=new LinearLayoutManager(context);
    private RecyclerView ryRecy;
    private List<Object> lists=new ArrayList<>();

    public SortView(Context context,Object object) {
        this.context = context;
        this.object = object;
    }

    public void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    public View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.sort_layout, null);
        ryRecy = (RecyclerView) view.findViewById(R.id.ry_recy);

        return view;
    }
    public void setData(List<Object> sorts){
        lists=sorts;
        NearbyCityMessageAdapter mAdapter=new NearbyCityMessageAdapter(context,sorts);
        ryRecy.setAdapter(mAdapter);
        ryRecy.setLayoutManager(lm);
        mAdapter.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
