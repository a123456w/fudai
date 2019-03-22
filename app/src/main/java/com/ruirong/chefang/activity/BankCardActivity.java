package com.ruirong.chefang.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.security.rp.RPSDK;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.qlzx.mylibrary.widget.pullToRefresh.GoogleCircleProgressView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.BankCardAdapter;
import com.ruirong.chefang.bean.BankCardBean;
import com.ruirong.chefang.bean.IdentityStatusBean;
import com.ruirong.chefang.bean.IdentyBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.common.Constants;
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
 * 银行卡
 * Created by dillon on 2017/4/10.
 */

public class BankCardActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.rv_bankcard)
    RecyclerView rvBankcard;
    @BindView(R.id.btn_addbankcard)
    Button btnAddbankcard;
    @BindView(R.id.tv_authentication)
    TextView tvAuthentication;
    @BindView(R.id.linearlayout_authentication)
    LinearLayout linearlayoutAuthentication;
    @BindView(R.id.swipe_target)
    LinearLayout swipeTarget;
    @BindView(R.id.googleProgress)
    GoogleCircleProgressView googleProgress;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private List<BankCardBean.ListBean> listBeanList = new ArrayList<>();
    private BankCardAdapter bankCardAdapter;
    private BaseSubscriber<BaseBean<BankCardBean>> bankcardSubscriber;
    private int page = 1;
    private BaseSubscriber<BaseBean<IdentityStatusBean>> identityStatusSubscriber;

    private boolean isBack = false;

    private BaseSubscriber<BaseBean<UserInforBean>> userInforSubscriber;
    private String idToken = "";
    private String isCard = "";
    UserInforBean userInforBean;


    @Override
    public int getContentView() {
        return R.layout.activity_bankcard;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Loading);
        titleBar.setTitleText("银行卡");
        isBack = getIntent().getBooleanExtra(Constants.ISBACK, false);
        bankCardAdapter = new BankCardAdapter(rvBankcard);
        rvBankcard.setLayoutManager(new LinearLayoutManager(this));
        rvBankcard.addItemDecoration(new RecycleViewDivider(this));
        rvBankcard.setAdapter(bankCardAdapter);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        bankCardAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                if (isBack) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.BANKCARDID, ((BankCardBean.ListBean) bankCardAdapter.getItem(position)).getId());
                    intent.putExtra(Constants.BANKCARDNUM, ((BankCardBean.ListBean) bankCardAdapter.getItem(position)).getBank_card_number());
                    intent.putExtra(Constants.BANKCARDNAME, ((BankCardBean.ListBean) bankCardAdapter.getItem(position)).getBank());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent(BankCardActivity.this, BankCardDetailActivity.class);
                    int bgColor = 0;
                    intent.putExtra("bankcardid", ((BankCardBean.ListBean) bankCardAdapter.getItem(position)).getId());
                    intent.putExtra("bankName", ((BankCardBean.ListBean) bankCardAdapter.getItem(position)).getBank());
                    intent.putExtra("bankNumber", ((BankCardBean.ListBean) bankCardAdapter.getItem(position)).getBank_card_number());


                    switch (((BankCardBean.ListBean) bankCardAdapter.getItem(position)).getBank()) {
                        case "中国工商银行":
                            bgColor = R.color.blue;
                            break;
                        case "中国建设银行":
                            bgColor = R.color.light_grey;
                            break;
                        case "中国招商银行":
                            bgColor = R.color.black;
                            break;
                        case "中国农业银行":
                            bgColor = R.color.green;
                            break;
                        default:
                            bgColor = R.color.green;
                            break;
                    }
                    intent.putExtra("bgColor", bgColor);
                    startActivityForResult(intent, Constants.BANKCARKDETAILREQUESTCODE);
                }
            }
        });
    }

    @Override
    public void getData() {
        bankcardSubscriber = new BaseSubscriber<BaseBean<BankCardBean>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<BankCardBean> bankCardBeanBaseBean) {
                super.onNext(bankCardBeanBaseBean);
                swipeToLoadLayout.setLoadingMore(false);
                swipeToLoadLayout.setRefreshing(false);
                if (bankCardBeanBaseBean.code == 0) {
                    if ("0".equals(bankCardBeanBaseBean.data.getStatus())) {
                        linearlayoutAuthentication.setVisibility(View.VISIBLE);
                    } else if ("1".equals(bankCardBeanBaseBean.data.getStatus())) {
                        linearlayoutAuthentication.setVisibility(View.GONE);
                    }
                    listBeanList = bankCardBeanBaseBean.data.getList();
                    if (page == 1) {
                        bankCardAdapter.setData(listBeanList);
                    } else {
                        if (listBeanList.size() == 0) {
                            ToastUtil.showToast(BankCardActivity.this, "我是有底线的");
                        } else {
                            bankCardAdapter.addMoreData(listBeanList);
                        }
                    }
                } else if (bankCardBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(BankCardActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).bankCardList(page, new PreferencesHelper(BankCardActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bankcardSubscriber);
    }

    @OnClick({R.id.btn_addbankcard, R.id.tv_authentication})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addbankcard:

                checkIdentity(new PreferencesHelper(BankCardActivity.this).getToken());
//                Intent intent = new Intent(BankCardActivity.this, AddBankCardActivity.class);
//                startActivityForResult(intent, Constants.ADDBANKCARDREQUESTCODE);
                break;
            case R.id.tv_authentication:
//                startActivity(new Intent(BankCardActivity.this, PersionMessageActivity.class));
                identity(new PreferencesHelper(BankCardActivity.this).getToken());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADDBANKCARDREQUESTCODE || requestCode == Constants.BANKCARKDETAILREQUESTCODE) {
            if (resultCode == RESULT_OK) {
                page = 1;
                getData();
            }
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bankcardSubscriber != null && bankcardSubscriber.isUnsubscribed()) {
            bankcardSubscriber.unsubscribe();
        }
        if (identityStatusSubscriber != null && identityStatusSubscriber.isUnsubscribed()) {
            identityStatusSubscriber.unsubscribe();
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    /**
     * 查询是否认证
     *
     * @param token
     */
    private void checkIdentity(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).idention(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IdentyBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<IdentyBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            IdentyBean identyBean = baseBean.data;
                            idToken = identyBean.getToken();
                            isCard = identyBean.getIs_card();
                            if ("0".equals(isCard)) {
                                //没有认证 去认证
                                showHintDialog();
                            } else {
                                //添加银行卡
                                Intent intent = new Intent(BankCardActivity.this, AddBankCardActivity.class);
                                startActivityForResult(intent, Constants.ADDBANKCARDREQUESTCODE);
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 获取token
     *
     * @param token
     */
    public void identity(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).idention(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IdentyBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<IdentyBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            IdentyBean identyBean = baseBean.data;
                            idToken = identyBean.getToken();
                            isCard = identyBean.getIs_card();

                            identytyAli(idToken);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void showHintDialog() {
        //未登录
        AlertDialog.Builder builder = new AlertDialog.Builder(BankCardActivity.this);
        builder.setTitle("提示");
        builder.setMessage("您未认证身份，请先完成认证");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                identytyAli(idToken);
            }
        });
        builder.create().show();
    }

    /**
     * 阿里实名认证
     *
     * @param token
     */
    public void identytyAli(String token) {

        RPSDK.start(token, BankCardActivity.this,
                new RPSDK.RPCompletedListener() {
                    @Override
                    public void onAuditResult(RPSDK.AUDIT audit) {
                        if (audit == RPSDK.AUDIT.AUDIT_PASS) { //认证通过
                            if ("0".equals(isCard)) {
                                updateIdentity(new PreferencesHelper(BankCardActivity.this).getToken());
                            }
                        } else if (audit == RPSDK.AUDIT.AUDIT_FAIL) { //认证不通过
                        } else if (audit == RPSDK.AUDIT.AUDIT_NOT) { //未认证，用户取消
                        } else if (audit == RPSDK.AUDIT.AUDIT_EXCEPTION) { //系统异常
                        }
                    }
                });


    }

    /**
     * 上传实名认证后数据
     *
     * @param token
     */
    public void updateIdentity(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).updateIdention(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            getData();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }


}
