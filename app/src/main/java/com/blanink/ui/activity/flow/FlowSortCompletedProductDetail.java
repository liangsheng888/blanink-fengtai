package com.blanink.ui.activity.flow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.AttachmentBrow;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.FlowDetail;
import com.blanink.bean.FlowSort;
import com.blanink.bean.OneOrderProduct;
import com.blanink.bean.OrderProductAttributes;
import com.blanink.bean.OrderProductFlow;
import com.blanink.bean.OrderProgress;
import com.blanink.bean.Response;
import com.blanink.bean.ResponseDelete;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;
import com.blanink.utils.PriorityUtils;
import com.blanink.utils.StringToListUtils;
import com.blanink.view.CusProgressView;
import com.blanink.view.NoScrollListview;
import com.blanink.view.PopBottomWinFlow;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 已派流程 产品详情
 */
public class FlowSortCompletedProductDetail extends BaseActivity {

    OneOrderProduct orderProduct;
    FlowDetail flowDetail;
    @BindView(R.id.come_order)
    TextView comeOrder;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_product_stu)
    TextView tvProductStu;
    @BindView(R.id.tv_modify)
    TextView tvModify;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_seek_progress)
    TextView tvSeekProgress;
    @BindView(R.id.tv_flow_detail)
    TextView tvFlowDetail;
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
    @BindView(R.id.rl_flow)
    RelativeLayout rlFlow;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.cp_progress)
    CusProgressView cpProgress;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_sort_completed_product_detail);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        DialogLoadUtils.getInstance(this);
        initData();
    }

    private void initData() {
        tvFlowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(FlowSortCompletedProductDetail.this, FlowSortCompletedDetail.class);
                it.putExtra("productId", orderProduct.getResult().getId());
                startActivity(it);
            }
        });
        tvModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowSortCompletedProductDetail.this, FlowOftenUse.class);
                intent.putExtra("orderProductId", orderProduct.getResult().getId());
                intent.putExtra("isModify", "modify");
                startActivity(intent);
            }
        });
        tvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPublish.setEnabled(false);
                showDialogExit(flowDetail.getResult().getId(), v);

            }
        });
        //  查看进度
        tvSeekProgress.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  DialogLoadUtils.showDialogLoad("努力加载中...");
                                                  postAsynHttp();
                                              }
                                          }

        );

        loadProductDetail();

        Bundle bundle = getIntent().getExtras();
        FlowSort.ResultBean.RowsBean orderProduct = ((FlowSort.ResultBean.RowsBean) bundle.getSerializable("orderProduct"));

        List<String> arrayList = null;
        if (orderProduct.getOrderProduct() != null && orderProduct.getOrderProduct().getImages() != null && !"".equals(orderProduct.getOrderProduct().getImages())) {
            arrayList = StringToListUtils.stringToList(orderProduct.getOrderProduct().getImages(), "\\|");
        } else {
            arrayList = new ArrayList<>();
        }


        final List<String> finalArrayList = arrayList;
        //final List<String> picList = StringToListUtils.stringToList(orderProduct.getOrderProduct().getImages(), "\\|");

        //final List<String> picList = StringToListUtils.stringToList(orderProduct.getOrderProduct().getImages(), "\\|");

        if (finalArrayList.size() == 0) {
            tvAttactment.setText("无附件");
            tvAttactment.setTextColor(getResources().getColor(R.color.gray));
            tvAttactment.setBackground(getResources().getDrawable(R.drawable.selector_corner_black));
        } else {
            tvAttactment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FlowSortCompletedProductDetail.this, AttachmentBrow.class);
                    intent.putExtra("imageList", new Gson().toJson(finalArrayList));
                    startActivity(intent);
                }
            });

              /*  //点击放大
                flowMyPager.setImageViewListener(new MyPagerList.ImageViewOnClickListener() {
                    @Override
                    public void imageViewOnClick(int position) {
                        PhotoPreview.builder()
                                .setPhotos(arrayList)
                                .setCurrentItem(position)
                                .setShowDeleteButton(false)
                                .start(FlowSortCompletedProductDetail.this);
                    }
                });*/
        }


        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("@@@@", "tvMore");
                tvMore.setEnabled(false);
                checkIsHasFlow(true, false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void checkIsHasFlow(final Boolean isClick, final Boolean isModify) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        if (orderProduct.getResult() != null) {
            mapParams.put("orderProduct.id", orderProduct.getResult().getId());
        }
        RetrofitFactory.getApiService(this)
                .getFlow(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FlowDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        tvMore.setEnabled(true);

                    }

                    @Override
                    public void onNext(final FlowDetail flowDetail) {
                        tvMore.setEnabled(true);
                        if ("1".equals(flowDetail.getResult().getStatus())) {
                            tvPublish.setVisibility(View.VISIBLE);
                            tvModify.setVisibility(View.VISIBLE);
                            tvSeekProgress.setVisibility(View.GONE);
                        }
                        if ("1".equals(flowDetail.getResult().getStatus()) && isModify) {
                            Intent intent = new Intent(FlowSortCompletedProductDetail.this, FlowOftenUse.class);
                            intent.putExtra("orderProductId", orderProduct.getResult().getId());
                            intent.putExtra("isModify", "modify");
                            startActivity(intent);
                        }

                        if (isClick) {
                            PopBottomWinFlow popBottomWin = new PopBottomWinFlow(FlowSortCompletedProductDetail.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogLoadUtils.getInstance(FlowSortCompletedProductDetail.this);
                                    switch (v.getId()) {
                                        case R.id.tv_public:

                                            showDialogExit(flowDetail.getResult().getId(), v);

                                            //发布
                                            break;
                                        case R.id.tv_seek_progress:
                                            DialogLoadUtils.showDialogLoad("努力加载...");
                                            postAsynHttp();
                                            //查看进度
                                            break;
                                        case R.id.tv_modify:
                                            checkIsHasFlow(false, true);

                                            //重新排流程
                                            break;
                                        case R.id.tv_seek:
                                            //流程详情
                                            Intent it = new Intent(FlowSortCompletedProductDetail.this, FlowSortCompletedDetail.class);
                                            it.putExtra("productId", orderProduct.getResult().getId());
                                            startActivity(it);
                                            break;
                                        case R.id.tv_remark:
                                            //留言
                                            Intent itRemark = new Intent(FlowSortCompletedProductDetail.this, FlowComments.class);
                                            itRemark.putExtra("flowId", flowDetail.getResult().getId());
                                            startActivity(itRemark);
                                            break;
                                    }
                                }
                            }, flowDetail.getResult().getStatus());
                            popBottomWin.showAtLocation(findViewById(R.id.come_order_detail_rl), Gravity.CENTER, 0, 0);
                        }
                    }
                });
    }

    private void initFlow() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
            mapParams.put("orderProduct.id", orderProduct.getResult().getId());

        RetrofitFactory.getApiService(this)
                .getFlow(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FlowDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FlowDetail fd) {
                        flowDetail=fd;
                    }
                });
    }

    private void upDataFlowState(String id, final View v) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", id);

        RetrofitFactory.getApiService(this)
                .publish(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        tvPublish.setEnabled(true);
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(FlowSortCompletedProductDetail.this, "服务器开了会儿小差", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if ("00000".equals(response.errorCode)) {
                            DialogLoadUtils.dismissDialog();
                            ((TextView) v).setText("流程已发布");
                            ((TextView) v).setBackgroundColor(getResources().getColor(R.color.colorBackGround));
                            ((TextView) v).setEnabled(false);
                            tvPublish.setText("流程已发布");
                            tvPublish.setBackgroundColor(getResources().getColor(R.color.colorBackGround));
                            tvPublish.setEnabled(false);
                            tvModify.setVisibility(View.GONE);
                            Map<String, Object> paramsMap = new HashMap<String, Object>();
                            paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
                            paramsMap.put("orderProduct.id", orderProduct.getResult().getId());
                            paramsMap.put("sendUser", orderProduct.getResult().getACompany().getId());
                            //激光推送
                            RetrofitFactory.getApiService(FlowSortCompletedProductDetail.this)
                                    .flowPublish(paramsMap)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<ResponseDelete>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(ResponseDelete responseDelete) {
                                            Log.e("ReceiveGoods", responseDelete.getReason());

                                        }
                                    });

                            Toast.makeText(FlowSortCompletedProductDetail.this, "流程已发布", Toast.LENGTH_SHORT).show();

                        } else {
                            DialogLoadUtils.dismissDialog();

                        }
                    }
                });

    }


    public void loadProductDetail() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", getIntent().getStringExtra("productId"));
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
                        orderProduct = oneOrderProduct;
                        //初始化数据
                        initFlow();
                        checkIsHasFlow(false, false);
                        if (getIntent().getStringExtra("photo") != null && !getIntent().getStringExtra("photo").equals("")) {
                            GlideUtils.glideImageView(FlowSortCompletedProductDetail.this, ivPhoto, getIntent().getStringExtra("photo"), true);
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
                        if (orderProduct.getResult().getProductDescription() != null && !orderProduct.getResult().getProductDescription().equals("")) {
                            webContent.getSettings().setDefaultTextEncodingName("UTF -8");
                            webContent.getSettings().setJavaScriptEnabled(true);
                            webContent.getSettings().setDefaultFontSize(14);
                            webContent.loadData(orderProduct.getResult().getProductDescription(), "text/html; charset=UTF-8", null);
                        }
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

                    }

                    @Override
                    public void onNext(final OrderProductAttributes orderProductAttributes) {
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
                                View view = View.inflate(FlowSortCompletedProductDetail.this, R.layout.item_product_attribute, null);
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


    private void postAsynHttp() {
        //   http://localhost:8080/blanink-api/flow/getFlowPlan?type=2&orderProduct.id=6c57048a31e741a3810e37440903032d//
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("type", "3")
                .add("orderProduct.id", orderProduct.getResult().getId())
                .build();
        Request request = new Request.Builder()
                .url(BaseUrlUtils.NET_URL + "flow/getFlowPlan")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Log.e("FlowSort", BaseUrlUtils.NET_URL + "flow/getFlowPlan?type=3&orderProduct.id=" + orderProduct.getResult().getId());
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
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
                            if (orderProgress.getResult().size() > 1) {
                                Intent intent = new Intent(FlowSortCompletedProductDetail.this, FlowProgress.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("orderProgress", orderProgress);
                                intent.putExtra("type", "3");
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (orderProgress.getResult().size() == 1) {
                                Intent intent = new Intent(FlowSortCompletedProductDetail.this, FlowProgressDetail.class);
                                intent.putExtra("flowId", orderProgress.getResult().get(0).getFlow().getId());
                                intent.putExtra("type", "3");
                                intent.putExtra("flag", "FLOW_SORTED");
                                startActivity(intent);
                            }
                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(FlowSortCompletedProductDetail.this, "服务器开了会小儿差", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //根据产品id 判断是否可以重新拍流程
    public void checkIsCanSortFlowByOrderProdutId(String orderProductId) {
        String url = BaseUrlUtils.NET_URL + "flow/getFlowByOrderProduct";
        OkHttpClient ok = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("orderProduct.id", orderProductId)
                .build();
        Request request = new Request.Builder().post(body).url(url).build();
        ok.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                final OrderProductFlow orderProductFlow = gson.fromJson(json, OrderProductFlow.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (orderProductFlow.getResult().size() == 0) {
                            //新建流程
                        } else {
                            if ("1".equals(orderProductFlow.getResult().get(0).getStatus())) {
                                //已有流程
                            }
                        }
                    }
                });
            }
        });
    }

    private void showDialogExit(final String id, final View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_exit);
        final Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);

        ((TextView) window.findViewById(R.id.tv_content)).setText("流程发布后不能进行修改，确定发布吗?");
        ((TextView) window.findViewById(R.id.tv_continue)).setText("取消");
        ((TextView) window.findViewById(R.id.tv_exit)).setText("确定");

        window.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                tvPublish.setEnabled(true);
            }
        });
        window.findViewById(R.id.tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                DialogLoadUtils.showDialogLoad("正在发布...");
                upDataFlowState(id, view);
            }
        });
    }

}
