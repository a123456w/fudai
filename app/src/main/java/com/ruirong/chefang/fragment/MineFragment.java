package com.ruirong.chefang.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.CommonV4Activity;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.AddressActivity;
import com.ruirong.chefang.activity.LoginActivity;
import com.ruirong.chefang.activity.MessageActivity;
import com.ruirong.chefang.activity.MyPurseActivity;
import com.ruirong.chefang.activity.PersionMessageActivity;
import com.ruirong.chefang.activity.SetActivity;
import com.ruirong.chefang.activity.WebActivity;
import com.ruirong.chefang.bean.PersionMoneyBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.event.BalanceEvent;
import com.ruirong.chefang.event.LoginOutEvent;
import com.ruirong.chefang.event.LoginSuccessEvent;
import com.ruirong.chefang.event.UpdateInformationEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.ExpenseCalendarActivity1;
import com.ruirong.chefang.personalcenter.LookRoomHistoryActivity;
import com.ruirong.chefang.personalcenter.LuckyBagOrderActivity;
import com.ruirong.chefang.personalcenter.MyCollectionActivity;
import com.ruirong.chefang.personalcenter.MyLuckyBagActivity;
import com.ruirong.chefang.personalcenter.MyReferralCodeActivity;
import com.ruirong.chefang.personalcenter.MyRoomTimeActivity;
import com.ruirong.chefang.personalcenter.MyStoredValueActivity;
import com.ruirong.chefang.personalcenter.ReserveOrderActivity;
import com.ruirong.chefang.personalcenter.ShopOrderActivity;
import com.ruirong.chefang.personalcenter.SpecialtyOrderActivity;
import com.ruirong.chefang.shoppingcart.ShoppingCartActivity;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的
 * Created by dillon on 2017/12/25.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    @BindView(R.id.iv_center_image)
    ImageView ivCenterImage;
    @BindView(R.id.tv_login_and_register)
    TextView tvLoginAndRegister;
    @BindView(R.id.v_middle_bg)
    View vMiddleBg;
    @BindView(R.id.ll_techan_dingdan)
    LinearLayout llTechanDingdan;
    @BindView(R.id.ll_yuding_dingdan)
    LinearLayout llYudingDingdan;
    @BindView(R.id.ll_shangcheng_dingdan)
    LinearLayout llShangchengDingdan;
    @BindView(R.id.ll_fudai_dingdan)
    LinearLayout llFudaiDingdan;
    @BindView(R.id.ll_top_layout)
    LinearLayout llTopLayout;
    @BindView(R.id.v_middle_line)
    View vMiddleLine;
    @BindView(R.id.ll_keyong_yue)
    LinearLayout llKeyongYue;
    @BindView(R.id.ll_zuori_shouhuo)
    LinearLayout llZuoriShouhuo;
    @BindView(R.id.ll_1_row)
    LinearLayout ll1Row;
    @BindView(R.id.ll_2_row)
    LinearLayout ll2Row;
    Unbinder unbinder;
    @BindView(R.id.ll_gouwuche)
    LinearLayout llGouwuche;
    @BindView(R.id.tv_yestedaymoney)
    TextView tvYesteDayMoney;
    @BindView(R.id.tv_mymoney)
    TextView tvMyMoney;
    @BindView(R.id.tv_mynickname)
    TextView tvMynickname;

    private UserInforBean userInforBean;
    private PreferencesHelper preferencesHelper;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        EventBusUtil.register(this);
        preferencesHelper = new PreferencesHelper(getActivity());

//        Eyes.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.stateColor));
    }

    @Override
    public void getData() {
        if (!TextUtils.isEmpty(new PreferencesHelper(getActivity()).getToken())) {
            tvLoginAndRegister.setVisibility(View.GONE);
            tvMynickname.setVisibility(View.VISIBLE);
            getMyMoney(preferencesHelper.getToken());
            getUserInfor(preferencesHelper.getToken());
        } else {
           // GlideUtil.display(getActivity(), R.drawable.wodefudai1, ivCenterImage);
            GlideUtil.displayHeadAvatar(getActivity(), R.mipmap.ic_launcher, ivCenterImage);
            tvLoginAndRegister.setVisibility(View.VISIBLE);
            tvMynickname.setVisibility(View.GONE);
        }
    }

    /**
     * 登录成功后初始化数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isLogin(LoginSuccessEvent event) {
        getData();
    }

    /**
     * 用于刷新余额的
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void balanceEvent(BalanceEvent event) {
        getData();
    }

    /**
     * 退出登录后重新调整界面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginOut(LoginOutEvent event) {
        getData();
    }

    /**
     * 个人信息修改后重新调用接口
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInformation(UpdateInformationEvent event) {
        getData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBusUtil.unregister(this);
    }

    /**
     * 获取可用余额和昨日收获
     *
     * @param token
     */
    public void getMyMoney(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).persionMoney(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PersionMoneyBean>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<PersionMoneyBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.code == 0 && baseBean.data != null) {
                                PersionMoneyBean persionMoneyBean = baseBean.data;
                                tvMyMoney.setText(persionMoneyBean.getBack_money());
                                tvYesteDayMoney.setText(persionMoneyBean.getYester_money());
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    public void getUserInfor(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).userInfor(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<UserInforBean>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<UserInforBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            userInforBean = baseBean.data;
                            GlideUtil.displayHeadAvatar(getActivity(), Constants.IMG_HOST + userInforBean.getPic(), ivCenterImage);
                            tvMynickname.setText(userInforBean.getName());
                         /*   if ("0".equals(userInforBean.getIs_partner())){
                                tvPartenerCenter.setVisibility(View.GONE);
                            }else if ("1".equals(userInforBean.getIs_partner())){
                                tvPartenerCenter.setVisibility(View.VISIBLE);
                            }
                            if ("1".equals(userInforBean.getRole())) {
                                tvMemberLevels.setText("VIP用户");
                            } else if ("2".equals(userInforBean.getRole())) {
                                tvMemberLevels.setText("VIP商户");

                        *//*商家的用户换成是其他的颜色但不是黄色，看着不显*//*
                      *//*  tvMemberLevels.setTextColor(getResources().getColor(R.color.gold));
                        GlideUtil.display(mContext,R.drawable.member_levelsg,memberLevels);*//*

                            }*/

                            // 将userinfor存储起来
                            new PreferencesHelper(getActivity()).saveUserInfo(new Gson().toJson(userInforBean));
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }

    /**
     * @param view
     * R.id.ll_mine_order,
     */
    @OnClick({R.id.ll_techan_dingdan, R.id.ll_yuding_dingdan, R.id.ll_shangcheng_dingdan, R.id.ll_keyong_yue,
            R.id.ll_zuori_shouhuo, R.id.ll_gouwuche, R.id.ll_wodefangshi, R.id.ll_wode_chuzhi,R.id.ll_shop_cart, R.id.ll_wode_fudai, R.id.ll_wode_qianbao, R.id.ll_kanfang_jilu,
            R.id.ll_customer_service
            ,R.id.ll_xiaoxi_jilu, R.id.wode_shoucang, R.id.wode_tuijian, R.id.ll_wode_dizhi, R.id.iv_set, R.id.iv_message, R.id.tv_login_and_register, R.id.iv_center_image, R.id.ll_fudai_dingdan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_shop_cart://房车
                startActivity(new Intent(getActivity(), CommonV4Activity.class)
                        .putExtra("fragment", GarageFragment.class.getName()));
                break;
//            case R.id.ll_mine_order://我的订单
//                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
//                    ToolUtil.goToLogin(getContext());
//                } else {
//                    startActivity(new Intent(getActivity(), ReserveOrderActivity.class));
//                }
//                break;
            case R.id.ll_customer_service://在线客服
                WebActivity.startActivity(getActivity(), "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.iv_center_image:     //个人中心
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), PersionMessageActivity.class));
                }
                break;
            case R.id.tv_login_and_register:   //登陆注册
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intentLogin);
                }
                break;
            case R.id.iv_set:   //设置
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), SetActivity.class));
                }
                break;
            case R.id.iv_message: //信息
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), MessageActivity.class));
                }
                break;
            case R.id.ll_techan_dingdan://特产订单
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), SpecialtyOrderActivity.class));
                }
                break;

            case R.id.ll_yuding_dingdan://预定订单
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), ReserveOrderActivity.class));
                }
                break;
            case R.id.ll_shangcheng_dingdan://商城订单
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), ShopOrderActivity.class));
                }
                break;
            case R.id.ll_fudai_dingdan:
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), LuckyBagOrderActivity.class));
                }
                break;
            case R.id.ll_gouwuche://购物车
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                }
                break;

            case R.id.ll_wodefangshi://我的房時
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), MyRoomTimeActivity.class));
                }
                break;
            case R.id.ll_wode_chuzhi://我的儲值
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), MyStoredValueActivity.class));
                }
                break;

            case R.id.ll_xiaoxi_jilu://消费记录
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
//                    startActivity(new Intent(getActivity(), PurchaseHistoryActivity.class));
                    startActivity(new Intent(getActivity(), ExpenseCalendarActivity1.class));
                }
                break;
            case R.id.ll_wode_fudai:// 我的福袋
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), MyLuckyBagActivity.class));
                }
                break;
            case R.id.ll_wode_qianbao://钱包
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), MyPurseActivity.class));
                }
                break;
            case R.id.ll_kanfang_jilu://看房记录
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), LookRoomHistoryActivity.class));
                }
                break;

            case R.id.wode_shoucang://我的收藏
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), MyCollectionActivity.class));
                }
                break;

            case R.id.wode_tuijian://我的推荐
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), MyReferralCodeActivity.class));
                }

                break;

            case R.id.ll_wode_dizhi://我的地址
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    ToolUtil.goToLogin(getContext());
                } else {
                    startActivity(new Intent(getActivity(), AddressActivity.class));
                }
                break;
        }
    }


}
