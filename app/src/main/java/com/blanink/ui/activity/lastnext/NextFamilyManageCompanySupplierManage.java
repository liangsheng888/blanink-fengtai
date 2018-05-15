package com.blanink.ui.activity.lastnext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.activity.MainActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ManyCustomer;
import com.blanink.utils.CheckNet;
import com.blanink.utils.GlideUtils;
import com.blanink.view.RefreshListView;


import java.text.DecimalFormat;
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
 * 下家管理供应商管理
 */
public class NextFamilyManageCompanySupplierManage extends BaseActivity {

    private static final int BACK_TASK = 0;
    @BindView(R.id.iv_seek_supplier)
    ImageView ivSeekSupplier;
    @BindView(R.id.loading_error_img)
    ImageView loadingErrorImg;
    @BindView(R.id.rl_load_fail)
    RelativeLayout rlLoadFail;
    @BindView(R.id.activity_next_family_manage_company_supplier_manage)
    RelativeLayout activityNextFamilyManageCompanySupplierManage;
    private TextView iv_customer_manage_last;
    private LinearLayout ll_load;
    private RelativeLayout ll_load_fail;
    private RefreshListView lv_supplier_queue;
    private List<ManyCustomer.Result.Customer> suppliers = new ArrayList<ManyCustomer.Result.Customer>();
    private SparseArray<View> sparseArray;
    private MyAdapter adapter;
    private Integer pageNo = 1;//当前页
    private ManyCustomer lmc;
    SharedPreferences sp;
    Boolean hasData = true;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //更新UI
            if (adapter == null) {
                rl_not_data.setVisibility(View.VISIBLE);
            } else {
                adapter.notifyDataSetChanged();
                lv_supplier_queue.completeRefresh(hasData);
            }
        }

        ;
    };
    private EditText et_supplier;
    private RelativeLayout rl_not_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_family_manage_company_supplier_manage);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initView();
        initData();
    }

    private void initView() {
        iv_customer_manage_last = ((TextView) findViewById(R.id.iv_customer_manage_last));
        lv_supplier_queue = ((RefreshListView) findViewById(R.id.lv_supplier_queue));
        ll_load = ((LinearLayout) findViewById(R.id.ll_load));//加载
        ll_load_fail = ((RelativeLayout) findViewById(R.id.rl_load_fail));//加载失败
        rl_not_data = ((RelativeLayout) findViewById(R.id.rl_not_data));
    }

    private void initData() {
        ivSeekSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NextFamilyManageCompanySupplierManage.this, NextAllSupplierSeek.class);
                startActivity(intent);
            }
        });
        getData();
        addSeekHeader();

        //重新加载
        ll_load_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_load_fail.setVisibility(View.GONE);
                ll_load.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 1500);
            }
        });
        iv_customer_manage_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //下拉刷新上拉加载
        lv_supplier_queue.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                requestDataFromServer(false);
            }

            @Override
            public void onLoadingMore() {
                requestDataFromServer(true);

            }
        });
        //跳转到供应商详情界面
        lv_supplier_queue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position < suppliers.size() + 2) {
                    Intent intent = new Intent(NextFamilyManageCompanySupplierManage.this, NextFamilyManageCompanyDetail.class);
                    intent.putExtra("id", suppliers.get(position - 2).getId());
                    intent.putExtra("companyName", suppliers.get(position - 2).getCompanyB().name);
                    intent.putExtra("companyB.id", suppliers.get(position - 2).getCompanyB().id);
                    intent.putExtra("type", suppliers.get(position - 2).getType());
                    startActivity(intent);
                }
            }
        });
        //我的供应商搜索

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intentBackTask = new Intent(this, MainActivity.class);
        intentBackTask.putExtra("DIRECT", BACK_TASK);
        startActivity(intentBackTask);
    }

    // 上拉加载下拉刷新
    private void requestDataFromServer(final boolean isLoadingMore) {

        if (isLoadingMore) {
            pageNo++;
            getData();
        } else {
            getDataRefresh();
        }
    }

    private void getDataRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!CheckNet.isNetWorkConnected(NextFamilyManageCompanySupplierManage.this)) {
                    ll_load.setVisibility(View.GONE);
                    ll_load_fail.setVisibility(View.VISIBLE);
                } else {
                    Map<String, Object> mapParams = new HashMap<String, Object>();
                    mapParams.put("userId", sp.getString("USER_ID", "NULL"));
                    mapParams.put("pageNo", pageNo + "");
                    RetrofitFactory.getApiService(NextFamilyManageCompanySupplierManage.this)
                            .partners(mapParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ManyCustomer>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    ll_load.setVisibility(View.GONE);
                                    ll_load_fail.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onNext(ManyCustomer manyCustomer) {
                                    ll_load.setVisibility(View.GONE);
                                    lmc = manyCustomer;
                                    Log.e("Next", "下家管理（已解析）:" + lmc.getErrorCode() + lmc.getReason());
                                    Log.e("Next", "下家管理（已解析）长度:" + lmc.getResult().getRows().size());
                                    Log.e("@@@@", "下家管理:" + "数据总长度:" + lmc.getResult().getTotal() + "  拿到的数据总长度:" + suppliers.size());
                                    if (lmc.getResult().getTotal() >= suppliers.size()) {
                                        hasData = false;
                                    } else {
                                        hasData = true;
                                        int oldIndex = suppliers.size() - 1;
                                        suppliers.addAll(lmc.getResult().getRows());

                                        if (adapter == null) {
                                            Log.e("Next", "adapter==null");
                                            adapter = new MyAdapter();
                                            lv_supplier_queue.setAdapter(adapter);
                                        } else {
                                            Log.e("Next", "adapter!=null");
                                            adapter.notifyDataSetChanged();
                                            lv_supplier_queue.setSelection(oldIndex);
                                        }
                                    }
                                    //在UI线程更新UI
                                    handler.sendEmptyMessage(0);
                                }
                            });

                }
            }
        }, 1500);
    }


    //访问服务器
    private void getData() {

        if (!CheckNet.isNetWorkConnected(NextFamilyManageCompanySupplierManage.this)) {
            ll_load.setVisibility(View.GONE);
            ll_load_fail.setVisibility(View.VISIBLE);
        } else {

            Map<String, Object> mapParams = new HashMap<String, Object>();
            mapParams.put("userId", sp.getString("USER_ID", "NULL"));
            mapParams.put("pageNo", pageNo + "");
            RetrofitFactory.getApiService(this)
                    .partners(mapParams)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ManyCustomer>() {
                        @Override
                        public void onCompleted() {
                            
                        }

                        @Override
                        public void onError(Throwable e) {
                            ll_load.setVisibility(View.GONE);
                            ll_load_fail.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNext(ManyCustomer manyCustomer) {
                            ll_load.setVisibility(View.GONE);
                            lmc =manyCustomer;
                            Log.e("Next", "下家管理（已解析）:" + lmc.getErrorCode() + lmc.getReason());
                            Log.e("Next", "下家管理（已解析）长度:" + lmc.getResult().getRows().size());
                            Log.e("@@@@", "下家管理:" + "数据总长度:" + lmc.getResult().getTotal() + "  拿到的数据总长度:" + suppliers.size());
                            if (lmc.getResult().getTotal() == suppliers.size()) {
                                hasData = false;
                            } else {
                                int oldIndex = suppliers.size() - 1;
                                suppliers.addAll(lmc.getResult().getRows());

                                if (adapter == null) {
                                    Log.e("Next", "adapter==null");
                                    adapter = new MyAdapter();
                                    lv_supplier_queue.setAdapter(adapter);
                                } else {
                                    Log.e("Next", "adapter!=null");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            //在UI线程更新UI
                            handler.sendEmptyMessage(0);
                        }
                    });

        }
    }


    public class MyAdapter extends BaseAdapter {
        public MyAdapter() {
            Log.e("Next", "customers ：");
        }

        @Override
        public int getCount() {
            return suppliers.size();
        }

        @Override
        public Object getItem(int position) {
            return suppliers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            sparseArray = new SparseArray<>();
            Log.e("Next", "getView  supplier ：" + suppliers.size() + "");
            if (sparseArray.get(position, null) == null) {
                Log.e("Next", "getView  convertView ==null");
                viewHolder = new ViewHolder();
                convertView = View.inflate(NextFamilyManageCompanySupplierManage.this, R.layout.item_customer_queue, null);
                viewHolder.tv_company_jc = (TextView) convertView.findViewById(R.id.tv_name);
                //    viewHolder.tv_apply_company_name = (TextView) convertView.findViewById(R.id.tv_apply_company_name);
                viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                viewHolder.tv_master = (TextView) convertView.findViewById(R.id.tv_master);
                viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                viewHolder.tv_honest = (TextView) convertView.findViewById(R.id.tv_honest);
                viewHolder.tv_company_apply_remark = (TextView) convertView.findViewById(R.id.tv_company_apply_remark);
                viewHolder.tv_company_apply_remark_other = (TextView) convertView.findViewById(R.id.tv_company_apply_other_remark);
                viewHolder.tv_apply_address = (TextView) convertView.findViewById(R.id.tv_apply_address);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                Log.e("Next", "getView  convertView !=null");
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ManyCustomer.Result.Customer customer = suppliers.get(position);
            Log.e("Next", " getView 下家管理 supplier:" + customer.getCompanyB().toString());

            viewHolder.tv_state.setText("1".equals(customer.getType()) ? "已合作" : "潜在");
            Log.e("Next", "type" + customer.getType());
            if ("1".equals(customer.getType())) {
                viewHolder.tv_state.setTextColor(getResources().getColor(R.color.colorTheme));
            } else {
                viewHolder.tv_state.setTextColor(getResources().getColor(R.color.colorBlue));
            }
            viewHolder.tv_company_jc.setText(customer.getCompanyB().name);
            viewHolder.tv_master.setText(customer.getCompanyB().master);
            viewHolder.tv_apply_address.setText(customer.getCompanyB().getArea().getName());
            viewHolder.tv_master.setText(customer.getCompanyB().master);
            viewHolder.tv_phone.setText(customer.getCompanyB().phone);
            DecimalFormat df = new DecimalFormat("0.0");
            viewHolder.tv_honest.setText(df.format((suppliers.get(position).companyB.reviewSelf + suppliers.get(position).companyB.reviewOthers) / 2.0));
            viewHolder.tv_company_apply_remark.setText(suppliers.get(position).companyB.reviewSelf + "");
            viewHolder.tv_company_apply_remark_other.setText(suppliers.get(position).companyB.reviewOthers + "");
            if (suppliers.get(position).getCompanyB() != null && suppliers.get(position).getCompanyB().photo != null && !suppliers.get(position).getCompanyB().equals("")) {
                GlideUtils.glideImageView(NextFamilyManageCompanySupplierManage.this, viewHolder.iv, suppliers.get(position).getCompanyB().photo, true);
            }
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView tv_company_jc;//公司简称
        public TextView tv_apply_company_name;//公司名称
        public TextView tv_state;//实有 虚拟
        public TextView tv_apply_address;//地址
        public TextView tv_master;//负责人
        public TextView tv_phone;//电话
        public TextView tv_honest;//公司信誉
        public TextView tv_company_apply_remark;//自评
        public TextView tv_company_apply_remark_other;//他评
        ImageView iv;

    }

    @Override
    protected void onStart() {
        super.onStart();
        //清空焦点
        et_supplier.clearFocus();
    }

    public void addSeekHeader() {
        View view = View.inflate(NextFamilyManageCompanySupplierManage.this, R.layout.layout_header_seek_suppler, null);
        et_supplier = ((EditText) view.findViewById(R.id.et_seek));
        lv_supplier_queue.addHeaderView(view);
        et_supplier.clearFocus();
        et_supplier.setCursorVisible(false);
        et_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_supplier.setFocusable(true);
                et_supplier.setCursorVisible(true);
            }
        });
        //设立焦点改变监听事件
        et_supplier.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //跳到搜索界面
                    Log.e("HomeFragment", "焦点:" + hasFocus);
                    et_supplier.setCursorVisible(true);
                    Intent intent = new Intent(NextFamilyManageCompanySupplierManage.this, NextAllSupplierSeek.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    et_supplier.setCursorVisible(false);
                }
            }
        });
    }

}
