package com.blanink.ui.activity.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.bean.Advertise;
import com.blanink.utils.BaseUrlUtils;
import com.blanink.utils.UrlPic;
import com.blanink.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportType extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.table)
    RelativeLayout table;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.tv_cost)
    TextView tvCost;
    @BindView(R.id.pay)
    TextView pay;
    @BindView(R.id.pager)
    MyViewPager pager;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_loss)
    TextView tvLoss;
    @BindView(R.id.ll)
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_type);
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
        List<Advertise> picLis = new ArrayList<>();
        picLis.add(new Advertise(UrlPic.pic,UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic2,UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic3,UrlPic.baseUrl));
        picLis.add(new Advertise(UrlPic.pic4,UrlPic.baseUrl));
        pager.pictureRoll(ReportType.this,picLis);
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportType.this, SaleAmountAnalysis.class);
                intent.putExtra("url", BaseUrlUtils.NET_URL + "report/saleDataList");
                intent.putExtra("name", "销售额分析");
                startActivity(intent);
            }
        });

        tvCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportType.this, CostAmountAnalysis.class);
                intent.putExtra("name", "成本分析");
                startActivity(intent);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportType.this, PayAmountAnalysis.class);
                startActivity(intent);
            }
        });
        tvProfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportType.this, ProfitAnalysis.class);
                startActivity(intent);
            }
        });

        tvLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportType.this, LossAnalysis.class);
                startActivity(intent);
            }
        });
    }
}
