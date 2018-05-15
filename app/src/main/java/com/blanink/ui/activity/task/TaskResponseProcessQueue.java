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
import com.blanink.bean.ListProcess;
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
 * 任务 反馈列表
 */
public class TaskResponseProcessQueue extends BaseActivity {

    private static final int BACK_TASK = 0;
    @BindView(R.id.workStepQueue_iv_last)
    TextView workStepQueueIvLast;
    @BindView(R.id.rl_workStepQueue)
    RelativeLayout rlWorkStepQueue;
    @BindView(R.id.rl_task)
    RelativeLayout rlTask;
    @BindView(R.id.workStepQueue_lv_taskQueue)
    ListView workStepQueueLvTaskQueue;
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

    private List<Integer> drawableLists;
    private SharedPreferences sp;
    private CommonAdapter<ListProcess.ResultBean> commonAdapter;
    private List<ListProcess.ResultBean> processFeedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_step_queue);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
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
        workStepQueueIvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskResponseProcessQueue.this, MainActivity.class);
                intent.putExtra("DIRECT", BACK_TASK);
                startActivity(intent);
                finish();
            }
        });
        workStepQueueLvTaskQueue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                if (position > 1&&position < processFeedbackList.size() + 2)


                                                                {
                                                                    Intent intent = null;
                                                                    intent = new Intent(TaskResponseProcessQueue.this, TaskResponseParent.class);
                                                                    intent.putExtra("processType", processFeedbackList.get(position - 2).getType());
                                                                    intent.putExtra("processId", processFeedbackList.get(position - 2).getId());
                                                                    intent.putExtra("name",processFeedbackList.get(position-2).getName());
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }

        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(TaskResponseProcessQueue.this, MainActivity.class);
        intent.putExtra("DIRECT", BACK_TASK);
        startActivity(intent);
    }

    public void loadDatafromServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));

        RetrofitFactory.getApiService(this)
                .listProcess(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListProcess>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(ListProcess listProcess) {
                        llLoad.setVisibility(View.GONE);
                        if (listProcess.getResult().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        }
                        processFeedbackList.addAll(listProcess.getResult());
                        if (commonAdapter == null) {
                            commonAdapter = new CommonAdapter<ListProcess.ResultBean>(TaskResponseProcessQueue.this, processFeedbackList, R.layout.item_task_response_queue) {
                                @Override
                                public void convert(ViewHolder viewHolder, ListProcess.ResultBean processFeedback, int position) {
                                    processFeedback = processFeedbackList.get(position);
                                    TextView tv = viewHolder.getViewById(R.id.tv_process);
                                    TextView tv_state = viewHolder.getViewById(R.id.tv_state);
                                    tv.setText(processFeedback.getName());
                                    if ("3".equals(processFeedback.getType())) {
                                        tv_state.setText("发货");
                                        tv_state.setTextColor(getResources().getColor(R.color.red));
                                        tv.setTextColor(getResources().getColor(R.color.red));
                                    }
                                }
                            };
                            workStepQueueLvTaskQueue.setAdapter(commonAdapter);
                        } else {
                            commonAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @OnClick(R.id.rl_load_fail)
    public void onClick() {
        llLoad.setVisibility(View.VISIBLE);
        rlLoadFail.setVisibility(View.GONE);
        loadDatafromServer();
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
        viewPager.pictureRoll(TaskResponseProcessQueue.this,picLis);

        workStepQueueLvTaskQueue.addHeaderView(viewHeader);
        workStepQueueLvTaskQueue.addHeaderView(viewHeaderStick);

    }
}
