package com.blanink.ui.activity.flow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.CommonAdapter;
import com.blanink.ui.adapter.ViewHolder;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.FlowRemark;
import com.blanink.bean.Response;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.view.KeyMapDialogFlow;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 流程 留言
 */
public class FlowComments extends BaseActivity {

    @BindView(R.id.tv_last)
    TextView tvLast;
    @BindView(R.id.rl_after_sale_demand)
    RelativeLayout rlAfterSaleDemand;
    @BindView(R.id.et_message)
    TextView etMessage;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.lv_comments)
    ListView lvComments;
    @BindView(R.id.rl_not_data)
    RelativeLayout rlNotData;
    KeyMapDialogFlow dialog;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_comments);
        sp=getSharedPreferences("DATA",MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        loadComments(getIntent().getStringExtra("flowId"));
        //弹出发送按钮
        etMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new KeyMapDialogFlow("发表评论：", new KeyMapDialogFlow.SendBackListener() {
                    @Override
                    public void sendBack(String inputText) {
                        sendComments(inputText);
                    }

                });

                dialog.show(getSupportFragmentManager(), "dialog");
            }


        });
    }

    public void sendComments(String remarks) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("flowRemark.flow.id", getIntent().getStringExtra("flowId"));
        mapParams.put("flowRemark.remarks", remarks);
        RetrofitFactory.getApiService(this)
                .saveFlowRemark(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(Response response) {
                        if ("00000".equals(response.getErrorCode())) {
                            dialog.hideProgressdialog();
                            dialog.dismiss();
                            loadComments(getIntent().getStringExtra("flowId"));
                        }
                    }
                });
    }

    public void loadComments(String id) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("id", id);
        RetrofitFactory.getApiService(this)
                .getFlowRemark(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FlowRemark>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final FlowRemark flowRemark) {
                        if (flowRemark.getResult().getRemaksList().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);
                        }
                        lvComments.setAdapter(new CommonAdapter<FlowRemark.ResultBean.RemaksListBean>(FlowComments.this, flowRemark.getResult().getRemaksList(), R.layout.item_order_product_remark) {
                            @Override
                            public void convert(ViewHolder viewHolder, FlowRemark.ResultBean.RemaksListBean resultBean, int position) {
                                resultBean = flowRemark.getResult().getRemaksList().get(position);
                                ImageView iv = viewHolder.getViewById(R.id.iv_photo);
                                TextView tv_company = viewHolder.getViewById(R.id.tv_company);
                                TextView tv_user = viewHolder.getViewById(R.id.tv_user);
                                TextView tv_time = viewHolder.getViewById(R.id.tv_time);
                                TextView tv_message = viewHolder.getViewById(R.id.tv_message);
                                tv_company.setText(resultBean.getCreateBy().getCompany().getName());
                                tv_user.setText(resultBean.getCreateBy().getName());
                                tv_time.setText(DateUtils.format(DateUtils.stringToDate(resultBean.getCreateDate())));
                                tv_message.setText(resultBean.getRemarks());
                                GlideUtils.glideImageView(FlowComments.this, iv, resultBean.getCreateBy().getCompany().getPhoto(), true);
//                        if(sp.getString("USER_ID",null).equals(resultBean.getCreateBy().getId())){
//                            tv_message.setTextColor(getResources().getColor(R.color.colorAccent));
//                        }
                            }
                        });
                        if (flowRemark.getResult().getRemaksList().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);
                        }
                    }
                });
    }

}
