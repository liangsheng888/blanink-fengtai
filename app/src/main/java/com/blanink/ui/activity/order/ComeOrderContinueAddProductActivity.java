package com.blanink.ui.activity.order;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blanink.R;
import com.blanink.bean.Attribute;
import com.blanink.bean.OrderProductSpecification;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.ui.adapter.PhotoAdapter;
import com.blanink.ui.adapter.RecyclerItemClickListener;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.ResponseOrder;
import com.blanink.oss.OssService;
import com.blanink.bean.CompanyProductCategory;
import com.blanink.bean.TypeCateGory;
import com.blanink.bean.RelIndustryCategoryAttribute;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.DialogNotifyUtils;
import com.blanink.view.NoScrollListview;
import com.google.gson.Gson;



import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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

/**
 * Created by Administrator on 2017/4/27.
 */
public class ComeOrderContinueAddProductActivity extends BaseActivity {

    @BindView(R.id.come_order_detail_tv_seek)
    TextView comeOrderDetailTvSeek;
    @BindView(R.id.tv_last)
    TextView tvLast;
    @BindView(R.id.come_order_add_product_rl)
    RelativeLayout comeOrderAddProductRl;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_order_hand_date)
    TextView tvOrderHandDate;
    @BindView(R.id.come_order_add_product_rl_company)
    LinearLayout comeOrderAddProductRlCompany;
    @BindView(R.id.tv_product_no)
    TextView tvProductNo;
    @BindView(R.id.et_product_no)
    EditText etProductNo;
    @BindView(R.id.tv_pro_category)
    TextView tvProCategory;
    @BindView(R.id.sp_product_category)
    Spinner spProductCategory;
    @BindView(R.id.gv_attributes)
    NoScrollListview gvAttributes;
    @BindView(R.id.tv_product)
    TextView tvProduct;
    @BindView(R.id.et_product_name)
    EditText etProductName;
    @BindView(R.id.tv_priority_a)
    TextView tvPriorityA;
    @BindView(R.id.sp_priority_a)
    Spinner spPriorityA;
    @BindView(R.id.tv_priority)
    TextView tvPriority;
    @BindView(R.id.sp_priority)
    Spinner spPriority;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.tv_hand_date)
    TextView tvHandDate;
    @BindView(R.id.tv_hand_date_time)
    TextView tvHandDateTime;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.rl_product_date)
    LinearLayout rlProductDate;
    @BindView(R.id.tv_hand_date_in)
    TextView tvHandDateIn;
    @BindView(R.id.tv_hand_date_time_in)
    TextView tvHandDateTimeIn;
    @BindView(R.id.rl_in)
    RelativeLayout rlIn;
    @BindView(R.id.rl_product_date_in)
    LinearLayout rlProductDateIn;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.come_order_add_product_ll)
    LinearLayout comeOrderAddProductLl;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.edt_price)
    EditText edtPrice;
    private SharedPreferences sp;
    private List<String> priorityValue = new ArrayList<>();
    private List<String> priorityName = new ArrayList<>();

    private ArrayList<String> selectedPhotos = new ArrayList<>();
    PhotoAdapter photoAdapter;
    private List<String> productCateGoryName = new ArrayList<>();
    private List<String> productCateGoryId = new ArrayList<>();
    public String customer;
    public String aCompanyId;//甲方公司id
    public String orderNumber;//订单编号
    public String orderDate;//订单交货日期
    private String dateIn;//内部交货日期
    private String date;//产品交货日期
    public String orderNote;//订单备注
    public String bMasterId;//接单人id
    private String priority_value;//内部优先级
    private String priority_value_a;//甲方优先级
    private String cateGoryId;//产品类Id
    public String amout;//产品数量
    public String productNote;//产品备注
    public String productNumber;//生产编号
    public String productName;//规格名称
    public String price;//产品价格
    private List<OrderProductSpecification> orderProductSpecificationList = new ArrayList<OrderProductSpecification>();
    final List<OrderProductSpecification> listSpinner = new ArrayList<OrderProductSpecification>();
    RelIndustryCategoryAttribute attribute;
    SparseArray<View> sparseArray;
    private Boolean isFirstAdd = true;//默认是第一次添加
    public String orderId;
    private OSSClient oss;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showNotifyTwo("保存订单成功", "继续添加产品", "我的订单");
        }
    };
    private String urls="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_come_order_add_product);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);


        oss = OssService.getOSSClientInstance(this);
        Intent intent = getIntent();
        customer = intent.getStringExtra("customer");
        orderNumber = intent.getStringExtra("orderNumber");
        orderDate = intent.getStringExtra("finishDate");
        orderNote = intent.getStringExtra("orderNote");
        aCompanyId = intent.getStringExtra("aCompanyId");
        bMasterId = intent.getStringExtra("bMasterId");
        orderId = intent.getStringExtra("orderId");
        tvCompanyName.setText(customer);
        tvOrderNumber.setText(orderNumber);
        tvOrderHandDate.setText(orderDate);
        loadPriority();//设置有优先级
        loadProductCategory();//显示产品
        //日期
        final Calendar calendar = Calendar.getInstance();

        tvHandDateTime.setText(calendar.get(calendar.YEAR) + "-" + (calendar.get(calendar.MONTH) + 1) + "-" + calendar.get(calendar.DAY_OF_MONTH));
        tvHandDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(ComeOrderContinueAddProductActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvHandDateTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        date = tvHandDateTime.getText().toString().trim();
                    }
                }, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        //内部交货日期
        tvHandDateTimeIn.setText(calendar.get(calendar.YEAR) + "-" + (calendar.get(calendar.MONTH) + 1) + "-" + calendar.get(calendar.DAY_OF_MONTH));
        tvHandDateTimeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(ComeOrderContinueAddProductActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvHandDateTimeIn.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        dateIn = tvHandDateTimeIn.getText().toString().trim();
                    }
                }, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        //返回
        tvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = etProductName.getText().toString().trim();
                productNumber = etProductNo.getText().toString().trim();
                productNote = etNote.getText().toString().trim();
                amout = edtNumber.getText().toString().trim();
                price = edtPrice.getText().toString().trim();
                if( TextUtils.isEmpty(productNote)
                        &&TextUtils.isEmpty(productName)
                        &&TextUtils.isEmpty(amout)
                        &&TextUtils.isEmpty(urls)
                        &&TextUtils.isEmpty(productNumber)
                        &&TextUtils.isEmpty(price)
                        ){
                    finish();
                }else {
                    DialogNotifyUtils.showNotifyDestroyActivity(ComeOrderContinueAddProductActivity.this,"继续","放弃","真的要放弃本次操作吗？");
                }            }
        });

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
                                    .start(ComeOrderContinueAddProductActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(ComeOrderContinueAddProductActivity.this);
                        }
                    }
                }));
        //保存订单
        btSave.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                orderProductSpecificationList.clear();
                for (int i = 0; i < gvAttributes.getChildCount(); i++) {
                    final List<OrderProductSpecification> listEdit = new ArrayList<OrderProductSpecification>();
                    final OrderProductSpecification orderProductSpecification = new OrderProductSpecification();
                    final Attribute attribute = new Attribute();
                    LinearLayout layout = (LinearLayout) gvAttributes.getChildAt(i);// 获得子item的layout
                    if (ComeOrderContinueAddProductActivity.this.attribute.getResult().get(i).getInputType().equals("1")) {
                        EditText et = (EditText) layout.findViewById(R.id.et_attribute);
                        attribute.setId(ComeOrderContinueAddProductActivity.this.attribute.getResult().get(i).getAttribute().getId());
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

                Log.e("ComeOrder", orderProductSpecificationList.size() + "___" + orderProductSpecificationList.toString());
                productName = etProductName.getText().toString().trim();
                productNumber = etProductNo.getText().toString().trim();
                productNote = etNote.getText().toString().trim();
                amout = edtNumber.getText().toString().trim();
                price = edtPrice.getText().toString().trim();
                date = tvHandDateTime.getText().toString().trim();
                dateIn = tvHandDateTimeIn.getText().toString().trim();
                if (TextUtils.isEmpty(productNumber)) {
                    Toast.makeText(ComeOrderContinueAddProductActivity.this, "请填写生产编号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(productName)) {
                    Toast.makeText(ComeOrderContinueAddProductActivity.this, "请输入产品名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(amout)) {
                    Toast.makeText(ComeOrderContinueAddProductActivity.this, "请输入产品数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(price)) {
                    Toast.makeText(ComeOrderContinueAddProductActivity.this, "请输入产品单价", Toast.LENGTH_SHORT).show();
                    return;
                }
                    if(!CommonUtil.isConnected(ComeOrderContinueAddProductActivity.this)){
                    Toast.makeText(ComeOrderContinueAddProductActivity.this, "请检查你的网络", Toast.LENGTH_SHORT).show();
                   return;
                }
                DialogLoadUtils.getInstance(ComeOrderContinueAddProductActivity.this);
                DialogLoadUtils.showDialogLoad("正在保存产品...");

                if(!CommonUtil.isConnected(ComeOrderContinueAddProductActivity.this)){
                    Toast.makeText(ComeOrderContinueAddProductActivity.this, "请检查你的网络", Toast.LENGTH_SHORT).show();
                    DialogLoadUtils.dismissDialog();
                    return;
                }
                saveOrder();
            }
        });

    }

    private void saveOrder() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
       mapParams.put("userId", sp.getString("USER_ID", null));
       mapParams.put("orderProductList[0].bCompany.id", sp.getString("COMPANY_ID", null));//乙方公司id
       mapParams.put("orderProductList[0].aCompany.id", aCompanyId);//甲方公司id
       mapParams.put("id",orderId);
        //mapParams.put("bOrderNumber", productNumber);//生产编号
       mapParams.put("orderProductList[0].companyCategory.id", cateGoryId);//产品类id
       mapParams.put("orderProductList[0].price", price);//价格
       mapParams.put("orderProductList[0].amount", amout);//数量
       mapParams.put("orderProductList[0].productDescription", productNote);//产品备注
       mapParams.put("orderProductList[0].remarks", productNote);//产品备注
       mapParams.put("orderProductList[0].productName", productName);//产品规格
       mapParams.put("orderProductList[0].deliveryTime", date);//产品交货日期
       mapParams.put("orderProductList[0].innerDeliveryTime", dateIn);//内部交货日期
       mapParams.put("orderProductList[0].companyAPriority", priority_value_a);//甲方优先级
       mapParams.put("orderProductList[0].companyBPriority", priority_value);//内部优先级
       mapParams.put("orderProductList[0].productSn", productNumber);//生产编号
        for(int i = 0; i< orderProductSpecificationList.size(); i++){
            mapParams.put("orderProductList[0].orderProductSpecificationList["+i+"].attribute.id", orderProductSpecificationList.get(i).getAttribute().getId());//产品图片
            mapParams.put("orderProductList[0].orderProductSpecificationList["+i+"].attributeValue", orderProductSpecificationList.get(i).getAttributeValue());//产品图片
        }
       //mapParams.put("orderProductSpecificationList", new Gson().toJson(orderProductSpecificationList));
       mapParams.put("orderProductList[0].images", urls);//产品图片
        RetrofitFactory.getApiService(this)
                .saveOrderProduct(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseOrder>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();

                    }

                    @Override
                    public void onNext(ResponseOrder responseOrder) {
                        if (responseOrder.getErrorCode().equals("00000")) {
                            List<String> photos=new ArrayList<String>();
                            photos.addAll(selectedPhotos);
                            uploadFiles(oss,photos);
                        }else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(ComeOrderContinueAddProductActivity.this, "添加产品失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //优先级
    public void loadPriority() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("type", "sys_assign_priority");
        RetrofitFactory.getApiService(this)
                .listValueByType(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TypeCateGory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TypeCateGory typeCateGory) {
                        for (int i = 0; i < typeCateGory.getResult().size(); i++) {
                            priorityValue.add(typeCateGory.getResult().get(i).getValue());
                            priorityName.add(typeCateGory.getResult().get(i).getLabel());
                        }
                        //内部优先级
                        spPriority.setAdapter(new ArrayAdapter<String>(ComeOrderContinueAddProductActivity.this, R.layout.spanner_item
                                ,priorityName));
                        //甲方优先级
                        spPriorityA.setAdapter(new ArrayAdapter<String>(ComeOrderContinueAddProductActivity.this, R.layout.spanner_item
                                ,priorityName));
                        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                priority_value = (position+1) + "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        spPriorityA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                priority_value_a = (position+1) + "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }

    //产品类
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
                        Gson gson = new Gson();
                        for (int i = 0; i < companyProductCategory.getResult().size(); i++) {
                            productCateGoryName.add(companyProductCategory.getResult().get(i).getName());
                            productCateGoryId.add(companyProductCategory.getResult().get(i).getId());
                        }

                        spProductCategory.setAdapter(new ArrayAdapter<String>(ComeOrderContinueAddProductActivity.this, R.layout.spanner_item
                                ,productCateGoryName));

                        spProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cateGoryId = productCateGoryId.get(position);
                                orderProductSpecificationList.clear();
                                if (orderProductSpecificationList.size() == 0) {
                                    loadProductAttributes(cateGoryId);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
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
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                selectedPhotos.clear();
                if (photos != null) {
                    selectedPhotos.addAll(photos);
                    for (int i = 0; i < selectedPhotos.size(); i++) {

                        urls = urls + "|" + OssService.OSS_URL+"alioss_"+ CommonUtil.getFileName(selectedPhotos.get(i)+ CommonUtil.getFileLastName(selectedPhotos.get(i)));
                    }
                    urls = urls.substring(1);
                    Log.e("ComeOrder",urls);
                }

                photoAdapter.notifyDataSetChanged();
            }
        }
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
                        attribute = relIndustryCategoryAttribute;
                        gvAttributes.setAdapter(new ComeOrderContinueAddProductActivity.AttributesAdapter(attribute.getResult()));
                    }
                });
    }

    //
    public void showNotifyTwo(String tilte, String btnLeft, String btnRight) {
        final AlertDialog alertDialog = new AlertDialog.Builder(ComeOrderContinueAddProductActivity.this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_bid);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        WindowManager windowManager = getWindowManager();
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
        lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        ((TextView) (window.findViewById(R.id.tv_message))).setText(tilte);
        ((TextView) (window.findViewById(R.id.tv_continue))).setText(btnLeft);
        ((TextView) (window.findViewById(R.id.tv_seek))).setText(btnRight);
        window.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        window.findViewById(R.id.tv_seek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent("com.blanink.REFRESH_DATA");
                it.putExtra("flag","REFRESH");
                sendBroadcast(it);
                Intent intent = new Intent(ComeOrderContinueAddProductActivity.this, ComeOrder.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });


    }

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
                convertView = View.inflate(ComeOrderContinueAddProductActivity.this, R.layout.attributes, null);
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
                viewHolder.sp_attribute_value.setAdapter(new ArrayAdapter<String>(ComeOrderContinueAddProductActivity.this, R.layout.simple_spinner_item, R.id.tv_name, strHardCodeValue));
                viewHolder.sp_attribute_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        listSpinner.clear();
                        orderProductSpecification.setAttribute(attribute);
                        orderProductSpecification.setAttributeValue(strHardCodeValue[position]);
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
    public  void uploadFiles(OSSClient oss,List<String> urls) {
        if (null == urls || urls.size() == 0) {
            DialogLoadUtils.dismissDialog();
            handler.sendEmptyMessage(0);
            return ;
        } // 上传文件
        ossUpload(oss,urls);
    }

    public void ossUpload(final OSSClient oss, final List<String> urls) {
        Log.e("ComeOrder","图片个数:"+urls.size());
        if (urls.size() <= 0) {
            Log.e("ComeOrder","通知提醒");
            DialogLoadUtils.dismissDialog();
            handler.sendEmptyMessage(0);
            // 文件全部上传完毕，这里编写上传结束的逻辑，如果要在主线程操作，最好用Handler或runOnUiThead做对应逻辑
            return ;// 这个return必须有，否则下面报越界异常，原因自己思考下哈
        }
        final String url = urls.get(0);
        if (TextUtils.isEmpty(url)) {
            urls.remove(0);
            // url为空就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(oss, urls);
            return ;
        }

        File file = new File(url);
        if (null == file || !file.exists()) {
            urls.remove(0);
            // 文件为空或不存在就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(oss, urls);
            return ;
        }
        // 文件后缀
        String fileSuffix = "";
        if (file.isFile()) {
            // 获取文件后缀名
            fileSuffix = CommonUtil.getFileName(url);
        }
        // 文件标识符objectKey
        final String objectKey = "alioss_"+ CommonUtil.getFileName(url)+ CommonUtil.getFileLastName(url);
        // 下面3个参数依次为bucket名，ObjectKey名，上传文件路径
        PutObjectRequest put = new PutObjectRequest("blanink", objectKey, url);

        // 设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                // 进度逻辑
            }
        });

        // 异步上传
        OSSAsyncTask task = oss.asyncPutObject(put,
                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) { // 上传成功
                        urls.remove(0);
                        ossUpload(oss, urls);// 递归同步效果
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                          ServiceException serviceException) { // 上传失败

                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e("ErrorCode", serviceException.getErrorCode());
                            Log.e("RequestId", serviceException.getRequestId());
                            Log.e("HostId", serviceException.getHostId());
                            Log.e("RawMessage", serviceException.getRawMessage());
                        }
                    }
                });
        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待直到任务完成
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            productName = etProductName.getText().toString().trim();
            productNumber = etProductNo.getText().toString().trim();
            productNote = etNote.getText().toString().trim();
            amout = edtNumber.getText().toString().trim();
            price = edtPrice.getText().toString().trim();
            if( TextUtils.isEmpty(productNote)
                    &&TextUtils.isEmpty(productName)
                    &&TextUtils.isEmpty(amout)
                    &&TextUtils.isEmpty(urls)
                    &&TextUtils.isEmpty(productNumber)
                    &&TextUtils.isEmpty(price)
                    ){
                finish();
            }else {
                DialogNotifyUtils.showNotifyDestroyActivity(ComeOrderContinueAddProductActivity.this,"继续","放弃","真的要放弃本次操作吗？");
            }
        }

        return false;
    }
}
