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
import android.util.Log;
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
import com.blanink.bean.ManyCustomer;
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
 * Created by Administrator on 2017/2/9.
 */

/***
 * 上家 我的客户详细信息
 */
public class LastCustomerInfo extends Fragment {

    private static final String TAG = "LastCustomerInfo";
    @BindView(R.id.major)
    TextView major;
    @BindView(R.id.tv_major_content)
    TextView tvMajorContent;
    @BindView(R.id.url)
    TextView url;
    @BindView(R.id.tv_url)
    TextView tvUrl;
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
    @BindView(R.id.btn_state)
    Button btnState;
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
    @BindView(R.id.rl_url)
    RelativeLayout rlUrl;

    private SharedPreferences sp;
    private String id;
    private ManyCustomer.Result.Company companyA;

    private SingleCustomer info;

    private String relType;
    private View view;//缓存Fragment view
    AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        Intent intent = getActivity().getIntent();
        relType = intent.getStringExtra("type");
        id = intent.getStringExtra("companyA.id");
        Log.e("LastCustomerInfo", "id:" + id);
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_company_info, null);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        //从服务器中获取数据
        getData();
        //
        btnState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(relType)) {
                    isCanRemoveRelation();

                } else {
                          /*  Intent intent = new Intent(getActivity(), LastFamilyManageCustomerApply.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("info", info);
                            intent.putExtras(bundle);
                            startActivity(intent);*/
                    downSendNotify(1, "申请合作", "确定申请", "取消");

                }
            }
        });
        //在线咨询
        btnConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(getActivity(), EaseChatActivity.class);
                intent.putExtra("userId",companyA.deputyPerson);
                intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                startActivity(intent);*/
            }
        });
    }

    //检查是否存在订单关联
    private void isCanRemoveRelation() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyA.id", companyA.getId());
        mapParams.put("companyB.id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(getActivity())
                .canRemove(mapParams)
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

                        if (response.getResult()) {
                              /*  Intent intent = new Intent(getActivity(), LastFamilyManageCustomerApplyDelete.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("info", info);
                                intent.putExtras(bundle);
                                startActivity(intent);*/
                            downSendNotify(0, "申请解除合作", "确定申请", "取消");

                        } else {
                            DialogNotifyUtils.showNotify(getActivity(), "存在订单关联，不能解除合作关系！");
                        }

                    }
                });
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
                    //解除合作
                    delRelation(content);

                }
                if (type == 1) {
                    //申请合作
                    uploadApplyInfo(content);
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        DialogLoadUtils.dismissDialog();
                        if ("00000".equals(response.getErrorCode())) {
                            alertDialog.dismiss();
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
                                            Log.e(TAG, e.getMessage());

                                        }

                                        @Override
                                        public void onNext(ResponseDelete responseDelete) {

                                        }
                                    });

                        } else {
                            Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }


    //
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

                    }

                    @Override
                    public void onNext(SingleCustomer singleCustomer) {
                        llLoad.setVisibility(View.GONE);
                        info = singleCustomer;
                        Log.e(TAG, "info+++++++" + info.toString());
                        companyA = info.result;
                        Log.e(TAG, "companyA+++++++" + companyA.toString());
                        if (companyA.getCreateCompanyBy() != null) {
                            //如果该客户是虚拟客户
                            llTalk.setVisibility(View.GONE);
                        }
                        //界面设置
                        // tv_company.setText(companyA.getName());
                        tvAddress.setText(companyA.getArea().getName());
                        tvMajorPerson.setText(companyA.getMaster());
                        tvPhone.setText(companyA.getPhone());
                        tvMajorContent.setText(companyA.getScope());
                        tvCompanyAddress.setText(companyA.getAddress());
                        DecimalFormat df = new DecimalFormat("0.0");
                        tvCompanyRemark.setText(Html.fromHtml(info.result.reviewSelf + ""));
                        tvCompanyOtherRemark.setText(info.result.reviewOthers + "");
                        tvCompanyXinYu.setText(df.format((info.result.reviewSelf + info.result.reviewOthers) / 2.0) + "");
                        if (companyA.homepage != null && !"".equals(companyA.homepage)) {
                            rlUrl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(WebViewActivity.getIntent(getActivity(),companyA.homepage));
                                }
                            });
                            tvUrl.setText(companyA.homepage);
                        }
                        tvRegisterTime.setText(companyA.createDate);
                        if ("1".equals(relType)) {
                            btnState.setText("已合作，解除合作");
                        } else if ("0".equals(relType)) {
                            btnState.setText("潜在客户，申请合作");
                        } else {
                            btnState.setText("申请合作");
                        }
                        //如果当前公司是自己所在公司不能进行操作
                        if (sp.getString("COMPANY_ID", null).equals(info.result.getId())) {
                            btnState.setVisibility(View.GONE);
                            btnConsult.setVisibility(View.GONE);
                        }
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void delRelation(String content) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyA.id", info.result.getId());
        mapParams.put("companyB.id", sp.getString("COMPANY_ID", null));
        mapParams.put("notify.remarks", content);
        mapParams.put("isApply", "1");
        RetrofitFactory.getApiService(getActivity())
                .customerApply(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        alertDialog.dismiss();
                        DialogLoadUtils.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(getActivity(), "服务器异常，稍后重试", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        DialogLoadUtils.dismissDialog();
                        if (response.getErrorCode().equals("00000")) {
                            DialogNotifyUtils.showNotify(getActivity(), "你的申请已发出,请等待！");
                            Map<String, Object> paramsMap = new HashMap<String, Object>();
                            paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
                            paramsMap.put("name", "管理员");
                            paramsMap.put("office.id", info.result.getId());
                            //激光推送
                            RetrofitFactory.getApiService(getActivity())
                                    .delCustomer(paramsMap)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<ResponseDelete>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e(TAG, e.getMessage());
                                        }

                                        @Override
                                        public void onNext(ResponseDelete responseDelete) {

                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
