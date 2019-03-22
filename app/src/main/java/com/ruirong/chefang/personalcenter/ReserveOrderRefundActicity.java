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

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ApplicationForRefundActivity;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.bean.ReserveOrderBean;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
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
 * Created by 16690 on 2018/4/9.
 * describe:
 */

public class ReserveOrderRefundActicity extends BaseActivity {

    @BindView(R.id.tv_reasons_for_content)
    TextView tvReasonsForContent;
    @BindView(R.id.reasons_for_refunds_pic)
    ImageView reasonsForRefundsPic;
    @BindView(R.id.tv_reasons_for_refunds)
    RelativeLayout tvReasonsForRefunds;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.tv_explain)
    EditText tvExplain;
    @BindView(R.id.tv_tijioa)
    TextView tvTijioa;
    @BindView(R.id.js_image_title)
    ImageView jsImageTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_specif)
    TextView tvSpecif;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_money)
    TextView tvMoney;


    private String explain = "";

    private ReserveOrderBean.ListBean data;

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_reserve_order_refund;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("预订订单退款");

        data = (ReserveOrderBean.ListBean) getIntent().getSerializableExtra("data");

        if (data == null) {
            finish();
        }


    }

    @Override
    public void getData() {
        updateUI();
    }

    private void updateUI() {
        goodsPrice.setText("￥" + data.getApyment_money());

        GlideUtil.display(ReserveOrderRefundActicity.this, Constants.IMG_HOST + data.getCover(), jsImageTitle);
        tvContent.setText(data.getSp_name());
        tvSpecif.setText(data.getSpecif());
        tvMoney.setText("￥" + data.getMoney());
        tvTime.setText(DateUtil.toDate(data.getCreate_time(), DateUtil.FORMAT_MD_CN) + " - " + DateUtil.toDate(data.getCreate_time(), DateUtil.FORMAT_MD_CN));
    }

    @OnClick({R.id.tv_reasons_for_refunds, R.id.tv_tijioa})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reasons_for_refunds:
                //退款原因
                storeValueDialog();
                break;
            case R.id.tv_tijioa:
                //TODO 提交
                if (TextUtils.isEmpty(reasonText)) {
                    ToastUtil.showToast(ReserveOrderRefundActicity.this, "请选择退款原因");
                    return;
                }

                explain = tvExplain.getText().toString().trim();

                submit(new PreferencesHelper(ReserveOrderRefundActicity.this).getToken(),
                        data.getOrder_id(), reasonText, data.getApyment_money(), explain);

                break;
        }
    }


    /**
     * 取消原因
     */
    private void storeValueDialog() {
        mDialog = new Dialog(ReserveOrderRefundActicity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ReserveOrderRefundActicity.this).inflate(R.layout.cancel_reason, null);
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
                    ToastUtil.showToast(ReserveOrderRefundActicity.this, "请选择原因");
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
        for (int i = 0; i < 4; i++) {
            SelectedStateBean selectedStateBean = new SelectedStateBean();
            selectedStateBean.setTrue(false);
            switch (i) {
                case 0:
                    selectedStateBean.setTitle("发票问题");
                    break;
                case 1:
                    selectedStateBean.setTitle("预订错了");
                    break;
                case 2:
                    selectedStateBean.setTitle("酒店服务态度不好");
                    break;
                case 3:
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
     * 退款
     *
     * @param token
     * @param order_id
     * @param cancelorder
     * @param tui_price
     * @param tui_content
     */
    private void submit(String token, String order_id, String cancelorder, String tui_price, String tui_content) {
        HttpHelp.getInstance().create(RemoteApi.class).reserveRefund(token, order_id, cancelorder, tui_price, tui_content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ReserveOrderRefundActicity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(ReserveOrderRefundActicity.this, baseBean.message);
                            EventBusUtil.post(new UpdateBeforeUIDateEvent());
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ReserveOrderRefundActicity.this);
                        } else {
                            ToastUtil.showToast(ReserveOrderRefundActicity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
