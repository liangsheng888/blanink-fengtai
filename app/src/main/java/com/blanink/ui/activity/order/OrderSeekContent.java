package com.blanink.ui.activity.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
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

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ComeOder;
import com.blanink.bean.OrderProductSpecification;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderStateUtils;
import com.blanink.utils.SysConstants;
import com.blanink.view.UpLoadListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 订单搜索内容
 */
public class OrderSeekContent extends BaseActivity {

    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order)
    RelativeLayout comeOrder;
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
    private String aCompanyId;
    private String bCompanyId;
    private String productName;
    private String aOrderNumber;
    private String bOrderNumber;
    private String companyCategoryId;
    private String bCompanyOrderOwnerUserId;
    private String orderProductStatus;
    private String source;
    private SharedPreferences sp;
    private List<ComeOder.ResultBean.RowsBean> rowsList = new ArrayList<>();
    List<OrderProductSpecification> orderProductSpecificationList =new ArrayList<>();
    private boolean isHasData = true;
    private Integer pageNo = 1;
    private SparseArray<View> sparseArray;
    private MyAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            if (adapter != null) {
                Log.e("ComeOrderActivity", "界面刷新了");
                adapter.notifyDataSetChanged();
            } else {
                rlNotData.setVisibility(View.VISIBLE);
            }
        }
    };
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_seek_content);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        aCompanyId = intent.getStringExtra("aCompanyId");
        bCompanyId = intent.getStringExtra("bCompanyId");
        productName = intent.getStringExtra("productName");
        aOrderNumber = intent.getStringExtra("aOrderNumber");
        bOrderNumber = intent.getStringExtra("bOrderNumber");
        companyCategoryId = intent.getStringExtra("companyCategoryId");
        bCompanyOrderOwnerUserId = intent.getStringExtra("bCompanyOrderOwnerUserId");
        orderProductStatus = intent.getStringExtra("orderProductStatus");
        source = intent.getStringExtra("source");
        flag = intent.getStringExtra("flag");
        String orderProductSpecificationsListJson = intent.getStringExtra("orderProductSpecificationList");
        orderProductSpecificationList = new Gson().fromJson(orderProductSpecificationsListJson, new TypeToken<List<OrderProductSpecification>>() {
        }.getType());
        Log.e("ComeOrder", orderProductSpecificationList.toString());
        for (OrderProductSpecification ops : orderProductSpecificationList) {

        }
        loadData();
        //重新加载
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                loadData();
            }
        });
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                if (position < rowsList.size()) {
                    Intent intent = new Intent(OrderSeekContent.this, ComeOrderProductActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderDetail", rowsList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void loadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("flag", flag);
        mapParams.put("aCompany.id", aCompanyId);
        mapParams.put("bCompany.id", bCompanyId);
        mapParams.put("orderProductList[0].productName", productName);
        mapParams.put("aOrderNumber", aOrderNumber);
        mapParams.put("bOrderNumber", bOrderNumber);
        mapParams.put("orderProductList[0].companyCategory.id", companyCategoryId);
        mapParams.put("bCompanyOrderOwnerUser.id", bCompanyOrderOwnerUserId);
        mapParams.put("orderProductList[0].orderProductStatus", orderProductStatus);
        mapParams.put("orderProductList[0].source", source);
        for (int i = 0; i< orderProductSpecificationList.size(); i++){
            mapParams.put("orderProductList[0].orderProductSpecificationList["+i+"].attributeSearchType",
                    orderProductSpecificationList.get(i).getAttributeSearchType());
            mapParams.put("orderProductList[0].orderProductSpecificationList["+i+"].attribute.id",
                    orderProductSpecificationList.get(i).getAttribute().getId());
            mapParams.put("orderProductList[0].orderProductSpecificationList["+i+"].attributeValue",
                    orderProductSpecificationList.get(i).getAttributeValue());
        }

        mapParams.put("pageSize", "10");
        mapParams.put("pageNo", pageNo+"");
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
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                        pageNo--;
                    }

                    @Override
                    public void onNext(ComeOder comeOder) {
                        llLoad.setVisibility(View.GONE);
                        if (comeOder.getResult().getTotal() <= rowsList.size()) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                convertView = View.inflate(OrderSeekContent.this, R.layout.item_comeorder, null);
                viewHolder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
                viewHolder.iv_log = (ImageView) convertView.findViewById(R.id.iv_log);
                viewHolder.tv_modify = (TextView) convertView.findViewById(R.id.tv_modify);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.e("ComeOrderActivity", "标题:");
            viewHolder.tv_company.setText(order.getACompany().getShortName()==null?order.getACompany().getName():order.getACompany().getShortName());
            viewHolder.tv_remark.setText((order.getRemarks()==null||order.getRemarks().equals(""))?order.getaOrderNumber():order.getRemarks());
            if (order.getACompany() != null && order.getACompany().getPhoto() != null && order.getACompany().getPhoto() != "") {
                GlideUtils.glideImageView(OrderSeekContent.this, viewHolder.iv_log, order.getACompany().getPhoto(), true);
            }
            viewHolder.tv_date.setText(DateUtils.format(DateUtils.stringToDate(order.getCreateDate())));
            viewHolder.tv_state.setText(OrderStateUtils.orderStatus(order.getOrderStatus()));

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
                                Intent intent = new Intent(OrderSeekContent.this, ComeOrderModify.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("OrderDetail", rowsList.get(position));
                                intent.putExtras(bundle);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        } else {
                            Intent intent = new Intent(OrderSeekContent.this, ComeOrderModifyNoModifyCustomer.class);
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
        ImageView iv_log;
        TextView tv_remark;
        TextView tv_modify;
    }
}
