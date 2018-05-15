package com.blanink.ui.activity.flow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.MyFragmentPageAdapter;
import com.blanink.ui.fragment.FlowDashboardFragment;
import com.blanink.ui.fragment.FlowOrderFrament;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//排流程 订单列表
public class FlowOrder extends BaseActivity {

    private static final String TAG = "FlowOrder";
    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.come_order_iv_last)
    TextView comeOrderIvLast;
    @BindView(R.id.tv_sorted)
    TextView tvSorted;
    @BindView(R.id.come_order)
    RelativeLayout comeOrder;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.activity_flow_order)
    RelativeLayout activityFlowOrder;
    List<Fragment> fragmentList=null;
    List<String> tabTitleList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_order);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //历史排流程
        tvSorted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowOrder.this, FlowSortCompleted.class);
                startActivity(intent);
            }
        });
        fragmentList=new ArrayList<>();
        tabTitleList=new ArrayList<>();
        fragmentList.add(new FlowOrderFrament());
        fragmentList.add(new FlowDashboardFragment());
        tabTitleList.add("流程订单");
        tabTitleList.add("生产看板");
        viewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager(),fragmentList,tabTitleList));
        tabLayout.setTabTextColors(Color.BLACK,getResources().getColor(R.color.colorTheme));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorTheme));
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.come_order_iv_last)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.come_order_iv_last://返回
                finish();
                break;
        }
    }


}
