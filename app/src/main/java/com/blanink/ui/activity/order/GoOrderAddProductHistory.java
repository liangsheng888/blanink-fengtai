package com.blanink.ui.activity.order;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.CommonAdapter;
import com.blanink.ui.adapter.ViewHolder;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.PurchaseProduct;
import com.blanink.utils.GlideUtils;
;

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
 * 去单的来单 采购记录
 */
public class GoOrderAddProductHistory extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.lv)
    ListView lv;
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
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    private SharedPreferences sp;
    private List<PurchaseProduct.ResultBean> purList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_order_add_product_history);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        loadHistory();
        ivLast.setOnClickListener(new View.OnClickListener() {
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
                loadHistory();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rlNotData.setVisibility(View.GONE);
                purList.clear();
                loadHistory();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void loadHistory() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("companyId", sp.getString("COMPANY_ID", null));
        mapParams.put("type", "1");
        mapParams.put("orderProdId", getIntent().getStringExtra("orderProductId"));
        RetrofitFactory.getApiService(this)
                .go_preAddOrder_lists(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PurchaseProduct>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                        rlLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(PurchaseProduct purchaseProduct) {
                        llLoad.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        if (purchaseProduct.getResult().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);
                        }
                        purList.addAll(purchaseProduct.getResult());
                        lv.setAdapter(new CommonAdapter<PurchaseProduct.ResultBean>(GoOrderAddProductHistory.this, purList, R.layout.item_purchase_product) {
                            @Override
                            public void convert(ViewHolder viewHolder, PurchaseProduct.ResultBean resultBean, int position) {
                                resultBean = purList.get(position);
                                TextView tv_product_category = viewHolder.getViewById(R.id.tv_product_category);


                                ImageView iv = viewHolder.getViewById(R.id.iv);
                                TextView tv_time = viewHolder.getViewById(R.id.tv_time);
                                TextView tv_product_name = viewHolder.getViewById(R.id.tv_product_name);
                                TextView tv_price = viewHolder.getViewById(R.id.tv_price);
                                TextView tv_amount = viewHolder.getViewById(R.id.tv_amount);
                                if (resultBean.getCompanyCategory().getCompany() != null&&resultBean.getCompanyCategory().getCompany().getPhoto()!=null&&!resultBean.getCompanyCategory().getCompany().getPhoto().equals("")) {
                                    GlideUtils.glideImageView(GoOrderAddProductHistory.this, iv, resultBean.getCompanyCategory().getCompany().getPhoto(), false);
                                }
                                tv_time.setText(resultBean.getCreateDate());
                                tv_product_name.setText(resultBean.getProductName());
                                tv_amount.setText(resultBean.getAmount() + "");
                                tv_price.setText(resultBean.getPrice());
                                tv_product_category.setText(resultBean.getCompanyCategory().getCompany().getName());
                            }
                        });
                    }
                });
    }
}
