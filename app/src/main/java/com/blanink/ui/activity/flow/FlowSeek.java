package com.blanink.ui.activity.flow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.Attribute;
import com.blanink.bean.OrderProductSpecification;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.CompanyProductCategory;
import com.blanink.bean.OfficeEmp;
import com.blanink.bean.PartnerInfo;
import com.blanink.bean.RelIndustryCategoryAttribute;
import com.blanink.view.NoScrollListview;
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

public class FlowSeek extends BaseActivity {
    @BindView(R.id.come_order_tv_seek)
    TextView comeOrderTvSeek;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order)
    RelativeLayout comeOrder;
    @BindView(R.id.tv_customer)
    TextView tvCustomer;
    @BindView(R.id.sp_customer)
    Spinner spCustomer;
    @BindView(R.id.tv_product_category)
    TextView tvProductCategory;
    @BindView(R.id.sp_product_category)
    Spinner spProductCategory;
    @BindView(R.id.lv_product_attribute)
    NoScrollListview lvProductAttribute;
    @BindView(R.id.tv_order_state)
    TextView tvOrderState;
    @BindView(R.id.sp_order_state)
    Spinner spOrderState;
    @BindView(R.id.tv_mine_master)
    TextView tvMineMaster;
    @BindView(R.id.sp_mine_master)
    Spinner spMineMaster;
    @BindView(R.id.tv_resource)
    TextView tvResource;
    @BindView(R.id.sp_tv_resource)
    Spinner spTvResource;
    @BindView(R.id.tv_product_number)
    TextView tvProductNumber;
    @BindView(R.id.et_product_number)
    EditText etProductNumber;
    @BindView(R.id.tv_A_number)
    TextView tvANumber;
    @BindView(R.id.et_A_number)
    EditText etANumber;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.et_product_name)
    EditText etProductName;
    @BindView(R.id.btn_seek)
    Button btnSeek;
    @BindView(R.id.rl_seek)
    LinearLayout rlSeek;
    private SharedPreferences sp;
    private RelIndustryCategoryAttribute attribute;
    private SparseArray<View> sparseArray;
    private String customer;
    private String master;
    private List<OrderProductSpecification> orderProductSpecificationList = new ArrayList<OrderProductSpecification>();
    private List<OrderProductSpecification> listSpinner = new ArrayList<OrderProductSpecification>();
    private List<String> productCateGoryName = new ArrayList<>();//产品类名称
    private List<String> productCateGoryId = new ArrayList<>();//产品类名称id
    private List<String> customerNameList = new ArrayList<>();//客户名称
    private List<String> customerIdList = new ArrayList<>();
    private List<String> masterNameList = new ArrayList<>();//责任人
    private String[] orderStateList = {"选择订单状态", "甲方已创建未发送", "甲方已绑定订单未发送", "甲方已发给乙方", "乙方自己已创建",
            "待甲方确认/修改", "甲方已确认", "下发生产中", "流程已创建待发布", "流程已发布",
            "生产完成待发货", "部分发货", "全部发出", "已确认收到部分货", "确认收到全部货",
            "已完成评论和检讨", "待接受售后申请", "售后已接受处理中", "售后处理已结束", "售后被接受",
            "发起还款", "还款已被确认收到", "还款未收到", "拒绝生产"};
    private String[] resources = {"选择产品来源", "甲方传输来的", "乙方自己创建的", "发起售后的", "发起还款的"};
    private String bMasterId;
    private String orderProductStatus;
    private String resource;
    private String productName;
    public String bCompanyId;
    public String aCompanyId;
    public String aOrderNumber;
    public String bOrderNumber;
    public String companyCategoryId;
    public String bCompanyOrderOwnerUserId;
    private String flag;
    private List<String> bMasterIdList = new ArrayList<>();
    private ArrayList<RelIndustryCategoryAttribute.ResultBean> attributeList = new ArrayList<>();
    private AttributesAdapter attributeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_seek);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initData();
    }

    private void initData() {
        flag = getIntent().getStringExtra("flag");
        //返回
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //加载客户
        getCustmoer();
        //加载产品类
        loadProductCategory();

        //获取责任人
        getCompanyEmp();

        //订单状态
        spOrderState.setAdapter(new ArrayAdapter<String>(FlowSeek.this, R.layout.simple_spinner_item, R.id.tv_name, orderStateList));
        spOrderState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    orderProductStatus = "";
                } else {
                    orderProductStatus = position + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //产品来源
        spTvResource.setAdapter(new ArrayAdapter<String>(FlowSeek.this, R.layout.simple_spinner_item, R.id.tv_name, resources));
        spTvResource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    resource = "";
                } else {
                    resource = position + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //搜索
        btnSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = etProductName.getText().toString();
                aOrderNumber = etANumber.getText().toString();
                bOrderNumber = etProductNumber.getText().toString();
                if ("请选择我方负责人".equals(master)) {
                    bMasterId = "";
                }
                orderProductSpecificationList.clear();
                for (int i = 0; i < lvProductAttribute.getChildCount(); i++) {
                    final List<OrderProductSpecification> listEdit = new ArrayList<OrderProductSpecification>();
                    final OrderProductSpecification orderProductSpecification = new OrderProductSpecification();
                    final Attribute attribute = new Attribute();
                    LinearLayout layout = (LinearLayout) lvProductAttribute.getChildAt(i);// 获得子item的layout
                    if (FlowSeek.this.attribute.getResult().get(i).getInputType().equals("1")) {
                        EditText et = (EditText) layout.findViewById(R.id.et_attribute);
                        attribute.setId(FlowSeek.this.attribute.getResult().get(i).getAttribute().getId());
                        orderProductSpecification.setAttribute(attribute);
                        orderProductSpecification.setAttributeValue(et.getText().toString());
                        orderProductSpecification.setAttributeSearchType("1");
                        listEdit.add(orderProductSpecification);
                        orderProductSpecificationList.addAll(listEdit);
                    }
                }
                //添加下拉框
                if (listSpinner != null) {
                    orderProductSpecificationList.addAll(listSpinner);
                }

                Log.e("ComeOrder", orderProductSpecificationList.size() + "___" + orderProductSpecificationList.toString());
                Intent intent=null;
                intent = new Intent(FlowSeek.this, FlowSeekContent.class);
                intent.putExtra("aCompanyId", aCompanyId);
                intent.putExtra("bCompanyId", sp.getString("COMPANY_ID", null));
                intent.putExtra("productName", productName);
                intent.putExtra("aOrderNumber", aOrderNumber);
                intent.putExtra("bOrderNumber", bOrderNumber);
                intent.putExtra("companyCategoryId", companyCategoryId);
                intent.putExtra("bCompanyOrderOwnerUserId", bMasterId);
                intent.putExtra("orderProductStatus", orderProductStatus);
                intent.putExtra("source", resource);
                intent.putExtra("flag", flag);
                intent.putExtra("orderProductSpecificationList", new Gson().toJson(orderProductSpecificationList));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //加载客户
    private void getCustmoer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("companyB.id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(this)
                .officeList(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PartnerInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PartnerInfo partnerInfo) {
                        customerNameList.add("选择客户名称");
                        customerIdList.add("");
                        if (partnerInfo.getResult().size() > 0) {
                            for (int i = 0; i < partnerInfo.getResult().size(); i++) {
                                customerNameList.add(partnerInfo.getResult().get(i).getCompanyA().getName());
                                customerIdList.add(partnerInfo.getResult().get(i).getCompanyA().getId());
                            }
                        }
                        if (partnerInfo.getResult().size() == 0) {
                            //  btnContiumeAdd.setText("你没有客户，无法添加订单");
                            // btnContiumeAdd.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        }
                        spCustomer.setAdapter(new ArrayAdapter<String>(FlowSeek.this, R.layout.simple_spinner_item, R.id.tv_name, customerNameList));
                        spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                customer = customerNameList.get(position);
                                aCompanyId = customerIdList.get(position);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }

    //加载产品类
    public void loadProductCategory() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("company.id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(this)
                .categoryList(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompanyProductCategory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CompanyProductCategory companyProductCategory) {
                        productCateGoryName.add("选择产品类");
                        productCateGoryId.add("");
                        for (int i = 0; i < companyProductCategory.getResult().size(); i++) {
                            productCateGoryName.add(companyProductCategory.getResult().get(i).getName());
                            productCateGoryId.add(companyProductCategory.getResult().get(i).getId());
                        }

                        spProductCategory.setAdapter(new ArrayAdapter<String>(FlowSeek.this, R.layout.simple_spinner_item
                                , R.id.tv_name, productCateGoryName));

                        spProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                companyCategoryId = productCateGoryId.get(position);
                                orderProductSpecificationList.clear();
                                if (orderProductSpecificationList.size() == 0) {

                                    loadProductAttributes(companyCategoryId);

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }


    //加载产品属性
    public void loadProductAttributes(String productCategoryId) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", productCategoryId);
        RetrofitFactory.getApiService(this)
                .attributeList(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RelIndustryCategoryAttribute>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RelIndustryCategoryAttribute relIndustryCategoryAttribute) {
                        attribute=relIndustryCategoryAttribute;
                        attributeList.clear();
                        if (attribute.getResult() != null) {
                            attributeList.addAll(attribute.getResult());
                            attributeAdapter = new AttributesAdapter(attributeList);
                            lvProductAttribute.setAdapter(attributeAdapter);
                        }else {
                            attributeAdapter = new AttributesAdapter(attributeList);
                            lvProductAttribute.setAdapter(attributeAdapter);
                        }

                    }
                });
    }


    //加载订单状态

    //获得本公司人员列表
    private void getCompanyEmp() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(this)
                .userList(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OfficeEmp>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OfficeEmp officeEmp) {
                        masterNameList.add("请选择我方负责人");
                        bMasterIdList.add("");
                        for (int i = 0; i < officeEmp.getResult().size(); i++) {
                            masterNameList.add(officeEmp.getResult().get(i).getName());
                            bMasterIdList.add(officeEmp.getResult().get(i).getId());
                        }
                        spMineMaster.setAdapter(new ArrayAdapter<String>(FlowSeek.this, R.layout.simple_spinner_item, R.id.tv_name, masterNameList));
                        spMineMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                master = masterNameList.get(position);
                                bMasterId = bMasterIdList.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }

    //加载产品来源


    public class AttributesAdapter extends BaseAdapter {
        private List<RelIndustryCategoryAttribute.ResultBean> attributeLists;

        public AttributesAdapter(List<RelIndustryCategoryAttribute.ResultBean> attributeLists) {
            this.attributeLists = attributeLists;
        }

        @Override
        public int getCount() {
            return attributeLists.size();
        }

        @Override
        public Object getItem(int position) {
            return attributeLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Log.e("@@@", "getView");
            sparseArray = new SparseArray<>();
            ViewHolder viewHolder = null;
            if (sparseArray.get(position, null) == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(FlowSeek.this, R.layout.attributes, null);
                viewHolder.tv_attribute_name = (TextView) convertView.findViewById(R.id.tv_attribute_name);
                viewHolder.sp_attribute_value = (Spinner) convertView.findViewById(R.id.sp_attribute_value);
                viewHolder.attribute_name = (TextView) convertView.findViewById(R.id.attribute_name);
                viewHolder.et_attribute = (EditText) convertView.findViewById(R.id.et_attribute);
                viewHolder.rl_edt = (LinearLayout) convertView.findViewById(R.id.rl_edt);
                viewHolder.rl_sp = (LinearLayout) convertView.findViewById(R.id.rl_sp);
                convertView.setTag(viewHolder);
                sparseArray.put(position, convertView);
            } else {
                convertView = sparseArray.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (attributeLists.get(position).getInputType().equals("1")) {
                //文本框
                viewHolder.rl_sp.setVisibility(View.GONE);
                viewHolder.rl_edt.setVisibility(View.VISIBLE);
                viewHolder.attribute_name.setText(attributeLists.get(position).getAttribute().getName());
            } else {
                //下拉框
                final OrderProductSpecification orderProductSpecification = new OrderProductSpecification();
                final Attribute attribute = new Attribute();
                viewHolder.rl_edt.setVisibility(View.GONE);
                viewHolder.rl_sp.setVisibility(View.VISIBLE);
                viewHolder.tv_attribute_name.setText(attributeLists.get(position).getAttribute().getName());
                attribute.setId(attributeLists.get(position).getAttribute().getId());
                final String[] strHardCodeValue = attributeLists.get(position).getHardcodeValue().split(",");
                viewHolder.sp_attribute_value.setAdapter(new ArrayAdapter<String>(FlowSeek.this, R.layout.simple_spinner_item, R.id.tv_name, strHardCodeValue));
                viewHolder.sp_attribute_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        listSpinner.clear();
                        orderProductSpecification.setAttribute(attribute);
                        orderProductSpecification.setAttributeValue(strHardCodeValue[position]);
                        orderProductSpecification.setAttributeSearchType("2");
                        listSpinner.add(orderProductSpecification);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            return convertView;
        }


    }

    static class ViewHolder {
        TextView tv_attribute_name;
        Spinner sp_attribute_value;
        TextView attribute_name;
        EditText et_attribute;
        LinearLayout rl_sp;
        LinearLayout rl_edt;
    }

}