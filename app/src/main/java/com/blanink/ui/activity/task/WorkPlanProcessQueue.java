package com.blanink.ui.activity.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.activity.MainActivity;
import com.blanink.ui.adapter.CommonAdapter;
import com.blanink.ui.adapter.ViewHolder;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.Advertise;
import com.blanink.bean.ProcessFeedback;
import com.blanink.utils.UrlPic;
import com.blanink.view.MyViewPager;


import java.util.ArrayList;
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
 * 任务表
 */
public class WorkPlanProcessQueue extends BaseActivity {


    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.rl_workStepQueue)
    RelativeLayout rlWorkStepQueue;
    @BindView(R.id.workStepQueue_lv_taskQueue)
    ListView workStepQueueLvTaskQueue;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.rl_task)
    RelativeLayout rlTask;
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
    @BindView(R.id.activity_work_plan)
    RelativeLayout activityWorkPlan;
    private List<Integer> drawableLists;
    private SharedPreferences sp;
    private CommonAdapter<ProcessFeedback.Result> commonAdapter;
    private List<ProcessFeedback.Result> processFeedbackList = new ArrayList<>();
    private static final int BACK_TASK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_plan);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        loadDatafromServer();
        addHeaderView();
        workStepQueueLvTaskQueue.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    rlTask.setVisibility(View.VISIBLE);
                } else {
                    rlTask.setVisibility(View.GONE);

                }
            }
        });

        workStepQueueLvTaskQueue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 1&&position < processFeedbackList.size() + 2) {
                    Intent intent = new Intent(WorkPlanProcessQueue.this, WorkPlanParent.class);
                    intent.putExtra("processId", processFeedbackList.get(position - 2).id);
                    intent.putExtra("name",processFeedbackList.get(position-2).name);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.iv_last, R.id.rl_load_fail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_last://返回
                finish();
                break;
            case R.id.rl_load_fail://重新加载
                llLoad.setVisibility(View.VISIBLE);
                rlLoadFail.setVisibility(View.GONE);
                loadDatafromServer();
                break;
        }
    }

    public void loadDatafromServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));

        RetrofitFactory.getApiService(this)
                .workPlanListProcess(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProcessFeedback>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ProcessFeedback processFeedback) {
                        llLoad.setVisibility(View.GONE);
                        if (processFeedback.result.size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        }
                        processFeedbackList.addAll(processFeedback.result);
                        if (commonAdapter == null) {
                            commonAdapter = new CommonAdapter<ProcessFeedback.Result>(WorkPlanProcessQueue.this, processFeedbackList, R.layout.item_work_plan_queue) {
                                @Override
                                public void convert(ViewHolder viewHolder, ProcessFeedback.Result processFeedback, int position) {
                                    processFeedback = processFeedbackList.get(position);
                                    TextView tv = viewHolder.getViewById(R.id.tv_process);
                                    tv.setText(processFeedback.name);
                                }
                            };
                            workStepQueueLvTaskQueue.setAdapter(commonAdapter);
                        } else {
                            commonAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("DIRECT", BACK_TASK);
        startActivity(intent);
    }

    private void addHeaderView() {
        View viewHeader = View.inflate(this, R.layout.layout_process_header, null);
        View viewHeaderStick = View.inflate(this, R.layout.layout_proccess_header_stick, null);
        MyViewPager viewPager = (MyViewPager) viewHeader.findViewById(R.id.myViewPager);
        List<Advertise> picLis = new ArrayList<>();
        picLis.add(new Advertise(UrlPic.pic,UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic2,UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic3,UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic4,UrlPic.baseUrl));

        viewPager.pictureRoll(WorkPlanProcessQueue.this, picLis);
        workStepQueueLvTaskQueue.addHeaderView(viewHeader);
        workStepQueueLvTaskQueue.addHeaderView(viewHeaderStick);

    }

}
