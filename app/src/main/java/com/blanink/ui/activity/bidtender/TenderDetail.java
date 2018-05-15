package com.blanink.ui.activity.bidtender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.BigSeekContent;
import com.blanink.bean.TenderAndBid;
import com.blanink.ui.activity.AttachmentBrow;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.StringToListUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * 符合招标列表   招标详情
 */
public class TenderDetail extends BaseActivity {
    @BindView(R.id.bid_detail_iv_last)
    TextView bidDetailIvLast;
    @BindView(R.id.bid_detail_rl)
    RelativeLayout bidDetailRl;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_bids)
    TextView tvBids;
    @BindView(R.id.tv_bids_num)
    TextView tvBidsNum;
    @BindView(R.id.end_date)
    TextView endDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.tv_company_url)
    TextView tvCompanyUrl;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_single_price)
    TextView tvSinglePrice;
    @BindView(R.id.single_price)
    TextView singlePrice;
    @BindView(R.id.tv_purchase)
    TextView tvPurchase;
    @BindView(R.id.tv_purchase_num)
    TextView tvPurchaseNum;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_first_pay)
    TextView tvFirstPay;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.tv_note_content)
    TextView tvNoteContent;
    @BindView(R.id.attactment)
    TextView attactment;
    @BindView(R.id.tv_attactment)
    TextView tvAttactment;
    @BindView(R.id.rl_down)
    LinearLayout rlDown;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_publish_date)
    TextView tvPublishDate;
    @BindView(R.id.ll_bid_detail)
    LinearLayout llBidDetail;
    @BindView(R.id.bt_bid)
    Button btBid;
    @BindView(R.id.activity_tender_detail)
    RelativeLayout activityTenderDetail;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.product)
    TextView product;
    private TenderAndBid.Result.Row row;
    private SharedPreferences sp;
    private TextView back;
    private TextView tv_product_name;
    private TextView tv_bids_num;
    private TextView tv_total_price;
    private TextView tv_company;
    private TextView tv_url;
    private TextView tv_single_price;
    private TextView tv_purchase_num;
    private TextView tv_first_pay;
    private TextView tv_end_date;
    private TextView tv_note_content;
    private TextView tv_publish_date;
    private Button bt_bid;
    private Button btn_chat;
    private BigSeekContent.ResultBean.ListBean bidDetailInfo;
    private TextView tv_company_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender_detail);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        receivedIntentInfo();
        initView();
        initData();
    }

    private void receivedIntentInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (intent.getIntExtra("flag", 0) != 1) {
            row = (TenderAndBid.Result.Row) bundle.getSerializable("TenderDetail");
            Log.e("TenderDetail", row.toString());
        } else {
            bidDetailInfo = ((BigSeekContent.ResultBean.ListBean) bundle.getSerializable("BidDetail"));
        }
    }

    private void initView() {
        back = ((TextView) findViewById(R.id.bid_detail_iv_last));
        tv_product_name = ((TextView) findViewById(R.id.tv_product_name));
        tv_bids_num = ((TextView) findViewById(R.id.tv_bids_num));
        tv_total_price = ((TextView) findViewById(R.id.tv_total_price));
        tv_company = ((TextView) findViewById(R.id.tv_company));
        tv_url = ((TextView) findViewById(R.id.tv_url));
        tv_single_price = ((TextView) findViewById(R.id.tv_single_price));
        tv_purchase_num = ((TextView) findViewById(R.id.tv_purchase_num));
        tv_first_pay = ((TextView) findViewById(R.id.tv_first_pay));
        tv_end_date = ((TextView) findViewById(R.id.tv_end_date));
        tv_note_content = ((TextView) findViewById(R.id.tv_note_content));
        tv_publish_date = ((TextView) findViewById(R.id.tv_publish_date));
        bt_bid = ((Button) findViewById(R.id.bt_bid));
        tv_company_url = ((TextView) findViewById(R.id.tv_company_url));

    }

    private void initData() {

        //set data
        if (row != null) {
            tv_product_name.setText(row.buyProductName);
            tvTitle.setText(row.title);
            tv_bids_num.setText(row.bidList.size() + "");
            tv_total_price.setText((Double.parseDouble(row.targetPrice) * row.count) + "");
            tv_company.setText(row.inviteCompany.name);
            tv_single_price.setText(row.targetPrice);//单价
            tv_purchase_num.setText(row.count + "");
            tv_note_content.setText(Html.fromHtml(row.remarks));
            tv_first_pay.setText(row.downPayment + "%");
            tv_publish_date.setText(row.inviteDate);
            tv_end_date.setText(CommonUtil.dateToString(CommonUtil.stringToDate(row.expireDate)));
            if (row.inviteCompany.homepage != null) {
                SpannableString spannableString = new SpannableString(row.inviteCompany.homepage);

                URLSpan urlSpan = new URLSpan(row.inviteCompany.homepage);
                spannableString.setSpan(urlSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tv_company_url.setMovementMethod(LinkMovementMethod.getInstance());
                tv_company_url.setHighlightColor(Color.parseColor("#36969696"));
                tv_company_url.setText(spannableString);
            }
            List<String> arrayList = null;
            if (row.attachment != null && row.attachment != "" && !"".equals(row.attachment)) {
                arrayList = StringToListUtils.stringToList(row.attachment, "\\|");
            } else {
                arrayList = new ArrayList<>();
            }


            final List<String> finalArrayList = arrayList;
            if (finalArrayList.size() == 0) {
                tvAttactment.setText("无附件");
                tvAttactment.setTextColor(getResources().getColor(R.color.gray));
                tvAttactment.setBackground(getResources().getDrawable(R.drawable.corner_common_border_gray));
            } else {
                tvAttactment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TenderDetail.this, AttachmentBrow.class);
                        intent.putExtra("imageList", new Gson().toJson(finalArrayList));
                        startActivity(intent);
                    }
                });
            }
        }
        if (bidDetailInfo != null) {
            //大搜索点击进入的详情
            tv_bids_num.setText(bidDetailInfo.getBidCount() + "");
            tv_total_price.setText((Double.parseDouble(bidDetailInfo.getInviteBid().getTargetPrice()) * bidDetailInfo.getInviteBid().getCount()) + "");
            tv_company.setText(bidDetailInfo.getInviteBid().getInviteCompany().getName());
            tv_product_name.setText(bidDetailInfo.getInviteBid().getBuyProductName());
            tv_single_price.setText(bidDetailInfo.getInviteBid().getTargetPrice());
            tvTitle.setText(bidDetailInfo.getInviteBid().getTitle());
            tv_purchase_num.setText(bidDetailInfo.getInviteBid().getCount() + "");
            tv_first_pay.setText(bidDetailInfo.getInviteBid().getDownPayment() + "%");
            tv_publish_date.setText(CommonUtil.dateToString(CommonUtil.stringToDate(bidDetailInfo.getInviteBid().getInviteDate())));
            tv_end_date.setText(CommonUtil.dateToString(CommonUtil.stringToDate(bidDetailInfo.getInviteBid().getExpireDate())));
            tv_note_content.setText(bidDetailInfo.getInviteBid().getRemarks());

            SpannableString spannableString = new SpannableString(bidDetailInfo.getInviteBid().getInviteCompany().getHomepage());

            URLSpan urlSpan = new URLSpan(bidDetailInfo.getInviteBid().getInviteCompany().getHomepage());
            spannableString.setSpan(urlSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_company_url.setMovementMethod(LinkMovementMethod.getInstance());
            tv_company_url.setHighlightColor(Color.parseColor("#36969696"));
            tv_company_url.setText(spannableString);
          /*  SpannableString spannableString = new SpannableString("http://www.jianshu.com/users/dbae9ac95c78");

            URLSpan urlSpan = new URLSpan("http://www.jianshu.com/users/dbae9ac95c78");
            spannableString.setSpan(urlSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_company_url.setMovementMethod(LinkMovementMethod.getInstance());
            tv_company_url.setHighlightColor(Color.parseColor("#36969696"));*/


        }

        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //我要投标
        Log.e("TenderDetail", "BidId:" + sp.getString("COMPANY_ID", null));
        if (row != null) {
            if (row.inviteCompany.id.equals(sp.getString("COMPANY_ID", null))) {
                bt_bid.setText("该招标信息是你公司发布的，不能投标");
                bt_bid.setEnabled(false);
                bt_bid.setBackgroundColor(Color.GRAY);
            }
        }
        if (bidDetailInfo != null) {
            if (bidDetailInfo.getInviteBid().getInviteCompany().getId().equals(sp.getString("COMPANY_ID", null))) {
                bt_bid.setText("该招标信息是你公司发布的，不能投标");
                bt_bid.setEnabled(false);
                bt_bid.setBackgroundColor(Color.GRAY);
            }
        }
        //如果已失效 ，不能投标
        if (row != null) {
            if (CommonUtil.compare_date(row.expireDate, CommonUtil.dateToString(new Date(System.currentTimeMillis()))) < 0) {
                bt_bid.setText("本次招标已失效，你不能投标！");
                bt_bid.setEnabled(false);
                bt_bid.setBackgroundColor(Color.GRAY);
            }
        }
        bt_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(TenderDetail.this, "taio", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TenderDetail.this, BidApplication.class);
                intent.putExtra("userId", sp.getString("USER_ID", null));
                if (row != null) {
                    intent.putExtra("inviteBid.id", row.id);
                    intent.putExtra("createBy", row.createBy.id);
                }
                if (bidDetailInfo != null) {
                    intent.putExtra("inviteBid.id", bidDetailInfo.getInviteBid().getId());
                    intent.putExtra("createBy", bidDetailInfo.getInviteBid().getCreateBy().getId());

                }
                startActivity(intent);
            }
        });

        if (bidDetailInfo != null) {
            if (CommonUtil.compare_date(bidDetailInfo.getInviteBid().getExpireDate(), CommonUtil.dateToString(new Date(System.currentTimeMillis()))) < 0) {
                bt_bid.setText("本次招标已失效，你不能投标！");
                bt_bid.setEnabled(false);
                bt_bid.setBackgroundColor(Color.GRAY);
            }
        }
        //如果投标公司的id 在bidList 中 表明 已投标 不能再次投标
        if (row != null) {
            if (row.bidList.size() > 0) {
                for (int i = 0; i < row.bidList.size(); i++) {
                    if (sp.getString("COMPANY_ID", null).equals(row.bidList.get(i).bidCompany.id)) {
                        bt_bid.setText("你已经投过标了，不能再次投标");
                        bt_bid.setEnabled(false);
                        bt_bid.setBackgroundColor(Color.GRAY);
                    }
                }
            }
        }
        if (bidDetailInfo != null) {
            if (bidDetailInfo.getMyBidCount() > 0) {
                bt_bid.setText("你已经投过标了，不能再次投标");
                bt_bid.setEnabled(false);
                bt_bid.setBackgroundColor(Color.GRAY);
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
