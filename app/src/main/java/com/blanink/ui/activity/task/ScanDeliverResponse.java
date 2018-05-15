package com.blanink.ui.activity.task;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.blanink.ui.adapter.EmpAdapter;
import com.blanink.ui.adapter.PhotoAdapter;
import com.blanink.ui.adapter.RecyclerItemClickListener;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.Emp;
import com.blanink.bean.FeedBackingTask;
import com.blanink.bean.Response;
import com.blanink.bean.ResponseDelete;
import com.blanink.oss.OssService;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.DialogNotifyUtils;
import com.blanink.utils.PriorityUtils;
import com.blanink.view.NoScrollGridview;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

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

/****
 * 扫描f发货反馈
 */
public class ScanDeliverResponse extends BaseActivity {

    private static final String TAG = "ScanDeliverResponse";
    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.deliver)
    RelativeLayout deliver;
    @BindView(R.id.tv_companyName)
    TextView tvCompanyName;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_master)
    TextView tvMaster;
    @BindView(R.id.order_item_ll)
    RelativeLayout orderItemLl;
    @BindView(R.id.pro_category)
    TextView proCategory;
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
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.sp_is_sender)
    Spinner spIsSender;
    @BindView(R.id.sp_sender)
    Spinner spSender;
    @BindView(R.id.sp_receiver)
    Spinner spReceiver;
    @BindView(R.id.gv_receiver)
    NoScrollGridview gvReceiver;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.task_response_rl_hand)
    RelativeLayout taskResponseRlHand;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.btn_send)
    Button btnSend;
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
    private List<String> receiverNameList = new ArrayList<>();
    private List<String> receiverIdList = new ArrayList<>();
    private List<Emp> empList = new ArrayList<>();
    private String receiverName;
    private String receiverId;
    private String senderId;
    private String[] isSend = {"部分发货", "全部发货"};
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    List<FeedBackingTask.ResultBean.SendUserListBean> sendUserList = new ArrayList<>();

    private List<String> masterItem = new ArrayList<String>();
    private List<String> userIdList = new ArrayList<String>();
    private List<String> sendUserId = new ArrayList<String>();

    PhotoAdapter photoAdapter;
    private String urls = "";
    private String achieveAmount;
    private String isFinished;//1部分发货，2全部发货
    private String remarks;
    private OSSClient oss;
    private SharedPreferences sp;
    private String flowId = "";
    private String workPlanId = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            EventBus.getDefault().post(new DataSynRefreshEvent(true, 1, "发货历史发聩刷新"));
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
            paramsMap.put("orderProduct.id", getIntent().getStringExtra("orderProductId"));
            paramsMap.put("achieveAmount", achieveAmount);
            paramsMap.put("sendUser", new Gson().toJson(sendUserId));
            //激光推送
            RetrofitFactory.getApiService(ScanDeliverResponse.this)
                    .deliver(paramsMap)
                    .subscribeOn(Schedulers.newThread())
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
            // DialogNotifyUtils.showNotify(TaskResponseDeliver.this, "发货成功");
            DialogNotifyUtils.showNotifyDestroyActivity(ScanDeliverResponse.this, "继续", "返回", "你已发货成功！");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_respone);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);

        initData();
    }

    public void deliver() {
        Map<String, Object> params = new HashMap<>();
        params.put("achieveAmount", achieveAmount);
        params.put("isFinished", isFinished);
        params.put("feedbackUser", senderId);
        params.put("remarks", remarks);
        params.put("feedbackAttachmentStr", urls);
        params.put("workPlan.id", workPlanId);
        for (int i = 0; i < sendUserId.size(); i++) {
            params.put("sendUser[" + i + "]", sendUserId.get(i));//接收人List
        }
        params.put("process.id", getIntent().getStringExtra("processId"));
        params.put("flow.id", flowId);
        params.put("currentUser.id", sp.getString("USER_ID", null));
        params.put("userId", sp.getString("USER_ID", null));
        RetrofitFactory.getApiService(this)
                .delivery(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(ScanDeliverResponse.this, "服务器开了会儿小差，稍后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response re) {
                        if ("00000".equals(re.getErrorCode())) {
                            List<String> photos = new ArrayList<String>();
                            photos.addAll(selectedPhotos);
                            uploadFiles(oss, photos);
                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(ScanDeliverResponse.this, "操作失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void initData() {
        oss = OssService.getOSSClientInstance(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);

        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        getCheckData();

        spIsSender.setAdapter(new ArrayAdapter<String>(this, R.layout.spanner_item, new String[]{"未完成", "已完成"}));
        spIsSender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                    .start(ScanDeliverResponse.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(ScanDeliverResponse.this);
                        }
                    }
                }));
        //重新加载
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                getCheckData();
            }
        });

        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent getIntent(Context context, String orderProductId, String processId) {
        Intent intent = new Intent(context, ScanDeliverResponse.class);
        intent.putExtra("processId", processId);
        intent.putExtra("orderProductId", orderProductId);
        return intent;
    }

    public void getCheckData() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderProduct.id", getIntent().getStringExtra("orderProductId"));
        params.put("currentUser.id", sp.getString("USER_ID", null));
        params.put("process.id", getIntent().getStringExtra("processId"));
        RetrofitFactory.getApiService(this)
                .accessControl(params)
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
                    public void onNext(final Response response) {
                        getResponseData(response);
                    }
                });
    }

    public void getResponseData(final Response response) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderProduct.id", getIntent().getStringExtra("orderProductId"));
        params.put("currentUser.id", sp.getString("USER_ID", null));
        params.put("process.id", getIntent().getStringExtra("processId"));
        RetrofitFactory.getApiService(this)
                .listFeedbackingTaskQRCode(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedBackingTask>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(final FeedBackingTask feedBackingTask) {
                        llLoad.setVisibility(View.GONE);
                        if (feedBackingTask.getResult() == null) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            flLoad.setVisibility(View.GONE);
                            if (feedBackingTask.getResult().getRelFlowProcess() != null && feedBackingTask.getResult().getRelFlowProcess().getFlow() != null) {
                                flowId = feedBackingTask.getResult().getRelFlowProcess().getFlow().getId();
                            }
                            if (feedBackingTask.getResult().getWorkPlan() != null && feedBackingTask.getResult().getWorkPlan().getId() != null) {
                                workPlanId = feedBackingTask.getResult().getWorkPlan().getId();
                            }
                            if (response.getErrorCode().equals("00000") && response.getResult()) {
                                for (int i = 0; i < feedBackingTask.getResult().getProcessWorkerList().size(); i++) {
                                    masterItem.add(i, feedBackingTask.getResult().getProcessWorkerList().get(i).getName());
                                    userIdList.add(i, feedBackingTask.getResult().getProcessWorkerList().get(i).getId());
                                    Log.e(TAG, "该用户有权限:" + masterItem.toString());
                                }
                            } else {
                                userIdList.add(sp.getString("USER_ID", null));
                                masterItem.add(sp.getString("NAME", null));
                                Log.e(TAG, "该用户没有有权限:" + masterItem.toString());
                            }
                            sendUserList.addAll(feedBackingTask.getResult().getSendUserList());

                            receiverNameList.add("请选择责任人");
                            receiverIdList.add("");
                            for (FeedBackingTask.ResultBean.SendUserListBean send : sendUserList) {
                                receiverNameList.add(send.getName());
                                receiverIdList.add(send.getId());
                            }
                            spReceiver.setAdapter(new ArrayAdapter<String>(ScanDeliverResponse.this, R.layout.spanner_item, receiverNameList));
                            spReceiver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    receiverName = receiverNameList.get(position);
                                    receiverId = receiverIdList.get(position);
                                    if (receiverId != "" && receiverId != null) {
                                        if (!compareIsExists(empList, receiverId)) {
                                            sendUserId.add(receiverId);
                                            empList.add(new Emp(receiverId, receiverName));
                                        }
                                        gvReceiver.setAdapter(new EmpAdapter(ScanDeliverResponse.this, empList));
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            spSender.setAdapter(new ArrayAdapter<String>(ScanDeliverResponse.this, R.layout.spanner_item, masterItem));
                            spSender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    senderId = userIdList.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }


                        tvCompanyName.setText(feedBackingTask.getResult().getCompanyA().getName());
                        if (feedBackingTask.getResult().getCompanyBOwner() != null && feedBackingTask.getResult().getCompanyBOwner().getName() != null) {
                            tvMaster.setText(feedBackingTask.getResult().getCompanyBOwner().getName());
                        }
                        //tvTime.setText(order.createDate);
                        tvProCategory.setText(feedBackingTask.getResult().getCompanyCategory().getName());
                        tvProName.setText(feedBackingTask.getResult().getProductName());
                        tvNum.setText(feedBackingTask.getResult().getAmount());
                        tvDeliveryTime.setText(feedBackingTask.getResult().getDeliveryTime());
                        tvMyTaskNum.setText(feedBackingTask.getResult().getRelFlowProcess().getTarget() + "");
                        tvResponse.setText(feedBackingTask.getResult().getAllFinishedAmount() + "");
                        if (feedBackingTask.getResult().getProductDescription() != null && !feedBackingTask.getResult().getProductDescription().equals("")) {
                            wbContent.getSettings().setDefaultTextEncodingName("UTF -8");
                            wbContent.getSettings().setJavaScriptEnabled(true);
                            wbContent.getSettings().setDefaultFontSize(14);
                            wbContent.loadData(feedBackingTask.getResult().getProductDescription(), "text/html; charset=UTF-8", null);
                        }
                        tvPriority.setText(PriorityUtils.getPriority(feedBackingTask.getResult().getCompanyAPriority()));
                        tvMyMyPriority.setText(PriorityUtils.getPriority(feedBackingTask.getResult().getCompanyBPriority()));

                    }
                });
    }

    @OnClick({R.id.iv_last, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_last:
                finish();//返回
                break;
            case R.id.btn_send:
                achieveAmount = etNumber.getText().toString().trim();
                remarks = etNote.getText().toString();
                if (TextUtils.isEmpty(achieveAmount)) {
                    Toast.makeText(ScanDeliverResponse.this, "数量不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (receiverIdList.size() == 0) {
                    Toast.makeText(ScanDeliverResponse.this, "请选择通知接收人", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(remarks)) {
                    Toast.makeText(ScanDeliverResponse.this, "需要填写备注信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(ScanDeliverResponse.this);
                DialogLoadUtils.showDialogLoad("发货中...");
                deliver();
                break;
        }


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
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(ScanDeliverResponse.this, "保存失败", Toast.LENGTH_SHORT).show();
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
                        urls = urls + "," + OssService.OSS_URL + "alioss_" + CommonUtil.getFileName(selectedPhotos.get(i)) + CommonUtil.getFileLastName(selectedPhotos.get(i));
                    }
                    urls = urls.substring(1);
                    Log.e("ComeOrder", urls);
                }

                photoAdapter.notifyDataSetChanged();
            }
        }
    }

    public Boolean compareIsExists(List<Emp> empList, String id) {
        Boolean flag = false;
        if (empList.size() == 0) {
            return false;
        }
        for (Emp em : empList) {
            if (em.EmpId.equals(id)) {
                flag = true;
                break;
            }

        }
        return flag;
    }
}
