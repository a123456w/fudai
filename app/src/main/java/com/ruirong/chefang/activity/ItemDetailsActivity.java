package com.ruirong.chefang.activity;

import android.text.TextUtils;
import android.view.View;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.util.SharedUtil;
import com.ruirong.chefang.util.ToolUtil;

/**
 * 美食 单品详情
 */
public class ItemDetailsActivity extends BaseActivity {



    @Override
    public int getContentView() {
        return R.layout.activity_item_details;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("单品详情");
        titleBar.setRightImageRes(R.drawable.ic_share);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(new PreferencesHelper(ItemDetailsActivity.this).getToken())) {
                    ToolUtil.goToLogin(ItemDetailsActivity.this);
                } else {
                    SharedUtil sharedUtil = new SharedUtil(ItemDetailsActivity.this);
                    sharedUtil.initShared(ItemDetailsActivity.this);
                }
            }
        });

    }

    @Override
    public void getData() {

    }
}
