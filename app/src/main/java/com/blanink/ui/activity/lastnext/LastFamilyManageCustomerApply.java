package com.blanink.ui.activity.lastnext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
 * 上家管理 客户管理  申请合作
 */
public class LastFamilyManageCustomerApply extends BaseActivity {

    private TextView tv_company_name;
    private TextView tv_state;
    private TextView tv_area;
    private TextView tv_company_xin_yu;
    private TextView tv_major_person;
    private TextView tv_phone;
    private TextView tv_introduce;
    private TextView tv_company_address;
    private TextView tv_url;
    private TextView tv_company_remark;
    private TextView tv_company_other_remark;
    private TextView et_apply_info;
    private Button btn_apply;
    private TextView customer_apply_iv_last;
    private SingleCustomer info;
    private SharedPreferences sp;
    private String message;
    private String TAG="CustomerApply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_family_manage_customer_apply);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        receiveIntentInfo();
        initView();
        initData();
    }

    private void initView() {
        customer_apply_iv_last = ((TextView) findViewById(R.id.customer_apply_iv_last));//發回
        tv_company_name = ((TextView) findViewById(R.id.tv_company_name));//公司名称
        tv_state = ((TextView) findViewById(R.id.tv_state));//公司状态
        tv_area = ((TextView) findViewById(R.id.tv_area));//区域
        tv_company_xin_yu = ((TextView) findViewById(R.id.tv_company_xin_yu));//公司信誉
        tv_major_person = ((TextView) findViewById(R.id.tv_major_person));//负责人
        tv_phone = ((TextView) findViewById(R.id.tv_phone));//電話
        tv_introduce = ((TextView) findViewById(R.id.tv_introduce));//簡介
        tv_company_address = ((TextView) findViewById(R.id.tv_company_address));//詳情地址
        tv_url = ((TextView) findViewById(R.id.tv_url));//主頁
        tv_company_remark = ((TextView) findViewById(R.id.tv_company_remark));//自評
        tv_company_other_remark = ((TextView) findViewById(R.id.tv_company_other_remark));//他評
        et_apply_info = ((TextView) findViewById(R.id.et_apply_info));//申請信息
        btn_apply = ((Button) findViewById(R.id.btn_apply));//發送請求
    }

    private void initData() {
        //返回
        customer_apply_iv_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化界面
        tv_company_name.setText(info.result.getName());
        tv_state.setText(info.result.getCreateCompanyBy() == null ? "实有" : "虚拟");
        tv_area.setText(info.result.getArea().getName());
        DecimalFormat df = new DecimalFormat("0.0");
        tv_company_xin_yu.setText(df.format((info.result.reviewOthers + info.result.reviewSelf) / 2.0));

        tv_major_person.setText(info.result.getMaster());
        tv_phone.setText(info.result.getPhone());
        tv_introduce.setText(info.result.getMaster());
        tv_company_address.setText(info.result.getAddress());
        tv_company_remark.setText(info.result.reviewSelf + "");
        tv_company_other_remark.setText(info.result.reviewOthers + "");

        //发送申请
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_apply_info.getText().toString())){
                    Toast.makeText(LastFamilyManageCustomerApply.this, "请填写申请信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                message=et_apply_info.getText().toString();
                DialogLoadUtils.getInstance(LastFamilyManageCustomerApply.this);
                DialogLoadUtils.showDialogLoad("操作进行中...");
                uploadApplyInfo();
            }
        });
    }

    private void receiveIntentInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        info = (SingleCustomer) bundle.getSerializable("info");
        Log.e("@@@@", info.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //上传申请信息
    public void uploadApplyInfo() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyA.id", info.result.getId());
        mapParams.put("companyB.id", sp.getString("COMPANY_ID", null));
        mapParams.put("notify.content", message);
        mapParams.put("isApply","0");
        RetrofitFactory.getApiService(this)
                .customerApply(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(LastFamilyManageCustomerApply.this, "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if(response.getErrorCode().equals("00000")){
                            if("00000".equals(response.getErrorCode())){
                                DialogLoadUtils.dismissDialog();
                                DialogNotifyUtils.showNotify(LastFamilyManageCustomerApply.this,"你的申请已发出,请等待！");
                                Map<String,Object> paramsMap=new HashMap<String, Object>();
                                paramsMap.put("currentUser.id",sp.getString("USER_ID", null));
                                paramsMap.put("office.id",info.result.getId());
                                //激光推送
                                RetrofitFactory.getApiService(LastFamilyManageCustomerApply.this)
                                        .applyCustomer(paramsMap)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<ResponseDelete>() {
                                            @Override
                                            public void onCompleted() {
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                DialogLoadUtils.dismissDialog();
                                                Log.e(TAG,e.getMessage());

                                            }

                                            @Override
                                            public void onNext(ResponseDelete responseDelete) {

                                            }
                                        });

                            }
                            else {
                                Toast.makeText(LastFamilyManageCustomerApply.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(LastFamilyManageCustomerApply.this, "操作失败", Toast.LENGTH_SHORT).show();
                            DialogLoadUtils.dismissDialog();
                        }

                    }
                });

    }
}
