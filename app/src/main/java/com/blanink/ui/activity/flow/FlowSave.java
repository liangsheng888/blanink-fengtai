package com.blanink.ui.activity.flow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.FlowDetail;
import com.blanink.bean.RelFlowProcess;
import com.blanink.bean.Response;
import com.blanink.utils.DialogLoadUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 流程保存
 */
public class FlowSave extends BaseActivity {

    @BindView(R.id.tv_last)
    TextView tvLast;
    @BindView(R.id.customer_delete_relation_tv_seek)
    TextView customerDeleteRelationTvSeek;
    @BindView(R.id.tv_select_user)
    TextView tvSelectUser;
    @BindView(R.id.et_flow_name)
    EditText etFlowName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.deliveryTime)
    TextView deliveryTime;
    @BindView(R.id.tv_deliveryTime)
    TextView tvDeliveryTime;
    @BindView(R.id.rl_deliveryTime)
    RelativeLayout rlDeliveryTime;
    @BindView(R.id.et_note_info)
    EditText etNoteInfo;
    @BindView(R.id.btn_send)
    Button btnSend;
    private String productId;
    Map<String, RelFlowProcess> os;
    Map<String, String> mapStr = new HashMap<>();
    private SharedPreferences sp;
    private String remarks, internalDeliveryDate, flowName;
    private String flowData;
    private String relations;
    String relFlowProcessMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_flow_save);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        productId = getIntent().getStringExtra("orderProductId");
        relFlowProcessMap = getIntent().getStringExtra("relFlowProcessMap");
        final Calendar calendar = Calendar.getInstance();

        tvDeliveryTime.setText(calendar.get(calendar.YEAR) + "-" + (calendar.get(calendar.MONTH) + 1) + "-" + calendar.get(calendar.DAY_OF_MONTH));
        tvDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(FlowSave.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvDeliveryTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        internalDeliveryDate = tvDeliveryTime.getText().toString().trim();
                    }
                }, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        os = new Gson().fromJson(relFlowProcessMap, new TypeToken<Map<String, RelFlowProcess>>() {
        }.getType());
        for (Map.Entry<String, RelFlowProcess> set : os.entrySet()) {
            mapStr.put(set.getKey(), new Gson().toJson(set.getValue()));
        }
        //返回
        tvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //保存
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowName = etFlowName.getText().toString();
                remarks = etNoteInfo.getText().toString();
                internalDeliveryDate = tvDeliveryTime.getText().toString();
                if (TextUtils.isEmpty(flowName)) {
                    Toast.makeText(FlowSave.this, "请填写流程名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(remarks)) {
                    Toast.makeText(FlowSave.this, "请填写备注", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(FlowSave.this);
                DialogLoadUtils.showDialogLoad("保存中...");
                checkIsHasFlow();


            }
        });
    }

    private void checkIsHasFlow() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("orderProduct.id", productId);
        RetrofitFactory.getApiService(this)
                .getFlow(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FlowDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FlowDetail flowDetail) {
                        if (flowDetail.getResult() == null) {
                            uploadProcessData("Y", "");
                        } else {
                            uploadProcessData("N", flowDetail.getResult().getId());

                        }
                    }
                });
    }


    private void uploadProcessData(String tempId, String flowId) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("company.id", sp.getString("COMPANY_ID", null));
        mapParams.put("orderProduct.id", productId);
        mapParams.put("name", flowName);
        mapParams.put("internalDeliveryDate", internalDeliveryDate);
        mapParams.put("remarks", remarks);
        mapParams.put("tempId", tempId);
        mapParams.put("id", flowId);
        mapParams.put("relations", new Gson().toJson(mapStr).replace("processId", "process.id").replace("flowId", "flow.id").replace("processName", "process.name"));
        mapParams.put("flowData", getIntent().getStringExtra("flowData"));
        RetrofitFactory.getApiService(this)
                .saveFlow(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(FlowSave.this, "服务器开了会儿小差，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response response) {
                        if ("00000".equals(response.getErrorCode())) {
                            Toast.makeText(FlowSave.this, "保存成功", Toast.LENGTH_SHORT).show();
                            showDialogExit();
                            DialogLoadUtils.dismissDialog();
                        } else {
                            DialogLoadUtils.dismissDialog();
                        }
                    }
                });
    }

    private void showDialogExit() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_exit);
        final Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);

        ((TextView) window.findViewById(R.id.tv_content)).setText("流程和产品已绑定！");
        ((TextView) window.findViewById(R.id.tv_continue)).setText("返回");
        ((TextView) window.findViewById(R.id.tv_exit)).setText("排流程");

        window.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowSave.this, FlowProduct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                alertDialog.dismiss();
                finish();

            }
        });
        window.findViewById(R.id.tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowSave.this, FlowOrder.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                alertDialog.dismiss();
                finish();
            }
        });
    }


}
