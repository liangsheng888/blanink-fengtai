package com.blanink.ui.activity.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.CommonAdapter;
import com.blanink.ui.adapter.PhotoAdapter;
import com.blanink.ui.adapter.RecyclerItemClickListener;
import com.blanink.ui.adapter.ViewHolder;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.FeedBackingTask;
import com.blanink.bean.OrderProduct;
import com.blanink.bean.Response;
import com.blanink.oss.OssService;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DateUtils;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.PriorityUtils;
import com.blanink.view.NoScrollListview;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 任务反馈 去反馈
 */
public class TaskNotAllocationToResponse extends BaseActivity {


    @BindView(R.id.task_response_iv_last)
    TextView taskResponseIvLast;
    @BindView(R.id.task_response_rl)
    RelativeLayout taskResponseRl;
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
    @BindView(R.id.my_priority)
    TextView myPriority;
    @BindView(R.id.tv_my_my_priority)
    TextView tvMyMyPriority;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.wb_content)
    WebView wbContent;
    @BindView(R.id.task_response_rl_gongXuInfo)
    LinearLayout taskResponseRlGongXuInfo;
    @BindView(R.id.lv_task_response_info)
    NoScrollListview lvTaskResponseInfo;
    @BindView(R.id.task_response_sp_person)
    Spinner taskResponseSpPerson;
    @BindView(R.id.task_response_ll)
    LinearLayout taskResponseLl;
    @BindView(R.id.task_response_et_finish_num)
    EditText taskResponseEtFinishNum;
    @BindView(R.id.task_response_ll2)
    LinearLayout taskResponseLl2;
    @BindView(R.id.task_response_et_bad_num)
    EditText taskResponseEtBadNum;
    @BindView(R.id.task_response_ll3)
    LinearLayout taskResponseLl3;
    @BindView(R.id.task_response_sp_state)
    Spinner taskResponseSpState;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.note_info)
    TextView noteInfo;
    @BindView(R.id.task_response_rl_edt_content)
    EditText taskResponseRlEdtContent;
    @BindView(R.id.task_response_rl2)
    RelativeLayout taskResponseRl2;
    @BindView(R.id.task_response_tv_hand)
    TextView taskResponseTvHand;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.task_response_rl_hand)
    RelativeLayout taskResponseRlHand;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.task_response_btn_commit)
    Button taskResponseBtnCommit;
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
    @BindView(R.id.progressBar3)
    ProgressBar progressBar3;
    @BindView(R.id.rl_load)
    RelativeLayout rlLoad;
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
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    @BindView(R.id.pro_category)
    TextView proCategory;
    private OrderProduct.ResultBean.RowsBean taskDetail;
    private String faultAmount;
    private String isFinished;
    private String achieveAmount;
    private String remarks;
    private SharedPreferences sp;
    private List<String> masterItem = new ArrayList<String>();
    private List<String> userIdList = new ArrayList<String>();
    private String feedbackUserId;
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private CommonAdapter<FeedBackingTask.ResultBean.ProcessFeedbackListBean> commonAdapter;
    private String feedbackAttachmentStr = "";
    OSSClient oss;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_allocation_detail);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        oss = OssService.getOSSClientInstance(this);
        ButterKnife.bind(this);
        receiveDataFromIntent();
        initData();
    }

    private void initData() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        checkUserRoleType();

//判断用户角色类型 assignment yes 显示下拉责任人 否不显示
        taskResponseSpState.setAdapter(new ArrayAdapter<String>(this, R.layout.spanner_item, new String[]{"未完成", "已完成"}));
        taskResponseSpState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        isFinished = "0";
                        break;
                    case 1:
                        isFinished = "1";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //图片放大
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(3)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(TaskNotAllocationToResponse.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(TaskNotAllocationToResponse.this);
                        }
                    }
                }));
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                checkUserRoleType();
            }
        });

    }

    //检查判断用户角色
    private void checkUserRoleType() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("id", taskDetail.getRelFlowProcess().getProcess().getId());

        RetrofitFactory.getApiService(this)
                .accessControl(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(Response response) {
                        loadData(response);
                    }
                });
    }

    private void receiveDataFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        taskDetail = ((OrderProduct.ResultBean.RowsBean) bundle.getSerializable("TaskDetail"));
        Log.e("TaskResponse", "待分配的任务:" + taskDetail.toString());
    }

    //列出我已经反馈过的某个产品，并包含所有反馈记录，展示页面用的
    public void loadData(final Response response) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("process.id", taskDetail.getRelFlowProcess().getProcess().getId());
        mapParams.put("flow.id", taskDetail.getRelFlowProcess().getFlow().getId());

        RetrofitFactory.getApiService(this)
                .listFeedbackingTask(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedBackingTask>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(final FeedBackingTask feedbackingTask) {
                        llLoad.setVisibility(View.GONE);
                        flLoad.setVisibility(View.GONE);
                        tvCompanyName.setText(taskDetail.getCompanyA().getName());
                        if (taskDetail.getCompanyBOwner() != null && taskDetail.getCompanyBOwner().getName() != null) {
                            tvMaster.setText(taskDetail.getCompanyBOwner().getName());
                        }
                        //tvTime.setText(taskDetail.getCreateDate());
                        tvProCategory.setText(taskDetail.getCompanyCategory().getName());
                        tvProName.setText(taskDetail.getProductName());
                        tvNum.setText(taskDetail.getAmount());
                        tvDeliveryTime.setText(taskDetail.getDeliveryTime());
                        tvMyTaskNum.setText(taskDetail.getRelFlowProcess().getTarget() + "");
                        if (feedbackingTask.getResult().getProductDescription() != null && !feedbackingTask.getResult().getProductDescription().equals("")) {
                            wbContent.getSettings().setDefaultTextEncodingName("UTF -8");
                            wbContent.getSettings().setJavaScriptEnabled(true);
                            wbContent.getSettings().setDefaultFontSize(14);
                            wbContent.loadData(feedbackingTask.getResult().getProductDescription(), "text/html; charset=UTF-8", null);
                        }
                        //tvNote.setText(Html.fromHtml(taskDetail.getProductDescription()));
                        tvPriority.setText(PriorityUtils.getPriority(taskDetail.getCompanyAPriority()));
                        tvMyMyPriority.setText(PriorityUtils.getPriority(taskDetail.getCompanyBPriority()));
                        tvResponse.setText((feedbackingTask.getResult().getAllFinishedAmount() + ""));
                        tvNoteNum.setText("(" + feedbackingTask.getResult().getProcessFeedbackList().size() + ")");

                        if (feedbackingTask.getResult().getProcessFeedbackList().size() >= 4) {
                            tvMore.setVisibility(View.VISIBLE);
                        }
                        if (feedbackingTask.getResult().getProcessFeedbackList().size() > 0) {
                            rlHistory.setVisibility(View.VISIBLE);
                        }
                        Log.e("TaskResponse", "我反馈过的2:" + feedbackingTask.toString());


                        //历史反馈记录
                        commonAdapter = new CommonAdapter<FeedBackingTask.ResultBean.ProcessFeedbackListBean>(TaskNotAllocationToResponse.this, feedbackingTask.getResult().getProcessFeedbackList(), R.layout.item_history_note_response, 4) {
                            @Override
                            public void convert(ViewHolder viewHolder, FeedBackingTask.ResultBean.ProcessFeedbackListBean processFeedbackUser, int position) {
                                processFeedbackUser = feedbackingTask.getResult().getProcessFeedbackList().get(position);
                                TextView date = viewHolder.getViewById(R.id.tv_date);
                                TextView tv_master = viewHolder.getViewById(R.id.tv_master);
                                TextView tv_finished = viewHolder.getViewById(R.id.tv_finished);
                                TextView tv_bad = viewHolder.getViewById(R.id.tv_bad);
                                tv_master.setText(processFeedbackUser.getFeedbackUser().getName() + "");
                                date.setText(DateUtils.format(DateUtils.stringToDate(processFeedbackUser.getCreateDate())));
                                tv_finished.setText((processFeedbackUser.getAchieveAmount()) + "个");
                                tv_bad.setText((processFeedbackUser.getFaultAmount()) + "个");
                            }
                        };
                        lvResponseNote.setAdapter(commonAdapter);

                        Log.e("@@@@", "getProcessWorkerList:" + feedbackingTask.getResult().getProcessWorkerList().size());

                        if (response.getErrorCode().equals("00000") && response.getResult()) {
                            for (int i = 0; i < feedbackingTask.getResult().getProcessWorkerList().size(); i++) {
                                if (feedbackingTask.getResult().getProcessWorkerList().get(i) != null) {
                                    Log.e("@@@@", feedbackingTask.getResult().getProcessWorkerList().get(i).toString());
                                    masterItem.add(i, feedbackingTask.getResult().getProcessWorkerList().get(i).getName() + "");

                                    userIdList.add(i, feedbackingTask.getResult().getProcessWorkerList().get(i).getId() + "");
                                    Log.e("TaskResponse", "该用户有权限:" + userIdList.toString() + "----" + masterItem.toString());
                                }
                            }
                        } else {
                            userIdList.add(sp.getString("USER_ID", null));
                            masterItem.add(sp.getString("NAME", null));
                            Log.e("TaskResponse", "该用户没有有权限:" + userIdList.toString() + "----" + masterItem.toString());
                        }
                        showSpinner(masterItem);//显示下拉列表
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择返回
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            ArrayList<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                selectedPhotos.clear();
                if (photos != null) {
                    selectedPhotos.addAll(photos);
                    for (int i = 0; i < selectedPhotos.size(); i++) {
                        feedbackAttachmentStr = feedbackAttachmentStr + "," + OssService.OSS_URL + "alioss_" + CommonUtil.getFileName(selectedPhotos.get(i)) + CommonUtil.getFileLastName(selectedPhotos.get(i));
                    }
                    feedbackAttachmentStr = feedbackAttachmentStr.substring(1);
                    Log.e("ComeOrder", feedbackAttachmentStr);
                }
                photoAdapter.notifyDataSetChanged();

            }
        }
    }

    //select master
    public void showSpinner(List<String> masterItem) {
        Log.e("TaskResponse", "反馈人:" + masterItem.toString());
        if (masterItem.size() == 0) {
            masterItem.add("没有权限");
            userIdList.add("");
            taskResponseBtnCommit.setText("没有权限");
            taskResponseBtnCommit.setEnabled(false);
            taskResponseBtnCommit.setTextColor(Color.WHITE);
            taskResponseBtnCommit.setBackgroundColor(Color.GRAY);
        }
        taskResponseSpPerson.setAdapter(new ArrayAdapter<String>(TaskNotAllocationToResponse.this, R.layout.spanner_item, masterItem));
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

    @OnClick({R.id.task_response_iv_last, R.id.task_response_btn_commit, R.id.tv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_response_iv_last:
                finish();//返回
                break;
            case R.id.task_response_btn_commit:
                //提交 前先验证
                faultAmount = taskResponseEtBadNum.getText().toString().trim();
                achieveAmount = taskResponseEtFinishNum.getText().toString().trim();
                remarks = taskResponseRlEdtContent.getText().toString().trim();
                if (TextUtils.isEmpty(taskResponseEtFinishNum.getText().toString())) {
                    Toast.makeText(this, "请填写完成数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(taskResponseEtBadNum.getText().toString())) {
                    Toast.makeText(this, "请填写次品", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(taskResponseRlEdtContent.getText().toString())) {
                    Toast.makeText(this, "请填写备注", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(this);
                DialogLoadUtils.showDialogLoad("反馈中...");
                uploadData();
                break;
            case R.id.tv_more://更多
                Intent intent = new Intent(TaskNotAllocationToResponse.this, MoreResponseNoteActivty.class);
                intent.putExtra("flow.id", taskDetail.getRelFlowProcess().getFlow().getId());
                intent.putExtra("process.id", taskDetail.getRelFlowProcess().getProcess().getId());
                startActivity(intent);
                break;
        }
    }

    //save
    private void uploadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("process.id", taskDetail.getRelFlowProcess().getProcess().getId());
        mapParams.put("flow.id", taskDetail.getRelFlowProcess().getFlow().getId());
        mapParams.put("feedbackUser.id", feedbackUserId);
        mapParams.put("faultAmount", faultAmount);
        mapParams.put("isFinished", isFinished);
        mapParams.put("achieveAmount", achieveAmount);
        mapParams.put("feedbackAttachmentStr", feedbackAttachmentStr);
        mapParams.put("remarks", remarks);
        RetrofitFactory.getApiService(this)
                .processFeedbackSave(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(TaskNotAllocationToResponse.this, "服务器开了会儿小差,请稍后重试", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getErrorCode().equals("00000")) {
                            List<String> photos = new ArrayList<String>();
                            photos.addAll(selectedPhotos);
                            uploadFiles(oss, photos);
                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(TaskNotAllocationToResponse.this, "反馈失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void showDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(TaskNotAllocationToResponse.this).create();
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
                taskResponseEtBadNum.setText("");
                taskResponseEtFinishNum.setText("");
                taskResponseRlEdtContent.setText("");
                masterItem.clear();
                userIdList.clear();
                checkUserRoleType();
            }
        });
    }

    public void uploadFiles(OSSClient oss, List<String> urls) {
        if (null == urls || urls.size() == 0) {
            DialogLoadUtils.dismissDialog();
            handler.sendEmptyMessage(0);
            return;
        } // 上传文件
        ossUpload(oss, urls);
    }

    public void ossUpload(final OSSClient oss, final List<String> urls) {
        Log.e("ComeOrder", "图片个数:" + urls.size());
        if (urls.size() <= 0) {
            Log.e("ComeOrder", "通知提醒");
            DialogLoadUtils.dismissDialog();
            handler.sendEmptyMessage(0);
            // 文件全部上传完毕，这里编写上传结束的逻辑，如果要在主线程操作，最好用Handler或runOnUiThead做对应逻辑
            return;// 这个return必须有，否则下面报越界异常，原因自己思考下哈
        }
        final String url = urls.get(0);
        if (TextUtils.isEmpty(url)) {
            urls.remove(0);
            // url为空就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(oss, urls);
            return;
        }

        File file = new File(url);
        if (null == file || !file.exists()) {
            urls.remove(0);
            // 文件为空或不存在就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(oss, urls);
            return;
        }
        // 文件后缀
        String fileSuffix = "";
        if (file.isFile()) {
            // 获取文件后缀名
            fileSuffix = CommonUtil.getFileName(url) + CommonUtil.getFileLastName(url);
        }
        // 文件标识符objectKey
        final String objectKey = "alioss_" + fileSuffix;
        // 下面3个参数依次为bucket名，ObjectKey名，上传文件路径
        PutObjectRequest put = new PutObjectRequest("blanink", objectKey, url);

        // 设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                // 进度逻辑
            }
        });

        // 异步上传
        OSSAsyncTask task = oss.asyncPutObject(put,
                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) { // 上传成功
                        urls.remove(0);
                        ossUpload(oss, urls);// 递归同步效果
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                          ServiceException serviceException) { // 上传失败

                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e("ErrorCode", serviceException.getErrorCode());
                            Log.e("RequestId", serviceException.getRequestId());
                            Log.e("HostId", serviceException.getHostId());
                            Log.e("RawMessage", serviceException.getRawMessage());
                        }
                    }
                });
        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待直到任务完成
    }

}
