package com.blanink.ui.activity.order;

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
import com.blanink.bean.GoOrderDown;
import com.blanink.http.RetrofitFactory;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderStateUtils;
import com.blanink.view.RefreshListView;
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

/***
 * 收货列表
 */
public class ReceiveGoods extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.receive)
    RelativeLayout receive;
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
    @BindView(R.id.tv_not_net)
    TextView tvNotNet;
    @BindView(R.id.rl_not_net)
    RelativeLayout rlNotNet;
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    private SharedPreferences sp;
    private int pageNo = 0;
    private boolean isHasData = true;
    private List<GoOrderDown.ResultBean.RowsBean> rowsList = new ArrayList<>();
    private MyAdapter adapter;
    private SparseArray<View> sparseArray;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);

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

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_goods);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    private void initData() {
        loadData();
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
        //加载更多，刷新
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
                RefreshData();
            }
        });

        //详情
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < rowsList.size()) {
                    Intent intent = new Intent(ReceiveGoods.this, ReceiveGoodsProduct.class);
                    intent.putExtra("orderId", rowsList.get(position).getId());
                    startActivity(intent);
                }
            }
        });
    }

    //访问服务器
    public void loadData() {
        if (!CommonUtil.isConnected(ReceiveGoods.this)) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(ReceiveGoods.this, "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("flag", "2");
        mapParams.put("aCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("pageSize", "10");
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("sortOrder", "asc");
        RetrofitFactory.getApiService(this)
                .listJson_going(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoOrderDown>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(GoOrderDown goOrderDown) {
                        llLoad.setVisibility(View.GONE);

                        if (goOrderDown.getResult().getTotal() <= rowsList.size()) {
                            isHasData = false;
                        } else {
                            rlNotData.setVisibility(View.GONE);
                            rowsList.addAll(goOrderDown.getResult().getRows());
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


    private void RefreshData() {
        if (!CommonUtil.isConnected(ReceiveGoods.this)) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(ReceiveGoods.this, "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("flag", "2");
        mapParams.put("aCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("pageSize", "10");
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("sortOrder", "asc");
        RetrofitFactory.getApiService(this)
                .listJson_going(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoOrderDown>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ReceiveGoods.this, "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(GoOrderDown goOrderDown) {
                        swipeRefreshLayout.setRefreshing(false);
                        rowsList.clear();
                        rowsList.addAll(goOrderDown.getResult().getRows());
                        Toast.makeText(ReceiveGoods.this, "已刷新", Toast.LENGTH_SHORT).show();

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
            final GoOrderDown.ResultBean.RowsBean order = rowsList.get(position);
            sparseArray = new SparseArray<>();
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(ReceiveGoods.this, R.layout.item_receive, null);
                viewHolder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
                viewHolder.tv_modify = (TextView) convertView.findViewById(R.id.tv_modify);
                viewHolder.iv_log = (ImageView) convertView.findViewById(R.id.iv_log);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Log.e("ComeOrderActivity", "标题:");
            viewHolder.tv_company.setText(order.getBCompany().getShortName() == null ? order.getBCompany().getName() : order.getBCompany().getShortName());
            //产品类
            viewHolder.tv_date.setText(DateUtils.format(DateUtils.stringToDate(order.getCreateDate())));
            viewHolder.tv_state.setText(OrderStateUtils.orderStatus(order.getOrderStatus()));
            viewHolder.tv_remark.setText(order.getAOrderNumber());
            if (order.getBCompany() != null && order.getBCompany().getPhoto() != null && !order.getBCompany().getPhoto() .equals( "")) {
                GlideUtils.glideImageView(ReceiveGoods.this, viewHolder.iv_log, order.getBCompany().getPhoto(), true);
            }
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
