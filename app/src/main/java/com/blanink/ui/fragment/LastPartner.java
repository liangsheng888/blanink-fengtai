package com.blanink.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ManyCustomer;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.GlideUtils;
import com.blanink.view.RefreshListView;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/9.
 */

/***
 * 上家 公司信息 合作伙伴
 */
public class LastPartner extends Fragment {
    private RefreshListView lv_partner;
    private SparseArray<View> sparseArray;
    private List<ManyCustomer.Result.Customer> partnerLists = new ArrayList();
    private SharedPreferences sp;
    private String companyId;
    private int pageNo = 1;
    private MyAdapter adapter;
    private boolean isHasData = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv_partner.completeRefresh(isHasData);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                rl_not_data.setVisibility(View.VISIBLE);
            }

        }
    };
    private LinearLayout ll_load;
    private RelativeLayout rl_load_fail;
    private RelativeLayout rl_not_data;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        companyId = getActivity().getIntent().getStringExtra("companyA.id");
        Log.e("Company", "LastPartner companyId" + companyId);
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_company_partner_queue, null);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        initView(view);

        return view;

    }

    private void initView(View view) {
        lv_partner = ((RefreshListView) view.findViewById(R.id.lv_partner));
        ll_load = ((LinearLayout) view.findViewById(R.id.ll_load));
        rl_load_fail = ((RelativeLayout) view.findViewById(R.id.rl_load_fail));
        rl_not_data = ((RelativeLayout) view.findViewById(R.id.rl_not_data));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        getData();

        lv_partner.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                getData();
            }

            @Override
            public void onLoadingMore() {
                pageNo++;
                getData();
            }
        });
        rl_load_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_load_fail.setVisibility(View.GONE);
                ll_load.setVisibility(View.VISIBLE);
                getData();
            }
        });
    }


    public static class ViewHolder {
        public TextView tv_company_jc;//公司简称
        public TextView tv_customer_num;//服务客户数量
        public TextView tv_state;//实有 虚拟
        public TextView tv_apply_address;//地址
        public TextView tv_master;//负责人
        public TextView tv_phone;//电话
        public TextView tv_honest;//公司信誉
        public TextView tv_company_apply_remark;//自评
        public TextView tv_company_apply_remark_other;//他评
        public TextView tv_major;//主营

        public ImageView iv;
    }

    public class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return partnerLists.size();
        }

        @Override
        public Object getItem(int position) {
            return partnerLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            sparseArray = new SparseArray<>();
            Log.e("Next", "getView  supplier ：" + partnerLists.size() + "");
            if (sparseArray.get(position, null) == null) {
                Log.e("Next", "getView  convertView ==null");
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item_partner_queue, null);
                viewHolder.tv_company_jc = (TextView) convertView.findViewById(R.id.tv_company_jc);
                viewHolder.tv_customer_num = (TextView) convertView.findViewById(R.id.tv_customer_num);
                viewHolder.tv_master = (TextView) convertView.findViewById(R.id.tv_apply_major_person);
                viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_apply_phone);
                viewHolder.tv_honest = (TextView) convertView.findViewById(R.id.tv_company_apply_xin_yu);
                viewHolder.tv_company_apply_remark = (TextView) convertView.findViewById(R.id.tv_company_apply_remark);
                viewHolder.tv_company_apply_remark_other = (TextView) convertView.findViewById(R.id.tv_company_apply_other_remark);
                viewHolder.tv_apply_address = (TextView) convertView.findViewById(R.id.tv_apply_address);
                viewHolder.tv_major = (TextView) convertView.findViewById(R.id.tv_major);
                viewHolder.iv = ((ImageView) convertView.findViewById(R.id.iv));
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                Log.e("Next", "getView  convertView !=null");
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ManyCustomer.Result.Customer customer = partnerLists.get(position);
            viewHolder.tv_company_jc.setText(customer.getCompanyA().getName());
            viewHolder.tv_master.setText(customer.getCompanyA().getMaster());
            if (customer.getCompanyA().getArea() != null && customer.getCompanyA().getArea().getName() != null) {
                viewHolder.tv_apply_address.setText(customer.getCompanyA().getArea().getName());
            }
            viewHolder.tv_master.setText(customer.getCompanyA().getMaster());
            viewHolder.tv_phone.setText(customer.getCompanyA().getPhone());
            viewHolder.tv_major.setText(customer.getCompanyA().getScope());
            viewHolder.tv_customer_num.setText(customer.getCompanyA().serviceCount + "家");
            DecimalFormat df = new DecimalFormat("0.0");
            viewHolder.tv_honest.setText(df.format((partnerLists.get(position).getCompanyA().reviewOthers + partnerLists.get(position).getCompanyA().reviewSelf) / 2.0));
            viewHolder.tv_company_apply_remark.setText(partnerLists.get(position).getCompanyA().reviewSelf + "");
            viewHolder.tv_company_apply_remark_other.setText(partnerLists.get(position).getCompanyA().reviewOthers + "");
            if (partnerLists.get(position).getCompanyA().photo != null && !partnerLists.get(position).getCompanyA().photo.equals("")) {
                GlideUtils.glideImageView(getActivity(), viewHolder.iv, partnerLists.get(position).getCompanyA().photo, true);
            }
            return convertView;
        }

    }

    //访问网络
    public void getData() {
        if (!CommonUtil.isConnected(getActivity())) {
            //判断网络是否连接
            ll_load.setVisibility(View.GONE);
            rl_load_fail.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", ""));
        mapParams.put("companyB.id", companyId);
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(getActivity())
                .customerList(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ManyCustomer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ll_load.setVisibility(View.GONE);
                        rl_load_fail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ManyCustomer manyCustomer) {
                        ll_load.setVisibility(View.GONE);
                        if (manyCustomer.getResult().getTotal() <= partnerLists.size()) {
                            isHasData = false;
                        } else {
                            partnerLists.addAll(manyCustomer.getResult().getRows());
                            if (adapter == null) {
                                adapter = new MyAdapter();
                                lv_partner.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }
                });

    }
}
