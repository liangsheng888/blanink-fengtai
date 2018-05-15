package com.blanink.ui.activity.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 已分配人 完成进度
 */
public class WorkPlanedFinsihProgressQueue extends BaseActivity {

    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.deliver)
    RelativeLayout deliver;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
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
    private SharedPreferences sp;
    private List<FeedBackingTask.ResultBean.ProcessFeedbackListBean> processFeedbackList=new ArrayList<>();
    private CommonAdapter<FeedBackingTask.ResultBean.ProcessFeedbackListBean> commonAdapter;
    FeedBackingTask pf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_planed_finsih_progress_queue);
        ButterKnife.bind(this);
        sp=getSharedPreferences("DATA",MODE_PRIVATE);
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
        loadDatafromServer();
        //刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                processFeedbackList.clear();
                loadDatafromServer();
            }
        });
        //反馈详情
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(WorkPlanedFinsihProgressQueue.this,WorkPlanAllocationDetail.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("ResopnseDetail",processFeedbackList.get(position));
                intent.putExtra("productName",pf.getResult().getProductName());
                intent.putExtra("productCategory",pf.getResult().getCompanyCategory().getName());

                intent.putExtra("productNum",pf.getResult().getAmount());

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    //加载数据
    public void loadDatafromServer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("process.id", getIntent().getStringExtra("processId"));
        mapParams.put("flow.id",getIntent().getStringExtra("flowId"));
        mapParams.put("workPlan.id",getIntent().getStringExtra("workPlanId"));

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
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(FeedBackingTask feedBackingTask) {
                        llLoad.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        swipeRefreshLayout.setRefreshing(false);
                        pf = feedBackingTask;
                        if (pf.getResult().getProcessFeedbackList().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                            tvNot.setText("暂无反馈记录");
                        }
                        Log.e("WorkPlanProcessQueue","解析后:"+ pf.toString());
                        processFeedbackList.addAll(pf.getResult().getProcessFeedbackList());
                        if (commonAdapter == null) {
                            commonAdapter = new CommonAdapter<FeedBackingTask.ResultBean.ProcessFeedbackListBean>(WorkPlanedFinsihProgressQueue.this, processFeedbackList, R.layout.item_work_plan_response) {
                                @Override
                                public void convert(ViewHolder viewHolder, FeedBackingTask.ResultBean.ProcessFeedbackListBean processFeedbackListBean, int position) {
                                    processFeedbackListBean = processFeedbackList.get(position);
                                    TextView name=viewHolder.getViewById(R.id.tv_work_name);
                                    TextView tv_time=viewHolder.getViewById(R.id.tv_time);

                                    TextView tv_achieveAmount=viewHolder.getViewById(R.id.tv_achieveAmount);

                                    TextView tv_bad=viewHolder.getViewById(R.id.tv_bad);
                                    name.setText(processFeedbackListBean.getFeedbackUser().getName());
                                    tv_time.setText(processFeedbackListBean.getCreateDate());
                                    tv_achieveAmount.setText(processFeedbackListBean.getAchieveAmount()+"");
                                    tv_bad.setText(processFeedbackListBean.getFaultAmount()+"");
                                }

                            };
                            lv.setAdapter(commonAdapter);
                        } else {
                            commonAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

}
