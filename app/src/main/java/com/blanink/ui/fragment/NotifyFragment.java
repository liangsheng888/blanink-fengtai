package com.blanink.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.notify.NotifySend;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/24.
 * 通知
 */
public class NotifyFragment extends Fragment {

    @BindView(R.id.my_notify)
    RadioButton myNotify;
    @BindView(R.id.notify_manage)
    RadioButton notifyManage;
    @BindView(R.id.rg_notify)
    RadioGroup rgNotify;
    @BindView(R.id.tv_add_notify)
    TextView tvAddNotify;
    @BindView(R.id.go_order_add_rl)
    RelativeLayout goOrderAddRl;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private Fragment[] fragments;
    private RadioButton[] radioButtons;
    private int newIndex;
    private int oldIndex;
    FragmentPagerAdapter adapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_notify, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        fragments = new Fragment[2];
        fragments[0] = new ReceiveEmailFragment();
        fragments[1] = new sendEmailFragment();
        radioButtons = new RadioButton[2];
        radioButtons[0] = myNotify;
        radioButtons[1] = notifyManage;
        //默认选中第一个
       // getChildFragmentManager().beginTransaction().add(R.id.fl_notify, fragments[0]).commit();

        rgNotify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.my_notify://我的通知
                        newIndex = 0;
                        break;
                    case R.id.notify_manage://通知管理
                        newIndex = 1;
                        break;
                }
                viewPager.setCurrentItem(newIndex);

            }
        });

        //发送通知

        //设置切换
        adapter=new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        };
        viewPager.setAdapter(adapter) ;



        viewPager.setCurrentItem(0);//默认选中第一个

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radioButtons[position].setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        }
    }

   /* public void changeFragment(int newIndex) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (newIndex != oldIndex) {
            fragmentTransaction.hide(fragments[oldIndex]);
            if (!fragments[newIndex].isAdded()) {
                fragmentTransaction.add(R.id.fl_notify, fragments[newIndex]);
            }
            fragmentTransaction.show(fragments[newIndex]).commit();
        }

        oldIndex = newIndex;
    }
*/
    @OnClick(R.id.tv_add_notify)
    public void onClick() {
        Intent intent = new Intent(getActivity(), NotifySend.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
