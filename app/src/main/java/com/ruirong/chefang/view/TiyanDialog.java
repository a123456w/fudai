package com.ruirong.chefang.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.TiYanBean;

/**
 * 领取体验金对话框
 */
public class TiyanDialog extends Dialog {

    public ImageView ivTiyan;
    TiyanInterface tiyanInterface;
    public TextView tvTiyan;
    public TextView tvTiyannum;
    public TextView tvGetTiyan;
    public TextView tvTiyanDay;

    public void setTiyanInterface(TiyanInterface tiyanInterface) {
        this.tiyanInterface = tiyanInterface;
    }

    public TiyanDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tiyan);
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.dimAmount = 0.5f;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.gravity = Gravity.CENTER;
        getWindow().setAttributes(layoutParams);
        ivTiyan = ((ImageView) findViewById(R.id.iv_tiyan));
        tvTiyan = ((TextView) findViewById(R.id.tv_tiyannum));
        tvTiyannum = ((TextView) findViewById(R.id.tv_tiyangoldnum));
        tvGetTiyan = ((TextView) findViewById(R.id.tv_gettiyan));
        tvTiyanDay = ((TextView) findViewById(R.id.tvhint));

        tvGetTiyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiyanInterface != null) {
                    tiyanInterface.getTiyan();
                }
            }
        });
    }

    public void setData(TiYanBean tiYanBean) {
        tvTiyan.setText(tiYanBean.getMoney());
        tvTiyannum.setText(tiYanBean.getMoney());
        tvTiyanDay.setText("体验时间" + tiYanBean.getDay() + "天");
    }
}
