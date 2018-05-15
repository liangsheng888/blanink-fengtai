package com.blanink.ui.activity.flow;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.RelFlow;
import com.blanink.utils.PriorityUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 查看 工序
 */
public class FlowSeekProcessDetail extends BaseActivity {

    private static final String TAG ="FlowSeekProcessDetail" ;
    @BindView(R.id.tv_set)
    TextView tvSet;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_priority)
    TextView tvPriority;
    @BindView(R.id.tv_isPublic)
    TextView tvIsPublic;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.ll)
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_seek_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        loadProcess(getIntent().getStringExtra("flowId"),getIntent().getStringExtra("processId"));
    }

    private void loadProcess(String flowId,String processId) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("flow.id",flowId);
        mapParams.put("process.id",processId);
        RetrofitFactory.getApiService(this)
                .getRelFlowProcess(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RelFlow>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RelFlow relFlow) {
                        tvAmount.setText(relFlow.getResult().getTarget()+"");
                        tvIsPublic.setText(relFlow.getResult().getIsPublic().equals("0")?"公开":"不公开");
                        tvPriority.setText(PriorityUtils.getPriority(relFlow.getResult().getProcessPriority()));
                        tvNote.setText(relFlow.getResult().getRemarks());
                    }
                });
    }
}
