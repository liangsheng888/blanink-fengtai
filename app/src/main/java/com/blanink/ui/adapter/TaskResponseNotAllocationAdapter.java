package com.blanink.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.OrderProduct;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/4 0004.
 * 任务分配 待分配适配器
 */

public class TaskResponseNotAllocationAdapter extends BaseAdapter {
    private Context context;
    private List<OrderProduct.ResultBean.RowsBean> list;
    private SparseArray<View> sparseArray;

    public TaskResponseNotAllocationAdapter(Context context, List<OrderProduct.ResultBean.RowsBean> list) {
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
            convertView = View.inflate(context, R.layout.item_not_task, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            sparseArray.put(position, convertView);

        } else {
            convertView = sparseArray.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderProduct.ResultBean.RowsBean result = list.get(position);
        viewHolder.tvCompanyName.setText(result.getCompanyA().getShortName() == null ? result.getCompanyA().getName() : result.getCompanyA().getShortName());
        viewHolder.tvTime.setText(CommonUtil.dateToString(CommonUtil.stringToDate(result.getCreateDate())));
        // tv_master.setText(result.getCompanyBOwner().getName());
        viewHolder.tvProName.setText(result.getProductName());
        viewHolder.tvProCategory.setText(result.getCompanyCategory().getName());
        viewHolder.tvNum.setText(result.getAmount() + "");//数量
        if (result.getCompanyA().getPhoto() != null && !"".equals(result.getCompanyA().getPhoto())) {
            GlideUtils.glideImageView(context, viewHolder.ivPhoto, result.getCompanyA().getPhoto(), true);
        }
        // viewHolder.tvNote.setText(result.getProductDescription());
        viewHolder.tvFinishedPercent.setText((result.getRelFlowProcess().getTotalCompletedQuantity()) + "/" + result.getRelFlowProcess().getTarget());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_companyName)
        TextView tvCompanyName;
        @BindView(R.id.tv_master)
        TextView tvMaster;
        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;
        @BindView(R.id.tv_time)
        TextView tvTime;
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
        @BindView(R.id.tv_finished_percent)
        TextView tvFinishedPercent;
        @BindView(R.id.order_item)
        LinearLayout orderItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
