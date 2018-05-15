package com.blanink.ui.activity.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.blanink.bean.ComeOder;
import com.blanink.bean.OrderDetail;
import com.blanink.bean.OrderProgress;
import com.blanink.bean.PurchaseProduct;
import com.blanink.http.RetrofitFactory;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.activity.flow.FlowProgress;
import com.blanink.ui.activity.flow.FlowProgressDetail;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 去单中的来单  产品
 */
public class GoComeOrderProduct extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.swipeMenuListView)
    SwipeMenuListView swipeMenuListView;
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
    private ComeOder.ResultBean.RowsBean order;
    private SharedPreferences sp;
    private List<OrderDetail.ResultBean> orderDetails = new ArrayList<>();
    private OrderProductAdapter adapter;
    private int size = 0;
    private PurchaseProduct pur;
    SwipeMenuCreator creator = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_come_order_product);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    private void receiveData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        order = (ComeOder.ResultBean.RowsBean) bundle.getSerializable("OrderProductQueue");
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

        //跳转到详情

        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(GoComeOrderProduct.this, GoOrderAddProduct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderProd", orderDetails.get(position));
                intent.putExtra("companyName", order.getACompany().getName());
                intent.putExtra("orderNumder", order.getAOrderNumber());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (orderDetails.get(position).getNumChild() > 0) {
                    switch (index) {
                        case 0:
                            DialogLoadUtils.getInstance(GoComeOrderProduct.this);
                            postAsynHttp(orderDetails.get(position).getId());
                            break;
                        case 1:
                            // 沟通记录
                            Intent intent = new Intent(GoComeOrderProduct.this, ComeOrderProductDetailTalkNote.class);
                            intent.putExtra("productId", orderDetails.get(position).getId());
                            startActivity(intent);
                            break;
                    }
                } else {
                    Intent intent = new Intent(GoComeOrderProduct.this, ComeOrderProductDetailTalkNote.class);
                    intent.putExtra("productId", orderDetails.get(position).getId());
                    startActivity(intent);
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void postAsynHttp(String id) {
        //   http://localhost:8080/blanink-api/flow/getFlowPlan?type=2&orderProduct.id=6c57048a31e741a3810e37440903032d//
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("type", "1")
                .add("orderProduct.id", id)
                .build();
        Request request = new Request.Builder()
                .url(BaseUrlUtils.NET_URL + "flow/getFlowPlan")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Log.e("FlowSort", BaseUrlUtils.NET_URL + "flow/getFlowPlan?type=1&orderProduct.id=" + id);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final OrderProgress orderProgress = gson.fromJson(str, OrderProgress.class);
                Log.e("FlowProgress", str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();

                        if ("00000".equals(orderProgress.getErrorCode())) {
                            DialogLoadUtils.dismissDialog();
                            if (orderProgress.getResult().size() > 1 || orderProgress.getResult().get(0).getFlow() == null) {
                                Intent intent = new Intent(GoComeOrderProduct.this, FlowProgress.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("orderProgress", orderProgress);
                                intent.putExtras(bundle);
                                intent.putExtra("type", "1");
                                startActivity(intent);

                            } else if (orderProgress.getResult().size() == 1 && orderProgress.getResult().get(0).getFlow() != null) {
                                Intent intent = new Intent(GoComeOrderProduct.this, FlowProgressDetail.class);
                                intent.putExtra("flowId", orderProgress.getResult().get(0).getFlow().getId());
                                intent.putExtra("type", "1");
                                startActivity(intent);
                            }
                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(GoComeOrderProduct.this, "服务器开了会小儿差", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
                            swipeMenuListView.setAdapter(adapter);
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
                convertView = View.inflate(GoComeOrderProduct.this, R.layout.item_come_order_detail_product, null);
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
            creator = new SwipeMenuCreator() {
                @Override
                public void create(final SwipeMenu menu) {
                    // 设置加入潜在框

                    if (orderDetails.get(position).getNumChild() > 0) {
                        SwipeMenuItem seekProgressItem = new SwipeMenuItem(GoComeOrderProduct.this);
                        seekProgressItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorGray)));
                        seekProgressItem.setWidth(dp2px(100));
                        seekProgressItem.setTitle("采购进展");
                        seekProgressItem.setTitleSize(18);
                        seekProgressItem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(seekProgressItem);
                    }
                    SwipeMenuItem talkNoteItem = new SwipeMenuItem(GoComeOrderProduct.this);
                    talkNoteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorBlue)));
                    talkNoteItem.setWidth(dp2px(100));
                    talkNoteItem.setTitle("沟通记录");
                    talkNoteItem.setTitleSize(18);
                    talkNoteItem.setTitleColor(Color.WHITE);
                    menu.addMenuItem(talkNoteItem);

                }
            };
            swipeMenuListView.setMenuCreator(creator);


            //下发产品
            viewHolder.come_order_detail_single_price.setText(orderDetails.get(position).getPrice());

            viewHolder.come_order_detail_tv_endDateHand.setText(orderDetails.get(position).getDeliveryTime());

            viewHolder.come_order_detail_tv_num.setText(orderDetails.get(position).getAmount());
            String orderProductState = OrderProductStateUtils.orderProductStatus(orderDetails.get(position).getOrderProductStatus());
            viewHolder.order_product_state.setText(orderProductState);
            viewHolder.proCateGory.setText(orderDetails.get(position).getCompanyCategory().getCompany().getShortName() == null ? orderDetails.get(position).getCompanyCategory().getCompany().getName() : orderDetails.get(position).getCompanyCategory().getCompany().getShortName());
            viewHolder.tv_product_name.setText(orderDetails.get(position).getProductName());
            if (orderDetails.get(position).getCompanyCategory().getCompany() != null && orderDetails.get(position).getCompanyCategory().getCompany().getPhoto() != null && orderDetails.get(position).getCompanyCategory().getCompany().getPhoto() != "") {
                GlideUtils.glideImageView(GoComeOrderProduct.this, viewHolder.iv, orderDetails.get(position).getCompanyCategory().getCompany().getPhoto(), true);
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
        ImageView iv;
    }

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
