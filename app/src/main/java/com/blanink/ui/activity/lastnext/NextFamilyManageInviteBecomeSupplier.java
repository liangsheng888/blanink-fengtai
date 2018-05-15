package com.blanink.ui.activity.lastnext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.NotifyOffice;
import com.blanink.bean.Response;
import com.blanink.bean.ResponseDelete;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.DialogNotifyUtils;
import com.blanink.utils.GlideUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 下家管理 邀请成为他的供应商
 */
public class NextFamilyManageInviteBecomeSupplier extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.customer_apply_iv_last)
    TextView customerApplyIvLast;
    @BindView(R.id.last_family_manage_customer_apply_rl)
    RelativeLayout lastFamilyManageCustomerApplyRl;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_master)
    TextView tvMaster;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;
    @BindView(R.id.info)
    LinearLayout info;
    @BindView(R.id.bt_agree)
    Button btAgree;
    @BindView(R.id.bt_refuse)
    Button btRefuse;
    @BindView(R.id.ll_manage)
    LinearLayout llManage;
    @BindView(R.id.activity_last_family_manage_customer_supplier_invite)
    RelativeLayout activityLastFamilyManageCustomerSupplierInvite;
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
    private SharedPreferences sp;
    private String companyBId;
    private String message;
    private String content;
    private String id;
    private NotifyOffice notifyOffice;
    AlertDialog alertDialog;
    private String title1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_family_manage_invite_bcome_supplier);
        ButterKnife.bind(this);
        DialogLoadUtils.getInstance(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        receiveData();
        initData();
    }

    private void receiveData() {
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        id = intent.getStringExtra("referrenceObjectId");
        title1 = intent.getStringExtra("title");
      /*  if ("0".equals(intent.getStringExtra("readFlag"))) {
            updateStatus();
            // EventBus.getDefault().post(new DataSynRefreshEvent(true, 7, "收件箱刷新"));
        }*/
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

    private void initData() {
        getDataFromServer();
        //返回
        customerApplyIvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getDataFromServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("referrenceObjectId", id);

        RetrofitFactory.getApiService(this)
                .getOffice(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NotifyOffice>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NotifyOffice office) {
                        llLoad.setVisibility(View.GONE);
                        notifyOffice = office;
                        tvCompany.setText(notifyOffice.getResult().getCompanyA().getName());
                        GlideUtils.glideImageView(NextFamilyManageInviteBecomeSupplier.this, iv, notifyOffice.getResult().getCompanyA().getPhoto(), true);
                        tvRemarks.setText(content);
                        tvMaster.setText(notifyOffice.getResult().getCompanyA().getMaster());
                        tvTime.setText(notifyOffice.getResult().getCreateDate());
                        if ("2".equals(notifyOffice.getResult().getReviewStatus())) {
                            btAgree.setText("你同意了对方的请求");
                            btAgree.setTextColor(Color.WHITE);
                            btAgree.setBackgroundColor(getResources().getColor(R.color.gray));
                            btAgree.setEnabled(false);
                            btRefuse.setVisibility(View.GONE);
                        }
                        if ("3".equals(notifyOffice.getResult().getReviewStatus())) {
                            btAgree.setText("已拒绝对方的请求");
                            btAgree.setEnabled(false);
                            btRefuse.setVisibility(View.GONE);
                            btAgree.setTextColor(Color.WHITE);
                            btAgree.setBackgroundColor(getResources().getColor(R.color.gray));
                        }
                        if ("4".equals(notifyOffice.getResult().getReviewStatus())) {
                            btAgree.setText("已解除合作关系");
                            btAgree.setEnabled(false);
                            btAgree.setTextColor(Color.WHITE);
                            btAgree.setBackgroundColor(getResources().getColor(R.color.gray));
                            btRefuse.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.bt_agree, R.id.bt_refuse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_agree:
                //同意
                deleteNofity("双方将建立合作关系,请谨慎!","取消","确定");
                break;
            case R.id.bt_refuse:
                //拒绝
                showNotify();
                break;
        }
    }

    private void showNotify() {
        alertDialog = new AlertDialog.Builder(this).create();
        final View view = View.inflate(this, R.layout.dialog_refuse, null);
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
        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送理由
                message = ((EditText) view.findViewById(R.id.et_info)).getText().toString();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(NextFamilyManageInviteBecomeSupplier.this, "请填写拒绝理由", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.showDialogLoad("操作进行中...");
                reject();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    //拒绝
    private void reject() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("companyB.id", sp.getString("COMPANY_ID", null));
        mapParams.put("companyA.id", notifyOffice.getResult().getCompanyA().getId());
        mapParams.put("notify.content", message);
        mapParams.put("isApply", "0");

        RetrofitFactory.getApiService(this)
                .nextHomeReject(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        alertDialog.dismiss();
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(NextFamilyManageInviteBecomeSupplier.this, "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        alertDialog.dismiss();
                        DialogLoadUtils.dismissDialog();
                        Gson gson = new Gson();
                        if ("00000".equals(response.getErrorCode())) {
                            DialogLoadUtils.dismissDialog();
                            btAgree.setEnabled(false);
                            btAgree.setText("已拒绝");
                            btRefuse.setVisibility(View.GONE);
                            Map<String, Object> params=new HashMap<String,Object>();
                            params.put("currentUser.id",sp.getString("USER_ID", null));
                            params.put("office.id",notifyOffice.getResult().getCompanyA().getId());
                            params.put("flag","AGREE_SUPPLIER");

                            RetrofitFactory.getApiService(NextFamilyManageInviteBecomeSupplier.this)
                                    .applyResponse(params)
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

                                        }
                                    });
                            DialogNotifyUtils.showNotify(NextFamilyManageInviteBecomeSupplier.this, "你拒绝了对方的请求");

                        } else {
                            Toast.makeText(NextFamilyManageInviteBecomeSupplier.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //同意
    public void agreeApply() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("companyB.id", sp.getString("COMPANY_ID", null));
        mapParams.put("companyA.id", notifyOffice.getResult().getCompanyA().getId());
        mapParams.put("isApply", "0");
        RetrofitFactory.getApiService(this)
                .nextHomeAgree(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(NextFamilyManageInviteBecomeSupplier.this, "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        DialogLoadUtils.dismissDialog();
                        Gson gson = new Gson();
                        if ("00000".equals(response.getErrorCode())) {
                            DialogLoadUtils.dismissDialog();
                            btAgree.setEnabled(false);
                            btAgree.setText("已同意");
                            btRefuse.setVisibility(View.GONE);
                            Map<String, Object> params=new HashMap<String,Object>();
                            params.put("currentUser.id",sp.getString("USER_ID", null));
                            params.put("office.id",notifyOffice.getResult().getCompanyA().getId());
                            params.put("flag","AGREE_SUPPLIER");

                            RetrofitFactory.getApiService(NextFamilyManageInviteBecomeSupplier.this)
                                    .applyResponse(params)
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

                                        }
                                    });
                            DialogNotifyUtils.showNotify(NextFamilyManageInviteBecomeSupplier.this, "你同意了对方的请求");

                        } else {
                            Toast.makeText(NextFamilyManageInviteBecomeSupplier.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void deleteNofity( String content, String left, String right) {
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
                DialogLoadUtils.getInstance(NextFamilyManageInviteBecomeSupplier.this);
                DialogLoadUtils.showDialogLoad("操作进行中...");
                agreeApply();
                alertDialog.dismiss();
            }
        });
    }
}
