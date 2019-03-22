package com.ruirong.chefang.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.MySearchEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.SharedPrefsStrListUtil;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.FlowLayout;
import com.ruirong.chefang.widget.Util;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/29 0029
 * describe:  搜索页面
 */
public class SearchActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_left_arrows)
    ImageView ivLeftArrows;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;
    @BindView(R.id.tv_search)
    TextView tvSearch;

    private List<String> nerList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        EventBusUtil.register(this);
        hideTitleBar();/***/
    }

    @Override
    public void getData() {
        getSearchHistory();
    }

    /**
     * 获取数据
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reGetList (MySearchEvent event){
      getData();
    }


    private void getSearchHistory() {
        nerList.clear();
        nerList = SharedPrefsStrListUtil.getStrListValue(SearchActivity.this, Constants.SEARCHLISTKEY);
        HashSet h = new HashSet(nerList);
        nerList.clear();
        nerList.addAll(h);
        Log.e("search",nerList.size()+"ss");

        flowlayout.removeAllViews();
        if (nerList.size() > 0) {
            addHistoryView(nerList);
            ivPic.setVisibility(View.VISIBLE);
        } else {
            ivPic.setVisibility(View.GONE);
        }



      /*
        LogUtil.i("mac address: " + ToolUtil.getMacAddr());
        HttpHelp.getInstance().create(RemoteApi.class).getSearchNer(ToolUtil.getMacAddr())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<String>>>(SearchActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List<String>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            nerList = baseBean.data;
                            if (nerList == null || nerList.size() == 0) {
                                ivPic.setVisibility(View.GONE);
                            } else {
                                addHistoryView(nerList);
                                ivPic.setVisibility(View.VISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });*/
    }

    /**
     * 返回
     */
    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }

    /**
     * 删除搜索记录
     */
    private void delSearchHistory() {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).delSearchNer(ToolUtil.getMacAddr())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(SearchActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            SharedPrefsStrListUtil.clear(SearchActivity.this);
                            nerList.clear();
                            flowlayout.removeAllViews();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


    private void addHistoryView(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(SearchActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            textView.setBackground(SearchActivity.this.getResources().getDrawable(R.drawable.bg_home_search_fl_textview_bg));
            int topBottom = Util.dip2px(getApplicationContext(), 5);
            int leftRight = Util.dip2px(getApplicationContext(), 13);

            textView.setPadding(leftRight, topBottom, leftRight, topBottom);

            textView.setTextSize(12);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(textView.getLayoutParams());

            lp.setMargins(10, 20, 20, 10);
            textView.setLayoutParams(lp);

            textView.setText(list.get(i));
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchResultActivity.startActivityWithResult(SearchActivity.this, ((TextView) v).getText().toString());
                }
            });
            flowlayout.addView(textView);
        }//for循环结束
    }

    @OnClick({R.id.tv_search, R.id.iv_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                if (TextUtils.isEmpty(etSearch.getText().toString().trim())) {
                    ToastUtil.showToast(SearchActivity.this, "搜索内容不能为空");
                    return;
                } else {
                    SearchResultActivity.startActivityWithResult(SearchActivity.this, etSearch.getText().toString().trim());
                }

                break;
            case R.id.iv_pic:
                delSearchHistory();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
