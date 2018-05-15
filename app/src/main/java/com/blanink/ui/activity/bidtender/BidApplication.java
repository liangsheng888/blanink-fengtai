package com.blanink.ui.activity.bidtender;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.blanink.ui.adapter.PhotoAdapter;
import com.blanink.ui.adapter.RecyclerItemClickListener;
import com.blanink.http.RetrofitFactory;
import com.blanink.oss.OssService;
import com.blanink.bean.Response;
import com.blanink.bean.ResponseDelete;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.DialogNotifyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 投标申请
 */
public class BidApplication extends BaseActivity {

    private static final int RESULT_UPLOAD = 1;
    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.bid_apply_iv_last)
    TextView bidApplyIvLast;
    @BindView(R.id.bid_apply_rl)
    RelativeLayout bidApplyRl;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.rl_theme)
    RelativeLayout rlTheme;
    @BindView(R.id.tv_price2)
    TextView tvPrice2;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.rl_price)
    RelativeLayout rlPrice;
    @BindView(R.id.tv_hand_date)
    TextView tvHandDate;
    @BindView(R.id.et_hand_date)
    EditText etHandDate;
    @BindView(R.id.sp_date_type)
    Spinner spDateType;
    @BindView(R.id.rl_hand_date)
    RelativeLayout rlHandDate;
    @BindView(R.id.go_order_add_ll)
    LinearLayout goOrderAddLl;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.rl_note)
    RelativeLayout rlNote;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.go_order_add_ll2)
    LinearLayout goOrderAddLl2;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.ll_save)
    LinearLayout llSave;
    @BindView(R.id.activity_bid_application)
    RelativeLayout activityBidApplication;
    private TextView bid_apply_iv_last;
    private SharedPreferences sp;
    private String inviteBidId;
    private String userId;
    private Button btn_upload;
    private EditText et_price;
    private EditText et_note;
    private TextView et_hand_date;
    private EditText et_title;
    private String handdate;
    private String note;
    private String singleprice;
    private String title;
    private Spinner sp_date_type;
    private String dateType;
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private String urls = "";
    private OSSClient oss;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DialogLoadUtils.dismissDialog();
            btnUpload.setEnabled(true);
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
            paramsMap.put("inviteBid.id", inviteBidId);
            //激光推送
            RetrofitFactory.getApiService(BidApplication.this)
                    .bidNotify(paramsMap)
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
                            Log.e("ReceiveGoods", responseDelete.getReason());

                        }
                    });
            showNotify();
        }
    };

    private void showNotify() {
        final AlertDialog alertDialog = new AlertDialog.Builder(BidApplication.this).create();
        // View view=View.inflate(BidApplication.this,R.layout_header_seek_suppler.dialog_custom_bid,null);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setContentView(R.layout.dialog_custom_bid);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        Display d = getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
        //  lp.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);

        window.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BidApplication.this, BidManage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                alertDialog.dismiss();
                finish();
            }
        });
        window.findViewById(R.id.tv_seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BidApplication.this, BidManage.class);
                intent.putExtra("DIRECT", "MINE_BID");
                startActivity(intent);
                alertDialog.dismiss();
                finish();

            }
        });
        alertDialog.getWindow().setWindowAnimations(R.style.dialogAnimationTranslate);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_application);
        ButterKnife.bind(this);
        // oss = OssService.getOSSClient(BidApplication.this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);

        receivedIntentInfo();
        initView();
        initData();
    }

    private void receivedIntentInfo() {
        Intent intent = getIntent();
        inviteBidId = intent.getStringExtra("inviteBid.id");
        userId = intent.getStringExtra("userId");
    }


    private void initView() {
        et_title = ((EditText) findViewById(R.id.et_title));
        bid_apply_iv_last = ((TextView) findViewById(R.id.bid_apply_iv_last));
        et_price = ((EditText) findViewById(R.id.et_price));//单价
        et_note = ((EditText) findViewById(R.id.et_note));//备注
        et_hand_date = ((TextView) findViewById(R.id.et_hand_date));//交货日期
        sp_date_type = ((Spinner) findViewById(R.id.sp_date_type));//周期类型
        btn_upload = (Button) findViewById(R.id.btn_upload);
    }

    private void initData() {

        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        oss = OssService.getOSSClientInstance(this);
        sp_date_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("年")) {
                    dateType = "5";
                }
                if (parent.getSelectedItem().toString().equals("月")) {
                    dateType = "4";
                }
                if (parent.getSelectedItem().toString().equals("周")) {
                    dateType = "3";
                }
                if (parent.getSelectedItem().toString().equals("日")) {
                    dateType = "2";
                }
                if (parent.getSelectedItem().toString().equals("时")) {
                    dateType = "1";
                }
                Log.e("BidApplication", "dateType:" + dateType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //返回
        bid_apply_iv_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = et_title.getText().toString();
                singleprice = et_price.getText().toString();
                note = et_note.getText().toString();
                handdate = et_hand_date.getText().toString();

                if (TextUtils.isEmpty(title) && TextUtils.isEmpty(singleprice)
                        && TextUtils.isEmpty(note)
                        && TextUtils.isEmpty(urls)
                        && TextUtils.isEmpty(handdate)
                        ) {
                    finish();
                } else {
                    DialogNotifyUtils.showNotifyDestroyActivity(BidApplication.this, "继续", "放弃", "真的要放弃本次操作吗？");
                }
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                title = et_title.getText().toString();
                singleprice = et_price.getText().toString();
                note = et_note.getText().toString();
                handdate = et_hand_date.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(BidApplication.this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(singleprice)) {
                    Toast.makeText(BidApplication.this, "请输入单价", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(handdate)) {
                    Toast.makeText(BidApplication.this, "请输入交货周期", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(note)) {
                    Toast.makeText(BidApplication.this, "请填写详情备注", Toast.LENGTH_SHORT).show();
                    return;
                }
                btnUpload.setEnabled(false);
                DialogLoadUtils.getInstance(BidApplication.this);
                DialogLoadUtils.showDialogLoad("投标申请中...");
                uploadDateToServer();
            }
        });
        //
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
                                    .start(BidApplication.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(BidApplication.this);
                        }
                    }
                }));
    }

    //上传服务器
    public void uploadDateToServer() {
        if (!CommonUtil.isConnected(this)) {
            Toast.makeText(this, "请检查你的网络！", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> mapParams = new HashMap<String, Object>();
            mapParams.put("userId", sp.getString("USER_ID", null));
            mapParams.put("bidCompany.id", sp.getString("COMPANY_ID", null));
            mapParams.put("title", title);
            mapParams.put("inviteBid.id", inviteBidId);
            mapParams.put("bidPrice", singleprice);
            mapParams.put("remarks", note);
            mapParams.put("attachment", urls);
            mapParams.put("productionCycle", handdate);
            mapParams.put("productionCycleUnit", dateType);
            RetrofitFactory.getApiService(this)
                    .saveBid(mapParams)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(BidApplication.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(Response response) {
                            if ("00000".equals(response.getErrorCode())) {
                                //上传图片
                                List<String> photos = new ArrayList<String>();
                                photos.addAll(selectedPhotos);
                                uploadFiles(oss, photos);
                            }
                        }
                    });

        }
    }

    public void uploadFiles(OSSClient oss, List<String> urls) {
        if (null == urls || urls.size() == 0) {
            handler.sendEmptyMessage(0);
            return;
        } // 上传文件
        ossUpload(oss, urls);
    }

    public void ossUpload(final OSSClient oss, final List<String> urls) {
        Log.e("ComeOrder", "图片个数:" + urls.size());
        if (urls.size() <= 0) {
            Log.e("ComeOrder", "通知提醒");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择返回
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            ArrayList<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (selectedPhotos != null) {
                    selectedPhotos.clear();
                }
                if (photos != null) {
                    selectedPhotos.addAll(photos);
                    for (int i = 0; i < selectedPhotos.size(); i++) {
                        urls = urls + "|" + OssService.OSS_URL + "alioss_" + CommonUtil.getFileName(selectedPhotos.get(i) + CommonUtil.getFileLastName(selectedPhotos.get(i)));
                    }

                    urls = urls.substring(1);
                    Log.e("ComeOrder", urls);
                }


                photoAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            title = et_title.getText().toString();
            singleprice = et_price.getText().toString();
            note = et_note.getText().toString();
            handdate = et_hand_date.getText().toString();

            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(singleprice)
                    && TextUtils.isEmpty(note)
                    && TextUtils.isEmpty(urls)
                    && TextUtils.isEmpty(handdate)
                    ) {
                finish();
            } else {
                DialogNotifyUtils.showNotifyDestroyActivity(BidApplication.this, "继续", "放弃", "真的要放弃本次操作吗？");
            }
        }
        return false;
    }


}