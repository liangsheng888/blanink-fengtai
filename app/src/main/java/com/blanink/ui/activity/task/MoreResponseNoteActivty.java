package com.blanink.ui.activity.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.CommonAdapter;
import com.blanink.ui.adapter.ViewHolder;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.FeedBackingTask;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 更多反馈记录
 */
public class MoreResponseNoteActivty extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.task_response_rl)
    RelativeLayout taskResponseRl;
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
    @BindView(R.id.activity_more_response_note_activty)
    RelativeLayout activityMoreResponseNoteActivty;
    private SharedPreferences sp;
    private String processId;
    private String flowId;
    private CommonAdapter<FeedBackingTask.ResultBean.ProcessFeedbackListBean> commonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_response_note_activty);
        sp=getSharedPreferences("DATA",MODE_PRIVATE);
        ButterKnife.bind(this);
        receiveData();
        initData();
    }

    private void receiveData() {
        Intent intent=getIntent();
        processId = intent.getStringExtra("process.id");
        flowId = intent.getStringExtra("flow.id");
    }

    private void initData() {
        loadData();

    }

    @OnClick({R.id.iv_last, R.id.rl_load_fail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_last:
                finish();
                break;
            case R.id.rl_load_fail:
                break;
        }
    }

    public void loadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("process.id", processId);
        mapParams.put("flow.id", flowId);

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
                    }

                    @Override
                    public void onNext(final FeedBackingTask feedbackingTask) {
                        llLoad.setVisibility(View.GONE);
                        Log.e("TaskResponse", "我反馈过的2:" + feedbackingTask.toString());
                        commonAdapter = new CommonAdapter<FeedBackingTask.ResultBean.ProcessFeedbackListBean>(MoreResponseNoteActivty.this, feedbackingTask.getResult().getProcessFeedbackList(), R.layout.item_history_note_response) {
                            @Override
                            public void convert(ViewHolder viewHolder,FeedBackingTask.ResultBean.ProcessFeedbackListBean processFeedbackUser, int position) {
                                processFeedbackUser = feedbackingTask.getResult().getProcessFeedbackList().get(position);
                                TextView date = viewHolder.getViewById(R.id.tv_date);
                                TextView tv_master = viewHolder.getViewById(R.id.tv_master);
                                TextView tv_finished = viewHolder.getViewById(R.id.tv_finished);
                                TextView tv_bad = viewHolder.getViewById(R.id.tv_bad);
                                tv_master.setText(processFeedbackUser.getFeedbackUser().getName());
                                date.setText(feedbackingTask.getResult().getProcessFeedbackList().get(position).getCreateDate());
                                tv_finished.setText((feedbackingTask.getResult().getProcessFeedbackList().get(position).getAchieveAmount() + "个"));
                                tv_bad.setText((feedbackingTask.getResult().getProcessFeedbackList().get(position).getFaultAmount() + "个"));
                            }
                        };
                        lv.setAdapter(commonAdapter);
                    }
                });

    }

}
