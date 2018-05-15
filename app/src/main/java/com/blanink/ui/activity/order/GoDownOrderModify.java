package com.blanink.ui.activity.order;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.GoOrderDown;
import com.blanink.bean.OfficeEmp;
import com.blanink.bean.OrderModify;
import com.blanink.bean.PartnerInfo;
import com.blanink.bean.Response;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.DialogLoadUtils;
import com.google.gson.Gson;
import com.hyphenate.util.NetUtils;


import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 去单编辑
 */
public class GoDownOrderModify extends BaseActivity {
    @BindView(R.id.come_order_new_add_tv_seek)
    TextView comeOrderNewAddTvSeek;
    @BindView(R.id.come_order_new_add_iv_last)
    TextView comeOrderNewAddIvLast;
    @BindView(R.id.come_order_new_add_rl)
    RelativeLayout comeOrderNewAddRl;
    @BindView(R.id.tv_select_user)
    TextView tvSelectUser;
    @BindView(R.id.sp_customer)
    TextView spCustomer;
    @BindView(R.id.come_order_new_add_rl2)
    RelativeLayout comeOrderNewAddRl2;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.sp_master)
    Spinner spMaster;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.et_order_number)
    EditText etOrderNumber;
    @BindView(R.id.tv_order_note)
    TextView tvOrderNote;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.tv_endDateHand)
    TextView tvEndDateHand;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_date)
    ImageView ivDate;
    @BindView(R.id.rl_DateHand)
    RelativeLayout rlDateHand;
    @BindView(R.id.come_order_new_add_ll)
    LinearLayout comeOrderNewAddLl;
    @BindView(R.id.btn_save)
    Button btnSave;
    private List<String> supplierNameList = new ArrayList<>();
    private List<String> masterNameList = new ArrayList<>();
    private List<String> supplierIdList = new ArrayList<>();
    private List<String> masterIdList = new ArrayList<>();

    private String MasterId;
    public String masterName;
    private SharedPreferences sp;
    private String supplierId;
    private GoOrderDown.ResultBean.RowsBean order;
    final Calendar calendar = Calendar.getInstance();
    private String masterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_down_order_modify);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        order = (GoOrderDown.ResultBean.RowsBean) getIntent().getExtras().getSerializable("order");
        initData();
    }

    private void initData() {
        //返回
        comeOrderNewAddIvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCompanyEmp();
        // getSupplier();
        etOrderNumber.setText(order.getAOrderNumber());
        etNote.setText(order.getRemarks());
        tvDate.setText(order.getDelieverTimeString());
        spCustomer.setText(order.getBCompany().getName());
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(GoDownOrderModify.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aOrderNumber = etOrderNumber.getText().toString();
                String remarks = etNote.getText().toString();
                String deliverTime = tvDate.getText().toString();
                if (TextUtils.isEmpty(etOrderNumber.getText().toString())) {
                    Toast.makeText(GoDownOrderModify.this, "请填写订单编号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etNote.getText().toString())) {
                    Toast.makeText(GoDownOrderModify.this, "请填写备注", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(GoDownOrderModify.this);
                DialogLoadUtils.showDialogLoad("正在修改...");
                OrderModify ord = new OrderModify(order.getId(), sp.getString("COMPANY_ID", null), aOrderNumber, remarks, deliverTime, masterId,masterName);
                uploadData(ord);
            }
        });
    }

    private void uploadData(OrderModify ord) {
        RequestParams params = new RequestParams(BaseUrlUtils.NET_URL+"order/save_order_head_go_list");
        params.addBodyParameter("id", sp.getString("COMPANY_ID", null));
        params.addBodyParameter("order", new Gson().toJson(ord));
        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Response response= gson.fromJson(result,Response.class);
                Log.e("@@@",result.toString());
                if ("00000".equals(response.getErrorCode())) {
                    DialogLoadUtils.dismissDialog();
                    showNotify("修改成功");
                } else {
                    DialogLoadUtils.dismissDialog();
                    Toast.makeText(GoDownOrderModify.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DialogLoadUtils.dismissDialog();
                Toast.makeText(GoDownOrderModify.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
       /* Map < String, Object > mapParams = new HashMap<String, Object>();
        mapParams.put("id", sp.getString("COMPANY_ID", null));
        //RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), );
        mapParams.put("order", new Gson().toJson(ord));
        RetrofitFactory.getApiService(this)
                .save_order_head_go_list(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(GoDownOrderModify.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if ("00000".equals(response.getErrorCode())) {
                            DialogLoadUtils.dismissDialog();
                            showNotify("修改成功");
                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(GoDownOrderModify.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //选择供应商
    private void getSupplier() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("companyA.id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(this)
                .partnerList(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PartnerInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PartnerInfo partnerInfo) {
                        if (partnerInfo.getResult().size() > 0) {
                            for (int i = 0; i < partnerInfo.getResult().size(); i++) {
                                supplierNameList.add(partnerInfo.getResult().get(i).getCompanyB().getName());
                                supplierIdList.add(partnerInfo.getResult().get(i).getCompanyB().getId());
                            }
                        }
                        if (partnerInfo.getResult().size() == 0) {
                            //  btnContiumeAdd.setText("你没有客户，无法添加订单");
                            // btnContiumeAdd.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        }

                    }
                });
    }

    private void getCompanyEmp() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(this)
                .userList(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OfficeEmp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final OfficeEmp officeEmp) {
                        for (int i = 0; i < officeEmp.getResult().size(); i++) {
                            masterNameList.add(officeEmp.getResult().get(i).getName());
                            masterIdList.add(officeEmp.getResult().get(i).getId());
                        }
                        Log.e("ComeOrder", masterNameList.toString());
                        spMaster.setAdapter(new ArrayAdapter<String>(GoDownOrderModify.this, R.layout.spanner_item,masterNameList));
                        spMaster.setSelection(getMasterPosition(order.getACompanyOrderOwnerUser().getId()), true);
                        spMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                masterName = masterNameList.get(position);
                                masterId = officeEmp.getResult().get(position).getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }

    //判断 当前supplier所处位置
    public int getsupplierPosition(String id) {
        int position = 0;
        for (int i = 0; i < supplierIdList.size(); i++) {
            if (id.equals(supplierIdList.get(i))) {
                position = i;
            }
        }
        return position;
    }

    //判断 当前负责人所处位置
    public int getMasterPosition(String id) {
        int position = 0;
        for (int i = 0; i < masterIdList.size(); i++) {
            if (id.equals(masterIdList.get(i))) {
                position = i;
            }
        }
        return position;
    }

    public void showNotify(String tilte) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_apply_delete_relation);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = getWindowManager();

        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        ((TextView) (window.findViewById(R.id.tv_message))).setText(tilte);
        window.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }
}
