package com.blanink.ui.activity.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.GoOrderDown;
import com.blanink.bean.OrderDetail;
import com.blanink.bean.ResponseDelete;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;
import com.blanink.utils.SysConstants;
import com.blanink.view.CustomSwipeRefreshLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 去单 已下单 产品列表
 */
public class GoDownOrderProduct extends BaseActivity {
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.come_order_detail_lv)
    SwipeMenuListView comeOrderDetailLv;
    @BindView(R.id.swipeRefreshLayout)
    CustomSwipeRefreshLayout swipeRefreshLayout;
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
    private GoOrderDown.ResultBean.RowsBean order;
    private SharedPreferences sp;
    private List<OrderDetail.ResultBean> orderDetails = new ArrayList<>();
    private OrderProductAdapter adapter;
    private SwipeMenuCreator creator;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            if (orderDetails.size() == 0) {
                //通知订单界面刷新
                Intent intent = new Intent("com.blanink.GO_ORDER_REFRESH_DATA");
                intent.putExtra("flag", "REFRESH");
                sendBroadcast(intent);
                /*Intent in=new Intent(GoDownOrderProduct.this, GoOrderPurchase.class);
                in.putExtra("flag","REFRESH");
                in.putExtra("DIRECT","2");
                startActivity(in);*/
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_down_order_product);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    private void receiveData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        order = (GoOrderDown.ResultBean.RowsBean) bundle.getSerializable("OrderProductQueue");
    }

    private void initData() {
        receiveData();
        getDatafromServer();
        //返回
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化数据
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
                RefreshDatafromServer();

            }
        });
        comeOrderDetailLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.e("Come", position + "");
                if (SysConstants.ORDER_PRODUCT_STATUS_COMPANY_A_CONBINED.equals(orderDetails.get(position).getOrderProductStatus()) ||
                        SysConstants.ORDER_PRODUCT_STATUS_BACK_TO_A.equals(orderDetails.get(position).getOrderProductStatus())) {
                    //删除
                    deleteNofity(orderDetails.get(position).getId(), position, "你真要删除该产品吗?", "取消", "确定");
                } else {

                    Intent intent = new Intent(GoDownOrderProduct.this, GoDownOrderProductDetail.class);
                    intent.putExtra("orderProductId", orderDetails.get(position).getId());
                    intent.putExtra("orderId", orderDetails.get(position).getOrder().getId());
                    intent.putExtra("photo", orderDetails.get(position).getCompanyCategory().getCompany().getPhoto());

                    startActivity(intent);
                }

                return false;
            }
        });
        //跳转到详情
        comeOrderDetailLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(GoDownOrderProduct.this, GoDownOrderProductDetail.class);
                intent.putExtra("orderProductId", orderDetails.get(position).getId());
                intent.putExtra("orderId", orderDetails.get(position).getOrder().getId());
                intent.putExtra("companyName", order.getBCompany().getName());
                intent.putExtra("orderNumder", order.getAOrderNumber());
                intent.putExtra("photo", orderDetails.get(position).getCompanyCategory().getCompany().getPhoto());

                startActivity(intent);
            }
        });
    }

    private void deleteNofity(final String orderProductId, final int position, String content, String left, String right) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_exit);
        final Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        ((TextView) window.findViewById(R.id.tv_content)).setText(content);
        ((TextView) window.findViewById(R.id.tv_continue)).setText(left);
        ((TextView) window.findViewById(R.id.tv_exit)).setText(right);
        window.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        window.findViewById(R.id.tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLoadUtils.getInstance(GoDownOrderProduct.this);
                DialogLoadUtils.showDialogLoad("删除中...");
                deleteData(orderProductId, position);
                alertDialog.dismiss();
            }
        });
    }

    public void deleteData(String orderProductId, final int position) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("orderProductId", orderProductId);
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("userName", sp.getString("NAME", null));
        RetrofitFactory.getApiService(this)
                .deleteOrderProduct(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseDelete>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(GoDownOrderProduct.this, "服务器开了会儿小车,请稍后重试", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(ResponseDelete responseDelete) {
                        if ("00000".equals(responseDelete.getErrorCode())) {
                            DialogLoadUtils.dismissDialog();
                            orderDetails.remove(position);
                            handler.sendEmptyMessage(0);
                            Toast.makeText(GoDownOrderProduct.this, "删除产品成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(GoDownOrderProduct.this, "删除产品失败", Toast.LENGTH_SHORT).show();
                            DialogLoadUtils.dismissDialog();

                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //订单详情
    public void getDatafromServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("order.id", order.getId());
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
                    }

                    @Override
                    public void onNext(OrderDetail orderDetail) {
                        llLoad.setVisibility(View.GONE);
                        //设置订单产品数量
                        orderDetails.addAll(orderDetail.getResult());
                        if (adapter == null) {
                            adapter = new OrderProductAdapter();
                            comeOrderDetailLv.setAdapter(adapter);
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

    public void RefreshDatafromServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("order.id", order.getId());
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
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onNext(OrderDetail orderDetail) {
                        llLoad.setVisibility(View.GONE);
                        orderDetails.clear();
                        //设置订单产品数量
                        orderDetails.addAll(0, orderDetail.getResult());
                        Toast.makeText(GoDownOrderProduct.this, "刷新出来成功", Toast.LENGTH_SHORT).show();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
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
                convertView = View.inflate(GoDownOrderProduct.this, R.layout.item_come_order_detail_product, null);
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
            creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem seekProgressItem = new SwipeMenuItem(GoDownOrderProduct.this);
                    seekProgressItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
                    seekProgressItem.setWidth(dp2px(100));
                    if (SysConstants.ORDER_PRODUCT_STATUS_COMPANY_A_CONBINED.equals(orderDetails.get(position).getOrderProductStatus()) ||
                            SysConstants.ORDER_PRODUCT_STATUS_BACK_TO_A.equals(orderDetails.get(position).getOrderProductStatus())) {
                        seekProgressItem.setTitle("删除");
                    } else {
                        seekProgressItem.setTitle("查看");
                    }
                    seekProgressItem.setTitleSize(18);
                    seekProgressItem.setTitleColor(Color.WHITE);
                    menu.addMenuItem(seekProgressItem);
                }
            };
            comeOrderDetailLv.setMenuCreator(creator);
            viewHolder.come_order_detail_single_price.setText(orderDetails.get(position).getPrice());

            viewHolder.come_order_detail_tv_endDateHand.setText(orderDetails.get(position).getDeliveryTime());

            viewHolder.come_order_detail_tv_num.setText(orderDetails.get(position).getAmount());
            String orderProductState = OrderProductStateUtils.orderProductStatus(orderDetails.get(position).getOrderProductStatus());
            viewHolder.order_product_state.setText(orderProductState);
            viewHolder.proCateGory.setText(orderDetails.get(position).getCompanyCategory().getCompany().getShortName()==null?orderDetails.get(position).getCompanyCategory().getCompany().getName():orderDetails.get(position).getCompanyCategory().getCompany().getShortName());
            viewHolder.tv_product_name.setText(orderDetails.get(position).getProductName());
            if (orderDetails.get(position).getCompanyCategory() != null && orderDetails.get(position).getCompanyCategory().getCompany().getPhoto() != null && orderDetails.get(position).getCompanyCategory().getCompany().getPhoto() != "") {


                GlideUtils.glideImageView(GoDownOrderProduct.this, viewHolder.iv, orderDetails.get(position).getCompanyCategory().getCompany().getPhoto(), true);
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

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}

