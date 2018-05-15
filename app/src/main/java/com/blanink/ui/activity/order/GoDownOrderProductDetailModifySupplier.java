package com.blanink.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.bean.Attribute;
import com.blanink.bean.OrderProductSpecification;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.PhotoAdapter;
import com.blanink.ui.adapter.RecyclerItemClickListener;
import com.blanink.http.RetrofitFactory;
import com.blanink.oss.OssService;
import com.blanink.bean.CompanyProductCategory;
import com.blanink.bean.OneOrderProduct;
import com.blanink.bean.PartnerInfo;
import com.blanink.bean.RelIndustryCategoryAttribute;
import com.blanink.bean.Response;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.PriorityUtils;
import com.blanink.utils.StringToListUtils;
import com.blanink.view.NoScrollListview;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 修改供应商
 */
public class GoDownOrderProductDetailModifySupplier extends BaseActivity {
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.go_order_new_add_rl)
    RelativeLayout goOrderNewAddRl;
    @BindView(R.id.tv_supplier)
    TextView tvSupplier;
    @BindView(R.id.sp_supplier)
    Spinner spSupplier;
    @BindView(R.id.rl_supplier)
    RelativeLayout rlSupplier;
    @BindView(R.id.go_order_add_rl_supplier)
    LinearLayout goOrderAddRlSupplier;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.sp_product_category)
    Spinner spProductCategory;
    @BindView(R.id.rl_category)
    RelativeLayout rlCategory;
    @BindView(R.id.lv_product_attribute)
    NoScrollListview lvProductAttribute;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.et_num)
    TextView etNum;
    @BindView(R.id.rl_num)
    RelativeLayout rlNum;
    @BindView(R.id.tv_prioirty)
    TextView tvPrioirty;
    @BindView(R.id.sp_priority)
    TextView spPriority;
    @BindView(R.id.rl_prioirty)
    RelativeLayout rlPrioirty;
    @BindView(R.id.tv_price2)
    TextView tvPrice2;
    @BindView(R.id.et_price)
    TextView etPrice;
    @BindView(R.id.rl_price)
    RelativeLayout rlPrice;
    @BindView(R.id.hand_date)
    TextView handDate;
    @BindView(R.id.tv_hand_date)
    TextView tvHandDate;
    @BindView(R.id.rl_hand_date)
    RelativeLayout rlHandDate;
    @BindView(R.id.go_order_add_ll)
    LinearLayout goOrderAddLl;
    @BindView(R.id.lv_major)
    NoScrollListview lvMajor;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.et_note)
    TextView etNote;
    @BindView(R.id.rl_note)
    RelativeLayout rlNote;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.go_order_add_ll2)
    LinearLayout goOrderAddLl2;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.ll_save)
    LinearLayout llSave;
    private String currentSupplierId;
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
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    PhotoAdapter photoAdapter;
    private String bMasterId;
    private String orderProductStatus;
    private String resource;
    private String productName;
    public String bCompanyId;
    public String aOrderNumber;
    public String bOrderNumber;
    public String companyCategoryId;
    private String flag;
    private List<String> bMasterIdList = new ArrayList<>();
    private List<RelIndustryCategoryAttribute.ResultBean> attributeList = new ArrayList<>();
    private AttributesAdapter attributeAdapter;
    private List<String> priorityValue = new ArrayList<>();
    private List<String> priorityName = new ArrayList<>();
    private String priority;
    private String deliverTime;
    private String urls;
    OneOrderProduct orderProduct;
    private String aCompanyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_down_order_product_detail_modify_supplier);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        currentSupplierId = intent.getStringExtra("currentSupplierId");
        orderProduct = (OneOrderProduct) intent.getExtras().getSerializable("orderProduct");
        getCustmoer();
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spPriority.setText(PriorityUtils.getPriority(orderProduct.getResult().getCompanyAPriority()));

        tvHandDate.setText(orderProduct.getResult().getDeliveryTime());
        etNum.setText(orderProduct.getResult().getAmount());
        etPrice.setText(orderProduct.getResult().getPrice());
        etNote.setText(orderProduct.getResult().getRemarks());
        if (orderProduct.getResult().getImages() != null) {
            List<String> imageList = StringToListUtils.stringToList(orderProduct.getResult().getImages(), "\\|");

            for (String s : imageList) {
                selectedPhotos.add(s);
            }
        }
        photoAdapter = new PhotoAdapter(GoDownOrderProductDetailModifySupplier.this, selectedPhotos);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        //图片放大
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(3)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(GoDownOrderProductDetailModifySupplier.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(GoDownOrderProductDetailModifySupplier.this);
                        }
                    }
                }));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(bCompanyId)) {
                    Toast.makeText(GoDownOrderProductDetailModifySupplier.this, "请选择你供应商", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(companyCategoryId)) {
                    Toast.makeText(GoDownOrderProductDetailModifySupplier.this, "请选择产品类", Toast.LENGTH_SHORT).show();
                    return;
                }
                orderProductSpecificationList.clear();
                for (int i = 0; i < lvProductAttribute.getChildCount(); i++) {
                    final List<OrderProductSpecification> listEdit = new ArrayList<OrderProductSpecification>();
                    final OrderProductSpecification orderProductSpecification = new OrderProductSpecification();
                    final Attribute attribute = new Attribute();
                    LinearLayout layout = (LinearLayout) lvProductAttribute.getChildAt(i);// 获得子item的layout
                    if (GoDownOrderProductDetailModifySupplier.this.attribute.getResult().get(i).getInputType().equals("1")) {
                        EditText et = (EditText) layout.findViewById(R.id.et_attribute);
                        if (TextUtils.isEmpty(et.getText().toString())) {
                            Toast.makeText(GoDownOrderProductDetailModifySupplier.this, attributeList.get(i).getAttribute().getName()+"不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        attribute.setId(GoDownOrderProductDetailModifySupplier.this.attribute.getResult().get(i).getAttribute().getId());
                        orderProductSpecification.setAttribute(attribute);
                        orderProductSpecification.setAttributeValue(et.getText().toString());
                        listEdit.add(orderProductSpecification);
                        orderProductSpecificationList.addAll(listEdit);
                    }
                }
                //添加下拉框
                if (listSpinner != null) {
                    orderProductSpecificationList.addAll(listSpinner);
                }
                DialogLoadUtils.getInstance(GoDownOrderProductDetailModifySupplier.this);
                DialogLoadUtils.showDialogLoad("正在修改供应商...");

                modifySupplier(orderProductSpecificationList);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择返回
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            ArrayList<String> photos = null;
            selectedPhotos.clear();
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                selectedPhotos.addAll(photos);
                for (int i = 0; i < selectedPhotos.size(); i++) {

                    urls = urls + "|" + OssService.OSS_URL + "alioss_" + CommonUtil.getFileName(selectedPhotos.get(i));
                }
                if(selectedPhotos.size()>0){
                urls = urls.substring(1);}
                Log.e("ComeOrder", urls);

                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
                recyclerView.setAdapter(photoAdapter);

                photoAdapter.notifyDataSetChanged();
            }
        }
    }

    //选择供应商
    private void getCustmoer() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("companyA.id", sp.getString("COMPANY_ID", null));
        RetrofitFactory.getApiService(this)
                .partnerList(mapParams)
                .subscribeOn(Schedulers.io())
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
                        customerNameList.add("选择供应商");
                        customerIdList.add("");

                        if (partnerInfo.getResult().size() > 0) {
                            for (int i = 0; i < partnerInfo.getResult().size(); i++) {
                                if (!partnerInfo.getResult().get(i).getCompanyB().getId().equals(currentSupplierId)) {
                                    customerNameList.add(partnerInfo.getResult().get(i).getCompanyB().getName());
                                    customerIdList.add(partnerInfo.getResult().get(i).getCompanyB().getId());
                                }

                            }
                        }

                        if (partnerInfo.getResult().size() == 0) {
                            //  btnContiumeAdd.setText("你没有客户，无法添加订单");
                            // btnContiumeAdd.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        }
                        spSupplier.setAdapter(new ArrayAdapter<String>(GoDownOrderProductDetailModifySupplier.this, R.layout.spanner_item,customerNameList));
                        spSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                customer = customerNameList.get(position);
                                bCompanyId = customerIdList.get(position);
                                productCateGoryName.clear();
                                productCateGoryId.clear();
                                loadProductCategory(bCompanyId);
                            }


                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }


    //加载产品类
    public void loadProductCategory(String supplierId) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("company.id", supplierId);
        RetrofitFactory.getApiService(this)
                .categoryList(mapParams)
                .subscribeOn(Schedulers.io())
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

                        spProductCategory.setAdapter(new ArrayAdapter<String>(GoDownOrderProductDetailModifySupplier.this, R.layout.spanner_item
                                , productCateGoryName));

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
                .subscribeOn(Schedulers.io())
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
                        attribute = relIndustryCategoryAttribute;
                        attributeList.clear();
                        if (attribute.getResult() != null) {
                            attributeList.addAll(attribute.getResult());
                            attributeAdapter = new AttributesAdapter(attributeList);
                            lvProductAttribute.setAdapter(attributeAdapter);
                        } else {
                            attributeAdapter = new AttributesAdapter(attributeList);
                            lvProductAttribute.setAdapter(attributeAdapter);
                        }
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
                convertView = View.inflate(GoDownOrderProductDetailModifySupplier.this, R.layout.attributes, null);
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
                viewHolder.sp_attribute_value.setAdapter(new ArrayAdapter<String>(GoDownOrderProductDetailModifySupplier.this, R.layout.spanner_item, strHardCodeValue));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void modifySupplier(List<OrderProductSpecification> list) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("orderProdId4go", orderProduct.getResult().getId());
        mapParams.put("aCompany.id", sp.getString("COMPANY_ID", null));
        mapParams.put("currentUser.id",sp.getString("USER_ID",null));
        mapParams.put("orderList[0].orderProductList[0].companyCategory.id", companyCategoryId);
        for (int i = 0; i < list.size(); i++) {
            mapParams.put("orderList[0].orderProductList[0].orderProductSpecificationList[" + i + "].attribute.id", list.get(i).getAttribute().getId());
            mapParams.put("orderList[0].orderProductList[0].orderProductSpecificationList[" + i + "].attributeValue", list.get(i).getAttributeValue());
        }

        mapParams.put("bCompany.id", bCompanyId);
        RetrofitFactory.getApiService(this)
                .changeProvider(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(GoDownOrderProductDetailModifySupplier.this, "服务器开小车啦，稍后再试", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(Response response) {
                        if ("00000".equals(response.getErrorCode())) {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(GoDownOrderProductDetailModifySupplier.this, "修改供应商成功", Toast.LENGTH_SHORT).show();
                            showNotify(GoDownOrderProductDetailModifySupplier.this,"修改供应商成功");
                        }else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(GoDownOrderProductDetailModifySupplier.this, "修改失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public  void showNotify(final Context context, String tilte) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_apply_delete_relation);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        ((TextView)(window.findViewById(R.id.tv_message))).setText(tilte);
        window.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent=new Intent(GoDownOrderProductDetailModifySupplier.this,GoOrderPurchase.class);
                intent.putExtra("DIRECT",1);
                startActivity(intent);
                finish();
            }
        });
    }
}
