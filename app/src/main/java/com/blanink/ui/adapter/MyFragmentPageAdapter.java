package com.blanink.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：Created by liangshen on 2018/1/9 0009 22:27
 * 邮箱：liangshen6868@163.com
 */
public class MyFragmentPageAdapter extends PagerAdapter {
    private FragmentManager manager;
    private List<Fragment> list_fragment;                         //fragment列表

    private List<String> list_title;                              //tab名的列表

    public MyFragmentPageAdapter(FragmentManager manager, List<Fragment> list_fragment, List<String> list_title) {
        this.manager = manager;
        this.list_fragment = list_fragment;
        this.list_title = list_title;
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = list_fragment.get(position);
        //判断当前的fragment是否已经被添加进入Fragmentanager管理器中
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragment, fragment.getClass().getSimpleName());
            //不保存系统参数，自己控制加载的参数
            transaction.commitAllowingStateLoss();
            //手动调用,立刻加载Fragment片段
            manager.executePendingTransactions();
        }
        if (fragment.getView().getParent() == null) {
            //添加布局
            container.addView(fragment.getView());
        }
        return fragment.getView();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //移除布局
        container.removeView(list_fragment.get(position).getView());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override

    public CharSequence getPageTitle(int position) {

        return list_title.get(position % list_title.size());

    }
}
