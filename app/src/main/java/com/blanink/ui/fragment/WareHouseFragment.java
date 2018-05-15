package com.blanink.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.adapter.WareHouseAdapter;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.Stock;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.view.UpLoadListView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/14 0014.
 */
public class WareHouseFragment extends Fragment {
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
    Unbinder unbinder;
    @BindView(R.id.smartRefreshLayout)
    SwipeRefreshLayout smartRefreshLayout;
    private SharedPreferences sp;
    private List<Stock.ResultBean.RowsBean> srr = new ArrayList<>();
    private boolean isHasData = true;
    private WareHouseAdapter wareHouseAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            if (wareHouseAdapter != null) {
                wareHouseAdapter.notifyDataSetChanged();
            }
        }
    };
    private int pageNo = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_stock, null);
        sp = getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initData() {
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
/*

        //刷新
        //设置 Header 为 水波
        WaveSwipeHeader waveSwipeHeader = new WaveSwipeHeader(getActivity());
        waveSwipeHeader.setPrimaryColors(getResources().getColor(R.color.colorOrange));
        waveSwipeHeader.setColorSchemeColors(Color.WHITE, Color.WHITE);
        smartRefreshLayout.setRefreshHeader(waveSwipeHeader);
        smartRefreshLayout.setEnableLoadmore(false);//是否开启加上拉加载功能（默认true）
        smartRefreshLayout.setEnableHeaderTranslationContent(false);//拖动Header的时候是否同时拖动内容（默认true）
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止内容的一切手势操作（默认false）
//设置 Footer 为 球脉冲
        // smartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        //刷新
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNo = 1;
                RefreshData();
            }
        });
    */
        smartRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        smartRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        smartRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        smartRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                RefreshData();
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void DataSycnRefreshEvent(DataSynRefreshEvent event) {
        if (event.getCode() == 3) {
            pageNo = 1;
            RefreshData();
        }

    }

    private void RefreshData() {

        String url = BaseUrlUtils.NET_URL + "companyInventory/list";
        OkHttpClient ok = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("companyId.id", sp.getString("COMPANY_ID", null))
                .add("pageNo", pageNo + "")
                .build();
        final Request request = new Request.Builder().post(body).url(url).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final Stock stock = gson.fromJson(result, Stock.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llLoad.setVisibility(View.GONE);
                        smartRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "已刷新", Toast.LENGTH_SHORT).show();
                        if (stock.getResult().getRows().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);

                            srr.clear();
                            srr.addAll(stock.getResult().getRows());
                            wareHouseAdapter = new WareHouseAdapter(getActivity(), srr);
                            lv.setAdapter(wareHouseAdapter);
                            wareHouseAdapter.notifyDataSetChanged();
                            rlNotData.setVisibility(View.GONE);

                        }


                    }
                });
            }
        });
    }


    private void loadData() {
        String url = BaseUrlUtils.NET_URL + "companyInventory/list";
        OkHttpClient ok = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("companyId.id", sp.getString("COMPANY_ID", null))
                .add("pageNo", pageNo + "")
                .build();
        final Request request = new Request.Builder().post(body).url(url).build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final Stock stock = gson.fromJson(result, Stock.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llLoad.setVisibility(View.GONE);
                        if (stock.getResult().getTotal() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                            tvNot.setText("暂无库存");
                        } else {
                            if (stock.getResult().getRows().size() == 0) {
                                isHasData = false;
                            } else {
                                isHasData = true;
                                srr.addAll(stock.getResult().getRows());
                                if (wareHouseAdapter == null) {
                                    wareHouseAdapter = new WareHouseAdapter(getActivity(), srr);
                                    lv.setAdapter(wareHouseAdapter);
                                }
                            }
                        }
                        handler.sendEmptyMessage(0);

                    }
                });
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
