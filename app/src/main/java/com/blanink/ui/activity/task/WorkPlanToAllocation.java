package com.blanink.ui.activity.task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.ResponseAllocation;
import com.blanink.bean.ResponseDelete;
import com.blanink.bean.TypeCateGory;
import com.blanink.http.RetrofitFactory;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.CommonAdapter;
import com.blanink.ui.adapter.ViewHolder;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.PriorityUtils;
import com.blanink.view.NoScrollListview;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 任务分配 去分配
 */
public class WorkPlanToAllocation extends BaseActivity {

    private static final String TAG = "WorkPlanToAllocation";
    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    @BindView(R.id.rl_load)
    RelativeLayout rlLoad;
    @BindView(R.id.come_order)
    RelativeLayout comeOrder;
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
    @BindView(R.id.tv_companyName)
    TextView tvCompanyName;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_master)
    TextView tvMaster;
    @BindView(R.id.order_item_ll)
    RelativeLayout orderItemLl;
    @BindView(R.id.tv_pro_category)
    TextView tvProCategory;
    @BindView(R.id.order_item_ll2)
    RelativeLayout orderItemLl2;
    @BindView(R.id.order_item_ll2_guigeName)
    TextView orderItemLl2GuigeName;
    @BindView(R.id.tv_pro_name)
    TextView tvProName;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.priority)
    TextView priority;
    @BindView(R.id.tv_priority)
    TextView tvPriority;
    @BindView(R.id.response)
    TextView response;
    @BindView(R.id.tv_response)
    TextView tvResponse;
    @BindView(R.id.tv_my_task)
    TextView tvMyTask;
    @BindView(R.id.tv_my_task_num)
    TextView tvMyTaskNum;
    @BindView(R.id.deliveryTime)
    TextView deliveryTime;
    @BindView(R.id.tv_deliveryTime)
    TextView tvDeliveryTime;
    @BindView(R.id.allocation)
    TextView allocation;
    @BindView(R.id.tv_allocation)
    TextView tvAllocation;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.web_content)
    WebView webContent;
    @BindView(R.id.task_response_rl_gongXuInfo)
    LinearLayout taskResponseRlGongXuInfo;
    @BindView(R.id.lv_task_response_info)
    NoScrollListview lvTaskResponseInfo;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.task_response_sp_person)
    Spinner taskResponseSpPerson;
    @BindView(R.id.task_response_ll)
    LinearLayout taskResponseLl;
    @BindView(R.id.task_response_et_finish_num)
    EditText taskResponseEtFinishNum;
    @BindView(R.id.task_response_ll2)
    LinearLayout taskResponseLl2;
    @BindView(R.id.sp_priority)
    Spinner spPriority;
    @BindView(R.id.task_priority)
    LinearLayout taskPriority;
    @BindView(R.id.et_finish_date)
    EditText etFinishDate;
    @BindView(R.id.task_response_ll3)
    LinearLayout taskResponseLl3;
    @BindView(R.id.tv_note_info)
    TextView tvNoteInfo;
    @BindView(R.id.task_response_rl_edt_content)
    EditText taskResponseRlEdtContent;
    @BindView(R.id.task_response_rl2)
    RelativeLayout taskResponseRl2;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.note_num)
    TextView noteNum;
    @BindView(R.id.tv_note_num)
    TextView tvNoteNum;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.rl_history)
    RelativeLayout rlHistory;
    @BindView(R.id.lv_response_note)
    NoScrollListview lvResponseNote;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.activity_work_plan_to_allocation)
    LinearLayout activityWorkPlanToAllocation;

    private List<String> masterItem = new ArrayList<String>();
    private List<String> userIdList = new ArrayList<String>();
    private String processId;
    private String flowId;
    private SharedPreferences sp;
    private String feedbackUserId;
    private List<String> priorityValue = new ArrayList<>();
    private List<String> priorityName = new ArrayList<>();
    private String priority_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_plan_to_allocation);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        receiveData();
        initData();
    }

    private void receiveData() {
        Intent intent = getIntent();
        processId = intent.getStringExtra("processId");
        flowId = intent.getStringExtra("flowId");
    }

    private void initData() {
        loadData();
        //限定完成日期
        loadPriority();
        etFinishDate.setText(new SimpleDateFormat("yyyy-MM-dd").format((new Date(System.currentTimeMillis()))));
        etFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(WorkPlanToAllocation.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etFinishDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
//保存分配
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(taskResponseEtFinishNum.getText().toString())) {
                    Toast.makeText(WorkPlanToAllocation.this, "请填写完成数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(taskResponseRlEdtContent.getText().toString())) {
                    Toast.makeText(WorkPlanToAllocation.this, "请填写备注信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(WorkPlanToAllocation.this);
                DialogLoadUtils.showDialogLoad("操作进行中...");
                saveAllocation();
            }
        });
    }

    @OnClick(R.id.iv_last)
    public void onClick() {
        finish();
    }

    public void loadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("process.id", processId);
        mapParams.put("flow.id", flowId);
        RetrofitFactory.getApiService(this)
                .listPlanTaskPage(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<com.blanink.bean.WorkPlanToAllocation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(final com.blanink.bean.WorkPlanToAllocation workPlan) {
                        flLoad.setVisibility(View.GONE);
                        tvCompanyName.setText(workPlan.getResult().getCompanyA().getName());
                        //tvTime.setText(workPlan.getResult().getOrder().getTakeOrderTime());
                        tvProCategory.setText(workPlan.getResult().getCompanyCategory().getName());
                        tvProName.setText(workPlan.getResult().getProductName());
                        tvNum.setText(workPlan.getResult().getAmount());
                        tvPriority.setText(PriorityUtils.getPriority(workPlan.getResult().getRelFlowProcess().getProcessPriority()));
                        tvResponse.setText(workPlan.getResult().getRelFlowProcess().getTotalCompletedQuantity() + "");
                        tvMyTaskNum.setText(workPlan.getResult().getRelFlowProcess().getTarget() + "");
                        tvDeliveryTime.setText(workPlan.getResult().getDeliveryTime());
                        tvAllocation.setText(workPlan.getResult().getPlanedAmount() + "");
                        tvNoteNum.setText("(" + workPlan.getResult().getWorkPlanList().size() + ")");
                        if (workPlan.getResult().getCompanyBOwner() != null) {
                            tvMaster.setText("" + workPlan.getResult().getCompanyBOwner().getName());
                        }
                        //历史分配
                        if (workPlan.getResult().getWorkPlanList().size() <= 2) {
                            tvMore.setVisibility(View.GONE);
                        }
                        if (workPlan.getResult().getWorkPlanList().size() == 0) {
                            rlHistory.setVisibility(View.GONE);
                        }
                        if (workPlan.getResult().getWorkPlanList().size() > 2) {
                            //更多
                            tvMore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(WorkPlanToAllocation.this, WorkPlanToAllocationHistory.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("workPlan", workPlan);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }
                        //如果当前分配的任务等于总任务量 无法进行分配
                        if (workPlan.getResult().getPlanedAmount() == workPlan.getResult().getRelFlowProcess().getTarget()) {
                            btnCommit.setEnabled(false);
                            btnCommit.setBackgroundColor(Color.GRAY);
                            btnCommit.setText("任务已经分配完了");
                        }

                        if (workPlan.getResult().getProductDescription() != null && !workPlan.getResult().getProductDescription().equals("")) {
                            webContent.getSettings().setDefaultTextEncodingName("UTF -8");
                            webContent.getSettings().setJavaScriptEnabled(true);
                            webContent.getSettings().setDefaultFontSize(14);
                            webContent.loadData(workPlan.getResult().getProductDescription(), "text/html; charset=UTF-8", null);
                        }
                        //tvNote.setText(workPlan.getResult().getProductDescription());
                        Log.e("WorkPlanToAllocation", workPlan.getResult().getProcessWorkerList().toString());
                        if (workPlan.getResult().getProcessWorkerList() != null) {
                            for (int i = 0; i < workPlan.getResult().getProcessWorkerList().size(); i++) {
                                masterItem.add(workPlan.getResult().getProcessWorkerList().get(i).getName());
                                userIdList.add(workPlan.getResult().getProcessWorkerList().get(i).getId());
                            }
                            Log.e("WorkPlanToAllocation", "被分配者:" + masterItem.toString());
                            showSpinner(masterItem);
                        }
                        Log.e("WorkPlanToAllocation", "planedAmount:" + workPlan.getResult().getPlanedAmount());


                        //分配记录
                        lvResponseNote.setAdapter(new CommonAdapter<com.blanink.bean.WorkPlanToAllocation.ResultBean.WorkPlanListBean>(WorkPlanToAllocation.this, workPlan.getResult().getWorkPlanList(), R.layout.item_work_plan_history_allocated, 2) {
                            @Override
                            public void convert(ViewHolder viewHolder, com.blanink.bean.WorkPlanToAllocation.ResultBean.WorkPlanListBean workPlanBean, int position) {
                                workPlanBean = workPlan.getResult().getWorkPlanList().get(position);
                                TextView tv_allocation_time = viewHolder.getViewById(R.id.tv_allocation_time);
                                TextView tv_task_person = viewHolder.getViewById(R.id.tv_task_person);
                                TextView tv_allocation_num = viewHolder.getViewById(R.id.tv_allocation_num);
                                tv_allocation_time.setText(workPlanBean.getCreateDate());
                                tv_task_person.setText(workPlanBean.getWorker().getName());
                                tv_allocation_num.setText(workPlanBean.getAchieveAmount());
                            }
                        });
                    }
                });

    }

    //select master
    private void showSpinner(List<String> masterItem) {
        Log.e("TaskResponse", masterItem.toString());
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, R.layout.spanner_item
                , masterItem);
        taskResponseSpPerson.setAdapter(_Adapter);
        //选择责任人
        taskResponseSpPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                feedbackUserId = userIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //保存分配
    public void saveAllocation() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("process.id", processId);
        mapParams.put("flow.id", flowId);
        mapParams.put("planTime", etFinishDate.getText().toString());
        mapParams.put("priority", priority_value);
        mapParams.put("worker.id", feedbackUserId);
        mapParams.put("remarks", taskResponseRlEdtContent.getText().toString());
        mapParams.put("achieveAmount", taskResponseEtFinishNum.getText().toString().trim());
        RetrofitFactory.getApiService(this)
                .saveWorkPlan(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseAllocation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(WorkPlanToAllocation.this, "服务器开了会儿小差，稍后再试", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(ResponseAllocation responseAllocation) {
                        Gson gson = new Gson();
                        if (responseAllocation.getErrorCode().equals("00000")) {
                            DialogLoadUtils.dismissDialog();
                            //发布事件
                            EventBus.getDefault().post(new DataSynRefreshEvent(true, 0, "刷新数据"));
                            Map<String, Object> paramsMap = new HashMap<String, Object>();
                            paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
                            paramsMap.put("worker.id", feedbackUserId);
                            paramsMap.put("process.id", processId);
                            paramsMap.put("flow.id", flowId);
                            paramsMap.put("id", responseAllocation.getResult());

                            //激光推送
                            RetrofitFactory.getApiService(WorkPlanToAllocation.this)
                                    .taskAllocation(paramsMap)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<ResponseDelete>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(ResponseDelete responseDelete) {
                                            Log.e(TAG, responseDelete.getReason());

                                        }
                                    });
                            showDialog();
                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(WorkPlanToAllocation.this, "反馈失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // 显示提示
    private void showDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(R.layout.dialog_custom_response);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        Display d = getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
        //  lp.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);

        window.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                taskResponseEtFinishNum.setText("");
                taskResponseRlEdtContent.setText("");
                masterItem.clear();
                userIdList.clear();
                loadData();
            }
        });
    }

    //优先级
    public void loadPriority() {
        EventBus.getDefault().post(new DataSynRefreshEvent(true, 0, "刷新数据"));
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("userId", sp.getString("USER_ID", null));
        paramsMap.put("type", "sys_assign_priority");
        //激光推送
        RetrofitFactory.getApiService(WorkPlanToAllocation.this)
                .listValueByType(paramsMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TypeCateGory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TypeCateGory prioritys) {
                        for (int i = 0; i < prioritys.getResult().size(); i++) {
                            priorityValue.add(prioritys.getResult().get(i).getValue());
                            priorityName.add(prioritys.getResult().get(i).getLabel());
                        }

                        spPriority.setAdapter(new ArrayAdapter<String>(WorkPlanToAllocation.this, R.layout.spanner_item
                                , priorityName));

                        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                priority_value = priorityValue.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });

    }
}
