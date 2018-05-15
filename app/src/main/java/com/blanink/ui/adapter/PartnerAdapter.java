package com.blanink.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.PartnerInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/25 0025.
 */
public class PartnerAdapter extends BaseAdapter {
    private Context context;
    private List<PartnerInfo.ResultBean> stringList;
    private SparseArray<View> sparseArray;
    private String type;

    public PartnerAdapter(Context context, List<PartnerInfo.ResultBean> stringList, String type) {
        this.context = context;
        this.stringList = stringList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
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
            convertView = View.inflate(context, R.layout.item_partner_text, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            sparseArray.put(position, convertView);

        } else {
            convertView = sparseArray.get(position);
            viewHolder = (ViewHolder) convertView.getTag();

        }
        if("0".equals(type)){
            viewHolder.tvCompanyName.setText(stringList.get(position).getCompanyA().getName());

        }
        if("1".equals(type)){
            viewHolder.tvCompanyName.setText(stringList.get(position).getCompanyB().getName());

        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_companyName)
        TextView tvCompanyName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
