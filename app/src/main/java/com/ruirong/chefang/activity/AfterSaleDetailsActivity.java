package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ruirong.chefang.R;
import com.zaaach.citypicker.model.HotCity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/31.
 * 售后详情
 */

public class AfterSaleDetailsActivity extends AppCompatActivity {
    private TextView currentTV;
    private CheckBox hotCB;
    private CheckBox animCB;
    private CheckBox enableCB;
    private Button themeBtn;

    private static final String KEY = "current_theme";

    private List<HotCity> hotCities;
    private int anim;
    private int theme;
    private boolean enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            theme = savedInstanceState.getInt(KEY);
            setTheme(theme > 0 ? theme : R.style.DefaultCityPickerTheme);
        }


        setContentView(R.layout.activity_after_sale_details);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.hide();  //隐藏掉标题栏
        }
//
//        themeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (themeBtn.getText().toString().startsWith("自定义")) {
//                    themeBtn.setText("默认主题");
//                    theme = R.style.DefaultCityPickerTheme;
//                } else if (themeBtn.getText().toString().startsWith("默认")) {
//                    themeBtn.setText("自定义主题");
//                    theme = R.style.CustomTheme;
//                }
//                recreate();
//            }
//        });


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, theme);
    }
}
