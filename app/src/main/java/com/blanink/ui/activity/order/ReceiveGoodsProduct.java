package com.blanink.ui.activity.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.OrderDetail;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;
import com.blanink.view.CustomSwipeRefreshLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReceiveGoodsProduct extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.receive)
    RelativeLayout receive;
    @BindView(R.id.lv)
    SwipeMenuListView lv;
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
    @BindView(R.id.swipeRefreshLayout)
    CustomSwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sp;
    private ArrayList<OrderDetail.ResultBean> orderDetails = new ArrayList<>();
    private OrderProductAdapter adapter;
    private SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(final SwipeMenu menu) {
            // 设置加入潜在框

            SwipeMenuItem talkNoteItem = new SwipeMenuItem(ReceiveGoodsProduct.this);
            talkNoteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorBlue)));
            talkNoteItem.setWidth(dp2px(100));
            talkNoteItem.setTitle("查看产品");
            talkNoteItem.setTitleSize(18);
            talkNoteItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(talkNoteItem);

        }
    };
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_goods_product);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }


    private void initData() {
        getDatafromServer();
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //进入发货列表
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (orderDetails.get(position).getRelFlowProcessList().size() >= 1) {
                    Intent intent = new Intent(ReceiveGoodsProduct.this, ReceiveGoodsProductReceiveQueue.class);
                    intent.putExtra("orderProductId", orderDetails.get(position).getId());
                    startActivity(intent);
                } else {
                    //表明有多个发货工序
                    //产品详情
                    Intent intent = new Intent(ReceiveGoodsProduct.this, ReceiveGoodsProductDetail.class);
                    intent.putExtra("orderProductId", orderDetails.get(position).getId());
                    startActivity(intent);
                }
            }
        });
        //刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getDatafromServer();
            }
        });


        lv.setMenuCreator(creator);
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Intent intent = new Intent(ReceiveGoodsProduct.this, ReceiveGoodsProductDetail.class);
                intent.putExtra("orderProductId", orderDetails.get(position).getId());
                startActivity(intent);
                return false;
            }
        });
    }

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void getDatafromServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("order.id", getIntent().getStringExtra("orderId"));
        RetrofitFactory.getApiService(this)
                .orderDetail(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderDetail>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(OrderDetail orderDetail) {
                        llLoad.setVisibility(View.GONE);
                        orderDetails.clear();
                        swipeRefreshLayout.setRefreshing(false);
                        //设置订单产品数量
                        orderDetails.addAll(orderDetail.getResult());
                        if (adapter == null) {
                            adapter = new OrderProductAdapter();
                            lv.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @OnClick(R.id.rl_load_fail)//加载失败 重新加载
    public void onClick() {
        rlLoadFail.setVisibility(View.GONE);
        llLoad.setVisibility(View.VISIBLE);
        getDatafromServer();
    }

    public class OrderProductAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return orderDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(ReceiveGoodsProduct.this, R.layout.item_come_order_detail_product, null);
                viewHolder.order_product_state = (TextView) convertView.findViewById(R.id.order_product_state);
                viewHolder.come_order_detail_single_price = (TextView) convertView.findViewById(R.id.come_order_detail_single_price);
                viewHolder.come_order_detail_tv_endDateHand = (TextView) convertView.findViewById(R.id.come_order_detail_tv_endDateHand);
                viewHolder.come_order_detail_tv_num = (TextView) convertView.findViewById(R.id.come_order_detail_tv_num);
                viewHolder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name); //
                viewHolder.proCateGory = (TextView) convertView.findViewById(R.id.proCateGory);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //添加备注
            //查看进度是否显示
            // loadHistory(orderDetails.get(position).getId());

            //下发产品
            viewHolder.come_order_detail_single_price.setText(orderDetails.get(position).getPrice());

            viewHolder.come_order_detail_tv_endDateHand.setText(orderDetails.get(position).getDeliveryTime());

            viewHolder.come_order_detail_tv_num.setText(orderDetails.get(position).getAmount());
            String orderProductState = OrderProductStateUtils.orderProductStatus(orderDetails.get(position).getOrderProductStatus());
            viewHolder.order_product_state.setText(orderProductState);
            viewHolder.proCateGory.setText(orderDetails.get(position).getCompanyCategory().getCompany().getShortName()==null?orderDetails.get(position).getCompanyCategory().getCompany().getName():orderDetails.get(position).getCompanyCategory().getCompany().getShortName());
            viewHolder.tv_product_name.setText(orderDetails.get(position).getProductName());
            if (orderDetails.get(position).getBCompany() != null && orderDetails.get(position).getBCompany().getPhoto() != null && orderDetails.get(position).getBCompany().getPhoto() != "") {
                GlideUtils.glideImageView(ReceiveGoodsProduct.this, viewHolder.iv, orderDetails.get(position).getBCompany().getPhoto(), true);
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
    }
}


