package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ComBolAdaper;
import com.ruirong.chefang.adapter.SelectionDateAdapter;
import com.ruirong.chefang.adapter.SelectionWeekAdapter;
import com.ruirong.chefang.bean.ComBinBean;
import com.ruirong.chefang.bean.ReservepackageBean;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/3 0003
 * describe:  娱乐 确认订单
 */
public class EntertainmentAffirmOrderActivity extends BaseActivity {

    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_dicinfo)
    TextView tvDicinfo;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.can_content_view)
    com.ruirong.chefang.view.CustomExpandableListView expandableListView;
    @BindView(R.id.selection_date)
    NoScrollGridView selectionDate;
    @BindView(R.id.selection_time)
    NoScrollGridView selectionTime;
    @BindView(R.id.package_detailed)
    TextView packageDetailed;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private String package_id;

    private Dialog mDialog;

    //日期选择
    private SelectionDateAdapter selectionDateAdapter;
    private List<SelectedStateBean> selectionDateList = new ArrayList<>();
    //时间选择
    private SelectionWeekAdapter selectionWeekAdapter;
    private List<SelectedStateBean> selectionWeekList = new ArrayList<>();
    private List<SelectedStateBean> aFalse;
    private String date;
    private String time;
    private String price;
    private String shop_id;

    private ComBolAdaper comBolAdaper;

    private List<ComBinBean.GoodidsBean> monthBeans = new ArrayList<>();

    private List<String> headYears = new ArrayList<>();

    private String myEtCall = "" ;
    private String myEtPhone = "" ;

    private static final String KTVSHOPID = "KTVSHOPID";
    private static final String KTVSHOPDESTID = "KTVSHOPDESTID";
    private String myShopid = "" ;
    private String myDeskid = "" ;
    @Override
    public int getContentView() {
        return R.layout.activity_entertainment_affirm_order;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("确认订单");

        myShopid = getIntent().getStringExtra(KTVSHOPID);
        myDeskid = getIntent().getStringExtra(KTVSHOPDESTID);

        package_id = getIntent().getStringExtra("package_id");
        shop_id = getIntent().getStringExtra("shop_id");
        if (package_id == null || shop_id == null) {
            finish();
        }

        selectionDateAdapter = new SelectionDateAdapter(selectionDate);
        selectionDate.setAdapter(selectionDateAdapter);
        selectionDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFalse(selectionDateAdapter.getData());
                selectionDateAdapter.getData().get(position).setTrue(true);
                date = selectionDateAdapter.getData().get(position).getName();
                selectionDateAdapter.notifyDataSetChanged();
            }
        });

        selectionWeekAdapter = new SelectionWeekAdapter(selectionTime);
        selectionTime.setAdapter(selectionWeekAdapter);
        selectionTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFalse(selectionWeekAdapter.getData());
                selectionWeekAdapter.getData().get(position).setTrue(true);
                time = selectionWeekAdapter.getData().get(position).getName();
                selectionWeekAdapter.notifyDataSetChanged();
            }
        });

        for (int i = 0; i < 7; i++) {
            SelectedStateBean selectedStateBean = new SelectedStateBean();
            String oldDate = TimeUtil.getOldDate(i);
            selectedStateBean.setName(oldDate);
            selectedStateBean.setTitle(TimeUtil.getWeek(oldDate));
            selectedStateBean.setTrue(false);
            selectionDateList.add(selectedStateBean);
        }
        selectionDateAdapter.setData(selectionDateList);


    }


    @Override
    public void getData() {
        reservepackage(new PreferencesHelper(EntertainmentAffirmOrderActivity.this).getToken(), package_id);

        getCombinData(new PreferencesHelper(EntertainmentAffirmOrderActivity.this).getToken(), package_id, "1");

    }




    /**
     * 填写称呼和电话号码的弹框
     */
    private void uploadHeadImage() {

        mDialog = new Dialog(EntertainmentAffirmOrderActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(EntertainmentAffirmOrderActivity.this).inflate(R.layout.dialog_pic_gain, null);
        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//      lp.y = ScreenUtil.getBottomStatusHeight(PersionMessageActivity.this);
        dialogwindow.setAttributes(lp);
        mDialog.show();

        TextView tvCancle,tvSure;
        final EditText etCall ,etPhone;
        tvSure = (TextView) inflate.findViewById(R.id.tv_sure);
        tvCancle = (TextView) inflate.findViewById(R.id.tv_cancle);

        etCall = (EditText)inflate.findViewById(R.id.et_call);
        etPhone = (EditText)inflate.findViewById(R.id.et_phone);

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                myEtCall = etCall.getText().toString().trim();
                myEtPhone = etPhone.getText().toString().trim();
                mDialog.dismiss();

                if (TextUtils.isEmpty(myEtCall)){
                    ToastUtil.showToast(EntertainmentAffirmOrderActivity.this,"请输入您的称呼");
                    return;
                }
                if (TextUtils.isEmpty(myEtPhone)){
                    ToastUtil.showToast(EntertainmentAffirmOrderActivity.this,"请输入您的电话号码");
                    return;
                }

                okorder(new PreferencesHelper(EntertainmentAffirmOrderActivity.this).getToken(), package_id, shop_id, null,
                        TimeUtil.dateToStamp(date), time,myEtCall,myEtPhone);



            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();


            }
        });
    }

    /**
     * 传递商品id和桌台号给订单页面
     * @param context
     * @param shop_id
     * @param desk_id
     */
    public static void startActivityWithParmeter(Context context,String shop_id,String desk_id){
        Intent intent = new Intent(context,EntertainmentAffirmOrderActivity.class);
        intent.putExtra(KTVSHOPID,shop_id);
        intent.putExtra(KTVSHOPDESTID,desk_id);
        context.startActivity(intent);
    }


    /**
     * 获取套餐详情
     *
     * @param token
     * @param id
     * @param xinban 新版本时候需要传递 1 。
     */
    public void getCombinData(String token, String id, String xinban) {
        HttpHelp.getInstance().create(RemoteApi.class).CombinData(token, id, xinban)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ComBinBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ComBinBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
           /*                 GlideUtil.display(EntertainmentAffirmOrderActivity.this,
                                    Constants.IMG_HOST + baseBean.data.getPic(), goodsPic);
                            shopName.setText(baseBean.data.getShopname());
                            shopName.getPaint().setFakeBoldText(true);   //加粗
                            tvUserName.setText(baseBean.data.getName());
                            tvDicinfo.setText(baseBean.data.getDicinfo());
                            tvPrice.setText(baseBean.data.getAll_price());
                            tvShopPrice.setText(baseBean.data.getShop_price());
                            tvShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            total.setText("￥" + baseBean.data.getAll_price());
                            price = baseBean.data.getAll_price();*/



                            monthBeans = baseBean.data.getGoodids();
                            if (monthBeans != null && monthBeans.size() > 0) {
                                // 遍历所有group,将所有项设置成默认展开
                                for (int i = 0; i < monthBeans.size(); i++) {
                                    headYears.add(monthBeans.get(i).getTitle());
                                }
                                Log.e("year",headYears.size()+"year长度");
                            }

                            comBolAdaper = new ComBolAdaper(monthBeans,EntertainmentAffirmOrderActivity.this,headYears);
                            expandableListView.setAdapter(comBolAdaper);
                            expandableListView.setGroupIndicator(null);

                            if (monthBeans != null && monthBeans.size() > 0) {
                                int groupCount = comBolAdaper.getGroupCount();
                                for (int i = 0; i < groupCount; i++) {
                                    expandableListView .expandGroup(i);
                                    Log.e("year",monthBeans.size()+"year长度");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
          }
    private void reservepackage(String token, String package_id) {

        HttpHelp.getInstance().create(RemoteApi.class).reservepackage(token, package_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReservepackageBean>>(this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ReservepackageBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {

                                GlideUtil.display(EntertainmentAffirmOrderActivity.this,
                                        Constants.IMG_HOST + baseBean.data.getPic(), goodsPic);
                                shopName.setText(baseBean.data.getName());
                                shopName.getPaint().setFakeBoldText(true);   //加粗
                                tvUserName.setText(baseBean.data.getName());
                                tvDicinfo.setText(baseBean.data.getBiaoqian());
                                tvPrice.setText(baseBean.data.getAll_price());
                                tvShopPrice.setText(baseBean.data.getShop_price());
                                tvShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                                total.setText("￥" + baseBean.data.getAll_price());
                                price = baseBean.data.getAll_price();

                                if (baseBean.data.getGoodids() != null && baseBean.data.getGoodids().size() > 0) {
                                    String str = "";
                                    for (int i = 0; i < baseBean.data.getGoodids().size(); i++) {
                                        str += baseBean.data.getGoodids().get(i).getName() + "x" + baseBean.data.getGoodids().get(i).getNum() + "  ";
                                    }
                                    if (!TextUtils.isEmpty(str)) {
                                        packageDetailed.setText(str);
                                    }
                                }

                                if (baseBean.data.getDao_time() != null && baseBean.data.getDao_time().size() > 0) {
                                    for (int i = 0; i < baseBean.data.getDao_time().size(); i++) {
                                        SelectedStateBean selectedStateBean = new SelectedStateBean();
                                        selectedStateBean.setName(baseBean.data.getDao_time().get(i));
                                        selectedStateBean.setTrue(false);
                                        selectionWeekList.add(selectedStateBean);
                                    }
                                    selectionWeekAdapter.setData(selectionWeekList);
                                }

                            }
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(EntertainmentAffirmOrderActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }


    public void setFalse(List<SelectedStateBean> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setTrue(false);
        }
    }

    @OnClick({R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(date)) {
                    ToastUtil.showToast(EntertainmentAffirmOrderActivity.this, "请选择日期");
                    return;
                }
                if (TextUtils.isEmpty(time)) {
                    ToastUtil.showToast(EntertainmentAffirmOrderActivity.this, "请选择时间段");
                    return;
                }

                uploadHeadImage();


                break;

        }
    }

    private void okorder(final String token, String id, String shop_id, String content, String time, String time_duan,String pName,String pPhone) {
        HttpHelp.getInstance().create(RemoteApi.class).ktvOkorder(token, id, shop_id, content, time, time_duan,pName,pPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + " " + baseBean.message);
                        if (baseBean.code == 0) {
                            Intent intent = new Intent(EntertainmentAffirmOrderActivity.this, ImmediatelyPaymentActivity.class);
                            intent.putExtra("place_type", "2");
                            intent.putExtra("shop_price", price);
                            intent.putExtra("order_sn", baseBean.data);
                            startActivity(intent);
                            finish();
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(EntertainmentAffirmOrderActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });


    }
}
