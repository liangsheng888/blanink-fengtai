package com.blanink.ui.activity.lastnext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;


import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 上家管理 客户管理 新建客户
 */
public class LastFamilyManageNewAddCustomer extends BaseActivity {

    private ImageView customer_add_iv_last;
    private EditText et_company_name;
    private EditText et_major;
    private EditText et_contact_address;
    private EditText et_contact;
    private EditText et_contact_phone;
    private EditText et_email;
    private EditText et_note;
    private Button btn_submit;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_family_manage_new_add_customer);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initView();
        initData();
    }
    private void initView() {
        customer_add_iv_last = ((ImageView) findViewById(R.id.customer_add_iv_last));
        et_company_name = ((EditText) findViewById(R.id.et_company_name));//公司全名
        et_major = ((EditText) findViewById(R.id.et_major));//公司主营
        et_contact_address = ((EditText) findViewById(R.id.et_contact_address));//联系地址
        et_contact = ((EditText) findViewById(R.id.et_contact));//联系人
        et_contact_phone = ((EditText) findViewById(R.id.et_contact_phone));//联系电话
        et_email = ((EditText) findViewById(R.id.et_email));//邮箱
        et_note = ((EditText) findViewById(R.id.et_note));//备注信息
        btn_submit = ((Button) findViewById(R.id.btn_submit));//确认
    }

    private void initData() {


        customer_add_iv_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //提交
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //访问服务器
    private void getData() {
        final String companyName = et_company_name.getText().toString();
        String master = et_contact.getText().toString();
        String address = et_contact_address.getText().toString();
        String scope = et_major.getText().toString();
        String phone = et_contact_phone.getText().toString();

        String email = et_email.getText().toString();
        String note = et_note.getText().toString();
        if (TextUtils.isEmpty(companyName)) {
            Toast.makeText(this, "请输入公司全称", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(master)) {
            Toast.makeText(this, "请输入公司联系人", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请输入联系人地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(scope)) {
            Toast.makeText(this, "请输入公司经营范围", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入联系人手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!CommonUtil.isConnected(this)){
            Toast.makeText(this, "请检查你的网络", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogLoadUtils.getInstance(this);
        DialogLoadUtils.showDialogLoad("正在创建中...");
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        Log.e("Last", "userId:" + sp.getString("USER_ID", null));
        mapParams.put("companyA.name", companyName);
        mapParams.put("companyA.master", master);
        mapParams.put("companyA.address", address);
        mapParams.put("companyA.phone", phone);
        mapParams.put("companyA.email", email);
        mapParams.put("companyA.scope", scope);
        mapParams.put("companyA.remarks", note);
        RetrofitFactory.getApiService(this)
                .customerSave(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        DialogLoadUtils.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(LastFamilyManageNewAddCustomer.this, "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if ("00000".equals(response.getErrorCode())) {
                            Log.e("Last", "创建客户成功");
                            final AlertDialog alertDialog = new AlertDialog.Builder(LastFamilyManageNewAddCustomer.this).create();
                            alertDialog.setTitle("创建客户成功");
                            View view = View.inflate(LastFamilyManageNewAddCustomer.this, R.layout.customer_new_add_success, null);
                            Button btn_continue = ((Button) view.findViewById(R.id.btn_continue));
                            Button btn_see = ((Button) view.findViewById(R.id.btn_see));
                            TextView tv_company2 = ((TextView) view.findViewById(R.id.tv_company2));
                            tv_company2.setText(companyName);
                            btn_continue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    clearContent();
                                    alertDialog.dismiss();


                                }
                            });
                            btn_see.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //跳转到我的客户
                                    Intent intent=new Intent(LastFamilyManageNewAddCustomer.this,LastFamilyManageCustomer.class);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setView(view);
                            alertDialog.show();
                        } else {
                            Log.e("Last", "创建客户失败");
                            Toast.makeText(LastFamilyManageNewAddCustomer.this, "该客户已存在！！！", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    //清空信息
    private void clearContent() {
        et_company_name.setText("");
        et_contact.setText("");
        et_contact_address.setText("");
        et_major.setText("");
        et_contact_phone.setText("");
        et_email.setText("");
        et_note.setText("");
    }
}
