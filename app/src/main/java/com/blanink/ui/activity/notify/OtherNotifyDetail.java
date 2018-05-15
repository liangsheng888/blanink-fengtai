package com.blanink.ui.activity.notify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.Response;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 其它通知详情
 */
public class OtherNotifyDetail extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.customer_apply_iv_last)
    TextView customerApplyIvLast;
    @BindView(R.id.last_family_manage_customer_apply_rl)
    RelativeLayout lastFamilyManageCustomerApplyRl;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.activity_last_family_manage_customer_supplier_invite)
    RelativeLayout activityLastFamilyManageCustomerSupplierInvite;
    @BindView(R.id.tv_receive)
    TextView tvReceive;
    @BindView(R.id.receive)
    TextView receive;
    private String sender;
    private String time;
    private String company;
    private String content;
    private String id;
    private String title1;
    private String receiver;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_notify_detail);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        receiveData();
        initData();
    }

    private void initData() {
        if (null != content) {
            tvContent.setText(Html.fromHtml(content));
        }
        tvSend.setText(sender);
        tvTitle.setText(title1);
        tvTime.setText(time);
        receive.setText(receiver);
    }

    private void receiveData() {
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        id = intent.getStringExtra("referrenceObjectId");
        title1 = intent.getStringExtra("title");
        sender = intent.getStringExtra("sender");
        time = intent.getStringExtra("time");
        company = intent.getStringExtra("company");
        receiver = intent.getStringExtra("receiver");
        if (receiver == null || receiver == "") {
            tvReceive.setVisibility(View.GONE);
        }

       /* if ("0".equals(intent.getStringExtra("readFlag"))) {
            updateStatus();
            // EventBus.getDefault().post(new DataSynRefreshEvent(true, 7, "收件箱刷新"));
        }*/
    }

    @OnClick(R.id.customer_apply_iv_last)
    public void onClick() {
        finish();
    }

    private void updateStatus() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("id", getIntent().getStringExtra("notifyId"));
        RetrofitFactory.getApiService(this)
                .updateStatus(mapParams)
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

                    }
                });
    }
}
