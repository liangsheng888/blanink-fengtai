package com.blanink.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.Dashboard;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Created by liangshen on 2018/1/12 0012 11:58
 * 邮箱：liangshen6868@163.com
 */
public class DashboardPurchaseAdapter extends BaseAdapter {
    private Context context;
    private List<Dashboard.ResultBean.RowsBean> rowsBeanList;
    private SparseArray<View> sparseArray;

    public DashboardPurchaseAdapter(Context context, List<Dashboard.ResultBean.RowsBean> rowsBeanList) {
        this.context = context;
        this.rowsBeanList = rowsBeanList;
    }

    @Override
    public int getCount() {
        return rowsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return rowsBeanList.get(position);
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
            convertView = View.inflate(context, R.layout.item_dash_borad, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            sparseArray.put(position, convertView);
        } else {
            convertView = sparseArray.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.company.setText(rowsBeanList.get(position).getOrder().getBCompany().getShortName());
        viewHolder.productName.setText(rowsBeanList.get(position).getProductName());
        viewHolder.tvProductSn.setText(rowsBeanList.get(position).getOrder().getAOrderNumber());
        viewHolder.tvDeliverDate.setText(rowsBeanList.get(position).getDeliveryTime());
        viewHolder.tvNum.setText(rowsBeanList.get(position).getAmount());

        String st = OrderProductStateUtils.orderProductStatus(rowsBeanList.get(position).getOrderProductStatus());
        viewHolder.tvMsg.setText(st);
        viewHolder.tvMsg.setTextColor(Color.RED);
        if (rowsBeanList.get(position).getOrder().getACompany() != null && rowsBeanList.get(position).getOrder().getACompany().getPhoto() != null && !"".equals(rowsBeanList.get(position).getOrder().getACompany().getPhoto())) {
            GlideUtils.glideImageView(context, viewHolder.ivPhoto, rowsBeanList.get(position).getOrder().getACompany().getPhoto(), true);
        }
        if (rowsBeanList.get(position).getOrderProductStatus().equals("1") || rowsBeanList.get(position).getOrderProductStatus().equals("2") || rowsBeanList.get(position).getOrderProductStatus().equals("3") || rowsBeanList.get(position).getOrderProductStatus().equals("4") || rowsBeanList.get(position).getOrderProductStatus().equals("6")) {
            viewHolder.tvMsg.setText("订单未下发");
            viewHolder.tvMsg.setTextColor(Color.RED);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }  if (rowsBeanList.get(position).getOrderProductStatus().equals("5")) {
            viewHolder.tvMsg.setText("等待我方确认");
            viewHolder.tvMsg.setTextColor(Color.RED);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }  if (rowsBeanList.get(position).getOrderProductStatus().equals("7")) {
            viewHolder.tvMsg.setText("订单已下发");
            viewHolder.tvMsg.setTextColor(Color.BLUE);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }
        if (rowsBeanList.get(position).getRelFlowProcessList().size() > 0) {
            for (int i = 0; i < rowsBeanList.get(position).getRelFlowProcessList().size(); i++) {
                if (rowsBeanList.get(position).getRelFlowProcessList().get(i).getLatestFeedback() != null) {
                    if (rowsBeanList.get(position).getRelFlowProcessList().get(i).getLatestFeedback().getAchieveAmount() > 0) {
                        viewHolder.tvMsg.setText("产品生产中");
                        viewHolder.tvMsg.setTextColor(Color.BLUE);
                        viewHolder.tvMsg.setVisibility(View.VISIBLE);
                        break;

                    }
                }
            }
        }
        if(rowsBeanList.get(position).getOrderProductStatus().equals("11")||rowsBeanList.get(position).getOrderProductStatus().equals("12")){
            viewHolder.tvMsg.setText("等待我方收货");
            viewHolder.tvMsg.setTextColor(Color.RED);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }
        viewHolder.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        viewHolder.recyclerView.setNestedScrollingEnabled(false);
        viewHolder.recyclerView.setAdapter(new DashboardPurchaseGridAdapter(context, rowsBeanList.get(position).getRelFlowProcessList(), rowsBeanList.get(position).getId()));
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.company)
        TextView company;
        @BindView(R.id.tv_msg)
        TextView tvMsg;
        @BindView(R.id.ll)
        LinearLayout ll;
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.product_name)
        TextView productName;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.ll_name)
        LinearLayout llName;
        @BindView(R.id.product_sn)
        TextView productSn;
        @BindView(R.id.tv_product_sn)
        TextView tvProductSn;
        @BindView(R.id.ll_number)
        LinearLayout llNumber;
        @BindView(R.id.deliver_date)
        TextView deliverDate;
        @BindView(R.id.tv_deliver_date)
        TextView tvDeliverDate;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.iv_alarm)
        ImageView ivAlarm;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
