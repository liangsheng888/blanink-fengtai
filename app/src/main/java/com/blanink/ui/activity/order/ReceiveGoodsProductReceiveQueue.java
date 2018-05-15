package com.blanink.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.ProductDeliverInfo;
import com.blanink.bean.ResponseDelete;
import com.blanink.http.RetrofitFactory;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.activity.stock.OutInStockAdd;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.DateUtils;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;
import com.blanink.view.CustomSwipeRefreshLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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
 * 收货列表
 */
public class ReceiveGoodsProductReceiveQueue extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.receive)
    RelativeLayout receive;
    @BindView(R.id.lv)
    SwipeMenuListView lv;
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
    private AlertDialog alertDialog;
    private SharedPreferences sp;
    private SparseArray<View> sparseArray;
    private ProductDeliverInfo orderProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_goods_product_receive_queue);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        EventBus.getDefault().register(this);

        ButterKnife.bind(this);
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSyncRefresh(DataSynRefreshEvent event) {
        if (event.getCode() == 4) {
            OkHttp();
        }

    }

    private void initData() {
        OkHttp();
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                OkHttp();
            }
        });

    }

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void OkHttp() {
        String url = BaseUrlUtils.NET_URL + "order/getOrderProductWithDeliveryInfo";
        OkHttpClient ok = new OkHttpClient();
        final RequestBody rb = new FormBody.Builder().add("id", getIntent().getStringExtra("orderProductId")).build();
        Request re = new Request.Builder().post(rb).url(url).build();
        ok.newCall(re).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llLoad.setVisibility(View.GONE);
                        Toast.makeText(ReceiveGoodsProductReceiveQueue.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("@@@@", json);
                Gson gson = new Gson();
                 orderProduct = gson.fromJson(json, ProductDeliverInfo.class);
                final List<ProductDeliverInfo.ResultBean.RelFlowProcessListBean> relList = orderProduct.getResult().getRelFlowProcessList();
                final List<ProductDeliverInfo.ResultBean.RelFlowProcessListBean.ProcessFeedbackListBean> pfList = new ArrayList<ProductDeliverInfo.ResultBean.RelFlowProcessListBean.ProcessFeedbackListBean>();
                for (int i = 0; i < relList.size(); i++) {
                    for (ProductDeliverInfo.ResultBean.RelFlowProcessListBean.ProcessFeedbackListBean pf : relList.get(i).getProcessFeedbackList()) {
                        pf.setProcess(relList.get(i).getProcess());
                        pfList.add(pf);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llLoad.setVisibility(View.GONE);
                        if (pfList.size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        lv.setAdapter(new ReceiveAdapter(pfList));

                        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                if (pfList.get(position).getConfirmAmount() == 0) {
                                    switch (index) {
                                        case 0:
                                            Intent intent = new Intent(ReceiveGoodsProductReceiveQueue.this, ReceiveGoodsToReceive.class);
                                            intent.putExtra("orderProductId", orderProduct.getResult().getId());
                                            intent.putExtra("orderId", orderProduct.getResult().getOrder().getId());
                                            if (orderProduct.getResult().getBCompany() != null) {
                                                intent.putExtra("companyId", orderProduct.getResult().getBCompany().getId());
                                            }
                                            intent.putExtra("productRemarks", orderProduct.getResult().getRemarks());

                                            intent.putExtra("price", orderProduct.getResult().getPrice());
                                            intent.putExtra("amount", orderProduct.getResult().getAmount());
                                            intent.putExtra("deliverTime", orderProduct.getResult().getDeliveryTime());
                                            intent.putExtra("companyCateGory", orderProduct.getResult().getCompanyCategory().getName());
                                            intent.putExtra("productName", orderProduct.getResult().getProductName());
                                            intent.putExtra("senderName", pfList.get(position).getFeedbackUser().getName());
                                            intent.putExtra("achieveAmount", pfList.get(position).getAchieveAmount());
                                            intent.putExtra("sendTime", pfList.get(position).getCreateDate());
                                            intent.putExtra("remarks", pfList.get(position).getRemarks());
                                            intent.putExtra("processFeedBackId", pfList.get(position).getId());
                                            intent.putExtra("processId", pfList.get(position).getProcess().getId());
                                            intent.putExtra("feedBackUserId", pfList.get(position).getFeedbackUser().getId());
                                            intent.putExtra("target", pfList.get(position).getTarget());

                                            intent.putExtra("images", pfList.get(position).getFeedbackAttachmentStr());
                                            startActivity(intent);
                                            break;
                                        case 1:
                                            showNotify(pfList.get(position).getFeedbackUser().getId(), pfList.get(position).getId(), pfList.get(position).getProcess().getId());
                                            break;
                                        case 2:
                                            //入库
                                            Intent in = new Intent(ReceiveGoodsProductReceiveQueue.this, OutInStockAdd.class);
                                            in.putExtra("inOut", "1");
                                            startActivity(in);
                                            break;
                                    }
                                } else if (pfList.get(position).getConfirmAmount() > 0) {
                                    switch (index) {
                                        case 0:
                                            break;
                                        case 1:
                                            //入库
                                            Intent in = new Intent(ReceiveGoodsProductReceiveQueue.this, OutInStockAdd.class);
                                            in.putExtra("inOut", "1");
                                            startActivity(in);
                                            break;
                                    }
                                }

                                return false;
                            }
                        });
//发货详情
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(ReceiveGoodsProductReceiveQueue.this, ReceiveGoodsDetails.class);
                                intent.putExtra("orderProductId", orderProduct.getResult().getId());
                                if (orderProduct.getResult().getBCompany() != null) {
                                    intent.putExtra("companyId", orderProduct.getResult().getBCompany().getId());
                                }
                                intent.putExtra("companyCateGory", orderProduct.getResult().getCompanyCategory().getName());
                                intent.putExtra("productName", orderProduct.getResult().getProductName());
                                intent.putExtra("productNo", orderProduct.getResult().getProductSn());

                                intent.putExtra("deliverTime", orderProduct.getResult().getDeliveryTime());

                                intent.putExtra("amount", orderProduct.getResult().getAmount());
                                intent.putExtra("attribute", new Gson().toJson(orderProduct.getResult().getOrderProductSpecificationList()));

                                intent.putExtra("processFeedBack", pfList.get(position));
                                if (orderProduct.getResult().getBCompany() != null && orderProduct.getResult().getBCompany().getPhoto() != null && !"".equals(orderProduct.getResult().getBCompany().getPhoto())) {
                                    intent.putExtra("photo", orderProduct.getResult().getBCompany().getPhoto());
                                }
                                intent.putExtra("productStus", OrderProductStateUtils.orderProductStatus(orderProduct.getResult().getOrderProductStatus()));
                                startActivity(intent);

                            }
                        });
                    }
                });
            }
        });
    }

    private void showNotify(final String receiverId, final String processFeedBackId, final String processId) {
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
        ((TextView) view.findViewById(R.id.tv_title)).setText("拒绝收货");
        ((Button) view.findViewById(R.id.btn_send)).setText("确定");
        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送理由
                final String recomments = ((EditText) view.findViewById(R.id.et_info)).getText().toString();
                if (TextUtils.isEmpty(recomments)) {
                    Toast.makeText(ReceiveGoodsProductReceiveQueue.this, "请填写备注信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(ReceiveGoodsProductReceiveQueue.this);
                DialogLoadUtils.showDialogLoad("操作进行中...");
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
                paramsMap.put("orderProduct.id", getIntent().getStringExtra("orderProductId"));
                paramsMap.put("sendUser", receiverId);
                paramsMap.put("confirmAmount", 0);
                paramsMap.put("flag", "NO");
                //激光推送
                RetrofitFactory.getApiService(ReceiveGoodsProductReceiveQueue.this)
                        .receiveGoods(paramsMap)
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
                                if ("00000".equals(responseDelete.getErrorCode())) {
                                    sendNotify(recomments, receiverId, processFeedBackId, processId);
                                } else {
                                    Toast.makeText(ReceiveGoodsProductReceiveQueue.this, "服务器异常", Toast.LENGTH_SHORT).show();
                                    DialogLoadUtils.dismissDialog();

                                }
                            }
                        });

            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    //拒绝收货
    private void sendNotify(String recomments, final String receiverId, String processFeedBackId, String processId) {

        String url = BaseUrlUtils.NET_URL + "order/orderProductReject";
        OkHttpClient ok = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("orderProdId", getIntent().getStringExtra("orderProductId"))
                .add("currentUser.id", sp.getString("USER_ID", null))
                .add("comments", recomments)
                .add("sendUser", receiverId)
                .add("id", processFeedBackId)
                .add("process.id", processId)
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                final com.blanink.bean.Response rp = gson.fromJson(json, com.blanink.bean.Response.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("00000".equals(rp.getErrorCode())) {
                            Toast.makeText(ReceiveGoodsProductReceiveQueue.this, "已拒绝", Toast.LENGTH_SHORT).show();

                            OkHttp();
                            DialogLoadUtils.dismissDialog();
                            alertDialog.dismiss();


                        } else {
                            Toast.makeText(ReceiveGoodsProductReceiveQueue.this, "服务器异常", Toast.LENGTH_SHORT).show();
                            DialogLoadUtils.dismissDialog();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public class ReceiveAdapter extends BaseAdapter {
        private List<ProductDeliverInfo.ResultBean.RelFlowProcessListBean.ProcessFeedbackListBean> pfList;

        public ReceiveAdapter(List<ProductDeliverInfo.ResultBean.RelFlowProcessListBean.ProcessFeedbackListBean> pfList) {
            this.pfList = pfList;
        }

        @Override
        public int getCount() {
            return pfList.size();
        }

        @Override
        public Object getItem(int position) {
            return pfList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            sparseArray = new SparseArray<>();
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                convertView = View.inflate(ReceiveGoodsProductReceiveQueue.this, R.layout.item_order_receive, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            {
                ProductDeliverInfo.ResultBean.RelFlowProcessListBean.ProcessFeedbackListBean processFeedbackListBean = pfList.get(position);

                viewHolder.tvProductName.setText(processFeedbackListBean.getProcess().getName());

                if (processFeedbackListBean.getConfirmAmount() == -1) {
                    viewHolder.tvState.setText("已拒绝收货");
                    viewHolder.tvState.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
                if (processFeedbackListBean.getConfirmAmount() > 0) {
                    viewHolder.tvState.setText("已收货");
                    viewHolder.tvState.setTextColor(getResources().getColor(R.color.colorBlue));

                }
                if (processFeedbackListBean.getConfirmAmount() == 0) {
                    viewHolder.tvState.setText("未收货");
                    viewHolder.tvState.setTextColor(getResources().getColor(R.color.upgrade_green));
                }
                viewHolder.tvDeliverNum.setText(processFeedbackListBean.getAchieveAmount() + "");
                viewHolder.tvSenderName.setText(processFeedbackListBean.getFeedbackUser().getName());
                viewHolder.tvDeliverNote.setText(processFeedbackListBean.getRemarks());
                viewHolder.tvTime.setText(DateUtils.format(DateUtils.stringToDate(processFeedbackListBean.getCreateDate())));
                if (processFeedbackListBean.getFeedbackUser() != null && processFeedbackListBean.getFeedbackUser().getPhoto() != null && !"".equals(processFeedbackListBean.getFeedbackUser().getPhoto())) {
                    GlideUtils.glideImageView(ReceiveGoodsProductReceiveQueue.this, viewHolder.iv, processFeedbackListBean.getFeedbackUser().getPhoto(), true);
                }
                if (pfList.get(position).getConfirmAmount() > 0) {
                    viewHolder.tvOk.setVisibility(View.GONE);
                    viewHolder.tvReject.setVisibility(View.GONE);
                }
                if (pfList.get(position).getConfirmAmount() == -1) {
                    viewHolder.llState.setVisibility(View.GONE);
                }

                viewHolder.tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReceiveGoodsProductReceiveQueue.this, ReceiveGoodsToReceive.class);
                        intent.putExtra("orderProductId", orderProduct.getResult().getId());
                        intent.putExtra("orderId", orderProduct.getResult().getOrder().getId());
                        if (orderProduct.getResult().getBCompany() != null) {
                            intent.putExtra("companyId", orderProduct.getResult().getBCompany().getId());
                        }
                        intent.putExtra("productRemarks", orderProduct.getResult().getRemarks());

                        intent.putExtra("price", orderProduct.getResult().getPrice());
                        intent.putExtra("amount", orderProduct.getResult().getAmount());
                        intent.putExtra("deliverTime", orderProduct.getResult().getDeliveryTime());
                        intent.putExtra("companyCateGory", orderProduct.getResult().getCompanyCategory().getName());
                        intent.putExtra("productName", orderProduct.getResult().getProductName());
                        intent.putExtra("senderName", pfList.get(position).getFeedbackUser().getName());
                        intent.putExtra("achieveAmount", pfList.get(position).getAchieveAmount());
                        intent.putExtra("sendTime", pfList.get(position).getCreateDate());
                        intent.putExtra("remarks", pfList.get(position).getRemarks());
                        intent.putExtra("processFeedBackId", pfList.get(position).getId());
                        intent.putExtra("processId", pfList.get(position).getProcess().getId());
                        intent.putExtra("feedBackUserId", pfList.get(position).getFeedbackUser().getId());
                        intent.putExtra("target", pfList.get(position).getTarget());

                        intent.putExtra("images", pfList.get(position).getFeedbackAttachmentStr());
                        startActivity(intent);
                    }
                });
                viewHolder.tvReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNotify(pfList.get(position).getFeedbackUser().getId(), pfList.get(position).getId(), pfList.get(position).getProcess().getId());

                    }
                }); 
                viewHolder.tvIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(ReceiveGoodsProductReceiveQueue.this, OutInStockAdd.class);
                    in.putExtra("inOut", "1");
                    startActivity(in);
                }
            });  
                final SwipeMenuCreator creator = new SwipeMenuCreator() {
                    @Override
                    public void create(final SwipeMenu menu) {
                        // 设置加入潜在框
                        if (pfList.get(position).getConfirmAmount() == 0) {
                            SwipeMenuItem talkNoteItem = new SwipeMenuItem(ReceiveGoodsProductReceiveQueue.this);
                            talkNoteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorBlue)));
                            talkNoteItem.setWidth(dp2px(100));
                            talkNoteItem.setTitle("收货");
                            talkNoteItem.setTitleSize(18);
                            talkNoteItem.setTitleColor(Color.WHITE);
                            menu.addMenuItem(talkNoteItem);


                            SwipeMenuItem refuse = new SwipeMenuItem(ReceiveGoodsProductReceiveQueue.this);
                            refuse.setBackground(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
                            refuse.setWidth(dp2px(100));
                            refuse.setTitle("拒绝收货");
                            refuse.setTitleSize(18);
                            refuse.setTitleColor(Color.WHITE);
                            menu.addMenuItem(refuse);

                            SwipeMenuItem in = new SwipeMenuItem(ReceiveGoodsProductReceiveQueue.this);
                            in.setBackground(new ColorDrawable(getResources().getColor(R.color.colorGray)));
                            in.setWidth(dp2px(100));
                            in.setTitle("入库");
                            in.setTitleSize(18);
                            in.setTitleColor(Color.WHITE);
                            menu.addMenuItem(in);
                        } else if (pfList.get(position).getConfirmAmount() > 0) {
                            SwipeMenuItem talkNoteItem = new SwipeMenuItem(ReceiveGoodsProductReceiveQueue.this);
                            talkNoteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorBlue)));
                            talkNoteItem.setWidth(dp2px(100));
                            talkNoteItem.setTitle("已收货");
                            talkNoteItem.setTitleSize(18);
                            talkNoteItem.setTitleColor(Color.WHITE);
                            menu.addMenuItem(talkNoteItem);

                            SwipeMenuItem in = new SwipeMenuItem(ReceiveGoodsProductReceiveQueue.this);
                            in.setBackground(new ColorDrawable(getResources().getColor(R.color.colorGray)));
                            in.setWidth(dp2px(100));
                            in.setTitle("入库");
                            in.setTitleSize(18);
                            in.setTitleColor(Color.WHITE);
                            menu.addMenuItem(in);
                        } else if (pfList.get(position).getConfirmAmount() == -1) {
                            //-1代表已拒绝
                            SwipeMenuItem talkNoteItem = new SwipeMenuItem(ReceiveGoodsProductReceiveQueue.this);
                            talkNoteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
                            talkNoteItem.setWidth(dp2px(100));
                            talkNoteItem.setTitle("已拒绝");
                            talkNoteItem.setTitleSize(18);
                            talkNoteItem.setTitleColor(Color.WHITE);
                            menu.addMenuItem(talkNoteItem);
                        }


                    }
                };
                lv.setMenuCreator(creator);
            }
            return convertView;
        }


    }

    static class ViewHolder {
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv_sender_name)
        TextView tvSenderName;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.deliver_num)
        TextView deliverNum;
        @BindView(R.id.tv_deliver_num)
        TextView tvDeliverNum;
        @BindView(R.id.deliver_note)
        TextView deliverNote;
        @BindView(R.id.tv_deliver_note)
        TextView tvDeliverNote;
        @BindView(R.id.ll)
        LinearLayout ll;
        @BindView(R.id.tv_in)
        TextView tvIn;
        @BindView(R.id.tv_reject)
        TextView tvReject;
        @BindView(R.id.tv_ok)
        TextView tvOk;
        @BindView(R.id.ll_state)
        LinearLayout llState;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.rl_receive)
        RelativeLayout rlReceive;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
