package com.blanink.ui.activity.lastnext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.Response;
import com.blanink.bean.ResponseDelete;
import com.blanink.bean.SingleCustomer;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.DialogNotifyUtils;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 下家管理供应商管理申请合作
 */
public class NextFamilyManageSupplierManageApplyCooperate extends BaseActivity {

    private ImageView customer_apply_iv_last;
    private TextView tv_company;
    private TextView tv_address;
    private TextView tv_master;
    private TextView tv_phone;
    private TextView tv_scope;
    private TextView tv_detail_address;
    private TextView tv_url;
    private TextView tv_introduce;
    private TextView tv_company_honest;
    private TextView tv_company_remark;
    private TextView tv_company_other_remark;
    private EditText et_apply_info;
    private Button btn_apply;
    private SingleCustomer info;
    private String companyId;
    private String message;
    private SharedPreferences sp;
    private static final String TAG = "SupplierCooperate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_family_manage_company_supplier_manage_apply_cooperate);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        receiveIntentInfo();
        initView();
        initData();
    }

    private void receiveIntentInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        info = (SingleCustomer) bundle.getSerializable("info");
        companyId = intent.getStringExtra("companyId");
        if (info != null) {
            Log.e("@@@@", info.toString());
        }
        if (companyId != null) {
            getData();
        }
    }


    private void initView() {
        customer_apply_iv_last = ((ImageView) findViewById(R.id.customer_apply_iv_last));
        tv_company = ((TextView) findViewById(R.id.tv_company));//公司名
        tv_address = ((TextView) findViewById(R.id.tv_address));//地址
        tv_master = ((TextView) findViewById(R.id.tv_master));//负责人
        tv_phone = ((TextView) findViewById(R.id.tv_phone));//手机
        tv_scope = ((TextView) findViewById(R.id.tv_scope));//主营
        tv_detail_address = ((TextView) findViewById(R.id.tv_detail_address));//详细地址
        tv_url = ((TextView) findViewById(R.id.tv_url));//公司主页
        tv_introduce = ((TextView) findViewById(R.id.tv_introduce));//公司简介
        tv_company_honest = ((TextView) findViewById(R.id.tv_company_honest));//公司信誉
        tv_company_remark = ((TextView) findViewById(R.id.tv_company_remark));//公司自评
        tv_company_other_remark = ((TextView) findViewById(R.id.tv_company_other_remark));//他评
        et_apply_info = ((EditText) findViewById(R.id.et_apply_info));//申请信息
        btn_apply = ((Button) findViewById(R.id.btn_apply));//发送申请

    }

    private void initData() {
        //back
        customer_apply_iv_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (info != null) {
            tv_company.setText(info.result.getName());
            if (info.result.getArea() != null&&info.result.getArea().getName()!=null) {
                tv_address.setText(info.result.getArea().getName());
            }
            tv_master.setText(info.result.getMaster());
            tv_phone.setText(info.result.getPhone());
            tv_detail_address.setText(info.result.getAddress());
            tv_introduce.setText(info.result.getRemarks());
            DecimalFormat df = new DecimalFormat("0.0");
            tv_company_remark.setText(info.result.reviewSelf + "");
            tv_company_other_remark.setText(info.result.reviewOthers + "");
            tv_company_honest.setText(df.format((info.result.reviewSelf + info.result.reviewOthers) / 2.0));
        }
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_apply_info.getText().toString())) {
                    Toast.makeText(NextFamilyManageSupplierManageApplyCooperate.this, "请填写申请信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                message = et_apply_info.getText().toString();
                DialogLoadUtils.getInstance(NextFamilyManageSupplierManageApplyCooperate.this);
                DialogLoadUtils.showDialogLoad("操作进行中...");
                uploadApplyInfo();
            }
        });
    }

    private void uploadApplyInfo() {
        //上传申请信息
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyB.id", info.result.getId());
        mapParams.put("companyA.id", sp.getString("COMPANY_ID", null));
        mapParams.put("notify.content", message);
        mapParams.put("isApply", "0");
        RetrofitFactory.getApiService(this)
                .nextHomeApply(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(NextFamilyManageSupplierManageApplyCooperate.this, "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getErrorCode().equals("00000")) {
                            DialogLoadUtils.dismissDialog();

                            DialogNotifyUtils.showNotify(NextFamilyManageSupplierManageApplyCooperate.this, "你的申请已发出,请等待！");

                            Map<String, Object> paramsMap = new HashMap<String, Object>();
                            paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
                            paramsMap.put("name", "公司管理员");
                            paramsMap.put("office.id", info.result.getId());
                            //激光推送
                            RetrofitFactory.getApiService(NextFamilyManageSupplierManageApplyCooperate.this)
                                    .applySupplier(paramsMap)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<ResponseDelete>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            DialogLoadUtils.dismissDialog();
                                            Log.e(TAG, e.getMessage());

                                        }

                                        @Override
                                        public void onNext(ResponseDelete responseDelete) {

                                        }
                                    });
                        } else {
                            Toast.makeText(NextFamilyManageSupplierManageApplyCooperate.this, "操作失败", Toast.LENGTH_SHORT).show();
                            DialogLoadUtils.dismissDialog();
                        }
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", companyId);
        mapParams.put("userId", sp.getString("USER_ID", null));
        RetrofitFactory.getApiService(this)
                .partnerInfo(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleCustomer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SingleCustomer singleCustomer) {
                        info = singleCustomer;
                        Log.e("@@@@", info.toString());
                        tv_company.setText(info.result.getName());
                        tv_address.setText(info.result.getArea().getName());
                        tv_master.setText(info.result.getMaster());
                        tv_phone.setText(info.result.getPhone());
                        tv_detail_address.setText(info.result.getAddress());
                        tv_introduce.setText(info.result.getRemarks());
                        DecimalFormat df = new DecimalFormat("0.0");
                        tv_company_remark.setText(info.result.reviewSelf + "");
                        tv_company_other_remark.setText(info.result.reviewOthers + "");
                        tv_company_honest.setText(df.format((info.result.reviewSelf + info.result.reviewOthers) / 2.0));
                    }
                });

    }
}
