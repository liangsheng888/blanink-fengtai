package com.blanink.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.CustomerActivity;
import com.blanink.ui.activity.task.TaskNotAllocationToResponse;
import com.blanink.ui.activity.task.TaskResponseDeliver;
import com.blanink.ui.adapter.TaskResponseNotAllocationAdapter;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.OrderProduct;
import com.blanink.bean.PartnerInfo;
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

/**
 * Created by Administrator on 2017/3/8.
 * 待分配的任务
 */

public class TaskResponseNotAllocation extends Fragment {
    @BindView(R.id.lv)
    UpLoadListView lv;
    @BindView(R.id.ll_load)
    LinearLayout llLoad;
    @BindView(R.id.rl_load_fail)
    RelativeLayout rlLoadFail;
    @BindView(R.id.rl_not_data)
    RelativeLayout rlNotData;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loading_error_img)
    ImageView loadingErrorImg;
    @BindView(R.id.tv_not)
    TextView tvNot;
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    private LinearLayout ll_header;
    private PopupWindow typeSelectPopup;
    private View view = null;
    private TextView tv_companyName;
    private TextView tv_seek;
    private String processId;
    private SharedPreferences sp;
    private TaskResponseNotAllocationAdapter commonAdapter;
    private List<OrderProduct.ResultBean.RowsBean> list;
    private List<String> companyA;
    private List<String> companyAId;
    private String productName = "";
    private String company = "";
    private String companyId = "";
    private Integer pageNo = 1;
    private boolean isHasData = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            if (commonAdapter != null) {
                commonAdapter.notifyDataSetChanged();
            } else {
                rlNotData.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout_header_order for this fragment
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        receivedData();
        View view = inflater.inflate(R.layout.fragment_my_task, container, false);
        list = new ArrayList<OrderProduct.ResultBean.RowsBean>();
        companyA = new ArrayList<>();
        companyAId = new ArrayList<>();
        companyA.add("客户筛选");
        companyAId.add("");
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        addHeaderView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<list.size()+1){
                if ("3".equals(getActivity().getIntent().getStringExtra("processType"))) {
                    Intent intent = new Intent(getActivity(), TaskResponseDeliver.class);
                    intent.putExtra("processId", processId);
                    if (list.get(position - 1).getACompany() != null) {
                        intent.putExtra("companyId", list.get(position - 1).getACompany().getId());
                    }
                    intent.putExtra("flowId", list.get(position - 1).getRelFlowProcess().getFlow().getId());
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(getActivity(), TaskNotAllocationToResponse.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("TaskDetail", list.get(position - 1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }}
            }
        });
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                refreshData();
            }
        });
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
    }

    private void receivedData() {
        Intent intent = getActivity().getIntent();
        processId = intent.getStringExtra("processId");
        Log.e("TaskResponse", "processId:" + processId + ",userId:" + sp.getString("USER_ID", null));
    }

    public void loadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("process.id", processId);
        mapParams.put("process.productName", productName);
        mapParams.put("process.companyA.id", companyId);
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(getActivity())
                .listTask(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderProduct>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(OrderProduct orderProduct) {
                        llLoad.setVisibility(View.GONE);
                        if (orderProduct.getResult().getTotal() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            if (orderProduct.getResult().getRows().size() == 0) {
                                isHasData = false;
                            } else {
                                list.addAll(orderProduct.getResult().getRows());
                                if (commonAdapter == null) {
                                    commonAdapter = new TaskResponseNotAllocationAdapter(getActivity(), list);
                                    lv.setAdapter(commonAdapter);
                                } else {
                                    commonAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    public void refreshData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("process.id", processId);
        mapParams.put("process.productName", productName);
        mapParams.put("process.companyA.id", companyId);
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(getActivity())
                .listTask(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderProduct>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onNext(OrderProduct orderProduct) {
                        llLoad.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        if (orderProduct.getResult().getTotal() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);
                            isHasData = true;

                        }
                        list.clear();
                        list.addAll(orderProduct.getResult().getRows());
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    @OnClick(R.id.rl_load_fail)//加载失败，点击重新加载
    public void onClick() {
        llLoad.setVisibility(View.VISIBLE);
        rlLoadFail.setVisibility(View.GONE);
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void addHeaderView() {

        view = View.inflate(getActivity(), R.layout.layout_response_category, null);
        ll_header = (LinearLayout) view.findViewById(R.id.ll_header);
        tv_seek = (TextView) view.findViewById(R.id.tv_seek);
        ll_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelectPopup();

                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    typeSelectPopup.showAsDropDown(ll_header, 0, 0);

                    tv_seek.setTextColor(getResources().getColor(R.color.blue_text));
                    Drawable drawableRight = getResources().getDrawable(
                            R.drawable.last_blue);
                    tv_seek.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight,
                            null);
                }
            }
        });
        lv.addHeaderView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initSelectPopup() {
        View view = View.inflate(getActivity(), R.layout.layout_response_filter, null);
        tv_companyName = (TextView) view.findViewById(R.id.tv_companyName);
        companyId = "";
        final EditText etProductName = (EditText) view.findViewById(R.id.et_product_name);
        tv_companyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CustomerActivity.class), 100);
            }
        });
        view.findViewById(R.id.tv_seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeSelectPopup.dismiss();
                rlNotData.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                productName = etProductName.getText().toString().trim();

                if (productName.equals("客户筛选")) {
                    company = "";
                }
                list.clear();
                loadData();
            }
        });
        // 选择完后关闭popup窗口
        typeSelectPopup = new PopupWindow(view, ll_header.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.color.colorWhite);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                typeSelectPopup.dismiss();
                tv_seek.setTextColor(getResources().getColor(R.color.black_text));
                Drawable drawableRight = getResources().getDrawable(
                        R.drawable.last_gray);
                tv_seek.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight,
                        null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == 200) {
            PartnerInfo.ResultBean.CompanyABean companyABean = (PartnerInfo.ResultBean.CompanyABean) data.getSerializableExtra("companyA");
            Log.e("getActivity", companyABean.toString());
            companyId = companyABean.getId();
            tv_companyName.setText(companyABean.getName());
        }
    }
}
