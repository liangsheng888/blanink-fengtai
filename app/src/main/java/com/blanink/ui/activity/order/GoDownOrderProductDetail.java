package com.blanink.ui.activity.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.AttachmentBrow;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.activity.aftersale.AfterSaleDemand;
import com.blanink.ui.activity.flow.FlowProgress;
import com.blanink.ui.activity.flow.FlowProgressDetail;
import com.blanink.ui.activity.remark.RemarkGoOrder;
import com.blanink.ui.activity.remark.RemarkGoOrderReview;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.OneOrderProduct;
import com.blanink.bean.OrderProductAttributes;
import com.blanink.bean.OrderProductStatus;
import com.blanink.bean.OrderProgress;
import com.blanink.bean.Response;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.DialogNotifyUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;
import com.blanink.utils.PriorityUtils;
import com.blanink.utils.StringToListUtils;
import com.blanink.utils.SysConstants;
import com.blanink.view.CusProgressView;
import com.blanink.view.NoScrollListview;
import com.blanink.view.PopBottomWinGo;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//去单 产品详情
public class GoDownOrderProductDetail extends BaseActivity {

    OneOrderProduct orderProduct;
    @BindView(R.id.come_order)
    TextView comeOrder;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.cp_progress)
    CusProgressView cpProgress;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_product_stu)
    TextView tvProductStu;
    @BindView(R.id.tv_modify_supplier)
    TextView tvModifySupplier;
    @BindView(R.id.tv_add_note)
    TextView tvAddNote;
    @BindView(R.id.tv_seek_progress)
    TextView tvSeekProgress;
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
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
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
    private SharedPreferences sp;
    private String coments;
    private AlertDialog alertDialog;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_modify:
                    Intent intent = new Intent(GoDownOrderProductDetail.this, OrderProductModify.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("productDetail", orderProduct);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.tv_modify_supplier:

                    Intent intentModify = new Intent(GoDownOrderProductDetail.this, GoDownOrderProductDetailModifySupplier.class);
                    intentModify.putExtra("currentSupplierId", orderProduct.getResult().getBCompany().getId());
                    Bundle bd = new Bundle();
                    bd.putSerializable("orderProduct", orderProduct);
                    intentModify.putExtras(bd);
                    startActivity(intentModify);

                    break;
                case R.id.tv_after_sale:
                    Intent intentAfter = new Intent(GoDownOrderProductDetail.this, AfterSaleDemand.class);
                    if (orderProduct.getResult() != null && orderProduct.getResult().getBCompany() != null) {
                        intentAfter.putExtra("orderProdCompanyId", orderProduct.getResult().getBCompany().getId());
                    }
                    intentAfter.putExtra("orderProdId", orderProduct.getResult().getId());
                    startActivity(intentAfter);

                    //售后
                    break;
                case R.id.tv_remark:
                    if ("1".equals(orderProduct.getResult().getReviewFinish())) {
                        //回复
                        Intent itRemark = new Intent(GoDownOrderProductDetail.this, RemarkGoOrderReview.class);
                        itRemark.putExtra("orderProductId", orderProduct.getResult().getId());
                        itRemark.putExtra("orderId", orderProduct.getResult().getOrder().getId());
                        itRemark.putExtra("productName", orderProduct.getResult().getProductName());
                        itRemark.putExtra("productCategory", orderProduct.getResult().getCompanyCategory().getName());
                        itRemark.putExtra("amount", orderProduct.getResult().getAmount());
                        itRemark.putExtra("deliverTime", orderProduct.getResult().getDeliveryTimeString());
                        itRemark.putExtra("productRemarks", orderProduct.getResult().getProductDescription());
                        itRemark.putExtra("price", orderProduct.getResult().getPrice());
                        itRemark.putExtra("productSo", orderProduct.getResult().getProductSn());
                        startActivity(itRemark);
                    } else {
                        //去评价
                        Intent itRemark = new Intent(GoDownOrderProductDetail.this, RemarkGoOrder.class);
                        itRemark.putExtra("orderProductId", orderProduct.getResult().getId());
                        itRemark.putExtra("orderId", orderProduct.getResult().getOrder().getId());
                        itRemark.putExtra("productName", orderProduct.getResult().getProductName());
                        itRemark.putExtra("amount", orderProduct.getResult().getAmount());
                        itRemark.putExtra("deliverTime", orderProduct.getResult().getDeliveryTime());
                        itRemark.putExtra("productRemarks", orderProduct.getResult().getProductDescription());
                        itRemark.putExtra("price", orderProduct.getResult().getPrice());
                        itRemark.putExtra("productSo", orderProduct.getResult().getProductSn());
                        startActivityForResult(itRemark, 0);
                    }


                    break;
                case R.id.tv_down_send:
                    callBackNotify("重新下发", "取消", "重新下发产品", v);
                    break;
                case R.id.tv_seek_progress:
                    DialogLoadUtils.showDialogLoad("努力加载中...");
                    postAsynHttp();
                    break;
                case R.id.tv_add_note:
                    Intent it = new Intent(GoDownOrderProductDetail.this, ComeOrderProductDetailTalkNote.class);
                    it.putExtra("productId", orderProduct.getResult().getId());
                    startActivity(it);
                    break;
            }
        }
    };
    private Dialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_down_order_product_detail);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }


    private void initData() {
        OkHttp();
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderProductStatus(orderProduct.getResult().getId());
            }
        });


        //  查看进度
        tvSeekProgress.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  DialogLoadUtils.getInstance(GoDownOrderProductDetail.this);
                                                  DialogLoadUtils.showDialogLoad("努力加载中...");
                                                  postAsynHttp();
                                              }
                                          }

        );

        tvAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(GoDownOrderProductDetail.this, ComeOrderProductDetailTalkNote.class);
                it.putExtra("productId", orderProduct.getResult().getId());
                startActivity(it);
            }
        });

        tvModifySupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentModify = new Intent(GoDownOrderProductDetail.this, GoDownOrderProductDetailModifySupplier.class);
                intentModify.putExtra("currentSupplierId", orderProduct.getResult().getBCompany().getId());
                Bundle bd = new Bundle();
                bd.putSerializable("orderProduct", orderProduct);
                intentModify.putExtras(bd);
                startActivity(intentModify);

            }
        });
    }
          /*  //点击放大
            myPager.setImageViewListener(new MyPagerList.ImageViewOnClickListener() {
                @Override
                public void imageViewOnClick(int position) {
                    PhotoPreview.builder()
                            .setPhotos(arrayList)
                            .setCurrentItem(position)
                            .setShowDeleteButton(false)
                            .start(GoDownOrderProductDetail.this);
                }
            });*/


/*

        //重新下发（当对方打回的时候）
        if (SysConstants.ORDER_PRODUCT_STATUS_BACK_TO_A.equals(orderProduct.getOrderProductStatus())) {
            tvDownSend.setVisibility(View.VISIBLE);
            tvModify.setVisibility(View.VISIBLE);
            tvModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GoDownOrderProductDetail.this, OrderProductModify.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("productDetail", orderProduct);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            tvDownSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //改变状态
                    callBackNotify("重新下发", "取消", "重新下发产品");
                }
            });
        }
        if (SysConstants.ORDER_PRODUCT_SRTATUS_REJECT.equals(orderProduct.getOrderProductStatus())) {
            //修改供应商（当对方拒绝的时候）
            tvModifySupplier.setVisibility(View.VISIBLE);
            tvModifySupplier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GoDownOrderProductDetail.this, GoDownOrderProductDetailModifySupplier.class);
                    intent.putExtra("currentSupplierId", orderProduct.getBCompany().getId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderProduct", orderProduct);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        if (SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_PRODUCTION_END.equals(orderProduct.getOrderProductStatus())
                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_DELIEVERY_PART.equals(orderProduct.getOrderProductStatus())
                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_RECEIVED_PART.equals(orderProduct.getOrderProductStatus())
                || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_RECEIVED_OVER.equals(orderProduct.getOrderProductStatus())) {
            //售后
            tvAfterSale.setVisibility(View.VISIBLE);
            tvAfterSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        //沟通记录
        tvAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoDownOrderProductDetail.this, ComeOrderProductDetailTalkNote.class);
                startActivity(intent);
            }
        });
        //评价
        tvRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/


    private void downSend(String comments, final View v) {
        //http://localhost:8080/blanink-api/order/reTransfer?
        // senderCompany=9ce7467e94884e7192a14ad3c29c554d&
        // orderProductId=13ca69d4c53c4a7c8185f5929cbc93b5&
        // comments=色色色谔谔谔谔&
        // senderName=额呃呃呃&
        // sender=3c70d715d02a4c4aadc1952ecde286e1
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("orderProductId", orderProduct.getResult().getId());
        mapParams.put("comments", comments);
        mapParams.put("sender", sp.getString("USER_ID", null));
        mapParams.put("senderName", sp.getString("NAME", null));
        mapParams.put("senderCompany", sp.getString("COMPANY_ID", null));

        RetrofitFactory.getApiService(this)
                .reTransfer(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response response) {
                        if ("00000".equals(response.getErrorCode())) {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(GoDownOrderProductDetail.this, "操作成功", Toast.LENGTH_SHORT).show();
                    /*tvDownSend.setText("已重新下发");
                    tvDownSend.setEnabled(false);
                    tvDownSend.setBackgroundColor(getResources().getColor(R.color.colorGray));*/
                            alertDialog.dismiss();
                            ((TextView) v).setText("已重新下发成功");
                            ((TextView) v).setBackgroundColor(getResources().getColor(R.color.colorBackGround));
                            ((TextView) v).setEnabled(false);
                            DialogNotifyUtils.showNotify(GoDownOrderProductDetail.this, "已重新下发成功");
                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(GoDownOrderProductDetail.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @OnClick(R.id.iv_last)
    public void onClick() {
        finish();
    }

    //加载产品属性
    public void loadProductAttribute(String productId) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", productId);


        RetrofitFactory.getApiService(this)
                .orderProductAttribute(mapParams)
                .subscribeOn(Schedulers.io())
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
                                View view = View.inflate(GoDownOrderProductDetail.this, R.layout.item_product_attribute, null);
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

    private void callBackNotify(String title, String left, String right, View v) {
        alertDialog = new AlertDialog.Builder(this).create();
        final View view = View.inflate(this, R.layout.dialog_send, null);
        alertDialog.setView(view);
        alertDialog.show();
        final Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((Button) view.findViewById(R.id.btn_cancel)).setText(left);
        ((Button) view.findViewById(R.id.btn_send)).setText(right);

        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送理由
                String content = ((EditText) view.findViewById(R.id.et_info)).getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(GoDownOrderProductDetail.this, "请填写备注信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.showDialogLoad("重新下发中...");
                downSend(content, v);
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void OrderProductStatus(String productId) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", productId);
        RetrofitFactory.getApiService(this)
                .getOneOrderProductStatus(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderProductStatus>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderProductStatus orderProductStatus) {
                        PopBottomWinGo popBottomWinGo = new PopBottomWinGo(GoDownOrderProductDetail.this, onClickListener, orderProductStatus.getResult());
                        popBottomWinGo.showAtLocation(findViewById(R.id.activity_come_order_product_detail), Gravity.CENTER, 0, 0);

                    }
                });
    }

    private void postAsynHttp() {
        //   http://localhost:8080/blanink-api/flow/getFlowPlan?type=2&orderProduct.id=6c57048a31e741a3810e37440903032d//
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("type", "2")
                .add("orderProduct.id", orderProduct.getResult().getId())

                .build();
        Request request = new Request.Builder()
                .url(BaseUrlUtils.NET_URL + "flow/getFlowPlan")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Log.e("FlowSort", BaseUrlUtils.NET_URL + "flow/getFlowPlan?type=2&orderProduct.id=" + orderProduct.getResult().getId());
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                Log.e("FlowProgress", str);
                final OrderProgress orderProgress = gson.fromJson(str, OrderProgress.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();

                        if ("00000".equals(orderProgress.getErrorCode())) {
                            DialogLoadUtils.dismissDialog();
                            if (orderProgress.getResult().size() > 1) {
                                Intent intent = new Intent(GoDownOrderProductDetail.this, FlowProgress.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("orderProgress", orderProgress);
                                intent.putExtras(bundle);
                                intent.putExtra("type", "2");

                                startActivity(intent);

                            } else if (orderProgress.getResult().size() == 1) {
                                Intent intent = new Intent(GoDownOrderProductDetail.this, FlowProgressDetail.class);
                                intent.putExtra("flowId", orderProgress.getResult().get(0).getFlow().getId());
                                intent.putExtra("type", "2");
                                intent.putExtra("notOwnCompany", "yes");
                                startActivity(intent);
                            }
                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(GoDownOrderProductDetail.this, "服务器开了会小儿差", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void OkHttp() {
        String url = BaseUrlUtils.NET_URL + "order/getOneOrderProduct";
        OkHttpClient ok = new OkHttpClient();
        RequestBody rb = new FormBody.Builder().add("id", getIntent().getStringExtra("orderProductId")).
                add("order.id", getIntent().getStringExtra("orderId")).build();
        Log.e("GoDown", url + "?order.id=" + getIntent().getStringExtra("orderId") + "&id=" + getIntent().getStringExtra("orderProductId"));
        Request re = new Request.Builder().post(rb).url(url).build();
        ok.newCall(re).enqueue(new okhttp3.Callback() {
                                   @Override
                                   public void onFailure(Call call, IOException e) {
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               cpProgress.setVisibility(View.GONE);

                                           }
                                       });
                                   }

                                   @Override
                                   public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                       String json = response.body().string();
                                       Log.e("@@@@", json);
                                       Gson gson = new Gson();
                                       orderProduct = gson.fromJson(json, OneOrderProduct.class);
                                       runOnUiThread(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             cpProgress.setVisibility(View.GONE);
                                                             //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
                                                             List<String> arrayList = null;
                                                             if (orderProduct.getResult() != null) {

                                                                 //查看进度
                                                                 if (SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_FLOW_PULISHED.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_PRODUCTION_END.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_DELIEVERY_PART.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_DELIEVERY_OVER.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_RECEIVED_PART.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_COMPANY_B_RECEIVED_OVER.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_COMMENT_OVER.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_AFTERSALES_START.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_AFTERSALES_DEALING.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_AFTERSALES_DEALING_OVER.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_STATUS_AFTERSALES_DEALING_OVER_CONFIRMED.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         || SysConstants.ORDER_PRODUCT_SRTATUS_REJECT_TAKE.equals(orderProduct.getResult().getOrderProductStatus())
                                                                         ) {
                                                                     tvSeekProgress.setVisibility(View.VISIBLE);
                                                                 }
                                                                 //修改供应商
                                                                 if (SysConstants.ORDER_PRODUCT_SRTATUS_REJECT.equals(orderProduct.getResult().getOrderProductStatus())) {
                                                                     tvModifySupplier.setVisibility(View.VISIBLE);

                                                                 }
                                                                 if (orderProduct.getResult().getImages() != null && orderProduct.getResult().getImages() != "" && !"".equals(orderProduct.getResult().getImages())) {
                                                                     arrayList = StringToListUtils.stringToList(orderProduct.getResult().getImages(), "\\|");
                                                                 } else {
                                                                     arrayList = new ArrayList<>();
                                                                 }


                                                                 final List<String> finalArrayList = arrayList;
                                                                 // final List<String> stringList = StringToListUtils.stringToList(orderProduct.getResult().getImages(), ",");
                                                                 if (finalArrayList.size() == 0) {
                                                                     tvAttactment.setText("无附件");
                                                                     tvAttactment.setTextColor(getResources().getColor(R.color.gray));
                                                                     tvAttactment.setBackground(getResources().getDrawable(R.drawable.selector_corner_black));
                                                                 } else {
                                                                     tvAttactment.setOnClickListener(new View.OnClickListener() {
                                                                         @Override
                                                                         public void onClick(View v) {
                                                                             Intent intent = new Intent(GoDownOrderProductDetail.this, AttachmentBrow.class);
                                                                             intent.putExtra("imageList", new Gson().toJson(finalArrayList));
                                                                             startActivity(intent);
                                                                         }
                                                                     });
                                                                 }
                                                                 proCateGory.setText(orderProduct.getResult().getCompanyCategory().getName());//产品类
                                                                 orderDetailLlProCateGoryRuler.setText(orderProduct.getResult().getProductName());//产品名称
                                                                 comeOrderDetailSinglePrice.setText(orderProduct.getResult().getPrice());//单价
                                                                 comeOrderDetailTvNum.setText(orderProduct.getResult().getAmount());
                                                                 comeOrderDetailTvEndDateHand.setText(orderProduct.getResult().getDeliveryTime());
                                                                 comeOrderDetailTvMinePriority.setText(PriorityUtils.getPriority(orderProduct.getResult().getCompanyAPriority()));//甲方优先级
                                                                 tvStartTime.setText(orderProduct.getResult().getCreateDate() + "");
                                                                 if (orderProduct.getResult().getProductDescription() != null && !orderProduct.getResult().getProductDescription().equals("")) {
                                                                     webContent.getSettings().setDefaultTextEncodingName("UTF -8");
                                                                     webContent.getSettings().setJavaScriptEnabled(true);
                                                                     webContent.getSettings().setDefaultFontSize(14);
                                                                     webContent.loadData(orderProduct.getResult().getProductDescription(), "text/html; charset=UTF-8", null);
                                                                 }
                                                                 if (getIntent().getStringExtra("photo") != null && !getIntent().getStringExtra("photo").equals("")) {
                                                                     GlideUtils.glideImageView(GoDownOrderProductDetail.this, ivPhoto, getIntent().getStringExtra("photo"), true);
                                                                 }
                                                                 tvProductStu.setText(OrderProductStateUtils.orderProductStatus(orderProduct.getResult().getOrderProductStatus()));
                                                                 tvProductNo.setText(orderProduct.getResult().getProductSn());
                                                                 loadProductAttribute(orderProduct.getResult().getId());//获得产品属性
                                                             }
                                                         }
                                                     }

                                       );
                                   }
                               }

        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OkHttp();
    }

}
