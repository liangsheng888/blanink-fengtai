package com.blanink.ui.activity.flow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.FlowSort;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;
import com.blanink.view.UpLoadListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****
 * 已派流程 产品列表
 */
public class FlowSortCompleted extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
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
    private SharedPreferences sp;
    private Integer pageNo = 1;
    private List<FlowSort.ResultBean.RowsBean> rowsBeanList = new ArrayList<>();
    private boolean isHasData = true;
    private OrderProductAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            if (adapter != null) {
                Log.e("Fow", "界面刷新了");
                adapter.notifyDataSetChanged();
            } else {
                rlNotData.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_sort_completed);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    private void initData() {
        loadData();
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        //刷新
        //刷新
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
                refreshData();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < rowsBeanList.size()) {
                    Log.e("FlowSort", "点击了" + position + "----" + rowsBeanList.get(position).toString());
                    Intent intent = new Intent(FlowSortCompleted.this, FlowSortCompletedProductDetail.class);
                    if (rowsBeanList.get(position).getOrderProduct() != null) {
                        intent.putExtra("productId", rowsBeanList.get(position).getOrderProduct().getId());
                    }
                    intent.putExtra("photo", rowsBeanList.get(position).getOrderProduct().getCompanyCategory().getCompany().getPhoto());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderProduct", rowsBeanList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void refreshData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(this)
                .sortCompletedList(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FlowSort>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(FlowSort flowSort) {
                        isHasData=true;
                        rowsBeanList.clear();
                        rowsBeanList.addAll(flowSort.getResult().getRows());
                        swipeRefreshLayout.setRefreshing(false);
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    public void loadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(this)
                .sortCompletedList(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FlowSort>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(FlowSort flowSort) {
                        llLoad.setVisibility(View.GONE);
                        if (flowSort.getResult().getRows().size() == 0) {
                            isHasData = false;
                        } else {
                            rowsBeanList.addAll(flowSort.getResult().getRows());
                            if (adapter == null) {
                                adapter = new OrderProductAdapter();
                                lv.setAdapter(adapter);

                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    public class OrderProductAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rowsBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return rowsBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(FlowSortCompleted.this, R.layout.item_come_order_detail_product, null);
                viewHolder.order_product_state = (TextView) convertView.findViewById(R.id.order_product_state);
                viewHolder.come_order_detail_single_price = (TextView) convertView.findViewById(R.id.come_order_detail_single_price);
                viewHolder.come_order_detail_tv_endDateHand = (TextView) convertView.findViewById(R.id.come_order_detail_tv_endDateHand);
                viewHolder.come_order_detail_tv_num = (TextView) convertView.findViewById(R.id.come_order_detail_tv_num);
                viewHolder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name); //
                viewHolder.proCateGory = (TextView) convertView.findViewById(R.id.proCateGory);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //添加备注
            //编辑订单产品

            //下发产品
            if (rowsBeanList.get(position).getOrderProduct() != null) {
                viewHolder.come_order_detail_single_price.setText(rowsBeanList.get(position).getOrderProduct().getPrice());

                viewHolder.come_order_detail_tv_endDateHand.setText(rowsBeanList.get(position).getOrderProduct().getDeliveryTime());

                viewHolder.come_order_detail_tv_num.setText(rowsBeanList.get(position).getOrderProduct().getAmount());
                String orderProductState = OrderProductStateUtils.orderProductStatus(rowsBeanList.get(position).getOrderProduct().getOrderProductStatus());
                viewHolder.order_product_state.setText(orderProductState);
                viewHolder.proCateGory.setText(rowsBeanList.get(position).getOrderProduct().getCompanyCategory().getCompany().getShortName() == null ? rowsBeanList.get(position).getOrderProduct().getCompanyCategory().getCompany().getName() : rowsBeanList.get(position).getOrderProduct().getCompanyCategory().getCompany().getShortName());
                viewHolder.tv_product_name.setText(rowsBeanList.get(position).getOrderProduct().getProductName());
                viewHolder.tv_time.setText(DateUtils.format(DateUtils.stringToDate(rowsBeanList.get(position).getUpdateDate())));

                if (rowsBeanList.get(position).getOrderProduct().getCompanyCategory().getCompany().getPhoto() != null && !"".equals(rowsBeanList.get(position).getOrderProduct().getCompanyCategory().getCompany().getPhoto())) {
                    GlideUtils.glideImageView(FlowSortCompleted.this, viewHolder.iv, rowsBeanList.get(position).getOrderProduct().getCompanyCategory().getCompany().getPhoto(), true);
                }

            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView order_product_state;//订单状态
        TextView come_order_detail_single_price;//单价
        TextView come_order_detail_tv_endDateHand;//交货时间
        TextView come_order_detail_tv_num;//数量
        TextView proCateGory;//产品类
        TextView tv_product_name;//产品规格
        public ImageView iv;
        public TextView tv_time;
    }

}
