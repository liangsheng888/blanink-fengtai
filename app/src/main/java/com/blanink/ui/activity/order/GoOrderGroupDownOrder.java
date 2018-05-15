package com.blanink.ui.activity.order;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.CommonAdapter;
import com.blanink.ui.adapter.ViewHolder;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.GoFormDownOrder;
import com.blanink.bean.OfficeEmp;
import com.blanink.bean.Response;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.view.NoScrollListview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 去单组合下单
 */
public class GoOrderGroupDownOrder extends BaseActivity {

    @BindView(R.id.go_order_group_tv_seek)
    TextView goOrderGroupTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.go_order_group)
    RelativeLayout goOrderGroup;
    @BindView(R.id.ll_pro_queue)
    LinearLayout llProQueue;
    @BindView(R.id.lv_product)
    NoScrollListview lvProduct;
    @BindView(R.id.tv_priority)
    TextView tvPriority;
    @BindView(R.id.et_order_number)
    EditText etOrderNumber;
    @BindView(R.id.tv_hand_date)
    TextView tvHandDate;
    @BindView(R.id.et_hand_date_time)
    TextView etHandDateTime;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv_product_no)
    TextView tvProductNo;
    @BindView(R.id.sp_master)
    Spinner spMaster;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.go_order_group_product_ll)
    LinearLayout goOrderGroupProductLl;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.tv_num)
    TextView tvNum;
    private List<GoFormDownOrder.ResultBean.OrderProductListBean> list = new ArrayList<>();
    private List<String> masterNameList = new ArrayList<>();
    private List<String> masterIdList = new ArrayList<>();
    private SharedPreferences sp;
    private String master;
    private String note;
    private String deliverTime;
    private String aNumber;
    private String masterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_order_group_down_order);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        DialogLoadUtils.getInstance(this);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        initIntentData();
        //返回
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvProduct.setAdapter(new CommonAdapter<GoFormDownOrder.ResultBean.OrderProductListBean>(GoOrderGroupDownOrder.this, list, R.layout.item_go_order_form) {
            @Override
            public void convert(ViewHolder viewHolder, GoFormDownOrder.ResultBean.OrderProductListBean orderProductListBean, int position) {

                orderProductListBean = list.get(position);
                TextView tv = viewHolder.getViewById(R.id.tv_company_category);
                TextView tv_delverTime = viewHolder.getViewById(R.id.tv_delverTime);
                TextView tv_product_name = viewHolder.getViewById(R.id.tv_product_name);
                tv.setText(orderProductListBean.getCompanyCategory().getName());
                tv_delverTime.setText(orderProductListBean.getDeliveryTime());
                tv_product_name.setText(orderProductListBean.getProductName());
            }
        });
        //我方责任人
        getCompanyEmp();
        //
        final Calendar calendar = Calendar.getInstance();
        etHandDateTime.setText(calendar.get(calendar.YEAR) + "-" + (calendar.get(calendar.MONTH) + 1) + "-" + calendar.get(calendar.DAY_OF_MONTH));
        etHandDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(GoOrderGroupDownOrder.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etHandDateTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        etHandDateTime.clearFocus();
                    }
                }, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        //去下单
        btnOk.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                aNumber = etOrderNumber.getText().toString();
                deliverTime = etHandDateTime.getText().toString();
                note = etNote.getText().toString();
                if (TextUtils.isEmpty(aNumber)) {
                    Toast.makeText(GoOrderGroupDownOrder.this, "请填写订单编号", Toast.LENGTH_SHORT).show();

                    return;
                }
                if ("请选择责任人".equals(master)) {
                    Toast.makeText(GoOrderGroupDownOrder.this, "请选择责任人", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(note)) {
                    Toast.makeText(GoOrderGroupDownOrder.this, "请填写备注", Toast.LENGTH_SHORT).show();

                    return;
                }
                DialogLoadUtils.showDialogLoad("正在下单中...");
                downOrder();
            }
        });
    }

    private void initIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String orderProduct = bundle.getString("OrderProductMap");
        Map<Integer, GoFormDownOrder.ResultBean.OrderProductListBean> orderProductMap = new Gson().fromJson(orderProduct, new TypeToken<Map<Integer, GoFormDownOrder.ResultBean.OrderProductListBean>>() {
        }.getType());

        for (Map.Entry<Integer, GoFormDownOrder.ResultBean.OrderProductListBean> product : orderProductMap.entrySet()) {
            if (product.getValue() != null) {
                list.add(product.getValue());
            }
        }
        tvNum.setText("代下单产品("+list.size()+")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //获得本公司人员列表
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
                    public void onNext(OfficeEmp officeEmp) {
                        masterNameList.add("请选择责任人");
                        masterIdList.add("");
                        for (int i = 0; i < officeEmp.getResult().size(); i++) {
                            masterNameList.add(officeEmp.getResult().get(i).getName());
                            masterIdList.add(officeEmp.getResult().get(i).getId());
                        }
                        spMaster.setAdapter(new ArrayAdapter<String>(GoOrderGroupDownOrder.this, R.layout.spanner_item, masterNameList));
                        spMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                master = masterNameList.get(position);
                                masterId = masterIdList.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });

    }

    public void downOrder() {

        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("providerId", list.get(0).getBCompany().getId());
        for (int i = 0; i < list.size(); i++) {
            mapParams.put("orderChecked[" + i + "]", list.get(i).getId());
        }
        mapParams.put("delieverTime", deliverTime);
        mapParams.put("aOrderNumber", aNumber);
        mapParams.put("aCompanyOrderOwnerUser.name", master);
        mapParams.put("aCompanyOrderOwnerUser.id", masterId);
        mapParams.put("remarks", note);

        RetrofitFactory.getApiService(this)
                .makeOrderGoCombination(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(GoOrderGroupDownOrder.this, "服务异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response response) {
                        DialogLoadUtils.dismissDialog();
                        if ("00000".equals(response.getErrorCode())) {
                            showNotifyTwo("组合下单成功", "继续组合下单", "查看已下单");
                        }

                    }
                });
    }

    //
    public void showNotifyTwo(String tilte, String btnLeft, String btnRight) {
        final AlertDialog alertDialog = new AlertDialog.Builder(GoOrderGroupDownOrder.this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_bid);
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
        ((TextView) (window.findViewById(R.id.tv_continue))).setText(btnLeft);
        ((TextView) (window.findViewById(R.id.tv_seek))).setText(btnRight);
        window.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoOrderGroupDownOrder.this, GoOrderPurchase.class);
                intent.putExtra("DIRECT", "1");
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

        window.findViewById(R.id.tv_seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GoOrderGroupDownOrder.this, GoOrderPurchase.class);
                intent.putExtra("DIRECT", "2");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });


    }

}
