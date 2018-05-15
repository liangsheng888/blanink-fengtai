package com.blanink.ui.activity.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.OrderTalkNoteAdapter;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.OrderProductRemark;
import com.blanink.bean.Response;
import com.blanink.view.KeyMapDialog;


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
 * 沟通记录
 */
public class ComeOrderProductDetailTalkNote extends BaseActivity {


    @BindView(R.id.tv_last)
    TextView tvLast;
    @BindView(R.id.rl_after_sale_demand)
    RelativeLayout rlAfterSaleDemand;
    @BindView(R.id.lv_comments)
    ListView lvComments;
    @BindView(R.id.et_message)
    TextView etMessage;
    @BindView(R.id.ly_main_weixin)
    RelativeLayout lyMainWeixin;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.rl_not_data)
    RelativeLayout rlNotData;
    private String productId;
    PopupWindow popupwindow;
    SharedPreferences sp;
    KeyMapDialog dialog;
    private List<OrderProductRemark.ResultBean> remarkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_come_order_product_detail_talk_note);;
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //接受从ComeOrderProductDetail 传递过来的数据
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        //加载历史沟通记录
        loadCommentsRecords();
        tvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etMessage.clearFocus();
        etMessage.setCursorVisible(false);
        //弹出发送按钮
        etMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new KeyMapDialog("发表评论：", new KeyMapDialog.SendBackListener() {
                    @Override
                    public void sendBack(String inputText, String type) {

                        sendNotify(inputText, type);
                    }

                });

                dialog.show(getSupportFragmentManager(), "dialog");
            }


        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void loadCommentsRecords() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", productId);
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("companyId", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(this)
                .viewComments(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderProductRemark>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderProductRemark orderProductRemark) {
                        remarkList.clear();
                        remarkList.addAll(orderProductRemark.getResult());
                        lvComments.setAdapter(new OrderTalkNoteAdapter(ComeOrderProductDetailTalkNote.this,remarkList));
                        if (orderProductRemark.getResult().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void sendNotify(String s, String type) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("orderProduct.id", productId);
        mapParams.put("comments", s);
        mapParams.put("sender.id", sp.getString("USER_ID", null));
        mapParams.put("senderCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("type", type);
        RetrofitFactory.getApiService(this)
                .sendComments(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.hideProgressdialog();
                        Toast.makeText(ComeOrderProductDetailTalkNote.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response response) {
                        if ("00000".equals(response.getErrorCode())) {
                            dialog.hideProgressdialog();
                            dialog.dismiss();
                            loadCommentsRecords();
                        }
                    }
                });
    }



}
