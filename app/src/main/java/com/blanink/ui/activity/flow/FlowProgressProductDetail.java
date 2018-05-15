package com.blanink.ui.activity.flow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.AttachmentBrow;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.bean.OneOrderProduct;
import com.blanink.bean.OrderProductAttributes;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.PriorityUtils;
import com.blanink.utils.StringToListUtils;
import com.blanink.view.NoScrollGridview;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FlowProgressProductDetail extends BaseActivity {


    @BindView(R.id.come_order)
    TextView comeOrder;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.proCateGory)
    TextView proCateGory;
    @BindView(R.id.tv_ruler)
    TextView tvRuler;
    @BindView(R.id.order_detail_ll_proCateGory_ruler)
    TextView orderDetailLlProCateGoryRuler;
    @BindView(R.id.order_detail_ll_proCateGory)
    LinearLayout orderDetailLlProCateGory;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.come_order_detail_single_price)
    TextView comeOrderDetailSinglePrice;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.come_order_detail_tv_num)
    TextView comeOrderDetailTvNum;
    @BindView(R.id.order_detail_rl2)
    LinearLayout orderDetailRl2;
    @BindView(R.id.tv_endDateHand)
    TextView tvEndDateHand;
    @BindView(R.id.come_order_detail_tv_endDateHand)
    TextView comeOrderDetailTvEndDateHand;
    @BindView(R.id.tv_mine_priority)
    TextView tvMinePriority;
    @BindView(R.id.come_order_detail_tv_mine_priority)
    TextView comeOrderDetailTvMinePriority;
    @BindView(R.id.order_detail_rl3)
    LinearLayout orderDetailRl3;
    @BindView(R.id.product_attribute)
    NoScrollGridview productAttribute;
    @BindView(R.id.attactment)
    TextView attactment;
    @BindView(R.id.tv_attactment)
    TextView tvAttactment;
    @BindView(R.id.rl_down)
    LinearLayout rlDown;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.order_detail_tv_note)
    TextView orderDetailTvNote;
    @BindView(R.id.order_detail_ll_note)
    LinearLayout orderDetailLlNote;
    @BindView(R.id.come_order_detail_lv_fujian_download)
    NoScrollGridview comeOrderDetailLvFujianDownload;
    @BindView(R.id.activity_come_order_product_detail)
    RelativeLayout activityComeOrderProductDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_progress_product_detail);
        ButterKnife.bind(this);
        DialogLoadUtils.getInstance(this);
        initData();
    }

    private void initData() {
        DialogLoadUtils.showDialogLoad("努力加载中...");
        OkHttp();
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void OkHttp() {
        String url = BaseUrlUtils.NET_URL + "order/getOneOrderProduct";
        OkHttpClient ok = new OkHttpClient();
        RequestBody rb = new FormBody.Builder().add("id", getIntent().getStringExtra("orderProductId")).build();
        Log.e("@@@@", BaseUrlUtils.NET_URL + "order/getOneOrderProduct?id="+getIntent().getStringExtra("orderProductId"));

        Request re = new Request.Builder().post(rb).url(url).build();
        ok.newCall(re).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                DialogLoadUtils.dismissDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("@@@@", json);
                DialogLoadUtils.dismissDialog();
                Gson gson = new Gson();
                final OneOrderProduct orderProduct = gson.fromJson(json, OneOrderProduct.class);
                DialogLoadUtils.dismissDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> arrayList = null;
                        if (orderProduct.getResult()!=null&&orderProduct.getResult().getImages() != null && orderProduct.getResult().getImages() != "" && !"".equals(orderProduct.getResult().getImages())) {
                            arrayList = StringToListUtils.stringToList(orderProduct.getResult().getImages(), "\\|");
                        } else {
                            arrayList = new ArrayList<>();
                        }


                        final List<String> finalArrayList = arrayList;
                        //final List<String> picList = StringToListUtils.stringToList(orderProduct.getResult().getImages(), "\\|");

                        tvAttactment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FlowProgressProductDetail.this, AttachmentBrow.class);
                                intent.putExtra("imageList", new Gson().toJson(finalArrayList));
                                startActivity(intent);
                            }
                        });

                        proCateGory.setText(orderProduct.getResult().getCompanyCategory().getName());//产品类
                        orderDetailLlProCateGoryRuler.setText(orderProduct.getResult().getProductName());//产品名称
                        comeOrderDetailSinglePrice.setText(orderProduct.getResult().getPrice());//单价
                        comeOrderDetailTvNum.setText(orderProduct.getResult().getAmount());
                        comeOrderDetailTvEndDateHand.setText(orderProduct.getResult().getDeliveryTime());
                        comeOrderDetailTvMinePriority.setText(PriorityUtils.getPriority(orderProduct.getResult().getCompanyAPriority()));//甲方优先级
                        orderDetailTvNote.setText(orderProduct.getResult().getProductDescription());
                        loadProductAttribute(orderProduct.getResult().getId());//获得产品属性
                    }
                });
            }
        });
    }

    private void loadProductAttribute(String id) {
        String url = BaseUrlUtils.NET_URL + "order/orderProductAttribute";
        OkHttpClient ok = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("id", id).build();
        Request request = new Request.Builder().post(body).url(url).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String json = null;
                json = response.body().string();
                final Gson gson = new Gson();
                final String finalJson = json;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final OrderProductAttributes attributes = gson.fromJson(finalJson, OrderProductAttributes.class);
                        Log.e("ComeOrderProductDetail", attributes.toString());
                        productAttribute.setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return attributes.getResult().getOrderProductSpecificationList().size();
                            }

                            @Override
                            public Object getItem(int position) {
                                return null;
                            }

                            @Override
                            public long getItemId(int position) {
                                return 0;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = View.inflate(FlowProgressProductDetail.this, R.layout.item_product_attribute, null);
                                TextView tv_attribute_name = ((TextView) view.findViewById(R.id.tv_attribute_name));
                                TextView tv_attribute_value = ((TextView) view.findViewById(R.id.tv_attribute_value));
                                tv_attribute_name.setText(attributes.getResult().getOrderProductSpecificationList().get(position).getAttribute().getName() + ":");
                                tv_attribute_value.setText(attributes.getResult().getOrderProductSpecificationList().get(position).getAttributeValue());

                                return view;
                            }
                        });
                    }

                });
            }
        });
    }
}
