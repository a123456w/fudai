package com.ruirong.chefang.personalcenter;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ShopOrderDetailsActivity;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.adapter.ShopOrderCommodityAdapter;
import com.ruirong.chefang.adapter.ShopOrderGoodsAdapter;
import com.ruirong.chefang.bean.RefundGoodsInfoBean;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.ShopOrderDetailBean;
import com.ruirong.chefang.event.ShopOrderRefundEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wu on 2018/4/3.
 * describe: 商城订单  退款
 */

public class ShopOrderRefundActivity extends BaseActivity {
    @BindView(R.id.tv_reasons_for_content)
    TextView tvReasonsForContent;
    @BindView(R.id.reasons_for_refunds_pic)
    ImageView reasonsForRefundsPic;
    @BindView(R.id.tv_reasons_for_refunds)
    RelativeLayout tvReasonsForRefunds;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.et_refund_amount)
    EditText etRefundAmount;
    @BindView(R.id.tv_explain)
    EditText tvExplain;
    @BindView(R.id.tv_tijioa)
    TextView tvTijioa;
    @BindView(R.id.lv_goods)
    NoScrollListView goodsList;


    private String goodsStr;
    private String allPrice;
    private String orderId;
    private String explain = "";
    private float goodsM;
    private int digits = 2;
    private boolean isMX = false;

    private ShopOrderGoodsAdapter adapter;
    private List<RefundGoodsInfoBean>  goods;

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;


    @Override
    public int getContentView() {
        return R.layout.activity_shop_order_refund;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("商城订单退款");

        goodsStr = getIntent().getStringExtra("goods");
        allPrice = getIntent().getStringExtra("all_price");
        orderId = getIntent().getStringExtra("order_id");
        if (TextUtils.isEmpty(goodsStr)  || TextUtils.isEmpty(allPrice) || TextUtils.isEmpty(orderId)){
            finish();
        }

        LogUtil.i("商城订单退款"+goodsStr);
        goodsM = Float.parseFloat(allPrice);
        adapter = new ShopOrderGoodsAdapter(goodsList);
        goodsList.setAdapter(adapter);
        goodsPrice.setText("￥"+allPrice);
        etRefundAmount.setHint("最多 ￥"+allPrice);

        etRefundAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        etRefundAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + digits + 1);
                        etRefundAmount.setText(s);
                        etRefundAmount.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etRefundAmount.setText(s);
                    etRefundAmount.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etRefundAmount.setText(s.subSequence(0, 1));
                        etRefundAmount.setSelection(1);
                        return;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    if (goodsM >= Float.parseFloat(s.toString().trim())) {
                        isMX = false;
                    } else {
                        isMX = true;
                        ToastUtil.showToast(ShopOrderRefundActivity.this, "最多可退款项" + goodsM);
                    }
                }
            }
        });
    }

    @Override
    public void getData() {
        goods = GsonToArrayList(goodsStr,RefundGoodsInfoBean.class);
        LogUtil.i("商城订单退款"+goods.size());
        adapter.setData(goods);

    }


    @OnClick({R.id.tv_reasons_for_refunds,R.id.tv_tijioa})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_reasons_for_refunds:
                //退款原因
                storeValueDialog();
                break;
            case R.id.tv_tijioa:
                //TODO 提交

                if (TextUtils.isEmpty(reasonText)) {
                    ToastUtil.showToast(ShopOrderRefundActivity.this, "请选择退款原因");
                    return;
                }
                if (isMX) {
                    ToastUtil.showToast(ShopOrderRefundActivity.this, "最多可退款项" + goodsM);
                    return;
                }

                if (TextUtils.isEmpty(etRefundAmount.getText().toString().trim())) {
                    ToastUtil.showToast(ShopOrderRefundActivity.this, "请选择退款金额");
                    return;
                }

                explain = tvExplain.getText().toString().trim();
                String str = etRefundAmount.getText().toString();

                submit(new PreferencesHelper(ShopOrderRefundActivity.this).getToken(),
                        str,reasonText,orderId,explain);

                break;
        }
    }

    /**
     * json 解析成集合
     */

    public static <T> List<T> GsonToArrayList(String gsonString, Class<T> cls) {
        ArrayList<T> lst = new ArrayList<>();
        JsonArray array = new JsonParser().parse(gsonString).getAsJsonArray();
        Gson gson = new Gson();
        if (gson != null) {
            for(final JsonElement elem : array){
                T t = gson.fromJson(elem, cls);
                lst.add(t);
            }
        }
        return lst;
    }


    /**
     * 取消原因
     *
     */
    private void storeValueDialog() {
        mDialog = new Dialog(ShopOrderRefundActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ShopOrderRefundActivity.this).inflate(R.layout.cancel_reason, null);
        initDialogView(inflate);
        id_reason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFalse();
                reasonList.get(position).setTrue(true);
                reasonText = reasonList.get(position).getTitle();
                reasonAdapter.notifyDataSetChanged();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(reasonText)) {
                    ToastUtil.showToast(ShopOrderRefundActivity.this, "请选择原因");
                } else {
                    mDialog.dismiss();
                    tvReasonsForContent.setText(reasonText);
                }
            }
        });

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();
    }

    private void initDialogView(View inflate) {
        no = (TextView) inflate.findViewById(R.id.no);
        yes = (TextView) inflate.findViewById(R.id.yes);
        id_reason = (ListView) inflate.findViewById(R.id.id_reason);


        reasonList.clear();
        for (int i = 0; i < 7; i++) {
            SelectedStateBean selectedStateBean = new SelectedStateBean();
            selectedStateBean.setTrue(false);
            switch (i) {
                case 0:
                    selectedStateBean.setTitle("七天无理由退换货");
                    break;
                case 1:
                    selectedStateBean.setTitle("退运费");
                    break;
                case 2:
                    selectedStateBean.setTitle("发票问题");
                    break;
                case 3:
                    selectedStateBean.setTitle("拍错了");
                    break;
                case 4:
                    selectedStateBean.setTitle("卖家发错货");
                    break;
                case 5:
                    selectedStateBean.setTitle("卖家服务态度不好");
                    break;
                case 6:
                    selectedStateBean.setTitle("其他原因");
                    break;
            }
            reasonList.add(selectedStateBean);
        }

        reasonAdapter = new ReasonAdapter(id_reason);
        id_reason.setAdapter(reasonAdapter);
        reasonAdapter.setData(reasonList);


    }

    private void setFalse() {
        for (int i = 0; i < reasonList.size(); i++) {
            reasonList.get(i).setTrue(false);
        }
    }


    /**
     * 提交
     * @param token
     * @param price
     * @param reason
     * @param orderId
     * @param explainTxt
     */
    private void submit(String token, String price,String reason,String orderId,String explainTxt){
        HttpHelp.getInstance().create(RemoteApi.class).shopOrderRefund(token, price, reason,orderId,explainTxt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ShopOrderRefundActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(ShopOrderRefundActivity.this, baseBean.message);
                            EventBusUtil.post(new ShopOrderRefundEvent());
                            finish();
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(ShopOrderRefundActivity.this);
                        }else {
                            ToastUtil.showToast(ShopOrderRefundActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }
}
