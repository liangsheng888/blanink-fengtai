package com.blanink.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.Stock;
import com.blanink.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/14 0014.
 */
public class OutInAdapter extends BaseAdapter {
    private Context context;
    private List<Stock.ResultBean.RowsBean> srr;
    private SparseArray<View> sparseArray;
    private Stock.ResultBean.RowsBean rowsBean;

    public OutInAdapter(Context context, List<Stock.ResultBean.RowsBean> srr) {
        this.context = context;
        this.srr = srr;
    }

    @Override
    public int getCount() {
        return srr.size();
    }

    @Override
    public Object getItem(int position) {
        return srr.get(position);
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
            convertView = View.inflate(context, R.layout.item_out_in_stock, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            sparseArray.put(position, convertView);
        } else {
            convertView = sparseArray.get(position);
            viewHolder = (ViewHolder) convertView.getTag();
        }
        rowsBean = srr.get(position);
        viewHolder.tvNo.setText(rowsBean.getProcuteNumber());
        viewHolder.tvCategory.setText(rowsBean.getCompanyCategoryId().getName());
        viewHolder.tvNum.setText(rowsBean.getChangeAmount() + "");
        viewHolder.tvPrice.setText(rowsBean.getUnitPrice() + "元/" + rowsBean.getUnit());
        String typeString = ("1".equals(rowsBean.getInOut()) ? "入库" : "出库");
        viewHolder.tvType.setText(typeString);
        viewHolder.tvNote.setText(rowsBean.getRemarks());
        viewHolder.tvName.setText(rowsBean.getName());
        viewHolder.tvUsername.setText(rowsBean.getCreateBy().getName() + "");
        viewHolder.tvTime.setText(DateUtils.format(DateUtils.stringToDate(rowsBean.getCreateDate())));
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.no)
        TextView no;
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.user)
        TextView user;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.category)
        TextView category;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.note)
        TextView note;
        @BindView(R.id.tv_note)
        TextView tvNote;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
