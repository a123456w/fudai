package com.qlzx.mylibrary.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.qlzx.mylibrary.R;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.TitleBar;


/**
 * Created by 87901 on 2016/10/8.
 */

public abstract class BaseFragment extends Fragment implements LoadingLayout.OnReloadListener {

    public Context mContext;
    FrameLayout frameLayout;
    View view;
    protected LoadingLayout loadingLayout;
    protected TitleBar titleBar;
    public DialogUtil mDialogUtil;
    public Dialog mBaseDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base, container, false);
        titleBar = (TitleBar) view.findViewById(R.id.titleBar);
        titleBar.setVisibility(View.GONE);
        loadingLayout = (LoadingLayout) view.findViewById(R.id.loading_layout);
        loadingLayout.setOnReloadListener(this);
        frameLayout = (FrameLayout) view.findViewById(R.id.container_view);
        frameLayout.addView(getContentView(frameLayout));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mDialogUtil = new DialogUtil(context);
    }



    public abstract View getContentView(FrameLayout frameLayout);

    public void showLoadingDialog(String msg) {
        if (msg!=null) {
            if (mBaseDialog != null && mBaseDialog.isShowing()) {
                mBaseDialog.dismiss();
                mBaseDialog = null;
            }
            mBaseDialog = mDialogUtil.showLoading(msg);
        }
    }

    public void hideLoadingDialog() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.dismiss();
        }
    }



    public abstract void initView();
    public abstract void getData();

    @Override
    public void onReload(View v) {
        reload();
    }

    protected void reload() {

    }

}
