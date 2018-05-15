package com.blanink.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.bean.AllReview;
import com.blanink.utils.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 * 评价适配器
 */

public class ReviewAdapter extends BaseAdapter {
    private Context context;
    private List<AllReview.ResultBean.RowsBean> reviewList;
    public ReviewAdapter(Context context,List<AllReview.ResultBean.RowsBean> reviewList) {
        this.reviewList=reviewList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView =View.inflate(context, R.layout.item_remark,null);
            viewHolder.tv_product=(TextView)convertView.findViewById(R.id.tv_product);
            viewHolder.tv_product=(TextView)convertView.findViewById(R.id.tv_product);
            viewHolder.tv_product_ruler=(TextView)convertView.findViewById(R.id.tv_product_ruler);
            viewHolder.tv_require=(TextView)convertView.findViewById(R.id.tv_require);
            viewHolder.tv_num=(TextView)convertView.findViewById(R.id.tv_num);
            viewHolder.iv=(ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(viewHolder);

        }else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.tv_product.setText(reviewList.get(position).getOrderProduct().getProductName());
        viewHolder.tv_product_ruler.setText(reviewList.get(position).getOrderProduct().getCompanyCategory().getName());
        viewHolder.tv_require.setText(reviewList.get(position).getOrder().getACompany().getName());
        viewHolder.tv_num.setText(reviewList.get(position).getOrderProduct().getAmount());
        if(reviewList.get(position).getOrder().getACompany().getPhoto()!=null){
            GlideUtils.glideImageView(context,viewHolder.iv,reviewList.get(position).getOrder().getACompany().getPhoto(),true);
        }
        return convertView;
    }
  static class ViewHolder{
      TextView tv_product;
      TextView tv_product_ruler;
      TextView tv_require;
      TextView tv_num;
      ImageView iv;

  }
}
