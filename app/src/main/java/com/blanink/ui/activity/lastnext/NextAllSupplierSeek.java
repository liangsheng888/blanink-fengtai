package com.blanink.ui.activity.lastnext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.Office;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.utils.GlideUtils;

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

/**
 * Created by Administrator on 2017/1/16.
 * 全部供应商搜索
 */
public class NextAllSupplierSeek extends BaseActivity {
    private static final String TAG = "NextFamilyAccordCompany";
    @BindView(R.id.iv_customer_matches_last)
    TextView ivCustomerMatchesLast;
    @BindView(R.id.rl_supplier_manage)
    RelativeLayout rlSupplierManage;
    @BindView(R.id.come_order_tv)
    TextView comeOrderTv;
    @BindView(R.id.et_seek_content)
    EditText etSeekContent;
    @BindView(R.id.tv_seek)
    TextView tvSeek;
    @BindView(R.id.rl_seek)
    RelativeLayout rlSeek;
    @BindView(R.id.lv_matches)
    ListView lvMatches;
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
    private TextView iv_accord_supplier_last;
    private TextView tv_seek;
    //private SwipeMenuListView lv_supplier;
    private List<Office.ResultBean> customer = new ArrayList<>();
    private MyAdapter adapter;
    private SharedPreferences sp;
    private EditText et_supplier;
    private SparseArray<View> sparseArray = new SparseArray<>();
    ;
    //private SwipeMenuCreator creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_supplier_seek);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initView();
        initData();
    }

    private void initView() {
        iv_accord_supplier_last = ((TextView) findViewById(R.id.iv_customer_matches_last));
        tv_seek = ((TextView) findViewById(R.id.tv_seek));//seek
        et_supplier = ((EditText) findViewById(R.id.et_seek_content));
    }

    private void initData() {
        //重新加载
        rlLoadFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlLoadFail.setVisibility(View.GONE);
                llLoad.setVisibility(View.VISIBLE);
                getData();
            }
        });
        //
        et_supplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customer.clear();
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //返回
        iv_accord_supplier_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索
        tv_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_supplier.getText().toString())) {
                    Toast.makeText(NextAllSupplierSeek.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                llLoad.setVisibility(View.VISIBLE);
                rlNotData.setVisibility(View.GONE);
                getData();
            }
        });

        lvMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("1".equals(customer.get(position).getPType())) {
                    Intent intent = new Intent(NextAllSupplierSeek.this, NextFamilyManageSupplierManageApplyDelete.class);
                    intent.putExtra("companyId", customer.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(NextAllSupplierSeek.this, NextFamilyManageCompanyDetail.class);
                    intent.putExtra("companyId", customer.get(position).getId());
                    intent.putExtra("companyB.id", customer.get(position).getId());
                    intent.putExtra("companyName", customer.get(position).getName());
                    intent.putExtra("companyType", customer.get(position).getServiceType());
                    intent.putExtra("type",customer.get(position).getPType());
                    startActivity(intent);
                }
            }
        });
        //跳转到详情
        lvMatches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String companyId = customer.get(position).getId();
                Intent intent = new Intent(NextAllSupplierSeek.this, NextFamilyManageCompanyDetail.class);
                intent.putExtra("id", companyId);
                intent.putExtra("companyB.id", customer.get(position).getId());
                intent.putExtra("companyName", customer.get(position).getName());
                intent.putExtra("type", customer.get(position).getPType());
                startActivity(intent);
            }
        });
    }

    private void getData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("currentUser.id", sp.getString("USER_ID", null));
        mapParams.put("name", et_supplier.getText().toString());
        RetrofitFactory.getApiService(this)
                .findNameB(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Office>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        rlLoadFail.setVisibility(View.VISIBLE);
                        llLoad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Office company) {
                        llLoad.setVisibility(View.GONE);
                        if (company.getResult().size() == 0) {
                            rlNotData.setVisibility(View.VISIBLE);
                        } else {
                            rlNotData.setVisibility(View.GONE);
                        }
                        customer = new ArrayList<Office.ResultBean>();
                        customer.addAll(company.getResult());
                        if (adapter == null) {
                            adapter = new MyAdapter();
                            lvMatches.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return customer.size();
        }

        @Override
        public Object getItem(int position) {
            return customer.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
/*            creator=  new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    // 设置加入潜在框
                    SwipeMenuItem deleteitem = new SwipeMenuItem(getApplicationContext());
                    deleteitem.setBackground(new ColorDrawable(Color.RED));
                    deleteitem.setWidth(dp2px(150));
                    if ("1".equals(customer.get(position).getPType())) {
                        deleteitem.setTitle("解除关系");
                        deleteitem.setTitleSize(18);
                        deleteitem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(deleteitem);
                    } else {
                        deleteitem.setTitle("申请合作");
                        deleteitem.setTitleSize(18);
                        deleteitem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(deleteitem);
                    }


                }
            };
            lv_supplier.setMenuCreator(creator);*/
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(NextAllSupplierSeek.this, R.layout.item_supplier_manage, null);
                viewHolder.tv_customer_num = (TextView) convertView.findViewById(R.id.tv_customer_num);
                viewHolder.tv_supplier_company_name = (TextView) convertView.findViewById(R.id.tv_supplier_company_name);
                viewHolder.tv_apply_address = (TextView) convertView.findViewById(R.id.tv_apply_address);
                viewHolder.tv_master = (TextView) convertView.findViewById(R.id.tv_master);
                viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                viewHolder.tv_honest = (TextView) convertView.findViewById(R.id.tv_honest);
                viewHolder.tv_major = (TextView) convertView.findViewById(R.id.tv_major);
                viewHolder.tv_company_apply_remark = (TextView) convertView.findViewById(R.id.tv_company_apply_remark);
                viewHolder.tv_company_apply_remark_other = (TextView) convertView.findViewById(R.id.tv_company_apply_other_remark);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Office.ResultBean companyB = customer.get(position);
            Log.e(TAG, "type:" + customer.get(position).getType());
            viewHolder.tv_customer_num.setText(companyB.getServiceCount() + "");//
            viewHolder.tv_supplier_company_name.setText(companyB.getName());
            if (companyB.getArea() != null && companyB.getArea().getName() != null) {
                viewHolder.tv_apply_address.setText(companyB.getArea().getName());
            }
            viewHolder.tv_phone.setText(companyB.getPhone());
            viewHolder.tv_master.setText(companyB.getMaster());
            DecimalFormat df = new DecimalFormat("0.0");
            viewHolder.tv_honest.setText(df.format((customer.get(position).getReviewOthers() + customer.get(position).getReviewSelf()) / 2.0));
            viewHolder.tv_company_apply_remark.setText(customer.get(position).getReviewSelf() + "");
            viewHolder.tv_company_apply_remark_other.setText(customer.get(position).getReviewOthers() + "");
            GlideUtils.glideImageView(NextAllSupplierSeek.this, viewHolder.iv, customer.get(position).getPhoto(), true);

            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView tv_supplier_company_name;//公司名称
        public TextView tv_customer_num;//服务客户数量
        public TextView tv_apply_address;//地址
        public TextView tv_master;//负责人
        public TextView tv_phone;//电话
        public TextView tv_honest;//公司信誉
        public TextView tv_company_apply_remark;//自评
        public TextView tv_company_apply_remark_other;//他评
        public TextView tv_major;//主营
        ImageView iv;
    }

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
