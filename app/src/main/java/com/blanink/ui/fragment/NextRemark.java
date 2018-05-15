package com.blanink.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.lastnext.ReviewDetail;
import com.blanink.ui.adapter.ReviewAdapter;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.AllReview;
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
 * Created by Administrator on 2017/2/9.
 * 下家 评价
 */
public class NextRemark extends Fragment {
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
    private SharedPreferences sp;
    private List<AllReview.ResultBean.RowsBean> reviewList = new ArrayList<>();
    private ReviewAdapter adapter;
    private boolean isHasData = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            if (adapter == null || adapter.getCount() == 0) {
                rlNotData.setVisibility(View.VISIBLE);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    };
    private String companyId;
    private int pageNo = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_remark, null);
        sp = getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        companyId = getActivity().getIntent().getStringExtra("companyB.id");
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadReview();
        //加载更多
        lv.setOnRefreshListener(new UpLoadListView.OnRefreshListener() {
            @Override
            public void onLoadingMore() {
                pageNo++;
                loadReview();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        //评价详情
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReviewDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ReviewDetail", reviewList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void loadReview() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", ""));
        mapParams.put("company.id", companyId);
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(getActivity())
                .allReview(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllReview>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(AllReview allReview) {
                        llLoad.setVisibility(View.GONE);
                        if (allReview.getResult().getTotal() <= reviewList.size()) {
                            isHasData = false;
                        } else {
                            reviewList.addAll(allReview.getResult().getRows());
                            if (adapter == null) {
                                adapter = new ReviewAdapter(getActivity(), reviewList);
                                lv.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }

                        }
                        handler.sendEmptyMessage(0);
                    }
                });
    }

    @OnClick(R.id.rl_load_fail)
    public void onClick() {
        rlLoadFail.setVisibility(View.GONE);
        llLoad.setVisibility(View.VISIBLE);
        loadReview();
    }
}
