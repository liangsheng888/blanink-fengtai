package com.blanink.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.FlowProgress;
import com.blanink.utils.FlowProcessStatusUtils;
import com.blanink.view.RoundProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class FlowProgressAdapter extends BaseAdapter {
    private Context context;
    private List<FlowProgress.ResultBean> list;
    private SparseArray<View> sparseArray;

    public FlowProgressAdapter(Context context, List<FlowProgress.ResultBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sparseArray = new SparseArray<>();
        ViewHolder viewHolder=null;
        if (sparseArray.get(position, null) == null) {

            convertView = View.inflate(context, R.layout.item_flow_progress, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            sparseArray.put(position,convertView);

        } else {
            convertView=sparseArray.get(position);
            viewHolder=(ViewHolder)convertView.getTag();
        }
        FlowProgress.ResultBean  flowProgress=list.get(position);
        if (flowProgress.getTarget() != 0) {
            viewHolder.pbProgress.setMax(flowProgress.getTarget());
        }
        viewHolder.pbProgress.setCricleColor(context.getResources().getColor(R.color.colorBackGround));
        viewHolder.pbProgress.setCricleProgressColor(context.getResources().getColor(R.color.colorTheme));
        viewHolder.pbProgress.setTextColor(Color.RED);
        viewHolder.pbProgress.setTextSize(18);
        viewHolder.pbProgress.setProgress(flowProgress.getTotalCompletedQuantity());
        viewHolder.tvProcess.setText(flowProgress.getProcess().getName());
        viewHolder.tvProgress.setText(flowProgress.getTotalCompletedQuantity() + "/" + flowProgress.getTarget());
        viewHolder.tvState.setText(FlowProcessStatusUtils.getProcessStatus(flowProgress.getStatus()));
        if ("3".equals(flowProgress.getStatus())) {
            viewHolder.tvState.setTextColor(Color.BLUE);
        }
        if (flowProgress.getLatestFeedback() != null) {
            viewHolder.tvFaultAmount.setText(flowProgress.getLatestFeedback().getFaultAmount() + "");
        } else {
            viewHolder.bad.setVisibility(View.GONE);
            viewHolder.tvFaultAmount.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.pb_progress)
        RoundProgressBar pbProgress;
        @BindView(R.id.progress)
        TextView progress;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_process)
        TextView tvProcess;
        @BindView(R.id.bad)
        TextView bad;
        @BindView(R.id.tv_faultAmount)
        TextView tvFaultAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
