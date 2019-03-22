package com.ruirong.chefang.activity;

import android.support.v4.app.FragmentManager;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.fragment.IdentityApplyFragment;
import com.ruirong.chefang.fragment.IdentityStatusFragment;

import butterknife.ButterKnife;

/**身份认证状态
 * Created by dillon on 2017/5/22.
 */

public class IdentityStatusActivity extends BaseActivity {

private FragmentManager fragmentManager;
    @Override
    public int getContentView() {
        return R.layout.activity_identitysss;

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("身份认证");

        IdentityStatusFragment identityStatusFragment = new IdentityStatusFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_content,identityStatusFragment).commit();

    }

    /**
     * 改变申请的页面
     */
    public  void changeFragment(){
        IdentityApplyFragment identityApplyFragment = new IdentityApplyFragment();
        fragmentManager.beginTransaction().replace(R.id.fl_content,identityApplyFragment).commit();

    }

    @Override
    public void getData() {

    }

}
