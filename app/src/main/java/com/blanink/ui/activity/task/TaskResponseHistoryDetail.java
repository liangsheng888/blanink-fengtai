package com.blanink.ui.activity.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.AttachmentBrow;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.FeedPrcessDetail;
import com.blanink.utils.StringToListUtils;
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
 * 历史分配详情
 */
public class TaskResponseHistoryDetail extends BaseActivity {

    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order)
    RelativeLayout comeOrder;
    @BindView(R.id.tv_pro_category)
    TextView tvProCategory;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.order_item_ll2)
    RelativeLayout orderItemLl2;
    @BindView(R.id.order_item_ll2_guigeName)
    TextView orderItemLl2GuigeName;
    @BindView(R.id.tv_pro_name)
    TextView tvProName;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.task)
    TextView task;
    @BindView(R.id.tv_target)
    TextView tvTarget;
    @BindView(R.id.response)
    TextView response;
    @BindView(R.id.tv_response)
    TextView tvResponse;
    @BindView(R.id.tv_my_task)
    TextView tvMyTask;
    @BindView(R.id.bad_num)
    TextView badNum;
    @BindView(R.id.attactment)
    TextView attactment;
    @BindView(R.id.tv_attactment)
    TextView tvAttactment;
    @BindView(R.id.rl_down)
    RelativeLayout rlDown;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.tv_note)
    TextView tvNote;
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
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.time)
    TextView time;
    private SharedPreferences sp;
    FeedPrcessDetail feed;
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_response_history_detail);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    private void initData() {
        loadResponseDetail();
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                loadResponseDetail();
            }
        });
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void loadResponseDetail() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", getIntent().getStringExtra("id"));

        RetrofitFactory.getApiService(this)
                .viewOneHistoryTask(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedPrcessDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(FeedPrcessDetail feedPrcessDetail) {
                        flLoad.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        feed =feedPrcessDetail;
                        tvTarget.setText(feed.getResult().getTarget() + "");
                        tvResponse.setText(feed.getResult().getAchieveAmount() + "");
                        badNum.setText(feed.getResult().getFaultAmount() + "");
                        tvNote.setText(feed.getResult().getRemarks());
                        tvTime.setText(feed.getResult().getCreateDate());
                        tvProName.setText(getIntent().getStringExtra("productName"));
                        tvProCategory.setText(getIntent().getStringExtra("productCategory"));
                        tvNum.setText(getIntent().getStringExtra("productNum"));
                        List<String> arrayList = null;
                        if (feed.getResult().getFeedbackAttachmentStr() != null && feed.getResult().getFeedbackAttachmentStr() != "" && !"".equals(feed.getResult().getFeedbackAttachmentStr())) {
                            arrayList = StringToListUtils.stringToList(feed.getResult().getFeedbackAttachmentStr(), ",");
                        } else {
                            arrayList = new ArrayList<>();
                        }

                        final List<String> finalArrayList = arrayList;
                        if (finalArrayList.size() == 0) {
                            tvAttactment.setTextColor(Color.GRAY);
                            tvAttactment.setText("无附件");
                            tvAttactment.setBackground(getResources().getDrawable(R.drawable.corner__gray));
                        } else {
                            tvAttactment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TaskResponseHistoryDetail.this, AttachmentBrow.class);
                                    intent.putExtra("imageList", new Gson().toJson(finalArrayList));
                                    startActivity(intent);
                                }
                            });
                        }

                    }
                });
    }
}
