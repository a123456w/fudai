package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CookingSwipBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 点餐类型选择
 * Created by dillon on 2018/5/5.
 */

public class ChooseOrderActivity extends BaseActivity {
    @BindView(R.id.btn_outdiningroom)
    Button btnOutdiningroom;
    @BindView(R.id.btn_indiningroom)
    Button btnIndiningroom;

    private static final String CHOOSEORDERSHOPID = "CHOOSEORDERSHOPID";
    private String myShopid = "";

    @Override
    public int getContentView() {
        return R.layout.activity_chooseorder;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("点餐");
        btnOutdiningroom.getPaint().setFakeBoldText(true);
        btnIndiningroom.getPaint().setFakeBoldText(true);
        myShopid = getIntent().getStringExtra(CHOOSEORDERSHOPID);
    }

    @Override
    public void getData() {

    }

    @OnClick({R.id.btn_outdiningroom, R.id.btn_indiningroom, R.id.rl_outdiningroom, R.id.rl_indiningroom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_indiningroom:
           /*     Intent intent1 = new Intent(ChooseOrderActivity.this, AllProductActivity.class);
                intent1.putExtra("shop_id", myShopid);
                startActivity(intent1);*/
                goScan();
                break;
            case R.id.rl_outdiningroom:
                OutsideOrderActivity.startActivityWithParmeter(ChooseOrderActivity.this, myShopid);
                break;
            case R.id.btn_outdiningroom:
                OutsideOrderActivity.startActivityWithParmeter(ChooseOrderActivity.this, myShopid);
                break;
            case R.id.btn_indiningroom:
        /*        Intent intent = new Intent(ChooseOrderActivity.this, AllProductActivity.class);
                intent.putExtra("shop_id", myShopid);
                startActivity(intent);*/
                goScan();
                break;
        }
    }


    /**
     * 进入扫描页
     */
    public void goScan() {

//        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
//        intentIntegrator
//                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
//                .setOrientationLocked(false)//扫描方向固定
//                .setPrompt("将条形码置于取景框内")
//                .setCaptureActivity(ScanAndPaymentActivity.class) // 设置自定义的activity是CustomActivity
//                .initiateScan(); // 初始化扫描
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);

        intentIntegrator
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setOrientationLocked(false)//扫描方向固定
                .setPrompt("将条形码/二维码置于取景框内")
                .setCaptureActivity(CookingSwipActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描
//
//        IntentIntegrator.forSupportFragment(this).setCaptureActivity(ScanActivity.class).initiateScan();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
//                ToastUtil.showToast(mContext, "Cancelled");
            } else {
//                ToastUtil.showToast(mContext, "Scanned: " + result.getContents());
                //商家二维码会返回类似index.php?s=/Admin/Store/code/marking/6A75B12FFBB68B4BD8EAB4A72132BA7F9312.html
                //截取6A75B12FFBB68B4BD8EAB4A72132BA7F9312部分
                String content = result.getContents();
                Gson gson = new Gson();
                Log.e("choseorder",content);
                CookingSwipBean cookingSwipBean = gson.fromJson(content,CookingSwipBean.class);
                Log.e("choseorder",cookingSwipBean.getDiancanid());

/*
                Intent intent = new Intent(ChooseOrderActivity.this, AllProductActivity.class);
                intent.putExtra("shop_id", myShopid);
                intent.putExtra("desk_id",cookingSwipBean.getNum());
                startActivity(intent);*/

                AllProductActivity.startActivityWith(ChooseOrderActivity.this,myShopid,cookingSwipBean.getDiancannum(),"1");

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }












    /**
     * 获取店铺id
     *
     * @param context
     * @param shopid
     */
    public static void startActivityWithParmeter(Context context, String shopid) {
        Intent intent = new Intent(context, ChooseOrderActivity.class);
        intent.putExtra(CHOOSEORDERSHOPID, shopid);
        context.startActivity(intent);
    }
}
