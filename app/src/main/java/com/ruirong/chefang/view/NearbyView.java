package com.ruirong.chefang.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.AllFoodsDropDownAdapter;
import com.ruirong.chefang.adapter.NearbyCityAdapter;
import com.ruirong.chefang.adapter.NearbyCityMessageAdapter;
import com.ruirong.chefang.event.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * create by xuxx on 2018/11/6
 * 附近界面
 */
public class NearbyView implements AllFoodsDropDownAdapter.OnItemClickListener,NearbyCityMessageAdapter.OnItemClickListener{
    private Context context;
    private MyItemClickListener listener;
    private Object object;
    private RecyclerView ryCity;
    private RecyclerView ryRegion;
    private LinearLayoutManager lm=new LinearLayoutManager(context);
    private LinearLayoutManager lm2=new LinearLayoutManager(context);

    public NearbyView(Context context,Object object) {
        this.context = context;
        this.object = object;
    }

    public void setListener(MyItemClickListener listener) {
        this.listener = listener;
    }

    public View getView() {

        View view = LayoutInflater.from(context).inflate(R.layout.nearby_layout, null);
        ryCity = (RecyclerView) view.findViewById(R.id.ry_city);
        ryRegion = (RecyclerView) view.findViewById(R.id.ry_region);

        return view;
    }
    public void setData(List<Object> cityname, final List<Object> citymessage){




        AllFoodsDropDownAdapter cityAdapter=new AllFoodsDropDownAdapter(context,cityname);
        ryCity.setAdapter(cityAdapter);
        ryCity.setLayoutManager(lm);
        cityAdapter.setOnItemClickListener(new AllFoodsDropDownAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String[] o = (String[]) citymessage.get(position);
                ArrayList<Object> objects = new ArrayList<>();
                for (int i = 0; i < o.length; i++) {
                    objects.add(o[i]);
                }
                NearbyCityMessageAdapter cityMessageAdapter=new NearbyCityMessageAdapter(context,objects);
                ryRegion.setAdapter(cityMessageAdapter);
                ryRegion.setLayoutManager(lm2);
                cityMessageAdapter.setOnItemClickListener(new NearbyCityMessageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
            }
        });



    }


    @Override
    public void onItemClick(View view, int position) {



    }
}
