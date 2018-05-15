package com.blanink.ui.activity.lastnext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
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

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.CompanyCateGory;
import com.blanink.utils.GlideUtils;
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

public class ProductMore extends BaseActivity {

    @BindView(R.id.tv_last)
    TextView tvLast;
    @BindView(R.id.rl_customer_manage)
    RelativeLayout rlCustomerManage;
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
    @BindView(R.id.fl_load)
    FrameLayout flLoad;
    SparseArray<View> sparseArray;
    private String content;
    private SharedPreferences sp;
    private int pageNo = 1;
    private Boolean isHasData;
    private CompanyAdapter adapter;
    private List<CompanyCateGory.ResultBean.ListBean> listBean = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv.completeRefresh(isHasData);
            adapter.notifyDataSetChanged();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_more);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        loadData();//
        //load more
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
        //enter detail
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductMore.this, CompanyProductDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ProductDetail", listBean.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.tv_last, R.id.rl_load_fail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_last:
                finish();
                break;
            case R.id.rl_load_fail:
                break;
        }
    }

    public void loadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("office.name", content);
        mapParams.put("companyProduct.productName", content);
        mapParams.put("inviteBid.title", content);
        mapParams.put("financing.title", content);
        mapParams.put("pageNo", pageNo + "");
        RetrofitFactory.getApiService(this)
                .productBigSearch(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompanyCateGory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(CompanyCateGory companyCateGory) {
                        llLoad.setVisibility(View.GONE);
                        if (companyCateGory.getResult().get(1).getCount() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            if (companyCateGory.getResult().get(1).getList().size()==0) {
                                isHasData = false;
                            } else {
                                listBean.addAll(companyCateGory.getResult().get(1).getList());
                                if (adapter == null) {
                                    adapter = new CompanyAdapter();
                                    lv.setAdapter(adapter);//公司
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
    }

    public class CompanyAdapter extends BaseAdapter {
        public CompanyAdapter() {

        }

        @Override
        public int getCount() {
            return listBean.size();
        }

        @Override
        public Object getItem(int position) {
            return listBean.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            sparseArray = new SparseArray<>();
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(ProductMore.this, R.layout.item_product_queue, null);
                viewHolder.iv_product_picture = (ImageView) convertView.findViewById(R.id.iv_product_picture);
                viewHolder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
                viewHolder.tv_down_price = (TextView) convertView.findViewById(R.id.tv_down_price);
                viewHolder.tv_high_price = (TextView) convertView.findViewById(R.id.tv_high_price);
                viewHolder.tv_specific_description = (TextView) convertView.findViewById(R.id.tv_specific_description);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_product_name.setText(listBean.get(position).getCompanyProduct().getProductName());
            viewHolder.tv_specific_description.setText(listBean.get(position).getCompanyProduct().getProductDescription());
            if(listBean.get(position).getCompanyProduct().getProductPhotos()!=null&&listBean.get(position).getCompanyProduct().getProductPhotos().length()>0&&listBean.get(position).getCompanyProduct().getProductPhotos()!=""){
                String[] url = listBean.get(position).getCompanyProduct().getProductPhotos().split(",");
                GlideUtils.glideImageView(ProductMore.this, viewHolder.iv_product_picture, url[0], false);
            }

            return convertView;
        }

    }

    static class ViewHolder {
        ImageView iv_product_picture;
        TextView tv_product_name;
        TextView tv_down_price;//
        TextView tv_high_price;
        TextView tv_specific_description;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
