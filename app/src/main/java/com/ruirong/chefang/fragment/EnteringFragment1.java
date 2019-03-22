package com.ruirong.chefang.fragment;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.EnteringActivity;
import com.ruirong.chefang.activity.ProvinceActivity;
import com.ruirong.chefang.activity.RegisterActivity;
import com.ruirong.chefang.bean.ShopCategoryBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.MyCollectionActivity;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.wheel.OnWheelChangedListener;
import com.ruirong.chefang.wheel.WheelView;
import com.ruirong.chefang.wheel.adapter.ArrayWheelAdapter;
import com.ruirong.chefang.wheel.model.CityModel;
import com.ruirong.chefang.wheel.model.DistrictModel;
import com.ruirong.chefang.wheel.model.ProvinceModel;
import com.ruirong.chefang.wheel.service.XmlParserHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2019/4/9.
 * 未入驻
 */

public class EnteringFragment1 extends BaseFragment
        implements OnWheelChangedListener {
    @BindView(R.id.ll_entering)
    LinearLayout llEntering;
    @BindView(R.id.et_shop_name)
    EditText etShopName;
    @BindView(R.id.et_shop_namestar)
    TextView etShopNamestar;
    @BindView(R.id.tv_shop_address)
    TextView tvShopAddress;
    @BindView(R.id.et_shop_detailaddress)
    EditText etShopDetailaddress;
    @BindView(R.id.tv_shop_kind)
    TextView tvShopKind;
    @BindView(R.id.et_shop_entername)
    EditText etShopEntername;
    @BindView(R.id.et_shop_enternamestar)
    TextView etShopEnternamestar;
    @BindView(R.id.tv_people_phone_num)
    EditText tvPeoplePhoneNum;
    @BindView(R.id.tv_people_phone_numstar)
    TextView tvPeoplePhoneNumstar;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;


    private final int REQUEST_CODE_CHOOSE_AREA = 600;
    private String provinceId = "";
    private String cityId = "";
    private String countyId = "";
    private String provinceName = "";
    private String cityName = "";
    private String countyName = "";

    private List<ShopCategoryBean> beanList = new ArrayList<>();
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private WheelView mViewBusinessChose;
    private Button mBtnConfirm;
    private String currentClassicId = "";
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";


    private String mChooseClassic = "";

    private String[] mChooseDatas;


    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_shop_settled, frameLayout, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        etShopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    etShopNamestar.setVisibility(View.GONE);
                } else {
                    etShopNamestar.setVisibility(View.VISIBLE);
                }
            }
        });
        etShopEntername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    etShopEnternamestar.setVisibility(View.GONE);
                } else {
                    etShopEnternamestar.setVisibility(View.VISIBLE);
                }
            }
        });
        tvPeoplePhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tvPeoplePhoneNumstar.setVisibility(View.GONE);
                } else {
                    tvPeoplePhoneNumstar.setVisibility(View.VISIBLE);
                }
            }
        });

        String text = "请输入店铺名称或姓名<font color = '#ff0000'>*</font>";
        String text1 = "请输入入驻人名称<font color = '#ff0000'>*</font>";
        String text2 = "请输入入驻人电话<font color = '#ff0000'>*</font>";
        SpannableString s = new SpannableString(Html.fromHtml(text));
        SpannableString s1 = new SpannableString(Html.fromHtml(text1));
        SpannableString s2 = new SpannableString(Html.fromHtml(text2));
        AbsoluteSizeSpan textSize = new AbsoluteSizeSpan(15, true);
        s.setSpan(textSize, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s1.setSpan(textSize, 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s2.setSpan(textSize, 0, s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        etShopName.setHint(s);
        etShopEntername.setHint(s1);
        tvPeoplePhoneNum.setHint(s2);

    }

    @Override
    public void getData() {

    }


    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getActivity().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }


    /**
     * 弹出地址的地址选择器
     */
    public void showWheelAddress() {
        final PopupWindow popwindow;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View inflate = inflater.inflate(R.layout.popupwindow_addressselect, null);

        popwindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popwindow.setBackgroundDrawable(new BitmapDrawable());
        popwindow.setAnimationStyle(R.style.ActionSheetDialogStyle); // 设置动画
        popwindow.setFocusable(false);
        popwindow.setOutsideTouchable(false);

        popwindow.showAtLocation(llEntering, Gravity.BOTTOM, 0, 0);

        mViewProvince = (WheelView) inflate.findViewById(R.id.id_province);
        mViewCity = (WheelView) inflate.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) inflate.findViewById(R.id.id_district);
        TextView tvCommit = (TextView) inflate.findViewById(R.id.tv_commit);
        TextView tvCancle = (TextView) inflate.findViewById(R.id.tv_dismiss);
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShopAddress.setText(mCurrentProviceName + "," + mCurrentCityName + "," + mCurrentDistrictName);
                popwindow.dismiss();
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });

        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);


        setUpData();


    }

    /**
     * 弹出行业种类的行业类型选择器
     */
    public void showWheelBusinessSelector() {
        final PopupWindow popwindow;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View inflate = inflater.inflate(R.layout.popupwindow_businesschoseselect, null);

        popwindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popwindow.setBackgroundDrawable(new BitmapDrawable());
        popwindow.setAnimationStyle(R.style.ActionSheetDialogStyle); // 设置动画
        popwindow.setFocusable(false);
        popwindow.setOutsideTouchable(false);

        popwindow.showAtLocation(llEntering, Gravity.BOTTOM, 0, 0);

        mViewBusinessChose = (WheelView) inflate.findViewById(R.id.id_businesschoose);
        mViewBusinessChose.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mChooseDatas));
        mViewBusinessChose.setCurrentItem(2);

        TextView tvCommit = (TextView) inflate.findViewById(R.id.tv_commit);
        TextView tvCancle = (TextView) inflate.findViewById(R.id.tv_dismiss);
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShopKind.setText(mChooseClassic);
                popwindow.dismiss();
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });

        // 添加change事件
        mViewBusinessChose.addChangingListener(this);


        mViewBusinessChose.setVisibleItems(9);

    }

    /**
     * 获取行业的数据
     */
    public void getShopCateList(String token, int type) {
        HttpHelp.getInstance().create(RemoteApi.class).getShopCategoryData(token, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ShopCategoryBean>>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<List<ShopCategoryBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            beanList = listBaseBean.data;
                            mChooseDatas = new String[beanList.size()];
                            for (int i = 0; i < beanList.size(); i++) {
                                mChooseDatas[i] = beanList.get(i).getName();
                            }

                            showWheelBusinessSelector();
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 设置地址选择器的数据
     */
    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(9);
        mViewCity.setVisibleItems(9);
        mViewDistrict.setVisibleItems(9);
        updateCities();
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 选择行业
     */
    @OnClick(R.id.rl_shop_category)
    public void selectCategory() {
        EnteringActivity activity = (EnteringActivity) getActivity();
        getShopCateList(new PreferencesHelper(getActivity()).getToken(), activity.getType());// 获取商家类型的列表
    }

    /**
     * 提交数据给后台
     */
    @OnClick(R.id.tv_submit)
    public void commitData() {
        String shopName = etShopName.getText().toString().trim();
        String shopLocation = tvShopAddress.getText().toString().trim() + "-" + etShopDetailaddress.getText().toString().trim();
        String name = etShopEntername.getText().toString().trim();
        String phone = tvPeoplePhoneNum.getText().toString().trim();
        String classic = tvShopKind.getText().toString().trim();
        if (TextUtils.isEmpty(shopName)) {
            ToastUtil.showToast(mContext, "店铺名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(shopLocation)) {
            ToastUtil.showToast(mContext, "店铺地址不能为空");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast(mContext, "入驻人名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(classic)) {
            ToastUtil.showToast(mContext, "请选择店铺所属行业");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(mContext, "入驻人电话不能为空");
            return;
        }

        if (phone.length() != 11) {
            ToastUtil.showToast(mContext, "请输入11位手机号");
            return;
        }
        EnteringActivity activity = (EnteringActivity) getActivity();
        upLoadData(new PreferencesHelper(getActivity()).getToken(), shopName, shopLocation, currentClassicId, name, phone, activity.getType(), provinceId, cityId, countyId);
    }

    /**
     * 商家入驻
     *
     * @param token
     * @param spName      店铺名称
     * @param spAddress   店铺详情地址
     * @param classifyId  行业id
     * @param enterName   入驻人姓名
     * @param enterMobile 入驻人手机号
     * @param type
     */
    public void upLoadData(String token, String spName, String spAddress, String classifyId, String enterName, String enterMobile, int type
            , String provinceId, String cityId, String countyId) {

  /*      HttpHelp.getInstance().create(RemoteApi.class).postEnteringInfo(token,spName,spAddress,classifyId,enterName,enterMobile,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(getActivity(),null){
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0){
                            ToastUtil.showToast(getActivity(),baseBean.message);
                            getActivity().finish();
                        }else {
                            ToastUtil.showToast(getActivity(),baseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });*/
        HttpHelp.getInstance().create(RemoteApi.class).postEnterSubmitInfo(token, spName, spAddress, classifyId, enterName, enterMobile, type, provinceId, countyId, cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(getActivity(), baseBean.message);
                            getActivity().finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
                        } else {
                            ToastUtil.showToast(getActivity(), baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 选择地址
     */
    @OnClick(R.id.rl_address)
    public void choseAddress() {
        //showWheelAddress();
        startActivityForResult(new Intent(mContext, ProvinceActivity.class), REQUEST_CODE_CHOOSE_AREA);
        //setUpData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        } else if (wheel == mViewBusinessChose) {
            currentClassicId = beanList.get(newValue).getId();
            mChooseClassic = beanList.get(newValue).getName();
        }
    }

    public static final int RESULT_OK = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CODE_CHOOSE_AREA:
                    //选择地区
                    provinceId = data.getStringExtra("provinceId");
                    provinceName = data.getStringExtra("provinceName");
                    cityId = data.getStringExtra("cityId");
                    cityName = data.getStringExtra("cityName");
                    countyId = data.getStringExtra("countyId");
                    countyName = data.getStringExtra("countyName");
                    tvShopAddress.setText(provinceName + " " + cityName + " " + countyName);
                    break;
            }
        }
    }
}
