package com.blanink.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.task.WorkPlanedFinsihProgressQueue;
import com.blanink.ui.adapter.CommonAdapter;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.WorkPlaned;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.GlideUtils;
import com.blanink.view.UpLoadListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


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

/**
 * Created by Administrator on 2017/3/16.
 * 任务分配 已分配
 */

public class WorkPlanAllocation extends Fragment {
    @BindView(R.id.lv)
    UpLoadListView lv;
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
    private String processId;
    private SharedPreferences sp;
    private CommonAdapter<WorkPlaned.ResultBean.Rows> commonAdapter;
    private List<WorkPlaned.ResultBean.Rows> list = new ArrayList<WorkPlaned.ResultBean.Rows>();
    private int pageNo = 1;
    private boolean isHasData = true;
    private String startDate = "";
    private String endDate = "";
    private SparseArray<View> sparseArray;
    private static final String TAG = "WorkPlanAllocation";
    private MyAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (adapter == null) {
                rlNotData.setVisibility(View.VISIBLE);
            } else {
                lv.completeRefresh(isHasData);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");

        View view = null;
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.work_plan_allocation, container, false);
        }
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        receivedData();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataRefreshEvent(DataSynRefreshEvent event) {
        if (event.getCode() == 0) {
            Log.e(TAG, "刷新数据");
            pageNo = 1;
            refresh();
        }


    }

    private void receivedData() {
        Intent intent = getActivity().getIntent();
        processId = intent.getStringExtra("processId");
        Log.e("WorkPlaned", "processId:" + processId + ",userId:" + sp.getString("USER_ID", null));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        lv.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {
            @Override
            public void onLoadingMore() {
                pageNo++;
                loadData();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        //分配详情
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < list.size()) {//防止出现下标越界
                    WorkPlaned.ResultBean.Rows rows = list.get(position);
                    Intent intent = new Intent(getActivity(), WorkPlanedFinsihProgressQueue.class);
                    intent.putExtra("processId", rows.getRelFlowProcess().getProcess().getId());
                    intent.putExtra("flowId", rows.getRelFlowProcess().getFlow().getId());
                    intent.putExtra("workPlanId", rows.getWorkPlan().getId());
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.rl_load_fail)
    public void onClick() {
        llLoad.setVisibility(View.VISIBLE);
        rlLoadFail.setVisibility(View.GONE);
        loadData();
    }

    public void refresh() {
        {
            Map<String, Object> mapParams = new HashMap<String, Object>();
            mapParams.put("userId", sp.getString("USER_ID", null));
            mapParams.put("process.id", processId);
            mapParams.put("pageNo", pageNo + "");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
            RetrofitFactory.getApiService(getActivity())
                    .searchWorkPlan(mapParams)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<WorkPlaned>() {
                        @Override
                        public void onCompleted() {
                            
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(WorkPlaned workPlaned) {
                            llLoad.setVisibility(View.GONE);
                            if (workPlaned.getResult().rows.size() == 0) {
                                rlNotData.setVisibility(View.VISIBLE);
                            } else {
                                rlNotData.setVisibility(View.GONE);
                                list.clear();
                                list.addAll(workPlaned.getResult().rows);
                                lv.setAdapter(new MyAdapter());
                                adapter.notifyDataSetChanged();
                                Log.e(TAG, "刷新完毕");
                            }
                        }
                    });
        }
    }

    public void loadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("process.id", processId);
        mapParams.put("pageNo", pageNo + "");
        mapParams.put("startDate", startDate);
        mapParams.put("endDate", endDate);
        RetrofitFactory.getApiService(getActivity())
                .searchWorkPlan(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WorkPlaned>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(WorkPlaned workPlaned) {
                        llLoad.setVisibility(View.GONE);
                        if (workPlaned.getResult().total <= list.size()) {
                            isHasData = false;
                        } else {
                            list.addAll(workPlaned.getResult().rows);
                            if (adapter == null) {
                                adapter = new MyAdapter();
                                lv.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                        handler.sendEmptyMessage(0);//通知界面更新
                    }
                });
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WorkPlaned.ResultBean.Rows rows = list.get(position);
            ViewHolder viewHolder = null;
            sparseArray = new SparseArray<>();
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.item_work_plan_allocated, null);
                viewHolder.tv_companyName = (TextView) convertView.findViewById(R.id.tv_companyName);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_take_order_time);
                viewHolder.tv_master = (TextView) convertView.findViewById(R.id.tv_master);
                viewHolder.tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);
                viewHolder.tv_pro_category = (TextView) convertView.findViewById(R.id.tv_pro_category);
                viewHolder.tv_woker = (TextView) convertView.findViewById(R.id.tv_woker);
                viewHolder.tv_finish = (TextView) convertView.findViewById(R.id.tv_finish);
                viewHolder.iv=(ImageView)convertView.findViewById(R.id.iv);
                viewHolder.iv_photo=(ImageView)convertView.findViewById(R.id.iv_photo);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv_companyName.setText((rows.getCompanyA().getShortName()==null?rows.getCompanyA().getName():rows.getCompanyA().getShortName()));
            viewHolder.tv_time.setText(CommonUtil.dateToString(CommonUtil.stringToDate(rows.getWorkPlan().getCreateDate())));
            //viewHolder.tv_master.setText(rows.getCompanyBOwner().getName());
            viewHolder.tv_pro_name.setText(rows.getProductName());
            viewHolder.tv_pro_category.setText(rows.getCompanyCategory().getName());
            viewHolder.tv_woker.setText(rows.getWorkPlan().getWorker().getName());//分配人
            viewHolder.tv_finish.setText((rows.getWorkPlan().getFinishAmount()==null?"0":rows.getWorkPlan().getFinishAmount()) + "/" + rows.getWorkPlan().getAchieveAmount());
            if(rows.getWorkPlan().getAchieveAmount().equals((rows.getWorkPlan().getFinishAmount()==null?"0":rows.getWorkPlan().getFinishAmount()) )
                    ||Integer.parseInt((rows.getWorkPlan().getFinishAmount()==null?"0":rows.getWorkPlan().getFinishAmount()) )>=Integer.parseInt(rows.getWorkPlan().getAchieveAmount())){
                viewHolder.iv.setVisibility(View.VISIBLE);
            }
            if (rows.getCompanyA().getPhoto() != null && !"".equals(rows.getCompanyA().getPhoto())) {
                GlideUtils.glideImageView(getActivity(), viewHolder.iv_photo, rows.getCompanyA().getPhoto(), true);
            }
            return convertView;
        }

    }

    static class ViewHolder {
        TextView tv_companyName;
        TextView tv_time;
        TextView tv_master;
        TextView tv_pro_name;
        TextView tv_pro_category;
        TextView tv_woker;
        TextView tv_finish;
        ImageView iv;
        ImageView iv_photo;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);//解除订阅
    }
}
