package com.blanink.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.http.RetrofitFactory;
import com.blanink.utils.CheckNet;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.OpenFileUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AttachmentDownLoad extends BaseActivity {
    private static String TAG="AttachmentDownLoad";
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.btn_downLoad)
    Button btnDownLoad;
    @BindView(R.id.pb)
    ProgressBar pb;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(AttachmentDownLoad.this, "下载完成", Toast.LENGTH_SHORT).show();
            Intent intent = OpenFileUtils.openFile(fileStoreDir, endName);
            startActivity(intent);
        }
    };
    private String fileStoreDir;
    private String endName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_attachment_down_load);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //返回
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().getStringExtra("url").endsWith(".gif")
                || getIntent().getStringExtra("url").endsWith(".png")
                || getIntent().getStringExtra("url").endsWith(".jpg")
                || getIntent().getStringExtra("url").endsWith(".jpeg")
                || getIntent().getStringExtra("url").endsWith(".bmp")
                || getIntent().getStringExtra("url").endsWith(".GIF")
                || getIntent().getStringExtra("url").endsWith(".PNG")
                || getIntent().getStringExtra("url").endsWith(".JPG")
                || getIntent().getStringExtra("url").endsWith(".JPEG")
                || getIntent().getStringExtra("url").endsWith(".BMP")) {
            {
                Glide.with(AttachmentDownLoad.this).load(getIntent().getStringExtra("url")).placeholder(R.drawable.loading).error(R.drawable.fail).into(ivIcon);
                ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> pic = new ArrayList<String>();
                        pic.add(getIntent().getStringExtra("url"));
                        PhotoPreview.builder().setShowDeleteButton(false).setCurrentItem(0).setPhotos(pic).start(AttachmentDownLoad.this);
                    }
                });
            }

        } else if (getIntent().getStringExtra("url").endsWith(".doc")
                || getIntent().getStringExtra("url").endsWith(".docx")
                || getIntent().getStringExtra("url").endsWith(".DOC")
                || getIntent().getStringExtra("url").endsWith(".DOCX")) {
            ivIcon.setImageResource(R.drawable.word);
        } else if (getIntent().getStringExtra("url").endsWith(".ppt")
                || getIntent().getStringExtra("url").endsWith(".pptx") || getIntent().getStringExtra("url").endsWith(".PPT")
                || getIntent().getStringExtra("url").endsWith(".PPTX")) {

            ivIcon.setImageResource(R.drawable.ppt);
        } else if (getIntent().getStringExtra("url").endsWith(".pdf") || getIntent().getStringExtra("url").endsWith(".PDF")) {
              /*  Glide.with(mContext)
                        .load(new File(Environment.getExternalStorageDirectory(),"pdf.png"))
                       .into(ivIcon);*/
            ivIcon.setImageResource(R.drawable.pdf);

        } else if (getIntent().getStringExtra("url").endsWith(".xls")
                || getIntent().getStringExtra("url").endsWith(".xlsx") || getIntent().getStringExtra("url").endsWith(".XLS")
                || getIntent().getStringExtra("url").endsWith(".XLSX")) {
//                Glide.with(mContext)
//                        .load(new File(Environment.getExternalStorageDirectory(),"xml.png"))
//                        .into(ivIcon);
            ivIcon.setImageResource(R.drawable.excel);


        } else if (getIntent().getStringExtra("url").endsWith(".zip")
                || getIntent().getStringExtra("url").endsWith(".gizp") ||
                getIntent().getStringExtra("url").endsWith(".ZIP")
                || getIntent().getStringExtra("url").endsWith(".GZIP")) {
            ivIcon.setImageResource(R.drawable.zip);

        }else {
            ivIcon.setImageResource(R.drawable.unknow);

        }
        if (!getIntent().getStringExtra("url").equals("") && getIntent().getStringExtra("url") != null) {
            tvName.setText(CommonUtil.getFileName(getIntent().getStringExtra("url")));
        } else {
            tvName.setText("未知类型");
        }

        getFlieLength();
        btnDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDownLoad.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
                downLoad(getIntent().getStringExtra("url"));
            }
        });

    }

    public void getFlieLength() {
        String url = getIntent().getStringExtra("url");
        Log.e(TAG, "" + url);
        Boolean use = CheckNet.checkedURLIsUseful(url);
        if (!use) {
            return;
        }
        OkHttpClient ok = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request re = requestBuilder.method("GET", null).build();
        ok.newCall(re).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final InputStream is = response.body().byteStream();
                final long maxLenth = response.body().contentLength();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnDownLoad.setText("下载(" + CommonUtil.getPrintSize(maxLenth) + ")");
                    }
                });
            }
        });
    }

    public void downLoad(String url) {
        Boolean use = CheckNet.checkedURLIsUseful(url);
        if (!use) {
            Toast.makeText(AttachmentDownLoad.this, "附件地址不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitFactory.getApiService(this)
                .download(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.e(TAG,"總長度:"+responseBody.contentLength());
                        InputStream is=null;
                        fileStoreDir=Environment.getExternalStorageDirectory().getAbsolutePath();
                        endName=System.currentTimeMillis()+CommonUtil.getFileLastName(getIntent().getStringExtra("url"));
                        int currentLength = 0;
                        pb.setMax((int) responseBody.contentLength());
                        FileOutputStream fos=null;
                        File dir = new File(fileStoreDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file=new File(fileStoreDir,endName);
                        try {
                            is=responseBody.byteStream();
                            fos = new FileOutputStream(file);
                            byte[] buffer = new byte[2048];
                            int len = 0;
                            while ((len = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                                currentLength += len;
                                Log.e("OrderAttachment", "当前长度" + currentLength);
                                pb.setProgress(currentLength);
                            }
                            fos.flush();
                            handler.sendEmptyMessage(0);
                        } catch (IOException e) {
                            Log.e(TAG,e.getMessage());

                        }finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG,e.getMessage());
                            }
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG,e.getMessage());
                            }
                        }

                    }
                });

    }

    private void showDialogExit(final String path) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_exit);
        final Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        ((TextView) window.findViewById(R.id.tv_content)).setText("下载成功,去打开");
        ((TextView) window.findViewById(R.id.tv_continue)).setText("不打开");
        ((TextView) window.findViewById(R.id.tv_exit)).setText("打开");

        window.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        window.findViewById(R.id.tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开文件


            }
        });
    }

}
