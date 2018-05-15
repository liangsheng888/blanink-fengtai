package com.blanink.ui.activity.lastnext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
 * 下家管理供应商管理申请解除
 */
public class NextFamilyManageSupplierManageApplyDelete extends BaseActivity {

    private static final String TAG ="SupplierDelete" ;
    private TextView customer_apply_iv_last;
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
    private SharedPreferences sp;
    private AlertDialog alertDialog;
    public String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_family_manage_company_supplier_manage_apply_deltete);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        receiveIntentInfo();
        initView();
        initData();
    }

    private void initView() {
        customer_apply_iv_last = ((TextView) findViewById(R.id.customer_apply_iv_last));
        tv_company = ((TextView) findViewById(R.id.tv_company));//公司名
        tv_address = ((TextView) findViewById(R.id.tv_address));//地址
        tv_master = ((TextView) findViewById(R.id.tv_major_person));//负责人
        tv_phone = ((TextView) findViewById(R.id.tv_phone));//手机
        tv_scope = ((TextView) findViewById(R.id.tv_major_content));//主营
        tv_detail_address = ((TextView) findViewById(R.id.tv_detail_address));//详细地址
        tv_url = ((TextView) findViewById(R.id.tv_url));//公司主页
        tv_introduce = ((TextView) findViewById(R.id.tv_introduce));//公司简介
        tv_company_honest = ((TextView) findViewById(R.id.tv_company_xin_yu));//公司信誉
        tv_company_remark = ((TextView) findViewById(R.id.tv_company_remark));//公司自评
        tv_company_other_remark = ((TextView) findViewById(R.id.tv_company_other_remark));//他评
        et_apply_info = ((EditText) findViewById(R.id.et_apply_info));//申请解除信息
        btn_apply = ((Button) findViewById(R.id.bt_delete_next));//发送申请
    }

    private void initData() {
        //返回
        customer_apply_iv_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化数据
        if (info != null) {
            tv_company.setText(info.result.getName());
            tv_address.setText(info.result.getArea().getName());
            tv_master.setText(info.result.getMaster());
            tv_phone.setText(info.result.getPhone());
            tv_detail_address.setText(info.result.getAddress());
            tv_introduce.setText(info.result.getRemarks());
            tv_url.setText(info.result.homepage);
            DecimalFormat df = new DecimalFormat("0.0");
            tv_company_other_remark.setText(info.result.reviewOthers + "");
            tv_company_remark.setText(info.result.reviewSelf + "");
            tv_company_honest.setText(df.format((info.result.reviewOthers + info.result.reviewSelf) / 2.0));
        }
        //发送申请
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_apply_info.getText().toString())) {
                    Toast.makeText(NextFamilyManageSupplierManageApplyDelete.this, "请填写申请信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(NextFamilyManageSupplierManageApplyDelete.this);
                DialogLoadUtils.showDialogLoad("操作进行中...");
                message = et_apply_info.getText().toString();
                uploadApplyInfo();
            }
        });
    }

    //申请解除供应商合作关系
    private void uploadApplyInfo() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyB.id", info.result.getId());
        mapParams.put("companyA.id", sp.getString("COMPANY_ID", null));
        mapParams.put("notify.content", message);
        mapParams.put("isApply","0");
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
                        Toast.makeText(NextFamilyManageSupplierManageApplyDelete.this, "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getErrorCode().equals("00000")) {
                            DialogLoadUtils.dismissDialog();
                            DialogNotifyUtils.showNotify(NextFamilyManageSupplierManageApplyDelete.this,"你的申请已发出,请等待！");
                            Map<String,Object> paramsMap=new HashMap<String, Object>();
                            paramsMap.put("currentUser.id",sp.getString("USER_ID", null));
                            paramsMap.put("name","公司管理员");
                            paramsMap.put("office.id",info.result.getId());
                            //激光推送
                            RetrofitFactory.getApiService(NextFamilyManageSupplierManageApplyDelete.this)
                                    .delSupplier(paramsMap)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<ResponseDelete>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e(TAG,e.getMessage());
                                        }

                                        @Override
                                        public void onNext(ResponseDelete responseDelete) {

                                        }
                                    });                } else {
                            Toast.makeText(NextFamilyManageSupplierManageApplyDelete.this, "操作失败", Toast.LENGTH_SHORT).show();
                            DialogLoadUtils.dismissDialog();
                        }
                    }
                });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getData() {
        Map<String,Object> paramsMap=new HashMap<String, Object>();
        paramsMap.put("id", companyId);
        paramsMap.put("userId",sp.getString("USER_ID",null));
        RetrofitFactory.getApiService(NextFamilyManageSupplierManageApplyDelete.this)
                .partnerInfo(paramsMap)
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
                        tv_url.setText(info.result.homepage);
                        tv_company_honest.setText(df.format((info.result.reviewSelf + info.result.reviewOthers) / 2.0) + "");
                    }
                });

    }

    private void showDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(R.layout.dialog_custom_apply_delete_relation);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        Display d = getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setAttributes(lp);
        window.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setWindowAnimations(R.style.dialogAnimationTranslate);
    }

}
