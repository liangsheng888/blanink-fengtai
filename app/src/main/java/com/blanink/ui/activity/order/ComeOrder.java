package com.blanink.ui.activity.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
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
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ComeOder;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderStateUtils;
import com.blanink.utils.SysConstants;
import com.blanink.view.UpLoadListView;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ComeOrder extends BaseActivity {

    private static final String TAG = "ComeOrder";
    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.come_order)
    RelativeLayout comeOrder;
    @BindView(R.id.lv)
    UpLoadListView lv;
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
    private final static int BACK_TASK = 0;
    @BindView(R.id.iv_last)
    TextView ivLast;
    private SharedPreferences sp;
    private List<ComeOder.ResultBean.RowsBean> rowsList = new ArrayList<>();
    private boolean isHasData = true;
    private Integer pageNo = 1;
    private SparseArray<View> sparseArray;
    private MyAdapter adapter;
    private static String flag;

    private TextView etSeek;
    private boolean isGetData = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            if (msg.what == 100) {
                Log.e("ComeOrderActivity", "刷新");

                rowsList = (List<ComeOder.ResultBean.RowsBean>) msg.obj;
                if (rowsList.size() == 0) {
                    rlNotData.setVisibility(View.VISIBLE);
                }
                lv.setAdapter(new MyAdapter());
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
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
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_come_order);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化数据
        loadData();
        addHeaderView();
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
                rlNotData.setVisibility(View.GONE);
                pageNo = 1;
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
        //添加订单
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComeOrder.this, ComeOrderNewAddInfoActivity.class);
                startActivity(intent);
            }
        });
        //订单详情
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < rowsList.size() + 1) {
                    Intent intent = new Intent(ComeOrder.this, ComeOrderProductActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderDetail", rowsList.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                loadData();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < rowsList.size() + 1) {

                    }
                    return false;
            }
        });
    }

    private void RefreshData() {
        if (!CommonUtil.isConnected(ComeOrder.this)) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(ComeOrder.this, "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("flag", 0 + "");
        mapParams.put("bCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("pageSize", "10");
        mapParams.put("sortOrder", "asc");
        RetrofitFactory.getApiService(this)
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
                        Toast.makeText(ComeOrder.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ComeOder comeOder) {
                        rowsList.clear();
                        if (comeOder.getResult().getTotal()> 0) {
                            rlNotData.setVisibility(View.GONE);
                        }else {
                            rlNotData.setVisibility(View.VISIBLE);

                        }
                        rowsList.addAll(comeOder.getResult().getRows());
                        Toast.makeText(ComeOrder.this, "刷新成功", Toast.LENGTH_SHORT).show();

                        swipeRefreshLayout.setRefreshing(false);
                        handler.sendEmptyMessage(0);//通知ui界面更新
                    }
                });
    }

    @Override
    public void onStart() {
        if ("REFRESH".equals(flag)) {
            Log.e("ComeOrderActivity", "刷新开始");
            load();
        }
        super.onStart();

    }
    @OnClick(R.id.rl_load_fail)
    public void onClick() {
        rlLoadFail.setVisibility(View.GONE);
        llLoad.setVisibility(View.VISIBLE);
        rowsList.clear();
        loadData();
    }

    //访问服务器
    public void loadData() {
        if (!CommonUtil.isConnected(ComeOrder.this)) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(ComeOrder.this, "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("flag", 0 + "");
        mapParams.put("bCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("pageSize", "10");
        mapParams.put("sortOrder", "asc");
        RetrofitFactory.getApiService(this)
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
                        pageNo--;
                    }

                    @Override
                    public void onNext(ComeOder comeOder) {
                        llLoad.setVisibility(View.GONE);
                        if (comeOder.getResult().getTotal() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        }
                        if (comeOder.getResult().getRows().size() == 0) {
                            isHasData = false;
                            pageNo--;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ComeOder.ResultBean.RowsBean order = rowsList.get(position);
            sparseArray = new SparseArray<>();
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(ComeOrder.this, R.layout.item_comeorder, null);
                viewHolder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
                viewHolder.iv_log = (ImageView) convertView.findViewById(R.id.iv_log);
                viewHolder.tv_modify = (TextView) convertView.findViewById(R.id.tv_modify);
                viewHolder.rl_order = (RelativeLayout) convertView.findViewById(R.id.rl_order);
                viewHolder.iv_alarm = (ImageView) convertView.findViewById(R.id.iv_alarm);

                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.e(TAG, "orderProduct:"+order.getOrderProductList().toString());

            viewHolder.tv_company.setText(order.getACompany().getShortName()==null?order.getACompany().getName():order.getACompany().getShortName());
            //产品类
            viewHolder.tv_date.setText(DateUtils.format(DateUtils.stringToDate(order.getCreateDate())));
            viewHolder.tv_state.setText(OrderStateUtils.orderStatus(order.getOrderStatus()));
            viewHolder.tv_remark.setText((order.getRemarks()==null||order.getRemarks().equals(""))?order.getaOrderNumber():order.getRemarks());
            if (order.getACompany() != null && order.getACompany().getPhoto() != null && order.getACompany().getPhoto() != "") {
                GlideUtils.glideImageView(ComeOrder.this, viewHolder.iv_log, order.getACompany().getPhoto(), true);
            }
            //如果产品中有一个超多有效天数，变色
            List<ComeOder.ResultBean.RowsBean.OrderProductListBean> orderProductList = order.getOrderProductList();
            for (ComeOder.ResultBean.RowsBean.OrderProductListBean opb : orderProductList) {
                Log.e(TAG, "创建时间:"+opb.getCreateDate());

                int day= CommonUtil.compare_now_sub_old(DateUtils.dateToString(new Date()), opb.getCreateDate());
                int alarm=30000;
                if(opb.getCompanyCategory()!=null){
                    alarm= opb.getCompanyCategory().getAlarmDay();
                }
                if ( day>alarm) {

                    if ("1".equals(opb.getOrderProductStatus())
                            || "2".equals(opb.getOrderProductStatus())
                            || "3".equals(opb.getOrderProductStatus())
                            || "4".equals(opb.getOrderProductStatus())
                            || "5".equals(opb.getOrderProductStatus())
                            || "6".equals(opb.getOrderProductStatus())) {
                        Log.e(TAG, "超过有效天数");

                         //viewHolder.rl_order.setBackgroundColor(getResources().getColor(R.color.colorAlphaOrange));
                        viewHolder.iv_alarm.setVisibility(View.VISIBLE);

                    }
                    break;
                }
            }
            //都等于5 no ,有一个在7-14,23 no,
            int size = 0;
            for (int i = 0; i < order.getOrderProductList().size(); i++) {
                if (
                        SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_DISTRIBUTE.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_FLOW_CREATED.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_FLOW_PULISHED.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_PRODUCTION_END.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_DELIEVERY_PART.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_DELIEVERY_OVER.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_RECEIVED_PART.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_RECEIVED_OVER.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_SRTATUS_REJECT.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_COMMENT_OVER.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_AFTERSALES_START.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_AFTERSALES_DEALING.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_AFTERSALES_DEALING_OVER.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                || SysConstants.ORDER_PRODUCT_STATUS_AFTERSALES_DEALING_OVER_CONFIRMED.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                        ) {
                    viewHolder.tv_modify.setVisibility(View.GONE);
                    break;
                } else if ((SysConstants.ORDER_PRODUCT_STATUS_BACK_TO_A.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                )) {
                    size++;
                    if (size == order.getOrderProductList().size()) {
                        viewHolder.tv_modify.setVisibility(View.GONE);
                    }
                }
            }
            viewHolder.tv_modify.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int length = 0;
                    for (int i = 0; i < order.getOrderProductList().size(); i++) {
                        if (
                                SysConstants.ORDER_PRODUCT_STATUS_BACK_TO_A.equals(order.getOrderProductList().get(i).getOrderProductStatus())
                                ) {//订单产品状态都是4可以选修改客户
                            length++;
                            if (length == order.getOrderProductList().size()) {
                                Intent intent = new Intent(ComeOrder.this, ComeOrderModify.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("OrderDetail", rowsList.get(position));
                                intent.putExtras(bundle);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        } else {
                            Intent intent = new Intent(ComeOrder.this, ComeOrderModifyNoModifyCustomer.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("OrderDetail", rowsList.get(position));
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_company;
        TextView tv_state;
        TextView tv_date;
        ImageView iv_log,iv_alarm;
        TextView tv_remark;
        TextView tv_modify;
        RelativeLayout rl_order;
    }

    public void addHeaderView() {
        View view = View.inflate(ComeOrder.this, R.layout.layout_header_seek_order, null);
        lv.addHeaderView(view);
        etSeek = (TextView) view.findViewById(R.id.et_seek);
        etSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComeOrder.this, OrderSeek.class);
                intent.putExtra("flag", "0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void load() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("flag", 0 + "");
        mapParams.put("bCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("pageSize", "10");
        mapParams.put("sortOrder", "asc");
        RetrofitFactory.getApiService(this)
                .listJson_coming(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComeOder>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ComeOder comeOder) {
                        if (comeOder.getResult().getRows().size() == 0) {
                            rowsList.clear();
                        } else {
                            rlNotData.setVisibility(View.GONE);
                            rowsList.clear();
                            rowsList.addAll(comeOder.getResult().getRows());

                        }
                        Message msg = new Message();
                        msg.obj = rowsList;
                        msg.what = 100;
                        handler.sendMessage(msg);
                    }
                });
    }

    public static class RefreshBroReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Order", "收到刷新广播了");
            flag = intent.getStringExtra("flag");
        }


    }
}
