package com.blanink.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.utils.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 公司注册信息
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.tv_short_name)
    TextView tvShortName;
    @BindView(R.id.et_short_name)
    EditText etShortName;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_tv_address)
    EditText etTvAddress;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.et_tv_introduce)
    EditText etTvIntroduce;
    @BindView(R.id.tv_next)
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName=etCompanyName.getText().toString().trim();
                String shortName=etShortName.getText().toString().trim();
                String companyCode=etCode.getText().toString().trim();
                String address=etCode.getText().toString().trim();
                String introduce=etTvIntroduce.getText().toString();
                if(TextUtils.isEmpty(companyName)){
                    Toast.makeText(RegisterActivity.this, "公司名称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(shortName)){
                    Toast.makeText(RegisterActivity.this, "公司简称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(companyCode)){
                    Toast.makeText(RegisterActivity.this, "公司组织机构编码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!CommonUtil.isValid(companyCode)){
                    Toast.makeText(RegisterActivity.this, "公司组织机构编码不符合规范", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(address)){
                    Toast.makeText(RegisterActivity.this, "公司地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent= new Intent(RegisterActivity.this,PhoneCheckActivity.class);
                intent.putExtra("companyName",companyName);
                intent.putExtra("shortName",shortName);
                intent.putExtra("companyCode",companyCode);
                intent.putExtra("address",address);
                intent.putExtra("introduce",introduce);
                startActivity(intent);
            }
        });
    }
}
