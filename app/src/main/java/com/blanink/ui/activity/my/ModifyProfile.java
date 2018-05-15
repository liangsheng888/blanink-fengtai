package com.blanink.ui.activity.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.oss.OssService;
import com.blanink.bean.LoginResult;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.GlideUtils;


import java.io.File;
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
 * 修改个人资料
 */
public class ModifyProfile extends BaseActivity {
    private static final int REQUEST_CODE = 2;
    private static final String TAG ="ModifyProfile" ;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    private LoginResult loginResult;
    private SharedPreferences sp;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.tender_manager)
    RelativeLayout tenderManager;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_phone)
    EditText etPhone;
    private String name;
    private String psd;
    private String phone="";
    private String photo="";
    String path;
    OSSClient oss;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showNotify();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        receivedData();
        ButterKnife.bind(this);
        initData();
    }

    private void receivedData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loginResult = (LoginResult) bundle.getSerializable("MyProfile");
    }

    private void initData() {

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(OssService.AccessKey, OssService.AccessKeySecret);
        oss = new OSSClient(getApplicationContext(), OssService.endpoint, credentialProvider);
        etNick.setText(loginResult.getResult().name);
        etPhone.setText(loginResult.getResult().phone);
        if (loginResult.getResult().photo != null && !loginResult.getResult().photo .equals("")) {
            GlideUtils.glideImageView(ModifyProfile.this, ivPhoto, loginResult.getResult().photo, true);
        }
        //返回
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etNick.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                Log.e("ModifyProfile", name + "------" + phone);
                DialogLoadUtils.getInstance(ModifyProfile.this);
                DialogLoadUtils.showDialogLoad("操作进行中...");
                uploadDataToServer();
            }
        });
        //设置头像
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        //设置选择个数
                        .setPhotoCount(1)
                        //选择界面第一个显示拍照按钮
                        .setShowCamera(true)
                        //选择时点击图片放大浏览
                        .setPreviewEnabled(false)
                        //附带已经选中过的图片
                        .start(ModifyProfile.this);
            }
        });
    }

    //上传数据
    private void uploadDataToServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", sp.getString("USER_ID", null));
        mapParams.put("name", name);
        mapParams.put("phone", phone);
        mapParams.put("photo", photo);
        RetrofitFactory.getApiService(this)
                .userUpdate(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(ModifyProfile.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        DialogLoadUtils.dismissDialog();
                        if (loginResult.getErrorCode().equals("00000")) {
                            //上传图片到阿里云服务器
                            oss.asyncDeleteObject(new DeleteObjectRequest("blanink", loginResult.getResult().photo), new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
                                @Override
                                public void onSuccess(DeleteObjectRequest deleteObjectRequest, DeleteObjectResult deleteObjectResult) {
                                    Log.d(TAG, "DeleteSuccess");
                                    PutObjectRequest put = new PutObjectRequest("blanink", CommonUtil.getFileName(path), path);

                                    OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                                        @Override
                                        public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                            Log.d(TAG, result.toString());

                                            handler.sendEmptyMessage(0);


                                        }

                                        @Override
                                        public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                            // 请求异常
                                            if (clientExcepion != null) {
                                                // 本地异常如网络异常等
                                                clientExcepion.printStackTrace();
                                            }
                                            if (serviceException != null) {
                                                // 服务异常
                                                Log.e(TAG, "ErrorCode:" + serviceException.getErrorCode());
                                                Log.e(TAG, "RequestId:" + serviceException.getRequestId());
                                                Log.e(TAG, "HostId:" + serviceException.getHostId());
                                                Log.e(TAG, "RawMessage:" + serviceException.getRawMessage());
                                            }
                                        }
                                    });
                                    //http://blanink.oss-cn-hangzhou.aliyuncs.com/com_blanink_96x96
                                }

                                @Override
                                public void onFailure(DeleteObjectRequest deleteObjectRequest, ClientException e, ServiceException e1) {
                                    Log.d(TAG, e1.toString());
                                    Log.d(TAG, e.toString());
                                    Log.d(TAG, deleteObjectRequest.toString());

                                }
                            });



                            //弹出对话框

                        } else {
                            Toast.makeText(ModifyProfile.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void showNotify() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ModifyProfile.this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_success);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        Display d = getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
        //  lp.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        window.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择返回
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                path = Uri.fromFile(new File(photos.get(0))).getPath();
                photo = OssService.OSS_URL + CommonUtil.getFileName(path);
                Log.e(TAG,""+photo);
                ivPhoto.setImageBitmap(BitmapFactory.decodeFile(path));
            }

        }
    }
}

