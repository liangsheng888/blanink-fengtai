package com.blanink.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.order.ReceiveGoodsProductReceiveQueue;
import com.blanink.bean.Dashboard;
import com.blanink.view.RoundProgress;

import java.util.List;

/**
 * 作者：Created by liangshen on 2018/1/12 0012 15:16
 * 邮箱：liangshen6868@163.com
 */
public class DashboardPurchaseGridAdapter extends RecyclerView.Adapter<DashboardPurchaseGridAdapter.DashboardViewHolder> {
    private Context context;
    private List<Dashboard.ResultBean.RowsBean.RelFlowProcessBean> relFlowProcessBeanList;
    private String orderProductId;

    public DashboardPurchaseGridAdapter(Context context, List<Dashboard.ResultBean.RowsBean.RelFlowProcessBean> relFlowProcessBeanList, String orderProductId) {
        this.context = context;
        this.relFlowProcessBeanList = relFlowProcessBeanList;
        this.orderProductId = orderProductId;
    }

    @Override
    public DashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_process, parent, false);
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DashboardViewHolder holder, final int position) {
        if("3".equals(relFlowProcessBeanList.get(position).getProcess().getType())){
            holder.tvProcessName.setTextColor(Color.RED);
        }
        holder.tvProcessName.setText(relFlowProcessBeanList.get(position).getProcess().getName());
        if (relFlowProcessBeanList.get(position).getTarget() != 0) {
            holder.progressBar.setMax(relFlowProcessBeanList.get(position).getTarget());
        }
        holder.progressBar.setCricleColor(context.getResources().getColor(R.color.colorBackGround));
        holder.progressBar.setTextColor(context.getResources().getColor(R.color.gray_text));
        holder.progressBar.setTextSize(25);
        holder.progressBar.setProgress(relFlowProcessBeanList.get(position).getTotalCompletedQuantity());
        holder.llProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("3".equals(relFlowProcessBeanList.get(position).getProcess().getType())){
                    Intent intent = new Intent(context, ReceiveGoodsProductReceiveQueue.class);
                    intent.putExtra("orderProductId",orderProductId);
                    context.startActivity(intent);

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return relFlowProcessBeanList.size();
    }

    public static class DashboardViewHolder extends RecyclerView.ViewHolder {
        private RoundProgress progressBar;
        private TextView tvProcessName;
        private LinearLayout llProcess;

        public DashboardViewHolder(View itemView) {
            super(itemView);
            progressBar = (RoundProgress) itemView.findViewById(R.id.rp);
            tvProcessName = (TextView) itemView.findViewById(R.id.tv_name);
            llProcess=(LinearLayout)itemView.findViewById(R.id.ll_process);
        }
    }
}
