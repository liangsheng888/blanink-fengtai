package com.blanink.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.adapter.PartnerAdapter;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.PartnerInfo;
import com.blanink.utils.CommonUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CustomerActivity extends BaseActivity {
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_exit)
    TextView tvExit;
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
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_response_search);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    private void initData() {
        getCustomer();
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                getCustomer();
            }
        });
    }

    public void getCustomer() {
        //http://127.0.0.1:8080/blanink-api/processFeedback/getOfficeList?
        // company.id=f541afa391974409ba680e9bd9a394fd&userId=d6c8e9bc4c2b476ba84b962c27882f41
        if (!CommonUtil.isConnected(this)) {
            Toast.makeText(this, "请检查你的网络", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> params = new HashMap<>();

        params.put("companyB.id", sp.getString("COMPANY_ID", null));
        params.put("userId", sp.getString("USER_ID", null));
        RetrofitFactory.getApiService(this)
                .officeList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PartnerInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                       rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(final PartnerInfo partnerInfo) {
                        flLoad.setVisibility(View.GONE);
                        lv.setAdapter(new PartnerAdapter(CustomerActivity.this, partnerInfo.getResult(),"0"));
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                PartnerInfo.ResultBean pr = partnerInfo.getResult().get(position);
                                Intent intent = new Intent();
                                intent.putExtra("companyA", pr.getCompanyA());
                                setResult(200, intent);
                                finish();
                            }
                        });
                    }
                });
    }

}
