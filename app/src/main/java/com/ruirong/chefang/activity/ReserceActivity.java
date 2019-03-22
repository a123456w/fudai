package com.ruirong.chefang.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maning.calendarlibrary.constant.MNConst;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.DialogTimeAdapter;
import com.ruirong.chefang.adapter.HotelDetailsReserveAdapter;
import com.ruirong.chefang.adapter.ReserceRoomNumGridviewAdapter;
import com.ruirong.chefang.adapter.ReserveTitleAdapter;
import com.ruirong.chefang.adapter.RoomRegisterAdapter;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;
import com.ruirong.chefang.bean.ReserceRoomNumberBean;
import com.ruirong.chefang.bean.ReservePriceBean;
import com.ruirong.chefang.bean.ReserveTitleBean;
import com.ruirong.chefang.bean.RoomRegisterBean;
import com.ruirong.chefang.bean.StatusBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 预定
 * Created by BX on 2017/12/29.
 */

public class ReserceActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_title)
    GridView lvTitle;
    @BindView(R.id.iv_garade)
    RelativeLayout ivGarade;
    @BindView(R.id.gv_room_number)
    NoScrollGridView gvRoomNumber;
    @BindView(R.id.check_people)
    NoScrollListView checkPeople;
    @BindView(R.id.rl_estimate)
    RelativeLayout rlEstimate;
    @BindView(R.id.room_check)
    TextView roomCheck;
    @BindView(R.id.room_leave)
    TextView roomLeave;
    @BindView(R.id.room_night)
    TextView roomNight;
    @BindView(R.id.room_num)
    TextView roomNum;
    @BindView(R.id.room_phone)
    TextView roomPhone;
    @BindView(R.id.room_time)
    TextView roomTime;
    @BindView(R.id.room_price)
    TextView roomPrice;
    @BindView(R.id.room_pay)
    TextView roomPay;
    @BindView(R.id.xq_ll)
    LinearLayout xqLl;
    @BindView(R.id.host_pic)
    ImageView hostPic;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    private ReserceRoomNumGridviewAdapter homeLableAdapter;
    private ReserveTitleAdapter reserveTitleAdapter;
    private RoomRegisterAdapter roomRegisterAdapter;
    private List<ReserceRoomNumberBean> baseList = new ArrayList<>();
    private List<RoomRegisterBean> roomRegisterList = new ArrayList<>();
    private List<String> hotelBeanList = new ArrayList<>();
    private List<String> roomConditionList = new ArrayList<>();
    private String house_id;
    private String house_time;
    private Calendar showDate;
    private String startYear;
    private String startMonth;
    private String startDay;
    private String endYear;
    private String endMonth;
    private String endDay;
    private ReserveTitleBean reserveTitleBean;
    private String shop_id;

    private String house_xq;
    private FuBackHotelDetailsBean fuBackHotelDetailsBean;
    private int position;
    private String zPrice;//总价
    private int lTime = 1;//共计预订时间
    private int rNum = 1;//共计房间
    private String myTvTitle = "";
    private String nightCheckNum = "1";

    String startTime = "";
    String endTime = "";

    @Override
    public int getContentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_reserve;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("预订");
        showDate = Calendar.getInstance();

        house_id = getIntent().getStringExtra("house_id");
        shop_id = getIntent().getStringExtra("shop_id");
        house_xq = getIntent().getStringExtra("house_xq");
        position = getIntent().getIntExtra("position", -1);
        Log.i("XXX", house_id + " " + shop_id);
        if (house_id == null || shop_id == null || house_xq == null) {
            finish();
        } else {
            if (position == -1) {
                finish();
            }
        }
        showLoadingDialog("加载中");
        fuBackHotelDetailsBean = new Gson().fromJson(house_xq, FuBackHotelDetailsBean.class);

        reserveTitleAdapter = new ReserveTitleAdapter(lvTitle);
        lvTitle.setAdapter(reserveTitleAdapter);

        homeLableAdapter = new ReserceRoomNumGridviewAdapter(gvRoomNumber);
        gvRoomNumber.setFocusable(false);
        gvRoomNumber.setAdapter(homeLableAdapter);

        gvRoomNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (TextUtils.isEmpty(startTime)||TextUtils.isEmpty(endTime)){
                    ToastUtil.showToast(ReserceActivity.this,"请先选择入店日期和离店日期");
                    return;
                }

                rNum = position + 1;

                setOption();

                //   获取
                getTatalReserPrice(new PreferencesHelper(ReserceActivity.this).getToken(),
                        house_id, shop_id, DateUtil.toTimestamp(startTime, DateUtil.FORMAT_YMD) / 1000 + "", DateUtil.toTimestamp(endTime, DateUtil.FORMAT_YMD) / 1000 + "",String.valueOf(rNum));

                homeLableAdapter.getData().get(position).setChoice(true);

                roomNum.setText(homeLableAdapter.getData().get(position).getName());
                homeLableAdapter.notifyDataSetChanged();


                roomRegisterAdapter = new RoomRegisterAdapter(checkPeople);
                checkPeople.setFocusable(false);
                checkPeople.setAdapter(roomRegisterAdapter);

//                roomRegisterAdapter.notifyDataSetChanged();
//                roomRegisterAdapter.setData(roomRegisterList);
                roomRegisterList.clear();
                for (int i = 0; i <= position; i++) {
                    RoomRegisterBean roomRegisterBean = new RoomRegisterBean();
                    roomRegisterBean.setName("入住人" + (i + 1));
                    roomRegisterList.add(roomRegisterBean);
                }
                roomRegisterAdapter.setData(roomRegisterList);
            }
        });

    }

    /**
     * 获取预订房间的总价格
     *
     * @param token
     * @param id
     * @param shopid
     * @param ruTime
     * @param liTime
     */
    public void getTatalReserPrice(String token, String id, String shopid, String ruTime, String liTime,String num) {
        HttpHelp.getInstance().create(RemoteApi.class).getReservePrice(token, id, shopid, ruTime, liTime,num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReservePriceBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ReservePriceBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            roomNight.setText("共" + baseBean.data.getDaynum() + "晚");
                            roomPrice.setText("￥" + baseBean.data.getTotal_money());
                            zPrice =baseBean.data.getTotal_money();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }

    @Override
    public void getData() {

        homeLableAdapter.setData(baseList);

        getListData();

        nightCheck(shop_id);
    }

    /**
     * 选日期的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {

                startTime = data.getStringExtra("startservertime");
                endTime = data.getStringExtra("endservertime");
                Date nowDate = new Date();
                String now_yyy_mm_dd = MNConst.sdf_yyy_MM_dd.format(nowDate);

                if (!TextUtils.isEmpty(startTime)) {
                    if ("2".equals(nightCheckNum)) {
                        if (TimeUtil.isDateOneBigger(now_yyy_mm_dd, startTime)) {
                            roomCheck.setText(now_yyy_mm_dd);
                        }
                    } else {
                        roomCheck.setText(startTime);
                    }
                }

                if (!TextUtils.isEmpty(endTime)) {
                    roomLeave.setText(endTime);
                }

                long timeNight = TimeUtil.dateDiff(startTime, endTime, "yyyy-MM-dd");

                //  roomNight.setText("共" + timeNight + "晚");


                Log.e("time", TimeUtil.dateToStamp(startTime));
                Log.e("time", TimeUtil.dateToStamp(endTime));


                //   获取
                getTatalReserPrice(new PreferencesHelper(ReserceActivity.this).getToken(),
                        house_id, shop_id, DateUtil.toTimestamp(startTime, DateUtil.FORMAT_YMD) / 1000 + "", DateUtil.toTimestamp(endTime, DateUtil.FORMAT_YMD) / 1000 + "",String.valueOf(rNum));


            }
        }

    }

    /**
     * 房间信息
     */
    private void getListData() {
        HttpHelp.getInstance().create(RemoteApi.class).Hotelreserve(new PreferencesHelper(ReserceActivity.this).getToken(), house_id, shop_id,"1.52")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReserveTitleBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ReserveTitleBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        hideLoadingDialog();
                        if (listBaseBean.code == 0) {
                            reserveTitleBean = listBaseBean.data;
                            if (reserveTitleBean != null) {
                                GlideUtil.display(ReserceActivity.this, Constants.IMG_HOST + reserveTitleBean.getHouse().getCover(), hostPic);
                                tvTitle.setText(reserveTitleBean.getHouse().getName());
                                myTvTitle = reserveTitleBean.getHouse().getName();
                                if (!TextUtils.isEmpty(reserveTitleBean.getHouse().getNum())) {
                                    baseList.clear();
                                    for (int i = 1; i <= Integer.parseInt(reserveTitleBean.getHouse().getNum()); i++) {
                                        ReserceRoomNumberBean homePageBean = new ReserceRoomNumberBean();
                                        homePageBean.setName(i + "间");
                                        baseList.add(homePageBean);
                                    }
                                    homeLableAdapter.setData(baseList);
                                }
                                hotelBeanList = reserveTitleBean.getHotel().getDao_time();
                                if (reserveTitleBean.getHouse() != null) {
                                    roomConditionList = reserveTitleBean.getHouse().getSpecif();
                                    if (roomConditionList != null && roomConditionList.size() > 0) {
                                        reserveTitleAdapter.setData(roomConditionList);
                                    }
                                }
                            }
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.loseToLogin(ReserceActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        Log.d("ReserceActivity", "onError: " + throwable.getLocalizedMessage());
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.iv_garade, R.id.rl_estimate, R.id.room_pay, R.id.room_night, R.id.room_check,
            R.id.room_leave, R.id.xq_ll, R.id.room_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.room_num:
            case R.id.iv_garade:

//                返回值为0，visible；返回值为4，invisible；返回值为8，gone。
                int visibility = gvRoomNumber.getVisibility();
                if (visibility == 0) { //要隐藏
                    gvRoomNumber.setVisibility(View.GONE);
                    ivArrow.setSelected(false);
                } else {//要显示
                    gvRoomNumber.setVisibility(View.VISIBLE);
                    ivArrow.setSelected(true);
                }

                break;
            case R.id.rl_estimate:
                Dialoga();
                break;
            case R.id.xq_ll:
                productParaDialog(fuBackHotelDetailsBean.getHotels().get(position).getAttribute(), fuBackHotelDetailsBean.getHotels().get(position).getLoop_pics());
                break;
            case R.id.room_night:
//                startActivity(ReserceActivity.this, EnterAndLeaveHotelActivity.class);
                break;
            case R.id.room_check://入店时间
                //TODO
                Intent intent = new Intent(ReserceActivity.this, ReserveHotelActivity.class);
                intent.putExtra("YESHENCHECK", nightCheckNum);
                startActivityForResult(intent, 100);

                // showstartTimeDialog();
                break;
            case R.id.room_leave://离店时间
                //TODO
                Intent intentrooleave = new Intent(ReserceActivity.this, ReserveHotelActivity.class);
                intentrooleave.putExtra("YESHENCHECK", nightCheckNum);
                startActivityForResult(intentrooleave, 100);

                /*             *//**\
             *    private String endYear;
             private String endMonth;
             private String endDay;

             *//*
                if (startYear == null || startMonth == null || startDay == null) {
                    ToastUtil.showToast(ReserceActivity.this, "请先选择入店日期");
                    return;
                }
                showendTimeDialog();*/
                break;
            case R.id.room_pay:
                Map<String, Object> map = new HashMap<>();

                map.put("token", new PreferencesHelper(ReserceActivity.this).getToken());
                map.put("house_id", this.house_id);
                //TODO
                if (TextUtils.isEmpty(startTime)) {
                    ToastUtil.showToast(ReserceActivity.this, "请选择入店日期");
                    return;
                }
                if (TextUtils.isEmpty(endTime)) {
                    ToastUtil.showToast(ReserceActivity.this, "请选择房间数");
                    return;
                }
                /* if (startYear == null || startMonth == null || startDay == null) {
                    ToastUtil.showToast(ReserceActivity.this, "请选择入店日期");
                    return;
                }

                if (endYear == null || endMonth == null || endDay == null) {
                    ToastUtil.showToast(ReserceActivity.this, "请选择离店日期");
                    return;
                }*/
                if (roomRegisterList.size() == 0) {
                    ToastUtil.showToast(ReserceActivity.this, "请选择房间数");
                    return;
                }

                String ru_time = TimeUtil.dateToStamp(startYear + "-" + startMonth + "-" + startDay);
                String li_time = TimeUtil.dateToStamp(endYear + "-" + endMonth + "-" + endDay);


                map.put("ru_time", DateUtil.toTimestamp(startTime, DateUtil.FORMAT_YMD) / 1000);
                map.put("li_time", DateUtil.toTimestamp(endTime, DateUtil.FORMAT_YMD) / 1000);


                map.put("wannum", lTime + "");

                Log.i("XXX", roomRegisterAdapter.getData().size() + " lTime" + lTime);
                if (roomRegisterAdapter.getData().size() > 0 && roomRegisterAdapter.getData() != null) {
                    String str = "";
                    for (int i = 0; i < roomRegisterAdapter.getData().size(); i++) {
                        if (roomRegisterAdapter.getData().get(i).getContent() != null) {
                            if (i != roomRegisterAdapter.getData().size() - 1) {
                                str += roomRegisterAdapter.getData().get(i).getContent() + "|";
                            } else {
                                str += roomRegisterAdapter.getData().get(i).getContent();
                            }
                        } else {
                            ToastUtil.showToast(ReserceActivity.this, "请输入所有预订人姓名！");
                            return;
                        }
                    }
                    Log.i("XXX", str);
                    if (!TextUtils.isEmpty(str)) {
                        map.put("usernames", str);
                    } else {
                        ToastUtil.showToast(ReserceActivity.this, "请输入所有预订人姓名！");
                        return;
                    }
                } else {
                    ToastUtil.showToast(ReserceActivity.this, "请输入预订人姓名！");
                    return;
                }
                map.put("num", roomRegisterAdapter.getData().size());
                map.put("ru_num", roomRegisterAdapter.getData().size());
                if (roomPhone.getText().toString().trim().length() == 11) {
                    map.put("mobile", roomPhone.getText().toString().trim());
                } else {
                    ToastUtil.showToast(ReserceActivity.this, "请输入正确的手机号！");
                    return;
                }
                if (!TextUtils.isEmpty(house_time)) {
                    map.put("ydao_time", house_time);
                } else {
                    ToastUtil.showToast(ReserceActivity.this, "请选择预订时间！");
                    return;
                }
                addorder(map);
                break;
        }
    }

    /**
     * 获取是否在夜审时间的状态
     *
     * @param shop_id
     */
    public void nightCheck(String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getNightCheck(shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<StatusBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<StatusBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            nightCheckNum = baseBean.data.getStatus();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }


    /**
     * 产品参数弹框
     */
    private void productParaDialog(List<FuBackHotelDetailsBean.HotelsBean.AttributeBean> lists, List<String> bList) {
        if (lists != null && lists.size() > 0) {
            final Dialog mDialog = new Dialog(ReserceActivity.this, R.style.ActionSheetDialogStyle);
            View inflate = LayoutInflater.from(ReserceActivity.this).inflate(R.layout.dialog_hotel_details_model, null);
            ListView viewById = (ListView) inflate.findViewById(R.id.lv_parameter);
            ImageView ivClose = (ImageView) inflate.findViewById(R.id.iv_close);
            TextView tvTitles = (TextView) inflate.findViewById(R.id.tv_titles);
            tvTitles.setText(myTvTitle);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
            Banner banner = (Banner) inflate.findViewById(R.id.banner_top);

            HotelDetailsReserveAdapter adapter = new HotelDetailsReserveAdapter(viewById);
            viewById.setAdapter(adapter);
            adapter.setData(lists);

            if (bList != null && bList.size() > 0) {
                banner.setImages(bList)
                        .setImageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(Context context, Object path, ImageView imageView) {
                                String imgUrl = Constants.IMG_HOST + path.toString();
                                GlideUtil.display(ReserceActivity.this, imgUrl, imageView);

                            }
                        }).setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                        // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));

                    }
                }).start();
            }

            mDialog.setContentView(inflate);
            Window dialogwindow = mDialog.getWindow();
            dialogwindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogwindow.getAttributes();
            lp.windowAnimations = R.style.PopUpBottomAnimation;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogwindow.setAttributes(lp);
            mDialog.show();
        }

    }


    private void addorder(Map<String, Object> map) {
        HttpHelp.getInstance().create(RemoteApi.class).addorder(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + "   " + baseBean.message);
                        if (baseBean.code == 0) {
                            Intent intent = new Intent(ReserceActivity.this, ImmediatelyPaymentActivity.class);
                            intent.putExtra("place_type", "3");
                            intent.putExtra("shop_price", zPrice);
                            intent.putExtra("order_sn", baseBean.data);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.d("addorder", "onError: " + throwable.getLocalizedMessage());
                    }
                });
    }


    /**
     * 选择时间弹框
     */
    private void Dialoga() {
        if (hotelBeanList != null && hotelBeanList.size() > 0) {
            final Dialog mDialog = new Dialog(ReserceActivity.this, R.style.ActionSheetDialogStyle);
            View inflate = LayoutInflater.from(ReserceActivity.this).inflate(R.layout.dialog_time_store, null);

            TextView content = inflate.findViewById(R.id.content);
            content.setText(reserveTitleBean.getHotel().getDescription());
            ListView viewById = (ListView) inflate.findViewById(R.id.lv_time);
            final DialogTimeAdapter adapter = new DialogTimeAdapter(viewById);
            viewById.setAdapter(adapter);
            adapter.setData(hotelBeanList);

            viewById.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    house_time = adapter.getData().get(position);
                    roomTime.setText(house_time);
                    mDialog.dismiss();
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
    }


    private void setOption() {

        for (int i = 0; i < homeLableAdapter.getData().size(); i++) {

            homeLableAdapter.getData().get(i).setChoice(false);

        }
    }


    /**
     * 展示日期弹窗
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showstartTimeDialog() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            boolean mFired = false;

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if (mFired == true) {
                    return;
                } else {
                    mFired = true;
                }
                int day = showDate.get(Calendar.DAY_OF_MONTH);
                if (day > i2) {
                    ToastUtil.showToast(ReserceActivity.this, i2 + "号已经过去了，请重新选择！");
                    return;
                }
                startYear = i + "";
                startMonth = (i1 + 1) + "";
                startDay = i2 + "";
                roomCheck.setText((i1 + 1) + "月" + i2 + "日");
            }
        }, showDate.get(Calendar.YEAR), showDate.get(Calendar.MONTH), showDate.get(Calendar.DAY_OF_MONTH)).show();
    }


    /**
     * 展示日期弹窗
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showendTimeDialog() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            boolean mFired = false;

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if (mFired == true) {
                    return;
                } else {
                    mFired = true;
                }

                if (i < Integer.parseInt(startYear)) {
                    ToastUtil.showToast(ReserceActivity.this, "离店时间不能小于或者等于入店时间！");
                    return;
                }
                if ((i1 + 1) < Integer.parseInt(startMonth)) {
                    ToastUtil.showToast(ReserceActivity.this, "离店时间不能小于或者等于入店时间！");
                    return;
                } else if ((i1 + 1) == Integer.parseInt(startMonth)) {
                    if (i2 <= Integer.parseInt(startDay)) {
                        ToastUtil.showToast(ReserceActivity.this, "离店时间不能小于或者等于入店时间！");
                        return;
                    }
                }

                endYear = i + "";
                endMonth = (i1 + 1) + "";
                endDay = i2 + "";


                long l = TimeUtil.dateDiff(startYear + "-" + startMonth + "-" + startDay,
                        endYear + "-" + endMonth + "-" + endDay, "yyyy-MM-dd");
                lTime = Integer.parseInt(l + "");
                String nightNum = "";
                if ("2".equals(nightCheckNum)) {
                    nightNum = TimeUtil.formatInteger(lTime + 1);
                } else {
                    nightNum = TimeUtil.formatInteger(lTime);
                }


            }
        }, showDate.get(Calendar.YEAR), showDate.get(Calendar.MONTH), showDate.get(Calendar.DAY_OF_MONTH)).show();
    }

}
