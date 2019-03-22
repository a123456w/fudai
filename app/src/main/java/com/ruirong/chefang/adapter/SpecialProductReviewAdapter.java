package com.ruirong.chefang.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.SpecialProductReview;
import com.ruirong.chefang.bean.TeCommentBean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;

/**
 * Created by Administrator on 2018/3/27.
 */

public class SpecialProductReviewAdapter extends BaseListAdapter<TeCommentBean> {
    private CommentPicListAdapter commentPicListAdapter;
    private Map<String, List<String>> listMap = new LinkedHashMap<>();
    private final SpecialProductReview mCon;

    public void setListMap(Map<String, List<String>> listMap) {
        if (listMap != null) {
            this.listMap = listMap;
        }
        notifyDataSetChanged();
    }

    public SpecialProductReviewAdapter(ListView listView) {
        super(listView, R.layout.item_special_product_review);
        mCon = (SpecialProductReview) SpecialProductReviewAdapter.this.mContext;
    }

    @Override
    public void fillData(final ViewHolder holder, int position, final TeCommentBean model) {

        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), holder.getImageView(R.id.goods_pic));
        holder.setText(R.id.goods_title, model.getGoods_name());
        if (model.isTrue()) {
            holder.setImageResource(R.id.evaluate_goods_type_pic, R.drawable.orange_choosed_icon);
            holder.setText(R.id.evaluate_goods_type_text, "你的评论会以匿名的形式展现");
        } else {
            holder.setImageResource(R.id.evaluate_goods_type_pic, R.drawable.no_choosed);
            holder.setText(R.id.evaluate_goods_type_text, "你的评论能帮助其他小伙伴呦");
        }

        //是否匿名的点击事件
        holder.getView(R.id.evaluate_goods_type_pic_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isTrue()) {
                    model.setTrue(false);
                    holder.setImageResource(R.id.evaluate_goods_type_pic, R.drawable.no_choosed);
                    holder.setText(R.id.evaluate_goods_type_text, "你的评论能帮助其他小伙伴呦");
                } else {
                    model.setTrue(true);
                    holder.setImageResource(R.id.evaluate_goods_type_pic, R.drawable.orange_choosed_icon);
                    holder.setText(R.id.evaluate_goods_type_text, "你的评论会以匿名的形式展现");
                }
                notifyDataSetChanged();
            }
        });

        //照片选择
        commentPicListAdapter = new CommentPicListAdapter(mContext);
        GridView goods_pid_list = holder.getView(R.id.goods_pid_list);
        goods_pid_list.setAdapter(commentPicListAdapter);
        commentPicListAdapter.setList(listMap.get(model.getGoods_id()));
        goods_pid_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCon != null) {
                    if (listMap.get(model.getGoods_id()) != null) {
                        if (listMap.get(model.getGoods_id()).size() <= position) {
                            mCon.DialogPic(model.getGoods_id(), 3 - listMap.get(model.getGoods_id()).size());
                        } else {
                            mContext.startActivity(BGAPhotoPreviewActivity.newIntent(mContext,
                                    null, listMap.get(model.getGoods_id()).get(position)));
                        }
                    } else {
                        mCon.DialogPic(model.getGoods_id(), 3);
                    }
                }
            }
        });

        //保存输入内容
        EditText user_content = holder.getView(R.id.user_content);
        user_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    model.setContent(s.toString().trim());
                }
            }
        });


    }
}
