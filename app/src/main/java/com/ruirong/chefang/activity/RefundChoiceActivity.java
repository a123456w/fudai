package com.ruirong.chefang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SpecialtyOrderDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/28.
 * 退款 选择服务类型
 */

public class RefundChoiceActivity extends BaseActivity {
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.goods_type)
    TextView goodsType;
    @BindView(R.id.goods_num)
    TextView goodsNum;
    @BindView(R.id.refund)
    LinearLayout refund;
    @BindView(R.id.return_goods)
    LinearLayout returnGoods;
    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    private String goodsContent;
    private String number_bh;

    @Override
    public int getContentView() {
        return R.layout.activity_refund_choice;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("选择服务类型");

        goodsContent = getIntent().getStringExtra("goodsContent");
        number_bh = getIntent().getStringExtra("number_bh");
        if (goodsContent == null || number_bh == null) {
            finish();
        }

        SpecialtyOrderDetailBean.GoodsInfoBean goodsInfoBean = new Gson().fromJson(goodsContent, SpecialtyOrderDetailBean.GoodsInfoBean.class);
        GlideUtil.display(RefundChoiceActivity.this, Constants.IMG_HOST + goodsInfoBean.getPic(), goodsPic);
        goodsName.setText(goodsInfoBean.getName());
        goodsType.setText(goodsInfoBean.getName());
        goodsNum.setText("x" + goodsInfoBean.getNum());

    }

    @Override
    public void getData() {


    }

    @OnClick({R.id.refund, R.id.return_goods})
    public void onViewClicked(View view) {
        Intent intent = new Intent(RefundChoiceActivity.this, ApplicationForRefundActivity.class);
        intent.putExtra("goodsContent", goodsContent);
        intent.putExtra("number_bh", number_bh);
        switch (view.getId()) {
            case R.id.refund:
                intent.putExtra("type", 1);
                startActivity(intent);
                finish();
                break;
            case R.id.return_goods:
                intent.putExtra("type", 2);
                startActivity(intent);
                finish();
                break;
        }
    }
}
