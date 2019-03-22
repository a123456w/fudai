package com.ruirong.chefang.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ChoiceBoxAdapter;
import com.ruirong.chefang.adapter.NearbyCityMessageAdapter;
import com.ruirong.chefang.event.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * create by xuxx on 2018/11/6
 * 筛选界面
 */
public class ScreenView implements NearbyCityMessageAdapter.OnItemClickListener,ChoiceBoxAdapter.OnItemClickListener,View.OnClickListener{
    private Context context;
    private MyItemClickListener listener;
    private Object object;
    private RecyclerView ryRecy,ryNumber,ryServe;
    private Button btn_storemoney,btn_paybill;
    private TextView tvNumber,tvServe;
    private String[] can={"单人餐","双人餐","3-4人餐","5人餐","10人餐"};
    private String[] fuwu={"买单","外卖送餐","预订","在线排队"};
    private LinearLayoutManager lm=new LinearLayoutManager(context);

    public ScreenView(Context context,Object object) {
        this.context = context;
        this.object = object;
    }

    public void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    public View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.screen_layout, null);
        ryRecy = (RecyclerView) view.findViewById(R.id.ry_recy);
        ryNumber = (RecyclerView) view.findViewById(R.id.ry_number);
        ryServe = (RecyclerView) view.findViewById(R.id.ry_serve);
        tvNumber=(TextView)view.findViewById(R.id.tv_number);
        tvServe=(TextView)view.findViewById(R.id.tv_serve);
        btn_storemoney=(Button)view.findViewById(R.id.btn_storemoney);
        btn_paybill=(Button)view.findViewById(R.id.btn_paybill);


        return view;
    }
    public void setData(final List<Object> screens){
        final NearbyCityMessageAdapter mAdapter=new NearbyCityMessageAdapter(context,screens);
        ryRecy.setAdapter(mAdapter);
        ryRecy.setLayoutManager(lm);
        mAdapter.setOnItemClickListener(this);

        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < can.length; i++) {
            objects.add(can[i]);
        }
        ArrayList<Object> objects1 = new ArrayList<>();
        for (int i = 0; i < fuwu.length; i++) {
            objects1.add(fuwu[i]);
        }

        final ChoiceBoxAdapter choiceBoxAdapter=new ChoiceBoxAdapter(context,objects);
        ryNumber.setAdapter(choiceBoxAdapter);
        ryNumber.setLayoutManager(new GridLayoutManager(context,4));
        choiceBoxAdapter.setOnItemClickListener(this);

        final ChoiceBoxAdapter choiceBoxAdapter1=new ChoiceBoxAdapter(context,objects1);
        ryServe.setAdapter(choiceBoxAdapter1);
        ryServe.setLayoutManager(new GridLayoutManager(context,4));
        choiceBoxAdapter1.setOnItemClickListener(this);
        btn_storemoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.notifyDataSetChanged();
                choiceBoxAdapter.notifyDataSetChanged();
                choiceBoxAdapter1.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_storemoney:
                break;
            case R.id.btn_paybill:
                break;
        }
    }
}
