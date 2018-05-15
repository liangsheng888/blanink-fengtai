package com.blanink.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.bidtender.BidMore;
import com.blanink.ui.activity.bidtender.TenderDetail;
import com.blanink.ui.activity.fincying.FincyingMore;
import com.blanink.ui.activity.lastnext.CompanyDetail;
import com.blanink.ui.activity.lastnext.CompanyMore;
import com.blanink.ui.activity.lastnext.CompanyProductDetail;
import com.blanink.ui.activity.lastnext.LastCustomerDetail;
import com.blanink.ui.activity.lastnext.NextFamilyManageCompanyDetail;
import com.blanink.ui.activity.lastnext.ProductMore;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.BigSeekContent;
import com.blanink.view.NoScrollListview;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 搜索界面
 */
public class SeekActivity extends BaseActivity {


    private static final int SEEK = 1;
    @BindView(R.id.SeekActivity_edt_query)
    EditText SeekActivityEdtQuery;
    @BindView(R.id.SeekActivity_search_clear)
    ImageButton SeekActivitySearchClear;
    @BindView(R.id.SeekActivity_seek_ll)
    LinearLayout SeekActivitySeekLl;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_company_more)
    TextView tvCompanyMore;
    @BindView(R.id.tv_not_company)
    TextView tvNotCompany;
    @BindView(R.id.lv_company)
    NoScrollListview lvCompany;
    @BindView(R.id.ll_company)
    LinearLayout llCompany;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_product_more)
    TextView tvProductMore;
    @BindView(R.id.tv_not_category)
    TextView tvNotCategory;
    @BindView(R.id.lv_category)
    NoScrollListview lvCategory;
    @BindView(R.id.ll_product)
    LinearLayout llProduct;
    @BindView(R.id.tv_bid)
    TextView tvBid;
    @BindView(R.id.tv_bid_more)
    TextView tvBidMore;
    @BindView(R.id.tv_not_bid)
    TextView tvNotBid;
    @BindView(R.id.lv_bid)
    NoScrollListview lvBid;
    @BindView(R.id.ll_bid)
    LinearLayout llBid;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_fincying_more)
    TextView tvFincyingMore;
    @BindView(R.id.tv_not_project)
    TextView tvNotProject;
    @BindView(R.id.lv_project)
    NoScrollListview lvProject;
    @BindView(R.id.ll_project)
    LinearLayout llProject;

    private int index = -1;
    private SharedPreferences sp;
    private String content;
    private BigSeekContent bigSeekContent;
    private List<BigSeekContent.ResultBean.ListBean> listBean = new ArrayList<>();
    private List<BigSeekContent.ResultBean.ListBean> listBean2 = new ArrayList<>();
    private List<BigSeekContent.ResultBean.ListBean> listBean3 = new ArrayList<>();
    private List<BigSeekContent.ResultBean.ListBean> listBean4 = new ArrayList<>();

    private CompanyAdapter adapter;
    private ProductAdapter adapter2;
    private BidAdapter adapter3;
    private BarrowAdapter adapter4;
    private Lock lock;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listBean.clear();
            listBean2.clear();
            listBean3.clear();
            listBean4.clear();
            tvNotCompany.setVisibility(View.GONE);
            tvNotCategory.setVisibility(View.GONE);
            tvNotBid.setVisibility(View.GONE);
            tvNotProject.setVisibility(View.GONE);

            if (listBean.size() == 0 && listBean2.size() == 0 && listBean3.size() == 0 && listBean4.size() == 0) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                if (adapter2 != null) {
                    adapter2.notifyDataSetChanged();
                }
                if (adapter3 != null) {
                    adapter3.notifyDataSetChanged();
                }
                if (adapter4 != null) {
                    adapter4.notifyDataSetChanged();
                }
                if (TextUtils.isEmpty(content)) {
                    tvBidMore.setVisibility(View.GONE);
                    tvCompanyMore.setVisibility(View.GONE);

                    tvFincyingMore.setVisibility(View.GONE);

                    tvProductMore.setVisibility(View.GONE);
                    return;
                }

                loadData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);
        ButterKnife.bind(this);
        index = getIntent().getIntExtra("DIRECT", -1);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();

    }

    private void initData() {
        SeekActivitySearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekActivityEdtQuery.setText("");
                tvBidMore.setVisibility(View.GONE);
                tvCompanyMore.setVisibility(View.GONE);

                tvFincyingMore.setVisibility(View.GONE);

                tvProductMore.setVisibility(View.GONE);

            }
        });
        SeekActivityEdtQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString().trim();
                handler.sendEmptyMessageDelayed(0, 500);

            }
        });
        //产品
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SeekActivity.this, CompanyProductDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ProductDetail", listBean2.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //借贷
        lvProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SeekActivity.this, "开发中...", Toast.LENGTH_SHORT).show();
             /*   Intent intent = new Intent(SeekActivity.this, BorrowingApplyDetail.class);
                startActivity(intent);*/
            }
        });
        //公司详情
        lvCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                String customerId = listBean.get(position).getOffice().getId();
                //客户
                if (listBean.get(position).getPartnershipAsA() != null) {
                    intent = new Intent(SeekActivity.this, LastCustomerDetail.class);
                    intent.putExtra("companyA.id", listBean.get(position).getOffice().getId());
                    intent.putExtra("type", listBean.get(position).getPartnershipAsA().getType());
                    Log.e("SeekActivity", "客户");
                } else if (listBean.get(position).getPartnershipAsB() != null) {
                    //供应商
                    Log.e("SeekActivity", "供应商");
                    intent = new Intent(SeekActivity.this, NextFamilyManageCompanyDetail.class);
                    intent.putExtra("companyB.id", listBean.get(position).getOffice().getId());
                    intent.putExtra("type", listBean.get(position).getPartnershipAsB().getType());
                } else {
                    //未合作
                    Log.e("SeekActivity", "未合作");
                    intent = new Intent(SeekActivity.this, CompanyDetail.class);
                }
                intent.putExtra("companyId", customerId);
                intent.putExtra("companyName", listBean.get(position).getOffice().getName());
                intent.putExtra("companyType", listBean.get(position).getOffice().getServiceType());
                startActivity(intent);
            }
        });
        //招标
        lvBid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SeekActivity.this, TenderDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BidDetail", listBean3.get(position));
                intent.putExtra("flag", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        Log.e("SeekActivity", "onDestroy");
        activityManager.popOneActivity(this);
        Intent intent = new Intent(SeekActivity.this, MainActivity.class);
        if (index > -1) {
            intent.putExtra("DIRECT", 0);
        } else {
            intent.putExtra("DIRECT", SEEK);
        }
        startActivity(intent);
        super.onDestroy();

    }

    public void loadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("office.name", content);
        mapParams.put("companyProduct.productName", content);
        mapParams.put("inviteBid.title", content);
        mapParams.put("financing.title", content);
        RetrofitFactory.getApiService(this)
                .bigHomeSearch(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BigSeekContent>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BigSeekContent bigSeekContent) {
                        listBean.clear();
                        listBean2.clear();
                        listBean3.clear();
                        listBean4.clear();
                        tvCompanyMore.setVisibility(View.GONE);
                        tvProductMore.setVisibility(View.GONE);
                        tvBidMore.setVisibility(View.GONE);
                        tvFincyingMore.setVisibility(View.GONE);
                    /*    Company company = gson.fromJson(result, Company.class);
                        CompanyCateGory companyCateGory = gson.fromJson(result, CompanyCateGory.class);
                        BidTender bid = gson.fromJson(result, BidTender.class);
                        Fincying fincying = gson.fromJson(result, Fincying.class);*/
                        Log.e("SeekActivity", bigSeekContent.toString());

                        listBean.addAll(bigSeekContent.getResult().get(0).getList());
                        listBean2.addAll(bigSeekContent.getResult().get(1).getList());
                        listBean3.addAll(bigSeekContent.getResult().get(2).getList());
                        listBean4.addAll(bigSeekContent.getResult().get(3).getList());
                        Log.e("SeekActivity", listBean.size() + "," + listBean2.size() + "," + listBean3.size() + "," + listBean4.size());
                        if (listBean.size() == 0) {
                            tvNotCompany.setVisibility(View.VISIBLE);
                        }
                        if (listBean.size() > 4) {
                            tvCompanyMore.setVisibility(View.VISIBLE);
                        } else {
                            tvCompanyMore.setVisibility(View.GONE);
                        }
                        if (listBean2.size() == 0) {
                            tvNotCategory.setVisibility(View.VISIBLE);
                        }
                        if (listBean2.size() > 4) {
                            tvProductMore.setVisibility(View.VISIBLE);
                        } else {
                            tvProductMore.setVisibility(View.GONE);
                        }
                        if (listBean3.size() == 0) {
                            tvNotBid.setVisibility(View.VISIBLE);
                        }
                        //更多
                        if (listBean3.size() > 4) {
                            tvBidMore.setVisibility(View.VISIBLE);
                        } else {
                            tvBidMore.setVisibility(View.GONE);
                        }
                        if (listBean4.size() == 0) {
                            tvNotProject.setVisibility(View.VISIBLE);
                        }
                        //更多显示
                        if (listBean4.size() > 4) {
                            tvFincyingMore.setVisibility(View.VISIBLE);
                        } else {
                            tvFincyingMore.setVisibility(View.GONE);
                        }

                        adapter = new CompanyAdapter();
                        lvCompany.setAdapter(adapter);//公司
                        adapter2 = new ProductAdapter();
                        lvCategory.setAdapter(adapter2);
                        adapter3 = new BidAdapter();
                        lvBid.setAdapter(adapter3);
                        adapter4 = new BarrowAdapter();
                        lvProject.setAdapter(adapter4);

                        adapter.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();
                        adapter3.notifyDataSetChanged();
                        adapter4.notifyDataSetChanged();
                    }
                });
    }

    @OnClick({R.id.tv_company_more, R.id.tv_product_more, R.id.tv_bid_more, R.id.tv_fincying_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_company_more:
                Intent intent = new Intent(SeekActivity.this, CompanyMore.class);
                intent.putExtra("content", content);
                startActivity(intent);
                break;
            case R.id.tv_product_more:
                Intent intent2 = new Intent(SeekActivity.this, ProductMore.class);
                intent2.putExtra("content", content);
                startActivity(intent2);
                break;
            case R.id.tv_bid_more:
                Intent intent3 = new Intent(SeekActivity.this, BidMore.class);
                intent3.putExtra("content", content);
                startActivity(intent3);
                break;
            case R.id.tv_fincying_more:
                Intent intent4 = new Intent(SeekActivity.this, FincyingMore.class);
                intent4.putExtra("content", content);
                startActivity(intent4);
                break;
        }
    }

    public class CompanyAdapter extends BaseAdapter {
        public CompanyAdapter() {

        }

        @Override
        public int getCount() {
            return listBean.size() > 4 ? 4 : listBean.size();
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
            View view = View.inflate(SeekActivity.this, R.layout.item_big_seek, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title_name);
            if (listBean != null) {
                tv_title.setText(listBean.get(position).getOffice().getName());
            }


            return view;
        }
    }

    public class BidAdapter extends BaseAdapter {
        public BidAdapter() {

        }

        @Override
        public int getCount() {
            return listBean3.size() > 4 ? 4 : listBean3.size();
        }

        @Override
        public Object getItem(int position) {
            return listBean3.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(SeekActivity.this, R.layout.item_big_seek, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title_name);
            if (listBean3 != null) {
                tv_title.setText(listBean3.get(position).getInviteBid().getTitle());
            }


            return view;
        }
    }

    public class BarrowAdapter extends BaseAdapter {
        public BarrowAdapter() {
        }

        @Override
        public int getCount() {
            return listBean4.size() > 4 ? 4 : listBean4.size();
        }

        @Override
        public Object getItem(int position) {
            return listBean4.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(SeekActivity.this, R.layout.item_big_seek, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title_name);
            if (listBean4 != null) {
                tv_title.setText(listBean4.get(position).getFinancing().getTitle());
            }
            return view;
        }
    }

    public class ProductAdapter extends BaseAdapter {
        public ProductAdapter() {

        }

        @Override
        public int getCount() {
            return listBean2.size() > 4 ? 4 : listBean2.size();
        }

        @Override
        public Object getItem(int position) {
            return listBean2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(SeekActivity.this, R.layout.item_big_seek, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title_name);
            if (listBean2 != null) {
                tv_title.setText(listBean2.get(position).getCompanyProduct().getProductName());
            }


            return view;
        }
    }
}