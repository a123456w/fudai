package com.ruirong.chefang.activity;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.RankingListAdatper;
import com.ruirong.chefang.bean.RankingListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 排行榜
 * Created by BX on 2017/12/29.
 */

public class RankingListActivity extends BaseActivity {
    @BindView(R.id.iv_ranking_List)
    ListView ivRankingList;

    private RankingListAdatper adatper;
    private List<RankingListBean> lists=new ArrayList<>();
    @Override
    public int getContentView() {
        return R.layout.activity_ranking_list;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("排行榜");
        initData();
    }

    private void initData() {
        adatper=new RankingListAdatper(ivRankingList);
        ivRankingList.setAdapter(adatper );
    }

    @Override
    public void getData() {
        for (int i = 0; i < 12; i++) {
            RankingListBean bean = new RankingListBean();
            lists.add(bean);
        }
        adatper.setData(lists);
    }

}
