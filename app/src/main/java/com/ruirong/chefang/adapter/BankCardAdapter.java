package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.BankCardBean;
import com.ruirong.chefang.util.TextUtils;

/**
 * Created by dillon on 2017/4/11.
 */

public class BankCardAdapter extends RecyclerViewAdapter<BankCardBean.ListBean> {

    public BankCardAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_bankcard_list);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, BankCardBean.ListBean model) {
        viewHolderHelper.setText(R.id.tv_bankname, model.getBank());
        viewHolderHelper.setText(R.id.tv_banknumber, TextUtils.hideText(model.getBank_card_number()));
        switch (model.getBank()) {
            case "中国工商银行":
                viewHolderHelper.getConvertView().findViewById(R.id.rl_bankcard).setBackgroundResource(R.color.blue);
                model.setBgcolor(R.color.blue);
                break;
            case "中国建设银行":
                viewHolderHelper.getConvertView().findViewById(R.id.rl_bankcard).setBackgroundResource(R.color.light_grey);
                model.setBgcolor(R.color.light_grey);
                break;
            case "中国招商银行":
                viewHolderHelper.getConvertView().findViewById(R.id.rl_bankcard).setBackgroundResource(R.color.black);
                model.setBgcolor(R.color.black);
                break;
            case "中国农业银行":
                viewHolderHelper.getConvertView().findViewById(R.id.rl_bankcard).setBackgroundResource(R.color.green);
                model.setBgcolor(R.color.green);
                break;
            default:
                viewHolderHelper.getConvertView().findViewById(R.id.rl_bankcard).setBackgroundResource(R.color.green);
                model.setBgcolor(R.color.green);
                break;

        }
    }
}
