package com.ruirong.chefang.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.MessageDetailBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 消息详情页
 * Created by dillon on 2017/6/11.
 */

public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private String id;
    private boolean isReaded=false;//网络成功时改为true


    private BaseSubscriber<BaseBean<MessageDetailBean>> messageDetailSubscriber;
    private int position;

    @Override
    public int getContentView() {
        return R.layout.activity_messagedetial;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("消息详情");
        id = getIntent().getStringExtra("messageId");
        position = getIntent().getIntExtra("position", -1);
    }

    @Override
    public void getData() {
        messageDetailSubscriber = new BaseSubscriber<BaseBean<MessageDetailBean>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<MessageDetailBean> messageDetailBeanBaseBean) {
                super.onNext(messageDetailBeanBaseBean);
                if (messageDetailBeanBaseBean.code==0){
                    MessageDetailBean data = messageDetailBeanBaseBean.data;
                    if (!TextUtils.isEmpty(data.getText())){
                        tvTitle.setText(data.getText());
                    }
                    if (!TextUtils.isEmpty(data.getContent())){
                        tvContent.setText(data.getContent());
                    }

                    String pic = data.getPic();
                    if (TextUtils.isEmpty(pic)){
                        ivPic.setVisibility(View.GONE);
                    }else {
                        GlideUtil.display(MessageDetailActivity.this, com.qlzx.mylibrary.common.Constants.IMG_HOST+pic,ivPic);
                    }

                    isReaded=true;
                }else if (messageDetailBeanBaseBean.code==4){
                    ToolUtil.loseToLogin(MessageDetailActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).messageDetail(id, new PreferencesHelper(MessageDetailActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageDetailSubscriber);

    }

    /**
     *
     * @param context
     * @param id
     * @param position 该item在消息列表中的位置
     * @param requestCode
     */
    public static void startActivityForResult(Activity context, String id,int position, int requestCode) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra("messageId", id);
        intent.putExtra("position", position);
        context.startActivityForResult(intent,requestCode);
    }

    /**
     * 返回
     */
    @OnClick(R.id.iv_titlebar_left)
    public void back(){
        Intent intent = new Intent();
        intent.putExtra("position",position);
        intent.putExtra("isReaded",isReaded);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.putExtra("position",position);
            intent.putExtra("isReaded",isReaded);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
