package com.blanink.ui.activity.notify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.NotifyOffice;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//收件箱 内容详情
public class NotifyMangeDetail extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.customer_apply_iv_last)
    TextView customerApplyIvLast;
    @BindView(R.id.last_family_manage_customer_apply_rl)
    RelativeLayout lastFamilyManageCustomerApplyRl;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.apply)
    RelativeLayout apply;
    @BindView(R.id.tv_apply_info)
    TextView tvApplyInfo;
    @BindView(R.id.customer_delete_relation_rl)
    LinearLayout customerDeleteRelationRl;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.t)
    TextView t;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_company_xin)
    TextView tvCompanyXin;
    @BindView(R.id.tv_company_xin_yu)
    TextView tvCompanyXinYu;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_major_person)
    TextView tvMajorPerson;
    @BindView(R.id.tv_company_zi_remark)
    TextView tvCompanyZiRemark;
    @BindView(R.id.tv_company_remark)
    TextView tvCompanyRemark;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_company_remark_other)
    TextView tvCompanyRemarkOther;
    @BindView(R.id.tv_company_other_remark)
    TextView tvCompanyOtherRemark;
    @BindView(R.id.introduce)
    TextView introduce;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.tv_company_address)
    TextView tvCompanyAddress;
    @BindView(R.id.url)
    TextView url;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.company_info)
    LinearLayout companyInfo;
    @BindView(R.id.activity_last_family_manage_customer_supplier_invite)
    RelativeLayout activityLastFamilyManageCustomerSupplierInvite;
    private SharedPreferences sp;
    private String companyBId;
    private String message;
    private String content;
    private String id;
    private NotifyOffice notifyOffice;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_mange_detail);
        ButterKnife.bind(this);
        receiveData();
        initData();
    }

    private void receiveData() {
        Intent intent=getIntent();
        content = intent.getStringExtra("content");
        id = intent.getStringExtra("referrenceObjectId");
        type = intent.getStringExtra("type");
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
        mapParams.put("referrenceObjectId",id);
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
                    public void onNext(NotifyOffice notifyOffice) {
                        //下家申请
                        if("5".equals(type)){
                            tvCompany.setText(notifyOffice.getResult().getCompanyA().getName());
                            tvAddress.setText(notifyOffice.getResult().getCompanyA().getArea().getName());
                            tvApplyInfo.setText(content);
                            tvCompanyAddress.setText(notifyOffice.getResult().getCompanyA().getAddress());
                            tvMajorPerson.setText(notifyOffice.getResult().getCompanyA().getMaster());
                            tvPhone.setText(notifyOffice.getResult().getCompanyA().getPhone());
                            tvIntroduce.setText(notifyOffice.getResult().getCompanyA().getRemarks());
                            tvCompanyRemark.setText(notifyOffice.getResult().getCompanyA().getReviewSelf()+"");
                            tvCompanyOtherRemark.setText(notifyOffice.getResult().getCompanyA().getReviewOthers()+"");
                            tvUrl.setText(notifyOffice.getResult().getCompanyA().getHomepage());
                            tvCompanyXinYu.setText(new DecimalFormat().format((notifyOffice.getResult().getCompanyA().getReviewSelf()+notifyOffice.getResult().getCompanyA().getReviewOthers())/2.0));
                            tv.setText(notifyOffice.getResult().getCompanyA().getServiceCount()+"");
                        }
                        //上家申请
                        if("6".equals(type)){
                            tvCompany.setText(notifyOffice.getResult().getCompanyB().getName());
                            tvAddress.setText(notifyOffice.getResult().getCompanyB().getArea().getName());
                            tvApplyInfo.setText(content);
                            tvCompanyAddress.setText(notifyOffice.getResult().getCompanyB().getAddress());
                            tvMajorPerson.setText(notifyOffice.getResult().getCompanyB().getMaster());
                            tvPhone.setText(notifyOffice.getResult().getCompanyB().getPhone());
                            tvIntroduce.setText(notifyOffice.getResult().getCompanyB().getRemarks());
                            tvCompanyRemark.setText(notifyOffice.getResult().getCompanyB().getReviewSelf()+"");
                            tvCompanyOtherRemark.setText(notifyOffice.getResult().getCompanyB().getReviewOthers()+"");
                            tvUrl.setText(notifyOffice.getResult().getCompanyB().getHomepage());
                            tvCompanyXinYu.setText(new DecimalFormat().format((notifyOffice.getResult().getCompanyB().getReviewSelf()+notifyOffice.getResult().getCompanyB().getReviewOthers())/2.0));
                            tv.setText(notifyOffice.getResult().getCompanyB().getServiceCount()+"");
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
