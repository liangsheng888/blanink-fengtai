package com.blanink.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.SingleCustomer;
import com.blanink.view.NoScrollListview;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/12.
 */
public class CompanyHonest extends Fragment {
    @BindView(R.id.tv_other_a)
    TextView tvOtherA;
    @BindView(R.id.tv_other_remark_a)
    TextView tvOtherRemarkA;
    @BindView(R.id.attitude)
    TextView attitude;
    @BindView(R.id.tv_attitude)
    TextView tvAttitude;
    @BindView(R.id.honest_a)
    TextView honestA;
    @BindView(R.id.tv_honest_a)
    TextView tvHonestA;
    @BindView(R.id.pay_satisfy_a)
    TextView paySatisfyA;
    @BindView(R.id.tv_pay_satisfy_a)
    TextView tvPaySatisfyA;
    @BindView(R.id.tv_self_a)
    TextView tvSelfA;
    @BindView(R.id.tv_remark_a)
    TextView tvRemarkA;
    @BindView(R.id.attitude_self)
    TextView attitudeSelf;
    @BindView(R.id.tv_attitude_self)
    TextView tvAttitudeSelf;
    @BindView(R.id.honest_self_a)
    TextView honestSelfA;
    @BindView(R.id.tv_honest_self_a)
    TextView tvHonestSelfA;
    @BindView(R.id.pay_time)
    TextView payTime;
    @BindView(R.id.tv_pay_time_self_a)
    TextView tvPayTimeSelfA;
    @BindView(R.id.generous)
    TextView generous;
    @BindView(R.id.tv_generous)
    TextView tvGenerous;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.tv_other_remark)
    TextView tvOtherRemark;
    @BindView(R.id.other_remark)
    RelativeLayout otherRemark;
    @BindView(R.id.service)
    TextView service;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.rl_service)
    RelativeLayout rlService;
    @BindView(R.id.quality)
    TextView quality;
    @BindView(R.id.tv_quality)
    TextView tvQuality;
    @BindView(R.id.rl_quality)
    RelativeLayout rlQuality;
    @BindView(R.id.efficiency)
    TextView efficiency;
    @BindView(R.id.tv_efficiency)
    TextView tvEfficiency;
    @BindView(R.id.rl_efficiency)
    RelativeLayout rlEfficiency;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.remark)
    RelativeLayout remark;
    @BindView(R.id.tv_service2)
    TextView tvService2;
    @BindView(R.id.tv_service_content)
    TextView tvServiceContent;
    @BindView(R.id.rl_service2)
    RelativeLayout rlService2;
    @BindView(R.id.tv_quality2)
    TextView tvQuality2;
    @BindView(R.id.tv_quality_content)
    TextView tvQualityContent;
    @BindView(R.id.rl_quality2)
    RelativeLayout rlQuality2;
    @BindView(R.id.tv_efficiency2)
    TextView tvEfficiency2;
    @BindView(R.id.tv_efficiency_content)
    TextView tvEfficiencyContent;
    @BindView(R.id.rl_efficiency2)
    RelativeLayout rlEfficiency2;
    @BindView(R.id.tv_cost2)
    TextView tvCost2;
    @BindView(R.id.tv_cost_content2)
    TextView tvCostContent2;
    @BindView(R.id.rl_cost2)
    RelativeLayout rlCost2;
    @BindView(R.id.credit)
    LinearLayout credit;
    Unbinder unbinder;
    @BindView(R.id.lv)
    NoScrollListview lv;
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
    @BindView(R.id.tv_not_net)
    TextView tvNotNet;
    @BindView(R.id.rl_not_net)
    RelativeLayout rlNotNet;
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    @BindView(R.id.rl)
    RelativeLayout rl;
    private TextView tv_company_credit;
    private TextView other_remark;
    private TextView tv_service;
    private TextView tv_quality;
    private TextView tv_efficiency;
    private TextView tv_cost_content;
    private TextView tv_satisfy_content;
    private TextView tv_remark;
    private TextView tv_service_content;
    private TextView tv_quality_content;
    private TextView tv_efficiency_content;
    private TextView tv_cost_content2;
    private TextView tv_pay_time_content;
    private SharedPreferences sp;
    private String companyId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        companyId = getActivity().getIntent().getStringExtra("companyId");
        Log.e("Company", "LastHonest companyId" + companyId);
        View view = View.inflate(getActivity(), R.layout.layout_cmpany_honest, null);
        // initView(view);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);

        initData();
    }

    private void initData() {
     /*   List<String> list = new ArrayList<>();
        list.add("http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_cf825116e3a34d64c1b91748e5a1b283.pictureunlock");
        list.add("http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_395a6c1f2e70b4667c8bf4c6df2ab2d9.pictureunlock.jpg");
        list.add("http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_53f19f8d84f539d238b9180d8db661de.pictureunlock.jpg");

        lv.setAdapter(new CertificateAdapter(getActivity(), list));*/
        loadAData();
        loadBData();
    }

    public void loadAData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", ""));
        mapParams.put("companyA.id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(getActivity())
                .companyAreview(mapParams)
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
                        tvAttitude.setText(singleCustomer.result.otherService == null ? "0" : singleCustomer.result.otherService + "");
                        tvHonestA.setText(singleCustomer.result.otherQuality == null ? "0" : singleCustomer.result.otherQuality + "");
                        tvPaySatisfyA.setText(singleCustomer.result.otherPayment == null ? "0" : singleCustomer.result.otherPayment + "");
                        //自评
                        // tv_remark.setText(singleCustomer.result.singleCustomerSelf == null ? "0" : singleCustomer.result.singleCustomerSelf + "");
                        tvAttitudeSelf.setText(singleCustomer.result.selfService == null ? "0" : singleCustomer.result.selfService + "");
                        tvHonestSelfA.setText(singleCustomer.result.selfQuality == null ? "0" : singleCustomer.result.selfQuality + "");
                        tvPayTimeSelfA.setText(singleCustomer.result.selfTime == null ? "0" : singleCustomer.result.selfTime + "");
                        tvPaySatisfyA.setText(singleCustomer.result.selfPayment == null ? "0" : singleCustomer.result.selfPayment + "");
                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void loadBData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", ""));
        mapParams.put("companyB.id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(getActivity())
                .companyBreview(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleCustomer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                         rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(SingleCustomer singleCustomer) {
                        flLoad.setVisibility(View.GONE);
                        // other_remark.setText(singleCustomer.result.singleCustomerOthers == null ? "0" : singleCustomer.result.singleCustomerOthers + "");
                        tvService.setText(singleCustomer.result.otherService == null ? "0" : singleCustomer.result.otherService + "");
                        tvQuality.setText(singleCustomer.result.otherQuality == null ? "0" : singleCustomer.result.otherQuality + "");
                        tvEfficiency.setText(singleCustomer.result.otherTime == null ? "0" : singleCustomer.result.otherTime + "");
                        //自评
                        // tv_remark.setText(singleCustomer.result.singleCustomerSelf == null ? "0" : singleCustomer.result.singleCustomerSelf + "");
                        tvServiceContent.setText(singleCustomer.result.selfService == null ? "0" : singleCustomer.result.selfService + "");
                        tvQualityContent.setText(singleCustomer.result.selfQuality == null ? "0" : singleCustomer.result.selfQuality + "");
                        tvEfficiencyContent.setText(singleCustomer.result.selfTime == null ? "0" : singleCustomer.result.selfTime + "");
                        tvCostContent2.setText(singleCustomer.result.selfCosting == null ? "0" : singleCustomer.result.selfCosting + "");
                    }
                });
    }
}
