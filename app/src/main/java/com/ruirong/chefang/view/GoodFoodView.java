package com.ruirong.chefang.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.AllFoodsDropDownAdapter;
import com.ruirong.chefang.event.MyItemClickListener;

import java.util.List;

/**
 * create by xuxx on 2018/11/1
 * 类型
 */
public class GoodFoodView implements AllFoodsDropDownAdapter.OnItemClickListener{
    private Context context;
    private MyItemClickListener listener;
    private Object object;
    private RecyclerView ryClassification;
    private LinearLayoutManager lm=new LinearLayoutManager(context);

    public GoodFoodView(Context context,Object object) {
        this.context = context;
        this.object = object;
    }

    void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    public View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.goodfood_layout, null);
        ryClassification = (RecyclerView) view.findViewById(R.id.ry_classification);
        ExpandableListView eplType = (ExpandableListView) view.findViewById(R.id.epl_type);

        return view;
    }



    @Override
    public void onItemClick(View view, int position) {

    }

    private class mClick implements View.OnClickListener {

        String string;

        private mClick(String string) {
            this.string = string;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, 1, string);
        }
    }
    public void setData(List<Object> foods){
        AllFoodsDropDownAdapter foodsAdapter=new AllFoodsDropDownAdapter(context,foods);
        ryClassification.setAdapter(foodsAdapter);
        ryClassification.setLayoutManager(lm);
        foodsAdapter.setOnItemClickListener(this);





    }
}
