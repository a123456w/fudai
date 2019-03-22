package com.ruirong.chefang.activity;

import android.os.Bundle;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.ButterKnife;

/**
 * Created by chenlipeng on 2017/12/26 0026
   describe:  娱乐详情
 */
public class EntertainmentDetailsActivity extends BaseActivity {

//        private MyAlbumAdapter adapter;
//        private List<MyOrderListviewBean> baseList = new ArrayList<>();


        @Override
        public int getContentView() {
            return R.layout.activity_entertainment_details;
        }

        @Override
        public void initView() {
            ButterKnife.bind(this);
            loadingLayout.setStatus(LoadingLayout.Success);
            titleBar.setTitleText("我的相册");


//            canContentView.setLayoutManager(new LinearLayoutManager(MyPhoneActivity.this));
//            canContentView.addItemDecoration(new RecycleViewDivider(MyPhoneActivity.this));





        }

        @Override
        public void getData() {
            getListData();
        }

        public void getListData() {
//            baseList.clear();
//
//            for (int i = 0; i < 12; i++) {
//                MyOrderListviewBean myOrderListviewBean = new MyOrderListviewBean();
//                myOrderListviewBean.setTitle("农业商圈发展电商");
//                baseList.add(myOrderListviewBean);
//            }
//            adapter = new MyAlbumAdapter(baseList);
//
//            //调用方法,传入一个接口回调
//            adapter.setOnItemClickListener(new MyAlbumAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view) {
//                Intent intent = new Intent(MyPhoneActivity.this, MyFriendCricleDetailsActivity.class);
//                intent.putExtra("string_data", "hello");
//                intent.putExtra("int_data", 100);
//                startActivity(intent);
//                }
//            });
//            canContentView.setAdapter(adapter);


        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // TODO: add setContentView(...) invocation
            ButterKnife.bind(this);
        }
}
