package com.blanink.ui.adapter;

import android.content.Context;
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
import com.blanink.utils.CommonUtil;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.PriorityUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/4 0004.
 * 任务分配 待分配适配器
 */

public class WorkPlanNotAllocationAdapter extends BaseAdapter {
    private Context context;
    private List<OrderProduct.ResultBean.RowsBean> list;
    private SparseArray<View> sparseArray;

    public WorkPlanNotAllocationAdapter(Context context, List<OrderProduct.ResultBean.RowsBean> list) {
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
        ViewHolder viewHolder = null;
        if (sparseArray.get(position, null) == null) {
            convertView = View.inflate(context, R.layout.item_work_plan_can_allocation, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            sparseArray.put(position, convertView);

        } else {
            convertView = sparseArray.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderProduct.ResultBean.RowsBean result = list.get(position);
        viewHolder.tvCompanyName.setText((result.getCompanyA().getShortName() == null ? result.getCompanyA().getName() : result.getCompanyA().getShortName()));
        viewHolder.tvTakeOrderTime.setText(CommonUtil.dateToString(CommonUtil.stringToDate(result.getCreateDate())));
        // tv_master.setText(result.getCompanyBOwner().getName());
        viewHolder.tvProName.setText(result.getProductName());
        viewHolder.tvProCategory.setText(result.getCompanyCategory().getName());
        viewHolder.tvNum.setText(result.getRelFlowProcess().getTarget() + "");//数量
        viewHolder.tvNote.setText(result.getProductDescription());
        viewHolder.tvPriority.setText(PriorityUtils.getPriority(result.getRelFlowProcess().getProcessPriority()));
        if (result.getCompanyA().getPhoto() != null && !"".equals(result.getCompanyA().getPhoto())) {
            GlideUtils.glideImageView(context, viewHolder.ivPhoto, result.getCompanyA().getPhoto(), true);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_companyName)
        TextView tvCompanyName;
        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;
        @BindView(R.id.tv_take_order_time)
        TextView tvTakeOrderTime;
        @BindView(R.id.order_item_ll)
        LinearLayout orderItemLl;
        @BindView(R.id.view)
        View view;
        @BindView(R.id.tv_pro_category)
        TextView tvProCategory;
        @BindView(R.id.order_item_ll2_guigeName)
        TextView orderItemLl2GuigeName;
        @BindView(R.id.tv_pro_name)
        TextView tvProName;
        @BindView(R.id.order_item_ll2)
        LinearLayout orderItemLl2;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.bad)
        TextView bad;
        @BindView(R.id.tv_priority)
        TextView tvPriority;
        @BindView(R.id.order_item)
        LinearLayout orderItem;
        @BindView(R.id.note)
        TextView note;
        @BindView(R.id.tv_note)
        TextView tvNote;
        @BindView(R.id.order_item3)
        RelativeLayout orderItem3;
        @BindView(R.id.rlContent)
        RelativeLayout rlContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
