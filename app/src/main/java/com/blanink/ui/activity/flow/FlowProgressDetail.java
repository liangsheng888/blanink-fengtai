package com.blanink.ui.activity.flow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.bean.FlowProgress;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.activity.MyApplication;
import com.blanink.ui.adapter.FlowProgressAdapter;
import com.blanink.ui.jshare.uitool.ShareBoard;
import com.blanink.ui.jshare.uitool.ShareBoardlistener;
import com.blanink.ui.jshare.uitool.SnsPlatform;
import com.blanink.utils.BaseUrlUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.utils.BitmapUtil;
import cn.jiguang.share.android.utils.Logger;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatFavorite;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jiguang.share.weibo.SinaWeiboMessage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 流程进度
 */
public class FlowProgressDetail extends BaseActivity {

    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order)
    RelativeLayout comeOrder;
    @BindView(R.id.lv)
    ListView lv;
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
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_not_net)
    TextView tvNotNet;
    @BindView(R.id.rl_not_net)
    RelativeLayout rlNotNet;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_progress_detail);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);

        initData();
    }

    private void initData() {
        postAsynOkHttp();
        //
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                postAsynOkHttp();
            }
        });
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
     tvShare.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

         }
     });
    }

    public void postAsynOkHttp() {
        String url = "";
        OkHttpClient okHttpClient = new OkHttpClient();

        if ("FLOW_SORTED".equals(getIntent().getStringExtra("flag"))) {
            url = BaseUrlUtils.NET_URL + "flow/getOwnCompanyProgress";
        } else {
            url = BaseUrlUtils.NET_URL + "flow/getProgress";
        }

        FormBody formBody = new FormBody.Builder()
                .add("id", getIntent().getStringExtra("flowId"))
                .add("currentUser.id", sp.getString("USER_ID", null))
                .add("notOwnCompany", getIntent().getStringExtra("notOwnCompany") == null ? "" : getIntent().getStringExtra("notOwnCompany"))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Log.e("FlowProgress", url + "?id=" + getIntent().getStringExtra("flowId") + "&currentUser.id=" + sp.getString("USER_ID", null));
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);

                    }

                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Gson gson = new Gson();
                final FlowProgress flowProgress = gson.fromJson(str, FlowProgress.class);
                Log.e("FlowProgress", flowProgress.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("00000".equals(flowProgress.getErrorCode())) {
                            llLoad.setVisibility(View.GONE);
                            if (flowProgress.getResult().size() == 0) {
                                rlNotData.setVisibility(View.VISIBLE);
                                tvNot.setText("流程暂无进度");
                            } else {
                                rlNotData.setVisibility(View.GONE);

                            }
                            lv.setAdapter(new FlowProgressAdapter(FlowProgressDetail.this, flowProgress.getResult()));
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()

                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    Intent intent = new Intent(FlowProgressDetail.this, FlowProgressDetailHistory.class);
                                    intent.putExtra("flowId", flowProgress.getResult().get(position).getFlow().getId());
                                    intent.putExtra("processId", flowProgress.getResult().get(position).getProcess().getId());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            llLoad.setVisibility(View.GONE);
                            Toast.makeText(FlowProgressDetail.this, "加载失败", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });
    }


}
