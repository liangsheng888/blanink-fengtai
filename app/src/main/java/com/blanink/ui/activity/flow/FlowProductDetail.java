package com.blanink.ui.activity.flow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.AttachmentBrow;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.OneOrderProduct;
import com.blanink.bean.OrderDetail;
import com.blanink.bean.OrderProductAttributes;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;
import com.blanink.utils.PriorityUtils;
import com.blanink.utils.StringToListUtils;
import com.blanink.view.CusProgressView;
import com.blanink.view.NoScrollListview;
import com.google.gson.Gson;


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
 * 流程产品 详情
 */
public class FlowProductDetail extends BaseActivity {
    @BindView(R.id.come_order)
    TextView comeOrder;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_product_stu)
    TextView tvProductStu;
    @BindView(R.id.btn_flow)
    Button btnFlow;
    @BindView(R.id.item_come_order_detail_product)
    LinearLayout itemComeOrderDetailProduct;
    @BindView(R.id.tv_product_no)
    TextView tvProductNo;
    @BindView(R.id.tv_ruler)
    TextView tvRuler;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.order_detail_ll_proCateGory_ruler)
    TextView orderDetailLlProCateGoryRuler;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.proCateGory)
    TextView proCateGory;
    @BindView(R.id.order_detail_ll_proCateGory)
    LinearLayout orderDetailLlProCateGory;
    @BindView(R.id.tv_mine_priority)
    TextView tvMinePriority;
    @BindView(R.id.come_order_detail_tv_mine_priority)
    TextView comeOrderDetailTvMinePriority;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.come_order_detail_single_price)
    TextView comeOrderDetailSinglePrice;
    @BindView(R.id.order_detail_rl2)
    LinearLayout orderDetailRl2;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.come_order_detail_tv_num)
    TextView comeOrderDetailTvNum;
    @BindView(R.id.tv_endDateHand)
    TextView tvEndDateHand;
    @BindView(R.id.come_order_detail_tv_endDateHand)
    TextView comeOrderDetailTvEndDateHand;
    @BindView(R.id.order_detail_rl3)
    LinearLayout orderDetailRl3;
    @BindView(R.id.product_attribute)
    NoScrollListview productAttribute;
    @BindView(R.id.attactment)
    TextView attactment;
    @BindView(R.id.tv_attactment)
    TextView tvAttactment;
    @BindView(R.id.rl_down)
    RelativeLayout rlDown;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.web_content)
    WebView webContent;
    @BindView(R.id.order_detail_ll_note)
    LinearLayout orderDetailLlNote;
    @BindView(R.id.activity_come_order_product_detail)
    RelativeLayout activityComeOrderProductDetail;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.cp_progress)
    CusProgressView cpProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_product_detail);

        ButterKnife.bind(this);
        DialogLoadUtils.getInstance(this);
        initData();
    }

    private void initData() {

        loadProductDetail();

        Bundle bundle = getIntent().getExtras();
        OrderDetail.ResultBean orderProduct = ((OrderDetail.ResultBean) bundle.getSerializable("orderProduct"));
        if ("7".equals(orderProduct.getOrderProductStatus())) {
            btnFlow.setVisibility(View.VISIBLE);
        }

        List<String> arrayList = null;
        if (orderProduct.getImages() != null && orderProduct.getImages() != "" && !"".equals(orderProduct.getImages())) {
            arrayList = StringToListUtils.stringToList(orderProduct.getImages(), "\\|");
        } else {
            arrayList = new ArrayList<>();
        }


        final List<String> finalArrayList = arrayList;
        // final List<String> picList = StringToListUtils.stringToList(orderProduct.getImages(), "\\|");
        if (finalArrayList.size() == 0) {
            tvAttactment.setText("无附件");
            tvAttactment.setTextColor(getResources().getColor(R.color.gray));
            tvAttactment.setBackground(getResources().getDrawable(R.drawable.selector_corner_black));
        } else {
            tvAttactment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FlowProductDetail.this, AttachmentBrow.class);
                    intent.putExtra("imageList", new Gson().toJson(finalArrayList));
                    startActivity(intent);
                }
            });
        }
             /*   final ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.addAll(picList);
                //点击放大
                myPager.setImageViewListener(new MyPagerList.ImageViewOnClickListener() {
                    @Override
                    public void imageViewOnClick(int position) {
                        PhotoPreview.builder()
                                .setPhotos(arrayList)
                                .setCurrentItem(position)
                                .setShowDeleteButton(false)
                                .start(FlowProductDetail.this);
                    }
                });*/


        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //排流程
        btnFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowProductDetail.this, FlowOftenUse.class);
                intent.putExtra("orderProductId", getIntent().getStringExtra("orderProductId"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void loadProductDetail() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", getIntent().getStringExtra("orderProductId"));
        RetrofitFactory.getApiService(this)
                .getOneOrderProduct(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OneOrderProduct>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        cpProgress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onNext(OneOrderProduct oneOrderProduct) {
                        cpProgress.setVisibility(View.GONE);

                        OneOrderProduct orderProduct =oneOrderProduct;
                        Log.e("@@@@", orderProduct.toString());
                        //初始化数据
                        if (getIntent().getStringExtra("photo") != null && !getIntent().getStringExtra("photo").equals("")) {
                            GlideUtils.glideImageView(FlowProductDetail.this, ivPhoto, getIntent().getStringExtra("photo"), true);
                        }
                        tvProductStu.setText(OrderProductStateUtils.orderProductStatus(orderProduct.getResult().getOrderProductStatus()));
                        tvProductNo.setText(orderProduct.getResult().getProductSn());
                        proCateGory.setText(orderProduct.getResult().getCompanyCategory().getName());//产品类
                        orderDetailLlProCateGoryRuler.setText(orderProduct.getResult().getProductName());//产品名称
                        comeOrderDetailSinglePrice.setText(orderProduct.getResult().getPrice());//单价
                        comeOrderDetailTvNum.setText(orderProduct.getResult().getAmount());
                        comeOrderDetailTvEndDateHand.setText(orderProduct.getResult().getDeliveryTime());
                        tvStartTime.setText(orderProduct.getResult().getCreateDate() + "");
                        comeOrderDetailTvMinePriority.setText(PriorityUtils.getPriority(orderProduct.getResult().getCompanyAPriority()));//甲方优先级
                        webContent.getSettings().setDefaultTextEncodingName("UTF -8");
                        webContent.getSettings().setJavaScriptEnabled(true);
                        webContent.loadData(orderProduct.getResult().getProductDescription(), "text/html; charset=UTF-8", null);
                        loadProductAttribute(orderProduct.getResult().getId());//获得产品属性
                    }
                });
    }

    //加载产品属性
    public void loadProductAttribute(String productId) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", productId);
        RetrofitFactory.getApiService(this)
                .orderProductAttribute(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderProductAttributes>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        cpProgress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onNext(final OrderProductAttributes orderProductAttributes) {
                        cpProgress.setVisibility(View.GONE);
                        productAttribute.setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return orderProductAttributes.getResult().getOrderProductSpecificationList().size();
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
                                View view = View.inflate(FlowProductDetail.this, R.layout.item_product_attribute, null);
                                TextView tv_attribute_name = ((TextView) view.findViewById(R.id.tv_attribute_name));
                                TextView tv_attribute_value = ((TextView) view.findViewById(R.id.tv_attribute_value));
                                tv_attribute_name.setText(orderProductAttributes.getResult().getOrderProductSpecificationList().get(position).getAttribute().getName() + ":");
                                tv_attribute_value.setText(orderProductAttributes.getResult().getOrderProductSpecificationList().get(position).getAttributeValue());

                                return view;
                            }
                        });

                    }
                });

    }

}
