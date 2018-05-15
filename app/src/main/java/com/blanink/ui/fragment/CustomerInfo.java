package com.blanink.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.blanink.bean.Response;
import com.blanink.bean.ResponseDelete;
import com.blanink.bean.SingleCustomer;
import com.blanink.http.RetrofitFactory;
import com.blanink.ui.activity.WebViewActivity;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.DialogNotifyUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/12.
 */
public class CustomerInfo extends Fragment {

    @BindView(R.id.major)
    TextView major;
    @BindView(R.id.tv_major_content)
    TextView tvMajorContent;
    @BindView(R.id.url)
    TextView url;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.rl_url)
    RelativeLayout rlUrl;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.tv_company_address)
    TextView tvCompanyAddress;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.tv_register_time)
    TextView tvRegisterTime;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_major_person)
    TextView tvMajorPerson;
    @BindView(R.id.tv_company_xin_yu)
    TextView tvCompanyXinYu;
    @BindView(R.id.tv_company_xin)
    TextView tvCompanyXin;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_company_remark)
    TextView tvCompanyRemark;
    @BindView(R.id.tv_company_zi_remark)
    TextView tvCompanyZiRemark;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_company_other_remark)
    TextView tvCompanyOtherRemark;
    @BindView(R.id.tv_company_remark_other)
    TextView tvCompanyRemarkOther;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_consult)
    Button btnConsult;
    @BindView(R.id.ll_talk)
    LinearLayout llTalk;
    @BindView(R.id.company_info)
    LinearLayout companyInfo;
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
    private String id;
    private SharedPreferences sp;
    private SingleCustomer info;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_company_deatil_info, null);
        ButterKnife.bind(this, view);
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        Intent intent = getActivity().getIntent();
        id = intent.getStringExtra("companyId");
        initData();
        return view;
    }

    private void initData() {
        getData();
        //客户
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(getActivity(), LastFamilyManageCustomerApply.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", info);
                intent.putExtras(bundle);
                startActivity(intent);*/
                downSendNotify(0, "客户申请合作", "确定申请", "取消");
            }
        });
        //供应商
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    /*          Intent intent = new Intent(getActivity(), NextFamilyManageSupplierManageApplyCooperate.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", info);
                intent.putExtras(bundle);
                startActivity(intent);*/
                downSendNotify(1, "供应商申请合作", "确定申请", "取消");

            }
        });
        //咨询
        btnConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("id", id);
        RetrofitFactory.getApiService(getActivity())
                .customerInfo(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleCustomer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(SingleCustomer singleCustomer) {
                        llLoad.setVisibility(View.GONE);
                        info = singleCustomer;
                        //界面设置
                        if (info != null) {
                            // tvCompany.setText(info.result.getName());
                            tvCompanyAddress.setText(info.result.getAddress());
                            tvMajorPerson.setText(info.result.getMaster());
                            tvPhone.setText(info.result.getPhone());
                            tvMajorContent.setText(info.result.getScope());
                            tvAddress.setText(info.result.getArea().getName());
                            // tvCustomerNum.setText(info.result.serviceCount + "");
                            tvCompanyRemark.setText(info.result.reviewSelf + "");
                            tvCompanyOtherRemark.setText(info.result.reviewOthers + "");
                            tvCompanyXinYu.setText((info.result.reviewSelf + info.result.reviewOthers) / 2.0 + "");
                            if (info.result.homepage != null && !"".equals(info.result.homepage)) {
                                rlUrl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(WebViewActivity.getIntent(getActivity(),info.result.homepage));
                                    }
                                });
                            }
                            tvRegisterTime.setText(info.result.createDate);
                            tvUrl.setText(info.result.homepage);
                            //

                            //如果当前公司是自己所在公司不能进行操作
                            if (sp.getString("COMPANY_ID", null).equals(info.result.getId())) {
                                btnLast.setVisibility(View.GONE);
                                btnConsult.setVisibility(View.GONE);
                                btnNext.setVisibility(View.GONE);
                            }
                        }
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void downSendNotify(final int type, String title, String left, String right) {
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        final View view = View.inflate(getActivity(), R.layout.dialog_send, null);
        alertDialog.setView(view);
        alertDialog.show();
        final Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((Button) view.findViewById(R.id.btn_cancel)).setText(right);
        ((Button) view.findViewById(R.id.btn_send)).setText(left);

        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送理由
                String content = ((EditText) view.findViewById(R.id.et_info)).getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getActivity(), "请填写备注信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(getActivity());
                DialogLoadUtils.showDialogLoad("正在发送申请...");
                if (type == 0) {
                    //申请客户
                    uploadApplyInfo(content);

                }
                if (type == 1) {
                    //申请供应商
                    uploadSupplierApplyInfo(content);
                }
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void uploadApplyInfo(String content) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyA.id", info.result.getId());
        mapParams.put("companyB.id", sp.getString("COMPANY_ID", null));
        mapParams.put("notify.content", content);
        mapParams.put("isApply", "0");
        RetrofitFactory.getApiService(getActivity())
                .customerApply(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getErrorCode().equals("00000")) {
                            if ("00000".equals(response.getErrorCode())) {
                                DialogLoadUtils.dismissDialog();
                                DialogNotifyUtils.showNotify(getActivity(), "你的申请已发出,请等待！");
                                Map<String, Object> paramsMap = new HashMap<String, Object>();
                                paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
                                paramsMap.put("office.id", info.result.getId());
                                //激光推送
                                RetrofitFactory.getApiService(getActivity())
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

                                            }

                                            @Override
                                            public void onNext(ResponseDelete responseDelete) {

                                            }
                                        });

                            } else {
                                Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
                            DialogLoadUtils.dismissDialog();
                        }

                    }
                });

    }


    private void uploadSupplierApplyInfo(String content) {
        //上传申请信息
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyB.id", info.result.getId());
        mapParams.put("companyA.id", sp.getString("COMPANY_ID", null));
        mapParams.put("notify.content", content);
        mapParams.put("isApply", "0");
        RetrofitFactory.getApiService(getActivity())
                .nextHomeApply(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        alertDialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getErrorCode().equals("00000")) {
                            DialogLoadUtils.dismissDialog();

                            DialogNotifyUtils.showNotify(getActivity(), "你的申请已发出,请等待！");

                            Map<String, Object> paramsMap = new HashMap<String, Object>();
                            paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
                            paramsMap.put("name", "公司管理员");
                            paramsMap.put("office.id", info.result.getId());
                            //激光推送
                            RetrofitFactory.getApiService(getActivity())
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

                                        }

                                        @Override
                                        public void onNext(ResponseDelete responseDelete) {

                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
                            DialogLoadUtils.dismissDialog();
                        }
                    }
                });

    }


}
