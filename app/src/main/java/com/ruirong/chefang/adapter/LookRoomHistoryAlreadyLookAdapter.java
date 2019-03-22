package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.qlzx.mylibrary.base.OnItemChildCheckedChangeListener;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LookRoomHistoryBean;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  看房记录   已看
 */

public class LookRoomHistoryAlreadyLookAdapter extends RecyclerViewAdapter<LookRoomHistoryBean.ListBean> {

    private int type=1;
    private Boolean isDisplay=false;
    public void setType(int type){
        this.type=type;
    };
    public LookRoomHistoryAlreadyLookAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_yet_look);
    }
    public void displayChecked(Boolean isDisplay){this.isDisplay=isDisplay;}

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, LookRoomHistoryBean.ListBean model) {
        if (isDisplay){
            viewHolderHelper.setVisibility(R.id.tv_is_check,VISIBLE);
        }else {
            viewHolderHelper.setVisibility(R.id.tv_is_check,GONE);
        }
        if (type==1){
            viewHolderHelper.setImageResource(R.id.iv_pic,R.drawable.yet_look);
        }else if (type==2){
            viewHolderHelper.setVisibility(R.id.iv_pic,GONE);
        }
        GlideUtil.display(mContext, Constants.IMG_HOST+ model.getCover(),viewHolderHelper.getImageView(R.id.item_lookroom_img));
        viewHolderHelper.setText(R.id.item_lookroom_name,model.getSp_name());
        viewHolderHelper.setText(R.id.item_lookroom_time,model.getLook_time());
       // viewHolderHelper.setText(R.id.item_lookroom_address,model.getSp_address());
        viewHolderHelper.setText(R.id.item_lookroom_address,model.getId());
        CheckBox checkBox=viewHolderHelper.getView(R.id.tv_is_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
//            viewHolderHelper.setText(R.id.tv_deresion,model.getSpecause());
//            viewHolderHelper.setText(R.id.tv_decidedate,model.getAbntime());
//            viewHolderHelper.setText(R.id.tv_removedata,model.getRemdate());
//            viewHolderHelper.setText(R.id.tv_decideappart,model.getDecorg());
//            viewHolderHelper.setText(R.id.tv_comname,model.getCname());

//        ProgressBar view = (ProgressBar) viewHolderHelper.getView(R.id.progress_bar);
//        view.setProgress(40);


//        viewHolderHelper.getView(R.id.progress_bar).get
//        HorizontalProgressBar view = (HorizontalProgressBar) viewHolderHelper.getView(R.id.hpb);
//        view.setCurrentLocation(0.4f);

//        view.startProgressAnimation();
    }
}
