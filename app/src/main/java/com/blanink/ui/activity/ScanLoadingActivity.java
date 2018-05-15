package com.blanink.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.task.ListProcessActivity;
import com.blanink.ui.activity.task.ScanDeliverResponse;
import com.blanink.ui.activity.task.ScanTaskResponseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ListProcess;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScanLoadingActivity extends AppCompatActivity {

    private static final String TAG = "ScanLoadingActivity";
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.deliver)
    RelativeLayout deliver;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.rl_result)
    RelativeLayout rlResult;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_loading);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    public static Intent getIntent(Context context, String content) {
        Intent intent = new Intent(context, ScanLoadingActivity.class);
        intent.putExtra("content", content);
        return intent;
    }

    private void initData() {
        final String content = getIntent().getStringExtra("content");
        Log.e(TAG, "掃描返回:" + content);
        if (!content.contains("returnFlag")) {
            rl.setVisibility(View.GONE);
            rlResult.setVisibility(View.VISIBLE);
            return;
        }
        String[] st = content.split("\\?");
        String[] str = st[1].split("&&");
        final String[] strOrderProductId = str[1].split("=");
        String[] returnFlag = str[0].split("=");
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("orderProduct.id", strOrderProductId[1]);
        mapParams.put("returnFlag", returnFlag[1]);
        RetrofitFactory.getApiService(ScanLoadingActivity.this)
                .listProcess(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListProcess>() {
                    @Override
                    public void onCompleted() {
                        rl.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        rl.setVisibility(View.GONE);
                        rlResult.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ListProcess processFeedback) {
                        if (processFeedback.getResult().size() > 1) {
                            startActivity(ListProcessActivity.getIntent(ScanLoadingActivity.this, strOrderProductId[1], new Gson().toJson(processFeedback.getResult())));
                            finish();

                        } else if (processFeedback.getResult().size() == 1) {
                            if ("3".equals(processFeedback.getResult().get(0).getType())) {
                                startActivity(ScanDeliverResponse.getIntent(ScanLoadingActivity.this, strOrderProductId[1], processFeedback.getResult().get(0).getId()));
                            } else {
                                startActivity(ScanTaskResponseActivity.getIntent(ScanLoadingActivity.this, strOrderProductId[1], processFeedback.getResult().get(0).getId()));

                            }
                            finish();
                        } else {
                            rl.setVisibility(View.GONE);
                            rlResult.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
