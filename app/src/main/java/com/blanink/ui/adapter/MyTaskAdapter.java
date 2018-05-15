package com.blanink.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.OrderProduct;
import com.blanink.ui.activity.stock.OutInStockAdd;
import com.blanink.ui.activity.task.TaskResponseDeliver;
import com.blanink.ui.activity.task.TaskResponseMine;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.PriorityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/15 0015.
 */
public class MyTaskAdapter extends BaseAdapter {
    private Context context;
    List<OrderProduct.ResultBean.RowsBean> list;
    SparseArray<View> sparseArray;
    private String processType, processId;

    public MyTaskAdapter(Context context, List<OrderProduct.ResultBean.RowsBean> list, String processType, String processId) {
        this.context = context;
        this.list = list;
        this.processType = processType;
        this.processId = processId;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        sparseArray = new SparseArray<>();
        ViewHolder viewHolder = null;
        if (sparseArray.get(position, null) == null) {
            convertView = View.inflate(context, R.layout.item_my_task, null);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            sparseArray.put(position, convertView);

        } else {
            convertView = sparseArray.get(position);
            viewHolder = (ViewHolder) convertView.getTag();

        }
        final OrderProduct.ResultBean.RowsBean result = list.get(position);
        viewHolder.tvCompanyName.setText(result.getCompanyA().getShortName() == null ? result.getCompanyA().getName() : result.getCompanyA().getShortName());
        if (result.getWorkPlan() != null && result.getWorkPlan().getPlanTime() != null && !result.getWorkPlan().getPlanTime().equals("")) {
            viewHolder.tvTime.setText(CommonUtil.dateToString(CommonUtil.stringToDate(result.getWorkPlan().getPlanTime())));
        }
        //  tv_master.setText(result.companyBOwner.name);
        viewHolder.tvProName.setText(result.getProductName());
        viewHolder.tvProCategory.setText(result.getCompanyCategory().getName());
        if (result.getWorkPlan() != null) {
            viewHolder.tvResponse.setText((result.getWorkPlan().getFinishAmount() == null ? 0 : result.getWorkPlan().getFinishAmount()) + "");//我的反馈
            viewHolder.tvMyTaskNum.setText(result.workPlan.achieveAmount);//我的任务
            viewHolder.tvPriority.setText(PriorityUtils.getPriority(result.workPlan.priority));
        }


        viewHolder.tvNum.setText(result.amount);//订单产品数量
        if ("3".equals(processType)) {
            viewHolder.llState.setVisibility(View.VISIBLE);
        } else {
            viewHolder.llState.setVisibility(View.GONE);

        }
        if (result.getCompanyA().getPhoto() != null && !"".equals(result.getCompanyA().getPhoto())) {
            GlideUtils.glideImageView(context, viewHolder.ivPhoto, result.getCompanyA().getPhoto(), true);
        }
        viewHolder.tvIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OutInStockAdd.class);
                intent.putExtra("inOut", "1");
                context.startActivity(intent);
            }
        });

        viewHolder.tvOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OutInStockAdd.class);
                intent.putExtra("inOut", "2");
                context.startActivity(intent);
            }
        });

        viewHolder.tvDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("3".equals(processType)) {
                    Intent intent = new Intent(context, TaskResponseDeliver.class);
                    intent.putExtra("processId", processId);
                    intent.putExtra("companyId", result.companyA.id);
                    intent.putExtra("flowId", result.relFlowProcess.flow.id);
                    intent.putExtra("workPlanId", result.workPlan.id);
                    intent.putExtra("myTask", result.workPlan.achieveAmount);
                    context.startActivity(intent);

                } else {
                    Intent intent = new Intent(context, TaskResponseMine.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("TaskDetail", result);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_companyName)
        TextView tvCompanyName;
        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.order_item_ll)
        LinearLayout orderItemLl;
        @BindView(R.id.view)
        View view;
        @BindView(R.id.rl_content)
        RelativeLayout rlContent;
        @BindView(R.id.pro_category)
        TextView proCategory;
        @BindView(R.id.tv_pro_category)
        TextView tvProCategory;
        @BindView(R.id.order_item_ll2_guigeName)
        TextView orderItemLl2GuigeName;
        @BindView(R.id.tv_pro_name)
        TextView tvProName;
        @BindView(R.id.order_item_ll2)
        LinearLayout orderItemLl2;
        @BindView(R.id.priority)
        TextView priority;
        @BindView(R.id.tv_priority)
        TextView tvPriority;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.order_item_ll3)
        LinearLayout orderItemLl3;
        @BindView(R.id.response)
        TextView response;
        @BindView(R.id.tv_response)
        TextView tvResponse;
        @BindView(R.id.tv_my_task)
        TextView tvMyTask;
        @BindView(R.id.tv_my_task_num)
        TextView tvMyTaskNum;
        @BindView(R.id.order_item)
        LinearLayout orderItem;
        @BindView(R.id.rl_detail)
        RelativeLayout rlDetail;
        @BindView(R.id.tv_deliver)
        TextView tvDeliver;
        @BindView(R.id.tv_out)
        TextView tvOut;
        @BindView(R.id.tv_in)
        TextView tvIn;
        @BindView(R.id.ll_state)
        LinearLayout llState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

