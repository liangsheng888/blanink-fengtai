package com.blanink.ui.activity.flow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.UsefulFlow;
import com.blanink.view.UpLoadListView;


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
 * 常用流程
 */
public class FlowOftenUse extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.lv)
    UpLoadListView lv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.activity_flow_often_use)
    RelativeLayout activityFlowOftenUse;
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
    private UsefulAdapter adpter;
    private int pageNo = 1;
    private List<UsefulFlow.ResultBean.RowsBean> lists = new ArrayList<>();
    private Boolean isHasData = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (adpter == null) {
                rlNotData.setVisibility(View.VISIBLE);
                tvNot.setText("没有常用流程，请登录电脑版创建常用流程");

            } else {
                lv.completeRefresh(isHasData);
                if (adpter.getCount() == 0) {
                    rlNotData.setVisibility(View.VISIBLE);
                    tvNot.setText("没有常用流程，请登录电脑版创建常用流程");
                }else {
                    rlNotData.setVisibility(View.GONE);
                }
                adpter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_often_use);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        loadUsefulFlow();
        //设置刷新样式
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        //刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo=1;
                RefreshUsefulFlow();
            }
        });
        //加载更多
        lv.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {
            @Override
            public void onLoadingMore() {
                pageNo++;
                loadUsefulFlow();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        //流程详情
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < lists.size()) {
                    Intent intent = new Intent(FlowOftenUse.this, FlowUseTheFlow.class);
                    intent.putExtra("id", lists.get(position).getId());
                    intent.putExtra("orderProductId",getIntent().getStringExtra("orderProductId"));
                    intent.putExtra("flag","isOften");
                    intent.putExtra("isModify",getIntent().getStringExtra("isModify"));
                    startActivity(intent);
                }
            }
        });
    }

    //加载常用流程
    public void loadUsefulFlow() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(this)
                .commonList(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UsefulFlow>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(UsefulFlow usefulFlow) {
                        llLoad.setVisibility(View.GONE);
                        if (lists.size() >= usefulFlow.getResult().getTotal()) {
                            isHasData = false;
                        } else {
                            lists.addAll(usefulFlow.getResult().getRows());
                            if (adpter == null) {
                                adpter = new UsefulAdapter();
                                lv.setAdapter(adpter);
                            } else {
                                adpter.notifyDataSetChanged();
                            }
                            Log.e("FlowOftenUse", usefulFlow.toString());
                        }
                        handler.sendEmptyMessage(0);//通知界面更新
                    }
                });
    }

    public void RefreshUsefulFlow() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(this)
                .commonList(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UsefulFlow>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(UsefulFlow usefulFlow) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (usefulFlow.getResult().getTotal() <= lists.size()) {
                            Toast.makeText(FlowOftenUse.this, "没有刷新出来数据", Toast.LENGTH_SHORT).show();
                        } else {
                            lists.clear();
                            lists.addAll(0, usefulFlow.getResult().getRows());
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_last, R.id.rl_load_fail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_last://返回
                finish();
                break;
            case R.id.rl_load_fail://加载失败
                llLoad.setVisibility(View.VISIBLE);
                rlLoadFail.setVisibility(View.GONE);
                loadUsefulFlow();
                break;
        }
    }

    public class UsefulAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(FlowOftenUse.this, R.layout.item_use_flow, null);
                viewHolder.tv_flow_name = ((TextView) convertView.findViewById(R.id.tv_flow_name));
                viewHolder.tv_flow_note = ((TextView) convertView.findViewById(R.id.tv_flow_note));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = ((ViewHolder) convertView.getTag());
            }
            viewHolder.tv_flow_name.setText(lists.get(position).getName());
            viewHolder.tv_flow_note.setText(lists.get(position).getRemarks());
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_flow_name;
        TextView tv_flow_note;
    }
}
