package com.blanink.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.order.GoOrderGroupDownOrderProduct;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.GoFormDownOrder;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.GlideUtils;


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
 * 待下单
 */
public class GoNotDownOrderFragment extends Fragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
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
    private List<GoFormDownOrder.ResultBean> rowsList = new ArrayList<>();
    private boolean isHasData = true;
    private Integer pageNo = 1;
    private SparseArray<View> sparseArray;
    private MyAdapter adapter;
    private SharedPreferences sp;
    private List<GoFormDownOrder.ResultBean.OrderProductListBean> productListBeen = new ArrayList<>();
    private static String flag;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                Log.e("ComeOrderActivity", "刷新");

                rowsList = (List<GoFormDownOrder.ResultBean>) msg.obj;
                if (rowsList.size() == 0) {
                    rlNotData.setVisibility(View.VISIBLE);
                }
                lv.setAdapter(new MyAdapter());
                adapter.notifyDataSetChanged();
                Log.e("ComeOrderActivity", "刷新over");

            } else {
                if (adapter != null) {
                    Log.e("ComeOrderActivity", "adapter!=null");
                    if (adapter.getCount() == 0) {
                        rlNotData.setVisibility(View.VISIBLE);
                    } else {
                        rlNotData.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("ComeOrderActivity", "adapter==null");
                    rlNotData.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_go_order_not, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        loadGoOrderData();

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
                RefreshData();
            }
        });

        //去下单
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), GoOrderGroupDownOrderProduct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderProd", rowsList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //加载失败重新加载
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                loadGoOrderData();
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
        mapParams.put("companyId", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(getActivity())
                .makeOrderGoForm(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoFormDownOrder>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(GoFormDownOrder goFormDownOrder) {
                        if (goFormDownOrder.getResult().size() > 0) {
                            rlNotData.setVisibility(View.GONE);
                        } else {
                            rlNotData.setVisibility(View.VISIBLE);
                        }
                        rowsList.clear();
                        rowsList.addAll(goFormDownOrder.getResult());
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "已刷新", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if ("REFRESH".equals(flag)) {
            load();
        }
    }

    private void load() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("companyId", sp.getString("COMPANY_ID", null));

        RetrofitFactory.getApiService(getActivity())
                .makeOrderGoForm(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoFormDownOrder>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GoFormDownOrder goFormDownOrder) {

                        if (goFormDownOrder.getResult().size() > 0) {
                            rlNotData.setVisibility(View.GONE);
                        } else {
                            rlNotData.setVisibility(View.VISIBLE);
                        }
                        rowsList.clear();
                        rowsList.addAll(goFormDownOrder.getResult());

                        Message msg = new Message();
                        msg.obj = rowsList;
                        msg.what = 100;
                        handler.sendMessage(msg);
                    }
                });
    }

    public void loadGoOrderData() {
        if (!CommonUtil.isConnected(getActivity())) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("companyId", sp.getString("COMPANY_ID", null));

        RetrofitFactory.getApiService(getActivity())
                .makeOrderGoForm(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoFormDownOrder>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(GoFormDownOrder goFormDownOrder) {
                        llLoad.setVisibility(View.GONE);
                        if (goFormDownOrder.getResult().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);

                        }
                        rowsList.addAll(goFormDownOrder.getResult());
                        Log.e("ComeOrderActivity", "rowList.size():" + rowsList.toString());
                        if (adapter == null) {
                            adapter = new MyAdapter();
                            lv.setAdapter(adapter);
                        } else {
                            Log.e("ComeOrderActivity", "commonAdapter!=null");
                            adapter.notifyDataSetChanged();
                        }

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
            GoFormDownOrder.ResultBean suplier = rowsList.get(position);
            sparseArray = new SparseArray<>();
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item_go_not_down_order, null);
                viewHolder.tv_supplier_name = (TextView) convertView.findViewById(R.id.tv_supplier_name);
                viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
                viewHolder.tv_amount = (TextView) convertView.findViewById(R.id.tv_num);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.e("ComeOrderActivity", "标题:");
            viewHolder.tv_supplier_name.setText(suplier.getProviderName());
            //产品类
            if (suplier.getOrderProductList().get(0).getBCompany() != null && suplier.getOrderProductList().get(0).getBCompany().getPhoto() != null) {
                GlideUtils.glideImageView(getActivity(), viewHolder.iv_photo, suplier.getOrderProductList().get(0).getBCompany().getPhoto(), true);
            }
            viewHolder.tv_amount.setText(suplier.getOrderProductList().size() + "");
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_supplier_name;
        TextView tv_amount;
        ImageView iv_photo;
    }

    public static class GoFormProReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            flag = intent.getStringExtra("flag");
        }
    }
}
