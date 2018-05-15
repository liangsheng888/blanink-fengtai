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
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DateUtils;
import com.blanink.utils.GlideUtils;
import com.blanink.utils.OrderProductStateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Created by liangshen on 2018/1/12 0012 11:58
 * 邮箱：liangshen6868@163.com
 */
public class DashboardAdapter extends BaseAdapter {
    private Context context;
    private List<Dashboard.ResultBean.RowsBean> rowsBeanList;
    private SparseArray<View> sparseArray;

    public DashboardAdapter(Context context, List<Dashboard.ResultBean.RowsBean> rowsBeanList) {
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
        viewHolder.company.setText(rowsBeanList.get(position).getOrder().getACompany().getShortName());
        viewHolder.productName.setText(rowsBeanList.get(position).getProductName());
        viewHolder.tvNum.setText(rowsBeanList.get(position).getAmount());
        viewHolder.tvProductSn.setText(rowsBeanList.get(position).getOrder().getAOrderNumber());
        if (rowsBeanList.get(position).getOrder().getACompany() != null && rowsBeanList.get(position).getOrder().getACompany().getPhoto() != null && !"".equals(rowsBeanList.get(position).getOrder().getACompany().getPhoto())) {
            GlideUtils.glideImageView(context, viewHolder.ivPhoto, rowsBeanList.get(position).getOrder().getACompany().getPhoto(), true);
        }
        viewHolder.tvDeliverDate.setText(rowsBeanList.get(position).getDeliveryTime());
        int alarmDay=3;
        if(rowsBeanList.get(position).getCompanyCategory()!=null){
            alarmDay= rowsBeanList.get(position).getCompanyCategory().getAlarmDay();
        }
        int subAlarmDay = CommonUtil.compare_now_sub_old(DateUtils.dateToString(new Date()), rowsBeanList.get(position).getCreateDate());
        int subDeiveryDay = CommonUtil.compare_now_sub_old( rowsBeanList.get(position).getDeliveryTime(),DateUtils.dateToString(new Date()));
        viewHolder.tvMsg.setText(OrderProductStateUtils.orderProductStatus(rowsBeanList.get(position).getOrderProductStatus()));
        if (rowsBeanList.get(position).getOrderProductStatus().equals("7")) {
            viewHolder.tvMsg.setText("流程未创建");
            viewHolder.ivAlarm.setImageResource(R.drawable.alarm_right);
            viewHolder.tvMsg.setTextColor(Color.RED);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }
        if (rowsBeanList.get(position).getOrderProductStatus().equals("23")) {
            viewHolder.tvMsg.setText("您已拒绝生产");
            viewHolder.tvMsg.setTextColor(Color.RED);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }
        if (rowsBeanList.get(position).getOrderProductStatus().equals("5")) {
            viewHolder.tvMsg.setText("等待甲方确认");
            viewHolder.tvMsg.setTextColor(Color.BLUE);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }
        if (rowsBeanList.get(position).getRelFlowProcessList().size() > 0) {
            for (int i = 0; i < rowsBeanList.get(position).getRelFlowProcessList().size(); i++) {
                if (rowsBeanList.get(position).getRelFlowProcessList().get(i).getLatestFeedback() != null) {
                    if (rowsBeanList.get(position).getRelFlowProcessList().get(i).getLatestFeedback().getAchieveAmount() > 0) {
                        viewHolder.tvMsg.setText("产品生产中");
                        viewHolder.tvMsg.setTextColor(context.getResources().getColor(R.color.colorTheme));
                        viewHolder.tvMsg.setVisibility(View.VISIBLE);
                        break;

                    }
                }
            }
        }
        if (subAlarmDay > alarmDay) {
            if ("1".equals(rowsBeanList.get(position).getOrderProductStatus())
                    || "2".equals(rowsBeanList.get(position).getOrderProductStatus())
                    || "3".equals(rowsBeanList.get(position).getOrderProductStatus())
                    || "4".equals(rowsBeanList.get(position).getOrderProductStatus())
                    || "5".equals(rowsBeanList.get(position).getOrderProductStatus())
                    || "6".equals(rowsBeanList.get(position).getOrderProductStatus())) {
                viewHolder.ivAlarm.setVisibility(View.VISIBLE);
                viewHolder.ivAlarm.setImageResource(R.drawable.alarm_right);
                viewHolder.tvMsg.setText("请及时下发生产");
                viewHolder.tvMsg.setTextColor(Color.RED);
                viewHolder.tvMsg.setVisibility(View.VISIBLE);
            }

        }
        if (subDeiveryDay<0) {
            viewHolder.ivAlarm.setVisibility(View.VISIBLE);
            viewHolder.tvMsg.setText("超过交货期限");
            viewHolder.tvMsg.setTextColor(Color.RED);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
            viewHolder.ivAlarm.setImageResource(R.drawable.alarm_right);
        }
        if(subDeiveryDay<=7&&subDeiveryDay>2){
            viewHolder.ivAlarm.setVisibility(View.VISIBLE);
            viewHolder.tvMsg.setText("距离交货期"+subDeiveryDay+"天");
            viewHolder.tvMsg.setTextColor(context.getResources().getColor(R.color.orange_text));
            viewHolder.ivAlarm.setImageResource(R.drawable.alarm_right);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }
        if(subDeiveryDay<=2&&subDeiveryDay>0){
            viewHolder.ivAlarm.setVisibility(View.VISIBLE);
            viewHolder.ivAlarm.setImageResource(R.drawable.alarm_right);
            viewHolder.tvMsg.setText("你要逾期了");
            viewHolder.tvMsg.setTextColor(Color.RED);
            viewHolder.tvMsg.setVisibility(View.VISIBLE);
        }

        viewHolder.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        viewHolder.recyclerView.setNestedScrollingEnabled(false);
        viewHolder.recyclerView.setAdapter(new DashboardGridAdapter(context, rowsBeanList.get(position).getRelFlowProcessList()));
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
