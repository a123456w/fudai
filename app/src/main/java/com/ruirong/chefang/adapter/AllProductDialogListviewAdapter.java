package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

/**
 * Created by chenlipeng on 2018/1/2 0002
 * 单品选项购物车
 * describe:
 */
public class AllProductDialogListviewAdapter extends RecyclerViewAdapter<CartListBean.CartlistBean> {
    public AllProductDialogListviewAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.allproduct_activity_buy_product_list_item);
    }

    @Override
    public void fillData(ViewHolderHelper holder, final int position, final CartListBean.CartlistBean model) {
        holder.setText(R.id.car_name, model.getGoods_name());
        holder.setText(R.id.car_price, "￥" + model.getPrice());
        holder.setText(R.id.tv_count, model.getNum());

        holder.setItemChildClickListener(R.id.iv_add);
        holder.setItemChildClickListener(R.id.iv_subtract);

//        holder.getImageView(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                model.setNum((Integer.parseInt(model.getNum()) + 1) + "");
//                addcart(mContext.token, model.getId(), model.getNum());
//                notifyDataSetChanged();
//            }
//        });
//
//        holder.getImageView(R.id.iv_subtract).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Integer.parseInt(model.getNum()) - 1 <= 0) {
//                    addcart(mContext.token, model.getId(), "0");
//                    removeItem(position);
//                } else {
//                    model.setNum(Integer.parseInt(model.getNum()) - 1 + "");
//                }
//                addcart(mContext.token, model.getId(), model.getNum());
//                notifyDataSetChanged();
//            }
//        });
    }

//    /**
//     * 加入购物车
//     *
//     * @param token
//     * @param id
//     * @param num
//     */
//    private void addcart(String token, String id, String num) {
//        HttpHelp.getInstance().create(RemoteApi.class).addcart(token, id, num)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
//                    @Override
//                    public void onNext(BaseBean<Object> baseBean) {
//                        super.onNext(baseBean);
//                        if (baseBean.code == 0) {
//
//                        }
//                        ToastUtil.showToast(mContext, baseBean.message);
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                    }
//                });
//
//    }

}
