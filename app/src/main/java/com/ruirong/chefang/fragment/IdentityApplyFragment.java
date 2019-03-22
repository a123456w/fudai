package com.ruirong.chefang.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.http.RemoteApi;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dillon on 2017/7/14.
 */

public class IdentityApplyFragment extends BaseFragment {
    @BindView(R.id.iv_ID_front)
    ImageView ivIDFront;
    @BindView(R.id.iv_delete_ID_front)
    ImageView ivDeleteIDFront;
    @BindView(R.id.rl_ID_front)
    RelativeLayout rlIDFront;
    @BindView(R.id.rl_upload_ID_front)
    RelativeLayout rlUploadIDFront;
    @BindView(R.id.iv_ID_otherside)
    ImageView ivIDOtherside;
    @BindView(R.id.iv_delete_ID_otherside)
    ImageView ivDeleteIDOtherside;
    @BindView(R.id.rl_ID_otherside)
    RelativeLayout rlIDOtherside;
    @BindView(R.id.rl_upload_ID_otherside)
    RelativeLayout rlUploadIDOtherside;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.et_name)
    EditText etName;

    private ArrayList<String> IDFront = new ArrayList<>();    // 身份证正面
    private ArrayList<String> IDBack = new ArrayList<>();     // 身份证负面

    private final int REQUEST_CODE_CHOOSE_ID_FRONT = 1;
    private final int REQUEST_CODE_CHOOSE_ID_BACK = 2;

    Unbinder unbinder;
    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_identity, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);

    }

    @Override
    public void getData() {

    }
    /**
     * 选择身份证正面
     */
    @OnClick(R.id.rl_upload_ID_front)
    public void selectIDFront() {
        startActivityForResult(BGAPhotoPickerActivity.newIntent(getActivity(), null, 1, IDFront, false), REQUEST_CODE_CHOOSE_ID_FRONT);
    }

    /**
     * 删除身份证正面
     */
    @OnClick(R.id.iv_delete_ID_front)
    public void deleteIDFront() {
        rlIDFront.setVisibility(View.INVISIBLE);
        rlUploadIDFront.setVisibility(View.VISIBLE);
        IDFront.clear();
    }

    @OnClick({R.id.iv_ID_front, R.id.iv_ID_otherside,})
    public void showBigPhoto(View view) {
        switch (view.getId()) {
            case R.id.iv_ID_front:
                startActivity(BGAPhotoPreviewActivity.newIntent(getActivity(), null, IDFront, 0));
                break;
            case R.id.iv_ID_otherside:
                startActivity(BGAPhotoPreviewActivity.newIntent(getActivity(), null, IDBack, 0));
        }
    }

    /**
     * 选择身份证背面
     */
    @OnClick(R.id.rl_upload_ID_otherside)
    public void selectIDBack() {
        startActivityForResult(BGAPhotoPickerActivity.newIntent(getActivity(), null, 1, IDBack, false), REQUEST_CODE_CHOOSE_ID_BACK);
    }

    /**
     * 删除身份证背面
     */
    @OnClick(R.id.iv_delete_ID_otherside)
    public void deleteIDBack() {
        rlIDOtherside.setVisibility(View.INVISIBLE);
        rlUploadIDOtherside.setVisibility(View.VISIBLE);
        IDBack.clear();
    }

    /**
     * 提交
     */
    @OnClick(R.id.btn_commit)
    public void commit() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.showToast(getActivity(), "请输入真实姓名");
            return;
        }
        if (IDFront.size() == 0) {
            ToastUtil.showToast(getActivity(), "请添加身份证正面照");
            return;
        }
        if (IDBack.size() == 0) {
            ToastUtil.showToast(getActivity(), "请添加身份证背面照");
            return;
        }

        File fileIDFront = new File(IDFront.get(0));
        RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileIDFront);
        File fileIDBack = new File(IDBack.get(0));
        RequestBody IDBackBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileIDBack);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("token", new PreferencesHelper(getActivity()).getToken())
                .addFormDataPart("real_name", etName.getText().toString())
                .addFormDataPart("card_pics[]", fileIDFront.getName(), IDFrontBody)
                .addFormDataPart("card_pics[]", fileIDBack.getName(), IDBackBody);

        showLoadingDialog("上传中...");

        BaseSubscriber<BaseBean<Object>> postPhotoSubscriber = new BaseSubscriber<BaseBean<Object>>(getActivity(), null) {

            @Override
            public void onNext(BaseBean<Object> objectBaseBean) {
                super.onNext(objectBaseBean);
                hideLoadingDialog();
                if (objectBaseBean.code == 0) {
                    getActivity().finish();
                }
                ToastUtil.showToast(getActivity(), objectBaseBean.message);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                hideLoadingDialog();
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).uploadIdentity(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postPhotoSubscriber);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_ID_FRONT) {
                // 显示身份证正面
                String path = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
                IDFront.add(path);
                Glide.with(getActivity()).load("file://" + path).into(ivIDFront);
                rlUploadIDFront.setVisibility(View.INVISIBLE);
                rlIDFront.setVisibility(View.VISIBLE);

            } else if (requestCode == REQUEST_CODE_CHOOSE_ID_BACK) {
                // 显示身份证背面
                String path = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
                IDBack.add(path);
                Glide.with(getActivity()).load("file://" + path).into(ivIDOtherside);
                rlUploadIDOtherside.setVisibility(View.INVISIBLE);
                rlIDOtherside.setVisibility(View.VISIBLE);
            }
        }
    }
}
