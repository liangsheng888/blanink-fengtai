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
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.PartnerActivity;
import com.blanink.ui.activity.order.GoDownOrderProduct;
import com.blanink.ui.activity.order.GoDownOrderProductDetail;
import com.blanink.ui.adapter.DashboardPurchaseAdapter;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.Dashboard;
import com.blanink.bean.PartnerInfo;
import com.blanink.utils.CommonUtil;
import com.blanink.view.MySwitchButton;
import com.blanink.view.UpLoadListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：Created by liangshen on 2018/1/16 0016 09:33
 * 邮箱：liangshen6868@163.com
 */
public class GoOrderDashBoardFragment extends Fragment {
    @BindView(R.id.lv)
    UpLoadListView lv;
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
    @BindView(R.id.tv_not_net)
    TextView tvNotNet;
    @BindView(R.id.rl_not_net)
    RelativeLayout rlNotNet;
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    private int pageNo = 1;
    private boolean isHasData = true;
    private SharedPreferences sp;
    private DashboardPurchaseAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            if (adapter != null) {
                Log.e("ComeOrderActivity", "界面刷新了");
                adapter.notifyDataSetChanged();
            } else {
                rlNotData.setVisibility(View.VISIBLE);
                tvNot.setText("暂无可排订单");
            }
        }
    };
    private List<Dashboard.ResultBean.RowsBean> currentRowsList;
    private String companyId = "";
    private View view;
    private LinearLayout ll_header;
    private PopupWindow typeSelectPopup;
    private TextView tv_companyName;
    private TextView tv_seek;
    private String productName = "";
    private String shipped="";
    private String companyName="供应商筛选";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_come_order, null);
        ButterKnife.bind(this, view);
        sp = getActivity().getSharedPreferences("DATA", getActivity().MODE_PRIVATE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        currentRowsList = new ArrayList<>();
        loadData();
        addHeaderView();
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
                loadDataRefresh();
            }
        });
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llLoad.setVisibility(View.VISIBLE);
                rlLoadFail.setVisibility(View.GONE);
                loadData();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<currentRowsList.size()+1){
                Intent intent = new Intent(getActivity(), GoDownOrderProductDetail.class);
                intent.putExtra("orderProductId", currentRowsList.get(position-1).getId());
                intent.putExtra("orderId", currentRowsList.get(position-1).getOrder().getId());
                startActivity(intent);
            }}
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

    private void loadDataRefresh() {
        if (!CommonUtil.isConnected(getActivity())) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("aCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("bCompany.id", companyId);
        mapParams.put("productName", productName);
        mapParams.put("shipped",shipped);
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(getActivity())
                .listSupplierOrderProductStatusJson(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Dashboard>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onNext(Dashboard dashboard) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (dashboard.getResult().getTotal() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);

                        }
                        isHasData = true;
                        currentRowsList.clear();
                        currentRowsList.addAll(dashboard.getResult().getRows());
                        handler.sendEmptyMessage(0);//通知ui界面更新
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    //访问服务器
    public void loadData() {
        if (!CommonUtil.isConnected(getActivity())) {
            llLoad.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "请检查你的网络！", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));

        mapParams.put("aCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("bCompany.id", companyId);
        mapParams.put("productName", productName);
        mapParams.put("shipped",shipped);
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(getActivity())
                .listSupplierOrderProductStatusJson(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Dashboard>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("okhttp", e.getMessage());
                        pageNo--;
                        rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Dashboard dashboard) {
                        llLoad.setVisibility(View.GONE);
                        if (dashboard.getResult().getTotal() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        }
                        if (dashboard.getResult().getRows().size() == 0) {
                            pageNo--;
                            isHasData = false;
                        } else {
                            currentRowsList.addAll(dashboard.getResult().getRows());
                            if (adapter == null) {
                                adapter = new DashboardPurchaseAdapter(getActivity(), currentRowsList);
                                lv.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }

                        }
                        handler.sendEmptyMessage(0);//通知ui界面更新
                    }
                });

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == 200) {
            PartnerInfo.ResultBean.CompanyBBean companyBBean = (PartnerInfo.ResultBean.CompanyBBean) data.getSerializableExtra("companyB");
            Log.e("getActivity", companyBBean.toString());
            companyId = companyBBean.getId();
            companyName=companyBBean.getName();
            tv_companyName.setText(companyBBean.getName());
        }
    }

    private void initSelectPopup() {
        View view = View.inflate(getActivity(), R.layout.layout_dashboard_filter, null);
        tv_companyName = (TextView) view.findViewById(R.id.tv_companyName);
        final EditText etProductName = (EditText) view.findViewById(R.id.et_product_name);
        TextView company=  (TextView) view.findViewById(R.id.company);

        final MySwitchButton sbFinish=(MySwitchButton)view.findViewById(R.id.esb_finish);
        tv_companyName.setText(companyName);
        company.setText("供应商");
        if("Y".equals(shipped)){
            sbFinish.openSwitch();
        }else {
            sbFinish.closeSwitch();
        }
        sbFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sbFinish.isSwitchOpen()){
                    sbFinish.closeSwitch();
                    shipped="N";
                }else{
                    sbFinish.openSwitch();
                    shipped="Y";
                }
            }
        });
        tv_companyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PartnerActivity.class), 100);
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
                    companyId = "";
                }
                currentRowsList.clear();
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

}
