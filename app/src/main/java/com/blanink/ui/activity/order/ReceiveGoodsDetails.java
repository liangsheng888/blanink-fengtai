package com.blanink.ui.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.AttachmentBrow;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.bean.ProductDeliverInfo;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.StringToListUtils;
import com.blanink.view.NoScrollListview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * 发货详情
 */
public class ReceiveGoodsDetails extends BaseActivity {


    @BindView(R.id.come_order)
    TextView comeOrder;
    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.come_order_detail_rl)
    RelativeLayout comeOrderDetailRl;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_product_stu)
    TextView tvProductStu;
    @BindView(R.id.btn_flow)
    Button btnFlow;
    @BindView(R.id.item_come_order_detail_product)
    LinearLayout itemComeOrderDetailProduct;
    @BindView(R.id.tv_product_no)
    TextView tvProductNo;
    @BindView(R.id.ruler)
    TextView ruler;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv_ruler)
    TextView tvRuler;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.proCateGory)
    TextView proCateGory;
    @BindView(R.id.order_detail_ll_proCateGory)
    LinearLayout orderDetailLlProCateGory;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_sender_num)
    TextView tvSenderNum;
    @BindView(R.id.order_detail_rl2)
    LinearLayout orderDetailRl2;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_endDateHand)
    TextView tvEndDateHand;
    @BindView(R.id.tv_deliver_time)
    TextView tvDeliverTime;
    @BindView(R.id.order_detail_rl3)
    LinearLayout orderDetailRl3;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_sender_time)
    TextView tvSenderTime;
    @BindView(R.id.tv_sender)
    TextView tvSender;
    @BindView(R.id.tv_sender_name)
    TextView tvSenderName;
    @BindView(R.id.product_attribute)
    NoScrollListview productAttribute;
    @BindView(R.id.attactment)
    TextView attactment;
    @BindView(R.id.tv_attactment)
    TextView tvAttactment;
    @BindView(R.id.rl_down)
    RelativeLayout rlDown;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.web_content)
    WebView webContent;
    @BindView(R.id.order_detail_ll_note)
    LinearLayout orderDetailLlNote;
    @BindView(R.id.activity_come_order_product_detail)
    RelativeLayout activityComeOrderProductDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_goods_details);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ProductDeliverInfo.ResultBean.RelFlowProcessListBean.ProcessFeedbackListBean  processFeedBack=(ProductDeliverInfo.ResultBean.RelFlowProcessListBean.ProcessFeedbackListBean)getIntent().getSerializableExtra("processFeedBack");
        String json=getIntent().getStringExtra("attribute");
        final List<ProductDeliverInfo.ResultBean.OrderProductSpecificationListBean> list=new Gson().fromJson(json,new TypeToken<List<ProductDeliverInfo.ResultBean.OrderProductSpecificationListBean>>(){}.getType());
        productAttribute.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(ReceiveGoodsDetails.this, R.layout.item_product_attribute, null);
                TextView tv_attribute_name = ((TextView) view.findViewById(R.id.tv_attribute_name));
                TextView tv_attribute_value = ((TextView) view.findViewById(R.id.tv_attribute_value));
                tv_attribute_name.setText(list.get(position).getAttribute().getName() + ":");
                tv_attribute_value.setText(list.get(position).getAttributeValue());

                return view;
            }
        });

        tvRuler.setText(getIntent().getStringExtra("productName"));
        proCateGory.setText(getIntent().getStringExtra("companyCateGory"));
        tvSenderName.setText(processFeedBack.getFeedbackUser().getName());
        tvSenderNum.setText(processFeedBack.getAchieveAmount()+"");
        tvSenderTime.setText(processFeedBack.getCreateDate());
        tvDeliverTime.setText(getIntent().getStringExtra("deliverTime"));
        tvNum.setText(getIntent().getStringExtra("amount"));
        tvProductNo.setText(getIntent().getStringExtra("productNo"));
        if (getIntent().getStringExtra("photo") != null && getIntent().getStringExtra("photo") != null&&!"".equals(getIntent().getStringExtra("photo"))) {
        GlideUtils.glideImageView(this,ivPhoto,getIntent().getStringExtra("photo"),true);}
        tvProductStu.setText(getIntent().getStringExtra("productStus"));
            webContent.getSettings().setDefaultTextEncodingName("UTF -8");
            webContent.getSettings().setJavaScriptEnabled(true);
            webContent.getSettings().setDefaultFontSize(14);
            webContent.loadData(processFeedBack.getRemarks(), "text/html; charset=UTF-8", null);
        //tvTarget.setText(getIntent().getIntExtra("target", 0) + "");
        List<String> arrayList = null;
        if (processFeedBack.getFeedbackAttachmentStr() != null && processFeedBack.getFeedbackAttachmentStr() != "" && !"".equals(processFeedBack.getFeedbackAttachmentStr())) {
            arrayList = StringToListUtils.stringToList(processFeedBack.getFeedbackAttachmentStr(), "|");
        } else {
            arrayList = new ArrayList<>();
        }


        final List<String> finalArrayList = arrayList;
        if (finalArrayList.size() == 0) {
            tvAttactment.setText("无附件");
            tvAttactment.setTextColor(getResources().getColor(R.color.gray));
            tvAttactment.setBackground(getResources().getDrawable(R.drawable.corner__gray));
        } else {
            // final List<String> stringList = StringToListUtils.stringToList(getIntent().getStringExtra("images"), ",");
            tvAttactment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ReceiveGoodsDetails.this, AttachmentBrow.class);
                    intent.putExtra("imageList", new Gson().toJson(finalArrayList));
                    startActivity(intent);
                }
            });
        }


    }
}