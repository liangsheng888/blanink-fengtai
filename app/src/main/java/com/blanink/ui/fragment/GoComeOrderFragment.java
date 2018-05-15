package com.blanink.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.order.GoComeOrderProduct;
import com.blanink.ui.activity.order.OrderSeek;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ComeOder;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderStateUtils;
import com.blanink.view.UpLoadListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/19.
 * 去单中的来单
 */
public class GoComeOrderFragment extends Fragment {
    @BindView(R.id.lv)
    UpLoadListView lv;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;
    @BindView(R.id.loading_error_img)
    ImageView loadingErrorImg;
    @BindView(R.id.rl_load_fail)
    RelativeLayout rlLoadFail;
    @BindView(R.id.tv_not)
    TextView tvNot;
    @BindView(R.id.rl_not_data)
    RelativeLayout rlNotData;
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<ComeOder.ResultBean.RowsBean> rowsList = new ArrayList<>();
    private boolean isHasData = true;
    private Integer pageNo = 1;
    private SparseArray<View> sparseArray;
    private MyAdapter adapter;
    private SharedPreferences sp;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            if (adapter != null) {
                Log.e("ComeOrderActivity", "界面刷新了");

                if (adapter.getCount() == 0) {
                    rlNotData.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            } else {
                rlNotData.setVisibility(View.VISIBLE);

            }
        }
    };
    private TextView et_seek;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_come_order, null);
        unbinder = ButterKnife.bind(this, view);
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initData() {
        loadData();
        addSeekHeader();
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                rlNotData.setVisibility(View.GONE);
                RefreshData();
            }
        });
        //加载更多
        lv.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {
            @Override
            public void onLoadingMore() {
                pageNo++;
                loadData();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < rowsList.size() + 1) {
                    Intent intent = new Intent(getActivity(), GoComeOrderProduct.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("OrderProductQueue", rowsList.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

        //加载失败重新加载
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                loadData();
            }
        });
    }

    //访问服务器
    public void loadData() {
        if (!CommonUtil.isConnected(getActivity())) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("flag", "1");
        mapParams.put("bCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("pageSize", "10");
        mapParams.put("pageNo", pageNo + "");
        Log.e("ComeOrderActivity", "pageNo:" + pageNo);
        RetrofitFactory.getApiService(getActivity())
                .listJson_coming(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComeOder>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ComeOder comeOder) {
                        llLoad.setVisibility(View.GONE);
                        if (comeOder.getResult().getTotal() <= rowsList.size()) {
                            isHasData = false;
                        } else {
                            rowsList.addAll(comeOder.getResult().getRows());
                            Log.e("ComeOrderActivity", "rowList.size():" + rowsList.toString());
                            if (adapter == null) {
                                adapter = new MyAdapter();
                                lv.setAdapter(adapter);
                            } else {
                                Log.e("ComeOrderActivity", "commonAdapter!=null");
                                adapter.notifyDataSetChanged();
                            }

                        }
                        handler.sendEmptyMessage(0);//通知ui界面更新
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    private void RefreshData() {
        if (!CommonUtil.isConnected(getActivity())) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("flag", "1");
        mapParams.put("bCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("pageSize", "10");
        mapParams.put("pageNo", pageNo + "");
        Log.e("ComeOrderActivity", "pageNo:" + pageNo);
        RetrofitFactory.getApiService(getActivity())
                .listJson_coming(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComeOder>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ComeOder comeOder) {
                        rowsList.clear();

                        rowsList.addAll(0, comeOder.getResult().getRows());
                        Toast.makeText(getActivity(), "已刷新", Toast.LENGTH_SHORT).show();

                        swipeRefreshLayout.setRefreshing(false);
                        handler.sendEmptyMessage(0);//通知ui界面更新
                    }
                });
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rowsList.size();
        }

        @Override
        public Object getItem(int position) {
            return rowsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ComeOder.ResultBean.RowsBean order = rowsList.get(position);
            sparseArray = new SparseArray<>();
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item_go_comeorder, null);
                viewHolder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
                viewHolder.iv_log = (ImageView) convertView.findViewById(R.id.iv_log);

                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.e("ComeOrderActivity", "标题:");
            viewHolder.tv_company.setText(order.getACompany().getShortName()==null?order.getACompany().getName():order.getACompany().getShortName());
            //产品类
            viewHolder.tv_date.setText(DateUtils.format(DateUtils.stringToDate(order.getCreateDate())));
            viewHolder.tv_state.setText(OrderStateUtils.orderStatus(order.getOrderStatus()));
            viewHolder.tv_remark.setText((order.getRemarks()==null||order.getRemarks().equals(""))?order.getaOrderNumber():order.getRemarks());
            if ( order.getACompany() != null&&  order.getACompany().getPhoto()!=null&&  order.getACompany().getPhoto()!="") {
         
            GlideUtils.glideImageView(getActivity(),viewHolder.iv_log, order.getACompany().getPhoto(), true);}
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_company;
        TextView tv_state;
        TextView tv_date;
        ImageView iv_log;
        TextView tv_remark;
    }

    public void addSeekHeader() {
        View view = View.inflate(getActivity(), R.layout.layout_header_seek_order, null);
        et_seek = ((TextView) view.findViewById(R.id.et_seek));
        lv.addHeaderView(view);
        et_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderSeek.class);
                intent.putExtra("flag", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
