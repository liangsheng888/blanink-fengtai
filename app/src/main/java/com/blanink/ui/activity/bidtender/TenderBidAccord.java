package com.blanink.ui.activity.bidtender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.AttachmentBrow;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.Response;
import com.blanink.bean.ResponseDelete;
import com.blanink.bean.TenderAndBid;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.StringToListUtils;
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

/***
 * 招标  确定合作
 */
public class TenderBidAccord extends BaseActivity {


    @BindView(R.id.bid_detail_iv_last)
    TextView bidDetailIvLast;
    @BindView(R.id.bid_detail_rl)
    RelativeLayout bidDetailRl;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.bid_date)
    TextView bidDate;
    @BindView(R.id.tv_bid_date)
    TextView tvBidDate;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_unit_price)
    TextView tvUnitPrice;
    @BindView(R.id.tv_single_cost)
    TextView tvSingleCost;
    @BindView(R.id.purchase_num)
    TextView purchaseNum;
    @BindView(R.id.tv_purchase_num)
    TextView tvPurchaseNum;
    @BindView(R.id.tv_first_price)
    TextView tvFirstPrice;
    @BindView(R.id.tv_first_cost)
    TextView tvFirstCost;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.attactment)
    TextView attactment;
    @BindView(R.id.tv_attactment)
    TextView tvAttactment;
    @BindView(R.id.rl_down)
    LinearLayout rlDown;
    @BindView(R.id.tv_note_detail)
    TextView tvNoteDetail;
    @BindView(R.id.tv_note_detail_content)
    TextView tvNoteDetailContent;
    @BindView(R.id.ll_my_bid_detail)
    LinearLayout llMyBidDetail;
    @BindView(R.id.btn_chat)
    Button btnChat;
    @BindView(R.id.btn_become_partner)
    Button btnBecomePartner;
    @BindView(R.id.activity_bender_bid_detail)
    RelativeLayout activityBenderBidDetail;
    private TenderAndBid.Result.Row.Bid bidCompany;
    private TextView tv_name;
    private TextView tv_company_name;
    private TextView tv_bid_date;
    private TextView tv_single_cost;
    private TextView tv_purchase_num;
    private TextView tv_first_cost;
    private TextView tv_total;
    private TextView tv_note_detail_content;
    private Button btn_become_partner;
    private Button btn_chat;
    private String downPayment;
    private String count;
    private SharedPreferences sp;
    private String id;
    AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender_bid_detail);
        ButterKnife.bind(this);
        receivedData();
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initView();
        initData();
    }

    private void initView() {
        tv_name = ((TextView) findViewById(R.id.tv_name));//负责人
        tv_company_name = ((TextView) findViewById(R.id.tv_company_name));//公司名称
        tv_bid_date = ((TextView) findViewById(R.id.tv_bid_date));//投标日期
        tv_single_cost = ((TextView) findViewById(R.id.tv_single_cost));//单件
        tv_purchase_num = ((TextView) findViewById(R.id.tv_purchase_num));//采购数量
        tv_first_cost = ((TextView) findViewById(R.id.tv_first_cost));//预付定金
        tv_total = ((TextView) findViewById(R.id.tv_total));//总价
        tv_note_detail_content = ((TextView) findViewById(R.id.tv_note_detail_content));//备注
        btn_become_partner = ((Button) findViewById(R.id.btn_become_partner));//合作
        btn_chat = ((Button) findViewById(R.id.btn_chat));
    }

    private void initData() {
        //set data
        tv_name.setText(bidCompany.bidCompany.master);
        tv_company_name.setText(bidCompany.bidCompany.name);
        tv_bid_date.setText(CommonUtil.dateToString(CommonUtil.stringToDate(bidCompany.bidDate)));
        tv_purchase_num.setText(count);
        tv_total.setText((Integer.parseInt(bidCompany.bidPrice) * Integer.parseInt(count)) + "元");
        tv_note_detail_content.setText(bidCompany.remarks);
        tv_first_cost.setText(downPayment + "%");
        tv_single_cost.setText(bidCompany.bidPrice);
        //返回
        bidDetailIvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //确定合作
        btn_become_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(TenderBidAccord.this).create();
                alertDialog.show();
                alertDialog.setContentView(R.layout.dialog_custom_accord);
                final Window window = alertDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                Display d = getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
                //  lp.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
                lp.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的1/2
                window.setWindowAnimations(R.style.dialogAnimation);
                window.setAttributes(lp);
                window.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogLoadUtils.getInstance(TenderBidAccord.this);
                        DialogLoadUtils.showDialogLoad("操作进行中...");
                        upLoadData();

                    }
                });
                window.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });
            }

        });
        //联系对方
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenderBidAccord.this, TenderDetailChatHistory.class);
                intent.putExtra("bid.id", bidCompany.id);
                intent.putExtra("inviteBid.id", bidCompany.inviteBid.id);
                intent.putExtra("createBy", bidCompany.createBy.id);
                intent.putExtra("companyName", bidCompany.bidCompany.name);
                startActivity(intent);
            }
        });

        List<String> arrayList = null;
        if (bidCompany.attachment != null && bidCompany.attachment != "" && !"".equals(bidCompany.attachment)) {
            arrayList = StringToListUtils.stringToList(bidCompany.attachment, ",");
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
                    Intent intent = new Intent(TenderBidAccord.this, AttachmentBrow.class);
                    intent.putExtra("imageList", new Gson().toJson(finalArrayList));
                    startActivity(intent);
                }
            });
        }

    }

    public void upLoadData() {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("userId", sp.getString("USER_ID", null));
        mapParams.put("companyA.id", sp.getString("COMPANY_ID", null));
        mapParams.put("companyB.id", bidCompany.bidCompany.id);
        mapParams.put("id", bidCompany.id);
        RetrofitFactory.getApiService(this)
                .savePartner(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(TenderBidAccord.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response response) {
                        DialogLoadUtils.dismissDialog();
                        if (response.getErrorCode().equals("00000")) {
                            //向对方发送通知 对方成为潜在合作关系
                            Map<String, Object> paramsMap = new HashMap<String, Object>();
                            paramsMap.put("currentUser.id", sp.getString("USER_ID", null));
                            paramsMap.put("createBy.id", bidCompany.createBy.id);
                            paramsMap.put("id", bidCompany.id);
                            //激光推送
                            RetrofitFactory.getApiService(TenderBidAccord.this)
                                    .bidAccord(paramsMap)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<ResponseDelete>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(ResponseDelete responseDelete) {
                                            Log.e("ReceiveGoods", responseDelete.getReason());

                                        }
                                    });
                            alertDialog.dismiss();
                            Toast.makeText(TenderBidAccord.this, "合作成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(TenderBidAccord.this, "合作失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void receivedData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bidCompany = (TenderAndBid.Result.Row.Bid) bundle.getSerializable("BidCompany");
        downPayment = intent.getStringExtra("downPayment");
        count = intent.getStringExtra("count");
        id = intent.getStringExtra("inviteCompany.id");//招标公司Id
        Log.e("TenderBidAccord", bidCompany.toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * dp to px
     */
    protected int dp2px(float dp) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
