package com.blanink.ui.activity.lastnext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.BigSeekContent;
import com.blanink.bean.ProductAttributes;
import com.blanink.view.MyPager;
import com.blanink.view.NoScrollGridview;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 产品属性
 */
public class CompanyProductDetail extends BaseActivity {

    @BindView(R.id.activity_product_detail_iv_last)
    TextView Last;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.rl_activity_product_detail)
    RelativeLayout rlActivityProductDetail;
    @BindView(R.id.iv_product_picture)
    MyPager ivProductPicture;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.gv_keyField)
    NoScrollGridview gvKeyField;
    @BindView(R.id.product_detail)
    RelativeLayout productDetail;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.activity_product_detail)
    RelativeLayout activityProductDetail;
    private BigSeekContent.ResultBean.ListBean productDetail1;
    private SharedPreferences sp;
    private Map<String, String> map = new HashMap<>();

    private Map<Integer, Map<String, String>> maps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_product_detail);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        ButterKnife.bind(this);
        receivedData();
        initData();
    }

    private void initData() {
        //公司详情
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CompanyProductDetail.this,CompanyDetail.class);
                String customerId = productDetail1.getOffice().getId();
                intent.putExtra("companyId", customerId);
                intent.putExtra("companyName", productDetail1.getOffice().getName());
                intent.putExtra("companyType",productDetail1.getOffice().getServiceType());
                intent.putExtra("type",productDetail1.getOffice().getpType());
                startActivity(intent);
            }
        });
    }

    private void receivedData() {
        Intent intent = getIntent();
        productDetail1 = ((BigSeekContent.ResultBean.ListBean) intent.getExtras().getSerializable("ProductDetail"));
        loadProductAttributes();
        if(productDetail1.getCompanyProduct().getProductPhotos()!=null&&productDetail1.getCompanyProduct().getProductPhotos().length()>0&&productDetail1.getCompanyProduct().getProductPhotos()!="") {
            ivProductPicture.setVisibility(View.VISIBLE);
            String[] pics = productDetail1.getCompanyProduct().getProductPhotos().split(",");
            ivProductPicture.pictureRoll(pics);
        }
    }

    @OnClick({R.id.activity_product_detail_iv_last, R.id.tv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_product_detail_iv_last:
                finish();
                break;
            case R.id.tv_more:
//更多
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void loadProductAttributes() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyProduct.id", productDetail1.getCompanyProduct().getId());
        RetrofitFactory.getApiService(this)
                .getCompanyProductAttributes(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProductAttributes>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ProductAttributes productAttributes) {
                        if (productAttributes.getResult() != null) {
                            map.putAll(productAttributes.getResult());
                            Integer index = 0;
                            Log.e("ProductDetail", "map：" + map.toString());
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
                                //entry.getKey() ;entry.getValue(); entry.setValue();
                                //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
                                Map<String, String> mapNew = new HashMap<>();
                                System.out.println("key= " + entry.getKey() + " and value= "
                                        + entry.getValue());
                                mapNew.put(entry.getKey(), entry.getValue());
                                maps.put(index, mapNew);
                                index++;
                            }
                            tvProductName.setText(productDetail1.getCompanyProduct().getProductName());
                            tvPrice.setText(productDetail1.getCompanyProduct().getProductPriceDownline() + "-" + productDetail1.getCompanyProduct().getProductPriceHighline());
                            tvRemark.setText(productDetail1.getCompanyProduct().getProductDescription());
                            gvKeyField.setAdapter(new MapAdapter());
                            //属性
                        }

                    }
                });
    }

    class MapAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return maps.size();
        }

        @Override
        public Object getItem(int position) {
            return maps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(CompanyProductDetail.this, R.layout.item_product_attribute, null);
            TextView tv_attribute_name = (TextView) view.findViewById(R.id.tv_attribute_name);
            TextView tv_attribute_value = (TextView) view.findViewById(R.id.tv_attribute_value);
            Map<String, String> map = maps.get(position);
            Log.e("ProductDetail", "getView：" + map.toString() + "map.size():" + maps.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
                //entry.getKey() ;entry.getValue(); entry.setValue();
                //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
                System.out.println("key= " + entry.getKey() + " and value= "
                        + entry.getValue());
                tv_attribute_name.setText(entry.getKey() + ":");
                tv_attribute_value.setText(entry.getValue());
            }
            return view;
        }
    }
}
