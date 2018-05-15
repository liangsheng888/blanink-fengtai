package com.blanink.ui.activity.lastnext;

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
import com.blanink.ui.fragment.NextHonest;
import com.blanink.ui.fragment.NextPartner;
import com.blanink.ui.fragment.NextPartnerInfo;
import com.blanink.ui.fragment.NextProduct;
import com.blanink.ui.fragment.NextRemark;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * 下家管理公司详情
 */
public class NextFamilyManageCompanyDetail extends BaseActivity {

    private static final String TAG = "NextFamilyDetail";
    @BindView(R.id.customer_apply_info)
    TextView customerApplyInfo;
    @BindView(R.id.customer_apply_iv_last)
    TextView customerApplyIvLast;
    @BindView(R.id.last_family_manage_customer_apply_rl)
    RelativeLayout lastFamilyManageCustomerApplyRl;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.activity_next_family_manage_company_detail)
    RelativeLayout activityNextFamilyManageCompanyDetail;
    private TextView customer_apply_iv_last;
    //private int newIndex;//下一个即将可见的
   // private int oldIndex;//当前可见的碎片
    //private Fragment[] fragments = new Fragment[5];
   // private RadioGroup rg_detail;
    private String name;
    private TextView customer_apply_info;
   // private RadioButton[] radioButtons;
   private List<Fragment> list_fragment;
    private List<String> list_title;
    private MyFragmentPageAdapter fAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_family_manage_company_detail_info);
        ButterKnife.bind(this);
        ReceiveDataByIntent();//接收从上个界面传来的数据
        initView();
        initData();
    }

    private void ReceiveDataByIntent() {
        Intent intent = getIntent();
        name = intent.getStringExtra("companyName");

    }

    private void initView() {
        customer_apply_iv_last = ((TextView) findViewById(R.id.customer_apply_iv_last));//返回
       // rg_detail = ((RadioGroup) findViewById(R.id.rg_detail));
        customer_apply_info = ((TextView) findViewById(R.id.customer_apply_info));
    }

    private void initData() {
        customer_apply_info.setText(name);
        slidingTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        slidingTabs.setSelectedTabIndicatorColor(Color.RED);
        slidingTabs.setTabTextColors(Color.GRAY, Color.RED);
        slidingTabs.setupWithViewPager(viewPager);
        list_fragment = new ArrayList<>();
        list_title=new ArrayList<>();
        list_fragment.add(new NextPartnerInfo());
        list_fragment.add(new NextProduct());
        list_fragment.add(new NextHonest());
        list_fragment.add(new NextPartner());
        list_fragment.add(new NextRemark());

        list_title.add("公司信息");
        list_title.add("公司产品");
        list_title.add("诚信档案");
        list_title.add("合作伙伴");
        list_title.add("评价详情");
        fAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), list_fragment, list_title);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(fAdapter);
        //初始化数据
     /*   fragments[0] = new NextPartnerInfo();
        fragments[1] = new NextProduct();

        fragments[2] = new NextHonest();

        fragments[3] = new NextPartner();

        fragments[4] = new NextRemark();
        radioButtons = new RadioButton[]{rbInfo, rbProduct, rbHonest, rbPartner, rbRemark};
        //默认选择公司信息将诶界面
        getSupportFragmentManager().beginTransaction().add(R.id.fl_customer_info, fragments[0]).commit();
        radioButtons[0].setTextSize(16);
        initLine();
        LineChange(0);
        //界面点击事件
        rg_detail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_info:
                        //公司信息
                        newIndex = 0;
                        break;
                    case R.id.rb_partner:
                        //合作伙伴
                        newIndex = 3;
                        break;
                    case R.id.rb_honest:
                        //诚信档案
                        newIndex = 2;
                        break;
                    case R.id.rb_product:
                        //产品
                        newIndex = 1;
                        break;
                    case R.id.rb_remark:
                        //评价
                        newIndex = 4;
                        break;
                }
                LineChange(newIndex);
                changeFragments(newIndex);
            }
        });*/
        customer_apply_iv_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
/*
    //底部直线选中状态切换
    private void LineChange(int currentPosition) {
        int currentPage = currentPosition;
        for (int i = 0; i < radioButtons.length; i++) {
            llBottom.getChildAt(i).setEnabled(currentPage == i);
        }
    }*/

  /*  //底部直线动态初始化
    private void initLine() {
        for (int i = 0; i < radioButtons.length; i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            //设置控件的显示位置,相当于控件的layout_gravity属性
            params.gravity = Gravity.CENTER;


            params.leftMargin = 20;
            params.rightMargin = 20;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_line);
            llBottom.addView(view);
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

/*
    //切换界面
    private void changeFragments(int newIndex) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (newIndex != oldIndex) {
            ft.hide(fragments[oldIndex]);//隐藏当前的界面
            if (!fragments[newIndex].isAdded()) {//如果没有添加则添加
                ft.add(R.id.fl_customer_info, fragments[newIndex]);
            }
            ft.show(fragments[newIndex]).commit();//显示
        }
        radioButtons[newIndex].setTextSize(16);
        radioButtons[oldIndex].setTextSize(14);
        //改变当前的选中项
        oldIndex = newIndex;
    }*/
}
